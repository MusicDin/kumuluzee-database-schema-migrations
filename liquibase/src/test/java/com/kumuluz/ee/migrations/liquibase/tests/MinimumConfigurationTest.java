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
 * Tests minimum configuration containing default values.
 *
 * @author Din Music
 * @since 1.0.0
 */
public class MinimumConfigurationTest extends Arquillian {

    @Deployment
    public static JavaArchive deployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addAsResource("minimum-config.yml", "config.yml")
                .addAsResource("test-changelog.xml", "db/changelog-master.xml");
    }

    public static final String[] emptyArray = new String[0];

    @Test
    public void minimumConfigurationTest() {

        LiquibaseConfigurationUtil configurationUtil = LiquibaseConfigurationUtil.getInstance();
        LiquibaseConfig config = configurationUtil.getLiquibaseConfigs().get(0);

        Assert.assertEquals("jdbc/TestDS", config.getJndiName());
        Assert.assertEquals("db/changelog-master.xml", config.getFile());
        Assert.assertEquals(emptyArray, config.getContexts().toArray());
        Assert.assertEquals(emptyArray, config.getLabels().toArray());
        Assert.assertFalse(config.isStartupDropAll());
        Assert.assertFalse(config.isStartupUpdate());
    }

}
