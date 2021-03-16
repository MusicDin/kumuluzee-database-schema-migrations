package com.kumuluz.ee.migrations.liquibase.tests;

import com.kumuluz.ee.migrations.liquibase.LiquibaseContainerProducer;
import com.kumuluz.ee.migrations.liquibase.annotations.LiquibaseChangelog;
import com.kumuluz.ee.migrations.liquibase.cdi.LiquibaseCdiExtension;
import com.kumuluz.ee.migrations.liquibase.configurations.LiquibaseConfig;
import com.kumuluz.ee.migrations.liquibase.utils.LiquibaseConfigurationUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.ShouldThrowException;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.enterprise.inject.spi.DefinitionException;

/**
 * Tests if invalid Liquibase data source JNDI name is recognized at deployment.
 *
 * @author Din Music
 * @since 1.0.0
 */
public class InvalidJndiNameInConfigurationTest extends Arquillian {

    @Deployment
    @ShouldThrowException(DefinitionException.class)
    public static JavaArchive invalidJNDINameDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClass(LiquibaseConfigurationUtil.class)
                .addClass(LiquibaseConfig.class)
                .addClass(LiquibaseChangelog.class)
                .addClass(LiquibaseContainerProducer.class)
                .addAsServiceProviderAndClasses(javax.enterprise.inject.spi.Extension.class, LiquibaseCdiExtension.class)
                .addAsResource("invalid-jndi-name-config.yml", "config.yml");
    }

    @Test
    public void dummyTest() {
        Assert.assertTrue(true);
    }

}
