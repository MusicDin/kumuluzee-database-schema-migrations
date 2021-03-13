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
 * Tests minimum configuration containing default values.
 *
 * @author Din Music
 * @since 1.0.0
 */
@RunWith(Arquillian.class)
public class MinimumConfigurationTest {

    @Deployment
    public static JavaArchive deployment(){
        return ShrinkWrap.create(JavaArchive.class)
                .addClass(LiquibaseConfigurationUtil.class)
                .addClass(LiquibaseConfig.class)
                .addAsResource("minimum-config.yml", "config.yml");
    }

    public static final String[] emptyArray = new String[0];

    @Test
    public void minimumConfigurationTest() {

        LiquibaseConfigurationUtil configurationUtil = LiquibaseConfigurationUtil.getInstance();
        LiquibaseConfig config = configurationUtil.getLiquibaseConfigs().get(0);

        Assert.assertEquals("jdbc/TestDS", config.getJndiName());
        Assert.assertEquals("db/changelog-master.xml", config.getFile());
        Assert.assertArrayEquals(emptyArray, config.getContexts().toArray());
        Assert.assertArrayEquals(emptyArray, config.getLabels().toArray());
        Assert.assertFalse(config.isStartupDropAll());
        Assert.assertFalse(config.isStartupUpdate());
    }

}
