package com.kumuluz.ee.migrations.liquibase.utils;

import com.kumuluz.ee.migrations.MigrationUtil;
import com.kumuluz.ee.migrations.liquibase.LiquibaseContainer;
import com.kumuluz.ee.migrations.liquibase.configurations.LiquibaseConfig;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;

import java.util.List;

public class LiquibaseMigrationUtil extends MigrationUtil {

    public LiquibaseMigrationUtil() {
        super();
    }

    // TODO: Add support for FileSystemResourceAccessor if data is not accessible via ClassLoaderResourceAccessor

    @Override
    public void migrate() {

        List<LiquibaseConfig> liquibaseConfigs = LiquibaseConfigurationUtil.getLiquibaseConfigs();

        for (LiquibaseConfig liquibaseConfig : liquibaseConfigs) {

            LiquibaseContainer liquibaseContainer = new LiquibaseContainer(liquibaseConfig.getJndiName());
            Liquibase liquibase = liquibaseContainer.createLiquibase();

            try {
                // startup dropAll
                if (liquibaseConfig.isStartupDropAll()) {
                    liquibase.dropAll();
                    liquibase.validate();
                }

                // startup update
                if (liquibaseConfig.isStartupUpdate()) {
                    liquibase.update(new Contexts(), new LabelExpression());
                    liquibase.validate();
                }

                liquibase.close();

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }

}
