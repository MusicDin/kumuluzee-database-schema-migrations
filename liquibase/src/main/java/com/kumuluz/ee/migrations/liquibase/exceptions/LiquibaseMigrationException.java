package com.kumuluz.ee.migrations.liquibase.exceptions;

import com.kumuluz.ee.migrations.common.exceptions.MigrationException;

/**
 * Exception is indicating that Liquibase migration has failed.
 *
 * @author Din Music
 * @since 1.0.0
 */
public class LiquibaseMigrationException extends MigrationException {

    public LiquibaseMigrationException(Exception e) {
        super("Liquibase migration has failed!", e);
    }

}
