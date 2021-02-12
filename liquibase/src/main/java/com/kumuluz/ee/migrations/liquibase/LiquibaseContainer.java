package com.kumuluz.ee.migrations.liquibase;

import com.kumuluz.ee.common.config.DataSourceConfig;
import com.kumuluz.ee.common.config.EeConfig;
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
import java.util.List;

public class LiquibaseContainer {

    private final String jndiName;

    public LiquibaseContainer(String jndiName) {
        this.jndiName = jndiName;
    }

    public Liquibase createLiquibase() {

        LiquibaseConfig liquibaseConfig;

        List<DataSourceConfig> dataSourceConfigs = EeConfig.getInstance().getDatasources();
        List<LiquibaseConfig> liquibaseConfigs = LiquibaseConfigurationUtil.getLiquibaseConfigs();

        if(liquibaseConfigs.size() == 0) {
            throw new RuntimeException("No liquibase configurations provided!");
        }

        if(dataSourceConfigs.size() == 0) {
            throw new RuntimeException("No datasource configuration provided!");
        }


        if (jndiName == null || jndiName.equals("")) {

            // If jndiName is not defined and only 1 liquibase configuration is provided,
            // return LiquibaseContainer for that configuration

            if(liquibaseConfigs.size() == 1) {

                liquibaseConfig = liquibaseConfigs.get(0);

            } else {
                throw new RuntimeException("There is more than 1 datasource configuration provided." +
                        " Please provide 'jndiName' of required datasource trough '@LiquibaseChangelog' annotation.");
            }

        } else {

            liquibaseConfig = liquibaseConfigs
                    .stream()
                    .filter(config -> config.getJndiName().equals(jndiName))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Liquibase configuration with jndi name '" + jndiName + "' not found"));
        }

        DataSourceConfig datasourceConfig = dataSourceConfigs
                .stream()
                .filter(ds -> ds.getJndiName().equals(jndiName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Datasource configuration with jndi name '" + liquibaseConfig.getJndiName() + "' not found"));


        try {
            Connection connection = DriverManager.getConnection(
                    datasourceConfig.getConnectionUrl(),
                    datasourceConfig.getUsername(),
                    datasourceConfig.getPassword());

            ResourceAccessor resourceAccessor = new ClassLoaderResourceAccessor(Thread.currentThread().getContextClassLoader());
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));

            return new Liquibase(liquibaseConfig.getFile(), resourceAccessor, database);

        } catch (SQLException | LiquibaseException e) {
            throw new RuntimeException(e);
        }
    }

}
