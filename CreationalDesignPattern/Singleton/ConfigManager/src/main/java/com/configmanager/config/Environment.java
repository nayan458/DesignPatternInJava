package com.configmanager.config;

public enum Environment {
    DEV("config/dev.json"),
    STAGE("config/stage.json"),
    PROD("config/prod.json");

    private final String path;

    Environment(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
