package com.kumuluz.ee.migrations.liquibase.exceptions;

import com.kumuluz.ee.migrations.common.exceptions.MigrationException;

/**
 * Exception indicating that Liquibase configuration with provided JNDI name could not be found.
 *
 * @author Din Music
 * @since 1.0.0
 */
public class LiquibaseConfigurationNotFoundException extends MigrationException {

    public LiquibaseConfigurationNotFoundException(String jndiName) {
        super("Liquibase configuration with JNDI name '" + jndiName + "' not found");
    }

}
