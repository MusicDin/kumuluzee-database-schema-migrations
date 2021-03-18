package com.kumuluz.ee.migrations.liquibase.exceptions;

import com.kumuluz.ee.migrations.common.exceptions.MigrationException;

/**
 * Exception indicating that multiple Liquibase configurations are provided but it's not clear
 * which one should be used.
 *
 * @author Din Music
 * @since 1.0.0
 */
public class AmbiguousLiquibaseConfigurationException extends MigrationException {

    public AmbiguousLiquibaseConfigurationException() {
        super("There are more than 1 Liquibase configurations provided." +
                " Please provide 'jndiName' of Liquibase configuration trough '@LiquibaseChangelog' annotation.");
    }

}
