package com.kumuluz.ee.migrations.liquibase.utils;

import com.kumuluz.ee.configuration.utils.ConfigurationUtil;
import com.kumuluz.ee.migrations.liquibase.configurations.DatasourceConfig;
import com.kumuluz.ee.migrations.liquibase.configurations.LiquibaseConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class LiquibaseConfigurationUtil {

    public static final Logger LOG = Logger.getLogger(LiquibaseConfigurationUtil.class.getName());

    public static final String LIQUIBASE_CHANGELOGS_CONFIG_PREFIX = "kumuluzee.migrations.liquibase.changelogs";
    public static final String DATASOURCES_CONFIG_PREFIX = "kumuluzee.datasources";
    public static final String DEFAULT_MASTER_CHANGELOG = "db/changelog-master.xml";

    public static List<LiquibaseConfig> getLiquibaseConfigs() {

        List<LiquibaseConfig> liquibaseConfigs = new ArrayList<>();

        final ConfigurationUtil config = ConfigurationUtil.getInstance();
        int changelogCount = config.getListSize(LIQUIBASE_CHANGELOGS_CONFIG_PREFIX).orElse(0);

        for (int i = 0; i < changelogCount; i++) {
            Optional<LiquibaseConfig> liquibaseConfig = getLiquibaseConfig(i);
            liquibaseConfig.ifPresent(liquibaseConfigs::add);
        }

        return liquibaseConfigs;
    }

    public static Optional<LiquibaseConfig> getLiquibaseConfig(int index) {

        final ConfigurationUtil config = ConfigurationUtil.getInstance();
        Optional<String> jndiName = config.get(LIQUIBASE_CHANGELOGS_CONFIG_PREFIX + "[" + index + "]" + ".jndi-name");

        if (jndiName.isPresent()) {
            return getLiquibaseConfig(jndiName.get());
        }

        return Optional.empty();
    }

    public static Optional<LiquibaseConfig> getLiquibaseConfig(String liquibaseJndiName) {

        final ConfigurationUtil config = ConfigurationUtil.getInstance();
        int changelogCount = config.getListSize(LIQUIBASE_CHANGELOGS_CONFIG_PREFIX).orElse(0);

        for (int i = 0; i < changelogCount; i++) {

            String changelogPrefix = LIQUIBASE_CHANGELOGS_CONFIG_PREFIX + "[" + i + "]";
            Optional<String> jndiName = config.get(changelogPrefix + ".jndi-name");

            if (jndiName.isEmpty() || !jndiName.get().equals(liquibaseJndiName)) {
                continue;
            }

            LiquibaseConfig changelogConfig = new LiquibaseConfig();
            changelogConfig.setJndiName(jndiName.get());
            changelogConfig.setFile(config.get(changelogPrefix + ".file").orElse(DEFAULT_MASTER_CHANGELOG));
            changelogConfig.setStartupDropAll(config.getBoolean(changelogPrefix + ".startup.drop-all").orElse(false));
            changelogConfig.setStartupUpdate(config.getBoolean(changelogPrefix + ".startup.update").orElse(false));
            return Optional.of(changelogConfig);
        }

        return Optional.empty();
    }

    public static Optional<DatasourceConfig> getDatasourceConfig(String datasourceJndiName) {

        if (datasourceJndiName == null || datasourceJndiName.isEmpty())
            return Optional.empty();

        final ConfigurationUtil config = ConfigurationUtil.getInstance();
        int datasourcesCount = config.getListSize(DATASOURCES_CONFIG_PREFIX).orElse(0);

        for (int i = 0; i < datasourcesCount; i++) {

            String dsPrefix = DATASOURCES_CONFIG_PREFIX + "[" + i + "]";
            Optional<String> jndiName = config.get(dsPrefix + ".jndi-name");

            if (jndiName.isPresent() && jndiName.get().equals(datasourceJndiName)) {

                Optional<String> url = config.get(dsPrefix + ".connection-url");
                Optional<String> username = config.get(dsPrefix + ".username");
                Optional<String> password = config.get(dsPrefix + ".password");

                if (url.isEmpty() || username.isEmpty() || password.isEmpty()) {
                    LOG.severe("Datasource with provided jndi name is not configured properly.");
                    return Optional.empty();
                }

                DatasourceConfig dsConfig = new DatasourceConfig();
                dsConfig.jndiName = jndiName.get();
                dsConfig.username = username.get();
                dsConfig.password = password.get();
                dsConfig.connectionUrl = url.get();

                return Optional.of(dsConfig);
            }
        }

        return Optional.empty();
    }


}
