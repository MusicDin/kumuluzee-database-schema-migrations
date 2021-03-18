package com.kumuluz.ee.migrations.liquibase.tests;

import com.kumuluz.ee.migrations.liquibase.configurations.LiquibaseConfig;
import com.kumuluz.ee.migrations.liquibase.utils.LiquibaseConfigurationUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Tests full and correct Liquibase configuration.
 *
 * @author Din Music
 * @since 1.0.0
 */
public class CorrectConfigurationTest extends Arquillian {

    @Deployment
    public static JavaArchive deployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addAsResource("correct-config.yml", "config.yml")
                .addAsResource("test-changelog.xml", "db/changelog.xml");
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
        Assert.assertEquals("db/changelog.xml", config.getFile());
        Assert.assertEquals(contexts, config.getContexts().toArray());
        Assert.assertEquals(labels, config.getLabels().toArray());
        Assert.assertTrue(config.isStartupDropAll());
        Assert.assertTrue(config.isStartupUpdate());
    }

}
