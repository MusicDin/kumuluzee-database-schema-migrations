package com.kumuluz.ee.migrations;

/**
 * @author Din Music
 * @since 1.0.0
 */
public abstract class MigrationUtil {

    protected MigrationUtil() {
    }

    public abstract void migrate();

}
