package com.kumuluz.ee.migrations.liquibase.exceptions;

import com.kumuluz.ee.migrations.common.exceptions.MigrationException;

/**
 * Exception indicating that Liquibase configuration's JNDI name does not match any data source.
 *
 * @author Din Music
 * @since 1.0.0
 */
public class InvalidLiquibaseConfigurationException extends MigrationException {

    public InvalidLiquibaseConfigurationException(String jndiName) {
        super("Liquibase configuration with JNDI name '" + jndiName + "' does not match any data source's JNDI name.");
    }

}
