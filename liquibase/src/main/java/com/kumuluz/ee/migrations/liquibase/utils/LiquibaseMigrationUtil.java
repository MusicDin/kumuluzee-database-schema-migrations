package com.kumuluz.ee.migrations.liquibase.utils;

import com.kumuluz.ee.migrations.common.MigrationUtil;
import com.kumuluz.ee.migrations.liquibase.LiquibaseContainer;
import com.kumuluz.ee.migrations.liquibase.configurations.LiquibaseConfig;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;

import java.util.List;

/**
 * Executes Liquibase migrations.
 *
 * @author Din Music
 * @since 1.0.0
 */
public class LiquibaseMigrationUtil extends MigrationUtil {

    public LiquibaseMigrationUtil() {
        super();
    }

    @Override
    public void migrate() {

        List<LiquibaseConfig> liquibaseConfigs = LiquibaseConfigurationUtil.getInstance().getLiquibaseConfigs();

        for (LiquibaseConfig liquibaseConfig : liquibaseConfigs) {

            LiquibaseContainer liquibaseContainer = new LiquibaseContainer(liquibaseConfig.getJndiName());
            Liquibase liquibase = liquibaseContainer.createLiquibase();

            try {
                // startup dropAll
                if (liquibaseConfig.isStartupDropAll()) {
                    liquibase.dropAll();
                }

                // startup update
                if (liquibaseConfig.isStartupUpdate()) {

                    Contexts contexts = new Contexts(liquibaseConfig.getContexts());
                    LabelExpression labelExpressions = new LabelExpression(liquibaseConfig.getLabels());

                    liquibase.update(contexts, labelExpressions);
                    liquibase.validate();
                }

                liquibase.close();

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }

}
