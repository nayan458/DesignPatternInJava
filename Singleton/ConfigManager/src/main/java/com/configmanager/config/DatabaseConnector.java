package com.configmanager.config;

public class DatabaseConnector {

    public void connect() {
        ConfigManager config = ConfigManager.getInstance(Environment.DEV);

        String url = config.getString("db.url");
        String user = config.getString("db.username");

        System.out.println(
            "Connecting to DB -> URL: " + url + ", USER: " + user
        );
    }
}
