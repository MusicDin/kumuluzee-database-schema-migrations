package com.kumuluz.ee.migrations.liquibase.configurations;

import java.util.Optional;

public class LiquibaseChangelogConfiguration {
    public Optional<String> jndiName;
    public Optional<String> file;
    public boolean startupDropAll;
    public boolean startupUpdate;

    public Optional<String> connectionUrl;
    public Optional<String> username;
    public Optional<String> password;
}
