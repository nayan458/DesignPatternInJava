
class DatabaseService {
    private final String username;
    private final String phoneNumber;

    public DatabaseService(){
        this.username = "Nayanmoni";
        this.phoneNumber = "+19 9101419696";
    }
    public String getMailFromUsername(String username) {
        return this.username;
    }
    public String getPhoneNbrFromUsername(String username) {
        return this.phoneNumber;
    }
}

interface INotifier {
    void send(String message);
    String getUsername();
}

class Notifier implements INotifier {
    private final String username;
    private final DatabaseService databaseService;

    public Notifier(String username) {
        this.username = username;
        databaseService = new DatabaseService();
    }

    @Override
    public void send(String msg) {
        String mail = databaseService.getMailFromUsername(username);
        System.out.println("Sending " + msg + " by Mail to " + mail);
    }

    @Override
    public String getUsername() {
        return this.username;
    }
}

abstract class BaseNotifierDecorator implements INotifier {
    private final INotifier wrapped;
    protected final DatabaseService databaseService;

    public BaseNotifierDecorator(INotifier wrapped) {
        this.wrapped = wrapped;
        this.databaseService = new DatabaseService();
    }

    @Override
    public void send(String msg) {
        wrapped.send(msg);
    }


    @Override
    public String getUsername() {
        return wrapped.getUsername();
    }
}

class WhatsAppDecorator extends BaseNotifierDecorator {
    public WhatsAppDecorator(INotifier wrapped) {
        super(wrapped);
    }

    @Override
    public void send(String msg) {
        super.send(msg);
        String phoneNbr = databaseService.getPhoneNbrFromUsername(getUsername());
        System.out.println("Sending " + msg + " by WhatsApp on " + phoneNbr);
    }
}

class FacebookDecorator extends BaseNotifierDecorator {
    public FacebookDecorator(INotifier wrapped) {
        super(wrapped);
    }

    @Override
    public void send(String msg) {
        super.send(msg);
        String phoneNbr = databaseService.getPhoneNbrFromUsername(getUsername());
        System.out.println("Sending " + msg + " by Facebook on " + phoneNbr);
    }
}

public class NotificationInterface {
    public static void main(String[] args) {
        INotifier facebookAndWhatsAppNotifier = new FacebookDecorator(
            new WhatsAppDecorator(
                new Notifier("Geek")
            )
        );
        // INotifier facebookAndWhatsAppNotifier = new FacebookDecorator(
        //     new WhatsAppDecorator(
        //         new Notifier("Geek")
        //     )
        // );

        facebookAndWhatsAppNotifier.send("this is a message");
    }
}