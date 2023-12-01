package com.bookstore.core.config;

import org.testcontainers.containers.MySQLContainer;

public class MySQLContainerConfig extends MySQLContainer<MySQLContainerConfig> {
    private static final String IMAGE_VERSION = "mysql:8";
    private static MySQLContainerConfig container;

    private MySQLContainerConfig() {
        super(IMAGE_VERSION);
    }

    public static MySQLContainerConfig getInstance() {
        if (container == null) {
            container = new MySQLContainerConfig();
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("TEST_DB_URL", container.getJdbcUrl());
        System.setProperty("TEST_DB_USERNAME", container.getUsername());
        System.setProperty("TEST_DB_PASSWORD", container.getPassword());
    }

    @Override
    public void stop() {
        super.stop();
    }
}
