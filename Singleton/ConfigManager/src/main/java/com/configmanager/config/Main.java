package com.configmanager.config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {

        ExecutorService executor = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 10; i++) {
            executor.submit(() -> {
                ConfigManager config = ConfigManager.getInstance(Environment.DEV);
                System.out.println(Thread.currentThread().getName() + " -> instance: " + config.hashCode());

                // Try reading some config
                System.out.println("App Name: " + config.getString("app.name"));

                // Test DB connector
                DatabaseConnector db = new DatabaseConnector();
                db.connect();
            });
        }

        executor.shutdown();
    }
}
