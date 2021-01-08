package com.kumuluz.ee.migrations.liquibase.utils;

import com.kumuluz.ee.configuration.utils.ConfigurationUtil;
import com.kumuluz.ee.migrations.liquibase.configurations.KeeDatasourceConfiguration;
import com.kumuluz.ee.migrations.liquibase.configurations.LiquibaseChangelogConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class LiquibaseConfigurationUtil {

    public static final Logger LOG = Logger.getLogger(LiquibaseConfigurationUtil.class.getName());

    public static final String LIQUIBASE_CHANGELOGS_CONFIG_PREFIX = "kumuluzee.migrations.liquibase.changelogs";
    public static final String DATASOURCES_CONFIG_PREFIX = "kumuluzee.datasources";

    public static Optional<KeeDatasourceConfiguration> getDatasourceConfiguration(String datasourceJndiName){

        final ConfigurationUtil config = ConfigurationUtil.getInstance();
        int datasourcesCount = config.getListSize(DATASOURCES_CONFIG_PREFIX).orElse(0);

        for (int i = 0; i < datasourcesCount; i++) {

            String dsPrefix = DATASOURCES_CONFIG_PREFIX + "[" + i + "]";
            Optional<String> jndiName = config.get(dsPrefix + ".jndi-name");

            if(jndiName.isPresent() && jndiName.get().equals(datasourceJndiName)) {

                Optional<String> url = config.get(dsPrefix + ".connection-url");
                Optional<String> username = config.get(dsPrefix + ".username");
                Optional<String> password = config.get(dsPrefix + ".password");

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

    public static List<LiquibaseChangelogConfiguration> getLiquibaseChangelogConfigurations(){

        final ConfigurationUtil config = ConfigurationUtil.getInstance();
        int changelogCount = config.getListSize(LIQUIBASE_CHANGELOGS_CONFIG_PREFIX).orElse(0);

        List<LiquibaseChangelogConfiguration> changelogConfigs = new ArrayList<>();

        for (int i = 0; i < changelogCount; i++) {

            String changelogPrefix = LIQUIBASE_CHANGELOGS_CONFIG_PREFIX + "[" + i + "]";
            Optional<String> jndiName = config.get(changelogPrefix + ".jndi-name");

            if(jndiName.isPresent()) {

                LiquibaseChangelogConfiguration changelogConfig = new LiquibaseChangelogConfiguration();
                changelogConfig.jndiName = jndiName;
                changelogConfig.file = config.get(changelogPrefix + ".file");
                changelogConfig.startupDropAll = config.getBoolean(changelogPrefix + ".startup.drop-all").orElse(false);
                changelogConfig.startupUpdate = config.getBoolean(changelogPrefix + ".startup.update").orElse(false);
                changelogConfigs.add(changelogConfig);

            } else {

                // TODO: Add option to directly provide connection-url, username and password
            }
        }

        return changelogConfigs;
    }

}
