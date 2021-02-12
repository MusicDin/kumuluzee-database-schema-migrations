package com.kumuluz.ee.migrations.liquibase.utils;

import com.kumuluz.ee.configuration.utils.ConfigurationUtil;
import com.kumuluz.ee.migrations.liquibase.configurations.LiquibaseConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LiquibaseConfigurationUtil {

    private static final String LIQUIBASE_CHANGELOGS_CONFIG_PREFIX = "kumuluzee.migrations.liquibase.changelogs";
    private static final String DEFAULT_MASTER_CHANGELOG = "db/changelog-master.xml";
    private static LiquibaseConfigurationUtil instance;
    private List<LiquibaseConfig> liquibaseConfigs;

    public static LiquibaseConfigurationUtil getInstance(){
        if(instance == null) {
            instance = new LiquibaseConfigurationUtil();
        }
        return instance;
    }

    private LiquibaseConfigurationUtil() {
        readConfigs();
    }

    private void readConfigs(){

        liquibaseConfigs = new ArrayList<>();

        final ConfigurationUtil config = ConfigurationUtil.getInstance();
        int changelogCount = config.getListSize(LIQUIBASE_CHANGELOGS_CONFIG_PREFIX).orElse(0);

        for (int i = 0; i < changelogCount; i++) {

            String changelogPrefix = LIQUIBASE_CHANGELOGS_CONFIG_PREFIX + "[" + i + "]";
            Optional<String> jndiName = config.get(changelogPrefix + ".jndi-name");

            if (jndiName.isPresent()) {
                LiquibaseConfig liquibaseConfig = new LiquibaseConfig();
                liquibaseConfig.setJndiName(jndiName.get());
                liquibaseConfig.setFile(config.get(changelogPrefix + ".file").orElse(DEFAULT_MASTER_CHANGELOG));
                liquibaseConfig.setStartupDropAll(config.getBoolean(changelogPrefix + ".startup.drop-all").orElse(false));
                liquibaseConfig.setStartupUpdate(config.getBoolean(changelogPrefix + ".startup.update").orElse(false));
                liquibaseConfigs.add(liquibaseConfig);
            }
        }
    }

    public List<LiquibaseConfig> getLiquibaseConfigs() {
        return liquibaseConfigs;
    }

}
