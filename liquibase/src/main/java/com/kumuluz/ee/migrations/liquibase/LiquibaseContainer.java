package com.kumuluz.ee.migrations.liquibase;

import com.kumuluz.ee.migrations.liquibase.configurations.DatasourceConfig;
import com.kumuluz.ee.migrations.liquibase.configurations.LiquibaseConfig;
import com.kumuluz.ee.migrations.liquibase.utils.LiquibaseConfigurationUtil;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.ResourceAccessor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class LiquibaseContainer {

    private final String jndiName;

    public LiquibaseContainer(String jndiName) {
        this.jndiName = jndiName;
    }

    public Liquibase createLiquibase() {

        LiquibaseConfig liquibaseConfig;

        // If jndiName is not defined, return first configuration
        if (jndiName == null || jndiName.isEmpty()) {
            liquibaseConfig = LiquibaseConfigurationUtil
                    .getLiquibaseConfig(0)
                    .orElseThrow(() -> new RuntimeException("Liquibase configuration not found"));

        } else {
            liquibaseConfig = LiquibaseConfigurationUtil
                    .getLiquibaseConfig(jndiName)
                    .orElseThrow(() -> new RuntimeException("Liquibase configuration with jndi name '" + jndiName + "' not found"));
        }

        DatasourceConfig datasourceConfig = LiquibaseConfigurationUtil
                .getDatasourceConfig(liquibaseConfig.jndiName)
                .orElseThrow(() -> new RuntimeException("Datasource configuration with jndi name '" + liquibaseConfig.jndiName + "' not found"));

        try {
            Connection connection = DriverManager.getConnection(
                    datasourceConfig.connectionUrl,
                    datasourceConfig.username,
                    datasourceConfig.password);

            ResourceAccessor resourceAccessor = new ClassLoaderResourceAccessor(Thread.currentThread().getContextClassLoader());
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));

            return new Liquibase(liquibaseConfig.file, resourceAccessor, database);

        } catch (SQLException | LiquibaseException e) {
            throw new RuntimeException(e);
        }
    }

}
