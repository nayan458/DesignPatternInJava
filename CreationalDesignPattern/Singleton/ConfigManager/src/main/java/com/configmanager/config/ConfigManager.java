package com.configmanager.config;
import java.util.*;

public class ConfigManager {

    private static volatile ConfigManager instance;
    private Map<String, String> data;

    private ConfigManager(Environment env) {
        System.out.println("Loading config for " + env + "...");
        data = new HashMap<>();
        data.put("message", "Hello from " + env);
    }

    public static ConfigManager getInstance(Environment env) {
        ConfigManager result = instance;
        if (result == null) {
            synchronized (ConfigManager.class) {
                result = instance;
                if (result == null) {
                    instance = result = new ConfigManager(env);
                }
            }
        }
        return result;
    }

    public String getString(String key) {
        return data.get(key);
    }
}
