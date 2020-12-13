package com.kumuluz.ee.migrations;

public abstract class MigrationUtil {

    protected MigrationUtil() {

    }

    public abstract void dropAll(String url, String username, String password, String masterChangelog);
    public abstract void update(String url, String username, String password, String masterChangelog);

}
