package com.kumuluz.ee.migrations.liquibase.tests;

import com.kumuluz.ee.migrations.liquibase.configurations.LiquibaseConfig;
import com.kumuluz.ee.migrations.liquibase.utils.LiquibaseConfigurationUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests full and correct Liquibase configuration.
 *
 * @author Din Music
 * @since 1.0.0
 */
@RunWith(Arquillian.class)
public class CorrectConfigurationTest {

    @Deployment
    public static JavaArchive deployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClass(LiquibaseConfigurationUtil.class)
                .addClass(LiquibaseConfig.class)
                .addAsResource("correct-config.yml", "config.yml");
    }

    private static final String[] contexts = {
            "context1",
            "context2"
    };

    private static final String[] labels = {
            "label1 and label2",
            "label3"
    };

    @Test
    public void configurationUtilTest() {

        LiquibaseConfigurationUtil configurationUtil = LiquibaseConfigurationUtil.getInstance();
        LiquibaseConfig config = configurationUtil.getLiquibaseConfigs().get(0);

        Assert.assertEquals("jdbc/TestDS", config.getJndiName());
        Assert.assertEquals("db/test-changelog.xml", config.getFile());
        Assert.assertArrayEquals(contexts, config.getContexts().toArray());
        Assert.assertArrayEquals(labels, config.getLabels().toArray());
        Assert.assertTrue(config.isStartupDropAll());
        Assert.assertTrue(config.isStartupUpdate());
    }

}
