package com.kumuluz.ee.migrations.liquibase.utils;

import com.kumuluz.ee.configuration.utils.ConfigurationUtil;
import com.kumuluz.ee.migrations.liquibase.configurations.KeeDatasourceConfiguration;
import com.kumuluz.ee.migrations.liquibase.configurations.LiquibaseDatasourceConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class LiquibaseConfigurationUtil {

    public static final Logger LOG = Logger.getLogger(LiquibaseConfigurationUtil.class.getName());

    public static final String LIQUIBASE_CONFIG_PREFIX   = "kumuluzee.migrations.liquibase";
    public static final String DATASOURCES_CONFIG_PREFIX = "kumuluzee.datasources";

    public static Optional<KeeDatasourceConfiguration> getDatasourceConfiguration(String datasourceJndiName){

        final ConfigurationUtil config = ConfigurationUtil.getInstance();
        int datasourcesCount = config.getListSize(DATASOURCES_CONFIG_PREFIX).orElse(0);

        for (int i = 0; i < datasourcesCount; i++) {

            String datasource = DATASOURCES_CONFIG_PREFIX + "[" + i + "]";
            Optional<String> jndiName = config.get(datasource + ".jndi-name");

            if(jndiName.isPresent() && jndiName.get().equals(datasourceJndiName)) {

                Optional<String> url = config.get(datasource + ".connection-url");
                Optional<String> username = config.get(datasource + ".username");
                Optional<String> password = config.get(datasource + ".password");

                if(url.isEmpty() || username.isEmpty() || password.isEmpty()) {
                    LOG.severe("Datasource with provided jndi name is not configured properly.");
                    return Optional.empty();
                }

                KeeDatasourceConfiguration dsConfig = new KeeDatasourceConfiguration();
                dsConfig.jndiName = jndiName.get();
                dsConfig.username = username.get();
                dsConfig.password = password.get();
                dsConfig.connectionUrl = url.get();

                return Optional.of(dsConfig);
            }
        }

        return Optional.empty();
    }

    public static List<LiquibaseDatasourceConfiguration> getLiquibaseDatasourceConfigurations(){

        final ConfigurationUtil config = ConfigurationUtil.getInstance();
        int datasourcesCount = config.getListSize(LIQUIBASE_CONFIG_PREFIX + ".datasources").orElse(0);

        List<LiquibaseDatasourceConfiguration> datasources = new ArrayList<>();

        for (int i = 0; i < datasourcesCount; i++) {

            String liquibaseDatasource = LIQUIBASE_CONFIG_PREFIX + ".datasources[" + i + "]";
            Optional<String> jndiName = config.get(liquibaseDatasource + ".jndi-name");

            if(jndiName.isPresent()) {

                LiquibaseDatasourceConfiguration dsConfig = new LiquibaseDatasourceConfiguration();
                dsConfig.jndiName = jndiName;
                dsConfig.changelog = config.get(liquibaseDatasource + ".changelog");
                dsConfig.startupDropAll = config.getBoolean(liquibaseDatasource + ".startup.dropAll").orElse(false);
                dsConfig.startupUpdate = config.getBoolean(liquibaseDatasource + ".startup.update").orElse(false);
                datasources.add(dsConfig);

            } else {

                // TODO: Add option to directly provide connection-url, username and password
            }
        }

        return datasources;
    }

}
