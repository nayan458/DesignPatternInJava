import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * RealNotificationEngine.java
 *
 * Single-file runnable example of an async notification engine.
 * Implements a simple in-memory broker, repo, adapters, rate limiter,
 * retries with exponential backoff + jitter, and DLQ handling.
 *
 * Compile: javac RealNotificationEngine.java
 * Run:     java RealNotificationEngine
 */
public class RealNotificationEngin {

    /***********************
     * Domain & DTOs
     ***********************/
    enum Channel { EMAIL, WHATSAPP, SMS, FACEBOOK, PUSH }

    static class Notification {
        public final String id; // UUID
        public final String userId; // recipient username or userId
        public final String subject;
        public final String body;
        public final Set<Channel> channels; // requested channels
        public final long createdAt;
        public final String idempotencyKey; // optional

        public Notification(String id, String userId, String subject, String body,
                            Set<Channel> channels, String idempotencyKey) {
            this.id = id;
            this.userId = userId;
            this.subject = subject;
            this.body = body;
            this.channels = channels == null ? Collections.emptySet() : EnumSet.copyOf(channels);
            this.createdAt = System.currentTimeMillis();
            this.idempotencyKey = idempotencyKey;
        }

        @Override
        public String toString() {
            return "Notification{" + "id=" + id + ", user=" + userId + ", channels=" + channels + '}';
        }
    }

    static class Request {
        public final String userId;
        public final String subject;
        public final String body;
        public final Set<Channel> channels;
        public final String idempotencyKey;

        public Request(String userId, String subject, String body, Set<Channel> channels, String idempotencyKey) {
            this.userId = userId;
            this.subject = subject;
            this.body = body;
            this.channels = channels;
            this.idempotencyKey = idempotencyKey;
        }
    }

    enum NotificationState { PENDING, DELIVERED, FAILED }

    static class NotificationRecord {
        public final String id;
        public final String userId;
        public volatile NotificationState state;
        // per-channel status: PENDING/DELIVERED/FAILED
        public final ConcurrentMap<Channel, ChannelState> channelStates = new ConcurrentHashMap<>();
        public final AtomicInteger attempts = new AtomicInteger(0);
        public final String idempotencyKey;

        NotificationRecord(String id, String userId, NotificationState state, String idempotencyKey, Set<Channel> channels) {
            this.id = id;
            this.userId = userId;
            this.state = state;
            this.idempotencyKey = idempotencyKey;
            if (channels != null) {
                for (Channel c : channels) channelStates.put(c, new ChannelState(ChannelStatus.PENDING));
            }
        }

        boolean allChannelsDelivered() {
            return channelStates.values().stream().allMatch(cs -> cs.status == ChannelStatus.DELIVERED);
        }
    }

    enum ChannelStatus { PENDING, DELIVERED, FAILED }

    static class ChannelState {
        volatile ChannelStatus status;
        volatile int attempts;
        volatile String lastError;
        ChannelState(ChannelStatus status) {
            this.status = status;
            this.attempts = 0;
        }
    }

    /***********************
     * Exceptions
     ***********************/
    static class TransientException extends Exception {
        TransientException(String msg) { super(msg); }
        TransientException(Throwable t) { super(t); }
    }
    static class PermanentException extends Exception {
        PermanentException(String msg) { super(msg); }
    }

    // provider exceptions used inside adapters
    static class ProviderTemporaryException extends Exception { ProviderTemporaryException(String m){ super(m);} }
    static class ProviderPermanentException extends Exception { ProviderPermanentException(String m){ super(m);} }

    /***********************
     * Repositories & Services (in-memory)
     ***********************/
    interface NotificationRepo {
        Optional<NotificationRecord> findByIdempotencyKey(String key);
        void save(NotificationRecord rec);
        NotificationRecord findById(String id);
        boolean isChannelDelivered(String notificationId, Channel channel);
        void markChannelDelivered(String notificationId, Channel channel);
        void markChannelFailed(String notificationId, Channel channel, String error);
        boolean allChannelsDelivered(String notificationId);
        void updateState(String notificationId, NotificationState state);
    }

    static class InMemoryNotificationRepo implements NotificationRepo {
        private final ConcurrentMap<String, NotificationRecord> map = new ConcurrentHashMap<>();
        private final ConcurrentMap<String, String> idempotencyIndex = new ConcurrentHashMap<>();

        @Override
        public Optional<NotificationRecord> findByIdempotencyKey(String key) {
            if (key == null) return Optional.empty();
            String id = idempotencyIndex.get(key);
            if (id == null) return Optional.empty();
            return Optional.ofNullable(map.get(id));
        }

        @Override
        public void save(NotificationRecord rec) {
            map.put(rec.id, rec);
            if (rec.idempotencyKey != null) idempotencyIndex.put(rec.idempotencyKey, rec.id);
        }

        @Override
        public NotificationRecord findById(String id) {
            NotificationRecord r = map.get(id);
            if (r == null) throw new IllegalStateException("Notification not found: " + id);
            return r;
        }

        @Override
        public boolean isChannelDelivered(String notificationId, Channel channel) {
            NotificationRecord r = findById(notificationId);
            ChannelState cs = r.channelStates.get(channel);
            return cs != null && cs.status == ChannelStatus.DELIVERED;
        }

        @Override
        public void markChannelDelivered(String notificationId, Channel channel) {
            NotificationRecord r = findById(notificationId);
            ChannelState cs = r.channelStates.computeIfAbsent(channel, c -> new ChannelState(ChannelStatus.DELIVERED));
            cs.status = ChannelStatus.DELIVERED;
            cs.attempts++;
        }

        @Override
        public void markChannelFailed(String notificationId, Channel channel, String error) {
            NotificationRecord r = findById(notificationId);
            ChannelState cs = r.channelStates.computeIfAbsent(channel, c -> new ChannelState(ChannelStatus.FAILED));
            cs.status = ChannelStatus.FAILED;
            cs.lastError = error;
            cs.attempts++;
        }

        @Override
        public boolean allChannelsDelivered(String notificationId) {
            return findById(notificationId).allChannelsDelivered();
        }

        @Override
        public void updateState(String notificationId, NotificationState state) {
            NotificationRecord r = findById(notificationId);
            r.state = state;
        }
    }

    static class PreferencesService {
        // in a real system this would fetch per-user preferences (opt-outs, do-not-disturb, etc)
        Set<Channel> resolveChannels(String userId, Set<Channel> requested) {
            if (requested == null || requested.isEmpty()) {
                return EnumSet.of(Channel.EMAIL);
            }
            // example rule: if user is "no-social", remove FACEBOOK
            if ("no-social".equals(userId)) {
                Set<Channel> filtered = EnumSet.copyOf(requested);
                filtered.remove(Channel.FACEBOOK);
                return filtered;
            }
            return EnumSet.copyOf(requested);
        }
    }

    /***********************
     * Broker / Publisher (in-memory)
     ***********************/
    static class BrokerPublisher {
        private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(4);
        private final BlockingQueue<Envelope> queue;
        private final List<Envelope> dlq = Collections.synchronizedList(new ArrayList<>());
        private final NotificationWorker worker;

        BrokerPublisher(BlockingQueue<Envelope> queue, NotificationWorker worker) {
            this.queue = queue;
            this.worker = worker;
            // start a simple consumer thread
            Thread consumer = new Thread(() -> {
                try {
                    while (!Thread.currentThread().isInterrupted()) {
                        Envelope env = queue.take();
                        worker.handle(env.notification, env.attempt);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }, "broker-consumer");
            consumer.setDaemon(true);
            consumer.start();
        }

        void publish(Notification n) {
            queue.offer(new Envelope(n, 0));
        }

        void requeueWithDelay(Notification n, long delayMillis, int nextAttempt) {
            scheduler.schedule(() -> queue.offer(new Envelope(n, nextAttempt)), delayMillis, TimeUnit.MILLISECONDS);
        }

        void requeueWithDelay(Notification n, long delayMillis) {
            requeueWithDelay(n, delayMillis, 1);
        }

        void sendToDLQ(Notification n, Channel channel, Exception e) {
            String entry = String.format("DLQ: notif=%s channel=%s err=%s", n.id, channel, e.getMessage());
            dlq.add(new Envelope(n, -1)); // store for inspection
            System.err.println(entry);
        }

        void shutdown() {
            scheduler.shutdownNow();
        }

        static class Envelope {
            final Notification notification;
            final int attempt;
            Envelope(Notification n, int attempt) { this.notification = n; this.attempt = attempt; }
        }
    }

    /***********************
     * Rate limiter (per-user-per-channel token bucket, in-memory)
     ***********************/
    interface RateLimiter {
        boolean tryAcquire(String userId, Channel channel);
    }

    static class TokenBucket {
        final int capacity;
        final long refillIntervalMillis;
        final int refillTokens;
        volatile double tokens;
        volatile long lastRefill;

        TokenBucket(int capacity, int refillTokens, Duration refillInterval) {
            this.capacity = capacity;
            this.refillTokens = refillTokens;
            this.refillIntervalMillis = refillInterval.toMillis();
            this.tokens = capacity;
            this.lastRefill = System.currentTimeMillis();
        }

        synchronized boolean tryConsume() {
            refillIfNeeded();
            if (tokens >= 1.0) {
                tokens -= 1.0;
                return true;
            }
            return false;
        }

        private void refillIfNeeded() {
            long now = System.currentTimeMillis();
            if (now - lastRefill >= refillIntervalMillis) {
                double toAdd = refillTokens * ((now - lastRefill) / (double) refillIntervalMillis);
                tokens = Math.min(capacity, tokens + toAdd);
                lastRefill = now;
            }
        }
    }

    static class InMemoryRateLimiter implements RateLimiter {
        // key -> token bucket
        private final ConcurrentMap<String, TokenBucket> map = new ConcurrentHashMap<>();

        @Override
        public boolean tryAcquire(String userId, Channel channel) {
            String key = userId + ":" + channel.name();
            TokenBucket tb = map.computeIfAbsent(key, k -> new TokenBucket(5, 5, Duration.ofMinutes(1)));
            return tb.tryConsume();
        }
    }

    /***********************
     * Channel adapters & factory (simple)
     ***********************/
    interface ChannelAdapter {
        void send(String userId, String subject, String body) throws TransientException, PermanentException;
    }

    static class ChannelAdapterFactory {
        private final EmailClient emailClient;
        ChannelAdapterFactory(EmailClient ec) { this.emailClient = ec; }
        ChannelAdapter get(Channel c) {
            switch (c) {
                case EMAIL: return new EmailAdapter(emailClient);
                default: return new ConsoleAdapter(c);
            }
        }
    }

    static class EmailAdapter implements ChannelAdapter {
        private final EmailClient client;
        EmailAdapter(EmailClient client) { this.client = client; }
        @Override
        public void send(String userId, String subject, String body) throws TransientException, PermanentException {
            String email = UserRepo.getEmail(userId);
            try {
                client.send(email, subject, body);
            } catch (ProviderTemporaryException e) {
                throw new TransientException(e);
            } catch (ProviderPermanentException e) {
                throw new PermanentException(e.getMessage());
            }
        }
    }

    static class ConsoleAdapter implements ChannelAdapter {
        private final Channel channel;
        ConsoleAdapter(Channel c) { this.channel = c; }
        @Override
        public void send(String userId, String subject, String body) throws TransientException {
            // simulate best-effort; sometimes transient failure for demonstration
            if (Math.random() < 0.05) throw new TransientException("simulated transient error for " + channel);
            System.out.println("[" + channel + "] to=" + UserRepo.getEmail(userId) + " subj=" + subject + " body=" + body);
        }
    }

    /***********************
     * Simple EmailClient & UserRepo stubs
     ***********************/
    static class EmailClient {
        void send(String email, String subject, String body) throws ProviderTemporaryException, ProviderPermanentException {
            // simulate transient failure 10% of the time
            double p = Math.random();
            if (p < 0.10) throw new ProviderTemporaryException("SMTP timeout");
            if (p < 0.12) throw new ProviderPermanentException("Invalid email address");
            System.out.println("[EMAIL-SENT] " + email + " subj=" + subject);
        }
    }

    static class UserRepo {
        private static final Map<String, String> users = Map.of(
                "alice", "alice@example.com",
                "bob", "bob@example.com",
                "Geek", "geek@example.com",
                "no-social", "nosocial@example.com"
        );
        static String getEmail(String userId) {
            return users.getOrDefault(userId, userId + "@example.com");
        }
    }

    /***********************
     * Worker
     ***********************/
    static class NotificationWorker {
        private final ChannelAdapterFactory adapterFactory;
        private final NotificationRepo repo;
        private final RateLimiter rateLimiter;
        private final BrokerPublisher publisher;
        private final int MAX_RETRIES = 5;

        NotificationWorker(ChannelAdapterFactory adapterFactory, NotificationRepo repo, RateLimiter rateLimiter, BrokerPublisher publisher) {
            this.adapterFactory = adapterFactory;
            this.repo = repo;
            this.rateLimiter = rateLimiter;
            this.publisher = publisher;
        }

        void handle(Notification n, int attempt) {
            try {
                NotificationRecord rec = repo.findById(n.id);
                // process channels one by one; if a transient failure happens, requeue the notification to try later
                for (Channel channel : n.channels) {
                    // idempotency
                    if (repo.isChannelDelivered(n.id, channel)) continue;

                    // rate-limit per recipient-channel
                    if (!rateLimiter.tryAcquire(n.userId, channel)) {
                        // requeue with small delay and increment attempt
                        System.out.println("Rate limited for " + n.userId + " channel " + channel + " -> requeue");
                        publisher.requeueWithDelay(n, 1000L, attempt + 1);
                        return;
                    }

                    ChannelAdapter adapter = adapterFactory.get(channel);
                    try {
                        adapter.send(n.userId, n.subject, n.body);
                        repo.markChannelDelivered(n.id, channel);
                        System.out.println("Delivered channel " + channel + " for " + n.id);
                    } catch (TransientException te) {
                        int nextAttempt = attempt + 1;
                        if (nextAttempt > MAX_RETRIES) {
                            repo.markChannelFailed(n.id, channel, te.getMessage());
                            publisher.sendToDLQ(n, channel, te);
                        } else {
                            long delay = calculateBackoff(nextAttempt);
                            System.out.println("Transient error -> requeue notif=" + n.id + " channel=" + channel + " attempt=" + nextAttempt + " delay=" + delay + "ms");
                            publisher.requeueWithDelay(n, delay, nextAttempt);
                        }
                        return; // stop processing other channels for now
                    } catch (PermanentException pe) {
                        repo.markChannelFailed(n.id, channel, pe.getMessage());
                        System.err.println("Permanent failure sending channel " + channel + " for " + n.id + ": " + pe.getMessage());
                        // continue to next channel (do not retry)
                    }
                }

                if (repo.allChannelsDelivered(n.id)) {
                    repo.updateState(n.id, NotificationState.DELIVERED);
                    System.out.println("Notification " + n.id + " fully delivered.");
                }
            } catch (Exception ex) {
                System.err.println("Worker fatal error handling notification " + n.id + ": " + ex.getMessage());
            }
        }

        private long calculateBackoff(int attempt) {
            // exponential backoff with jitter: base 1s * 2^(attempt-1)
            long base = 1000L * (1L << Math.max(0, attempt - 1));
            long jitter = ThreadLocalRandom.current().nextLong(0, Math.max(1, base / 2));
            long val = Math.min(base + jitter, TimeUnit.MINUTES.toMillis(15));
            return val;
        }
    }

    /***********************
     * Controller
     ***********************/
    static class NotificationController {
        private final NotificationRepo repo;
        private final BrokerPublisher publisher;
        private final PreferencesService prefs;

        NotificationController(NotificationRepo repo, BrokerPublisher publisher, PreferencesService prefs) {
            this.repo = repo;
            this.publisher = publisher;
            this.prefs = prefs;
        }

        public void sendNotification(Request req) {
            // idempotency: if idempotencyKey provided, check existing
            if (req.idempotencyKey != null) {
                Optional<NotificationRecord> existing = repo.findByIdempotencyKey(req.idempotencyKey);
                if (existing.isPresent()) {
                    System.out.println("Idempotent request ignored: " + req.idempotencyKey);
                    return; // idempotent: don't re-create
                }
            }

            Set<Channel> channels = prefs.resolveChannels(req.userId, req.channels);
            Notification n = new Notification(UUID.randomUUID().toString(),
                    req.userId, req.subject, req.body, channels, req.idempotencyKey);
            NotificationRecord rec = new NotificationRecord(n.id, n.userId, NotificationState.PENDING, n.idempotencyKey, channels);
            repo.save(rec);
            publisher.publish(n); // send to queue
            System.out.println("Published notification " + n.id + " for user " + n.userId + " channels=" + channels);
        }
    }

    /***********************
     * Main - demonstration
     ***********************/
    public static void main(String[] args) throws Exception {
        // components
        InMemoryNotificationRepo repo = new InMemoryNotificationRepo();
        PreferencesService prefs = new PreferencesService();
        EmailClient emailClient = new EmailClient();
        ChannelAdapterFactory adapterFactory = new ChannelAdapterFactory(emailClient);
        InMemoryRateLimiter rateLimiter = new InMemoryRateLimiter();

        // in-memory broker queue
        BlockingQueue<BrokerPublisher.Envelope> queue = new LinkedBlockingQueue<>();
        NotificationWorker worker = new NotificationWorker(adapterFactory, repo, rateLimiter, null);
        BrokerPublisher broker = new BrokerPublisher(queue, worker);
        // set publisher into worker (circular dependency satisfied after creation)
        // worker.publisher = broker; // access allowed because member is package-private in this file

        NotificationController controller = new NotificationController(repo, broker, prefs);

        // send a few notifications
        controller.sendNotification(new Request("alice", "Welcome", "Hello Alice!", EnumSet.of(Channel.EMAIL, Channel.WHATSAPP), null));
        controller.sendNotification(new Request("bob", "Promo", "Discount for you", EnumSet.of(Channel.EMAIL, Channel.FACEBOOK), "idem-key-1"));
        // idempotent duplicate (should be ignored)
        controller.sendNotification(new Request("bob", "Promo", "Discount for you", EnumSet.of(Channel.EMAIL, Channel.FACEBOOK), "idem-key-1"));

        // a user with 'no-social' preference
        controller.sendNotification(new Request("no-social", "Update", "No social allowed", EnumSet.of(Channel.FACEBOOK, Channel.EMAIL), null));

        // wait a bit to let worker process
        Thread.sleep(5000);

        broker.shutdown();
        System.out.println("Shutdown broker. Demo complete.");
    }
}
