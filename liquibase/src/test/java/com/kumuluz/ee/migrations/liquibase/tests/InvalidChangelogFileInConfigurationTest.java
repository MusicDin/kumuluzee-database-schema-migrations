package com.kumuluz.ee.migrations.liquibase.tests;

import com.kumuluz.ee.migrations.liquibase.LiquibaseContainerProducer;
import com.kumuluz.ee.migrations.liquibase.annotations.LiquibaseChangelog;
import com.kumuluz.ee.migrations.liquibase.cdi.LiquibaseCdiExtension;
import com.kumuluz.ee.migrations.liquibase.configurations.LiquibaseConfig;
import com.kumuluz.ee.migrations.liquibase.utils.LiquibaseConfigurationUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.ShouldThrowException;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.enterprise.inject.spi.DefinitionException;

/**
 * Tests if invalid Liquibase changelog file is recognized at deployment.
 *
 * @author Din Music
 * @since 1.0.0
 */
@RunWith(Arquillian.class)
public class InvalidChangelogFileInConfigurationTest {

    @Deployment
    @ShouldThrowException(DefinitionException.class)
    public static JavaArchive invalidChangelogFileDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClass(LiquibaseContainerProducer.class)
                .addClass(LiquibaseConfigurationUtil.class)
                .addClass(LiquibaseConfig.class)
                .addClass(LiquibaseChangelog.class)
                .addAsServiceProviderAndClasses(javax.enterprise.inject.spi.Extension.class, LiquibaseCdiExtension.class)
                .addAsResource("invalid-changelog-config.yml", "config.yml");
    }

    @Test
    public void dummyTest() {
        Assert.assertTrue(true);
    }

}
