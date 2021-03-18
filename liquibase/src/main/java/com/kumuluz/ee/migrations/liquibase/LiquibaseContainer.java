package com.kumuluz.ee.migrations.liquibase;

import com.kumuluz.ee.common.config.DataSourceConfig;
import com.kumuluz.ee.migrations.liquibase.configurations.LiquibaseConfig;
import liquibase.Contexts;
import liquibase.LabelExpression;
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

/**
 * Liquibase container initializes Liquibase object based on provided JNDI name.
 *
 * @author Din Music
 * @since 1.0.0
 */
public class LiquibaseContainer {

    private final LiquibaseConfig liquibaseConfig;
    private final DataSourceConfig dataSourceConfig;

    public LiquibaseContainer(LiquibaseConfig liquibaseConfig, DataSourceConfig dataSourceConfig) {
        this.liquibaseConfig = liquibaseConfig;
        this.dataSourceConfig = dataSourceConfig;
    }

    /**
     * Creates Liquibase object based on configuration provided with JNDI name.
     *
     * @return Returns Liquibase object.
     */
    public Liquibase createLiquibase() {

        try {
            Connection connection = DriverManager.getConnection(
                    dataSourceConfig.getConnectionUrl(),
                    dataSourceConfig.getUsername(),
                    dataSourceConfig.getPassword());

            ResourceAccessor resourceAccessor = new ClassLoaderResourceAccessor(Thread.currentThread().getContextClassLoader());
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));

            return new Liquibase(liquibaseConfig.getFile(), resourceAccessor, database);

        } catch (SQLException | LiquibaseException e) {
            throw new RuntimeException(e);
        }
    }

    public LiquibaseConfig getLiquibaseConfig() {
        return liquibaseConfig;
    }

    public Contexts getContexts() {
        return new Contexts(liquibaseConfig.getContexts());
    }

    public LabelExpression getLabels() {
        return new LabelExpression(liquibaseConfig.getLabels());
    }

    public String getDataSourceJndiName() {
        return dataSourceConfig.getJndiName();
    }

}