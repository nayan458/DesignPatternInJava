import java.util.*;

// The interface of a remote service.
interface ThirdPartyYouTubeLib {
    List<String> listVideos();
    String getVideoInfo(String id);
    void downloadVideo(String id);
}

// The concrete implementation of a service connector.
class ThirdPartyYouTubeClass implements ThirdPartyYouTubeLib {

    @Override
    public List<String> listVideos() {
        System.out.println("Fetching video list from YouTube...");
        return Arrays.asList("video1", "video2", "video3");
    }

    @Override
    public String getVideoInfo(String id) {
        System.out.println("Fetching video info from YouTube for id: " + id);
        return "Info about video " + id;
    }

    @Override
    public void downloadVideo(String id) {
        System.out.println("Downloading video from YouTube: " + id);
    }
}

// Proxy class with caching.
class CachedYouTubeClass implements ThirdPartyYouTubeLib {

    private ThirdPartyYouTubeLib service;
    private List<String> listCache;
    private Map<String, String> videoCache = new HashMap<>();
    private boolean needReset = false;

    public CachedYouTubeClass(ThirdPartyYouTubeLib service) {
        this.service = service;
    }

    @Override
    public List<String> listVideos() {
        if (listCache == null || needReset) {
            listCache = service.listVideos();
        }
        return listCache;
    }

    @Override
    public String getVideoInfo(String id) {
        if (!videoCache.containsKey(id) || needReset) {
            videoCache.put(id, service.getVideoInfo(id));
        }
        return videoCache.get(id);
    }

    @Override
    public void downloadVideo(String id) {
        if (needReset || !downloadExists(id)) {
            service.downloadVideo(id);
        }
    }

    private boolean downloadExists(String id) {
        // Simulate local cache check
        return false;
    }

    public void resetCache() {
        needReset = true;
    }
}

// GUI class.
class YouTubeManager {

    protected ThirdPartyYouTubeLib service;

    public YouTubeManager(ThirdPartyYouTubeLib service) {
        this.service = service;
    }

    public void renderVideoPage(String id) {
        String info = service.getVideoInfo(id);
        System.out.println("Rendering video page: " + info);
    }

    public void renderListPanel() {
        List<String> list = service.listVideos();
        System.out.println("Rendering list panel: " + list);
    }

    public void reactOnUserInput() {
        renderVideoPage("video1");
        renderListPanel();
    }
}

// Application configuration.
public class Application {

    public static void main(String[] args) {
        ThirdPartyYouTubeLib youTubeService = new ThirdPartyYouTubeClass();
        ThirdPartyYouTubeLib youTubeProxy = new CachedYouTubeClass(youTubeService);

        YouTubeManager manager = new YouTubeManager(youTubeProxy);
        manager.reactOnUserInput();
    }
}
