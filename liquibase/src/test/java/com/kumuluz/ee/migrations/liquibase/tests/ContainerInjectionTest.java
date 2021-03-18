package com.kumuluz.ee.migrations.liquibase.tests;

import com.kumuluz.ee.migrations.liquibase.LiquibaseContainer;
import com.kumuluz.ee.migrations.liquibase.tests.beans.AnnotatedLiquibaseContainer;
import com.kumuluz.ee.migrations.liquibase.tests.beans.EmptyAnnotatedLiquibaseContainer;
import com.kumuluz.ee.migrations.liquibase.tests.beans.UnannotatedLiquibaseContainer;
import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.exception.LiquibaseException;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.enterprise.inject.spi.CDI;

/**
 * Tests {@link LiquibaseContainer} injection.
 *
 * @author Din Music
 * @since 1.0.0
 */
public class ContainerInjectionTest extends Arquillian {

    @Deployment
    public static JavaArchive deployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClass(UnannotatedLiquibaseContainer.class)
                .addClass(EmptyAnnotatedLiquibaseContainer.class)
                .addClass(AnnotatedLiquibaseContainer.class)
                .addAsResource("correct-config.yml", "config.yml")
                .addAsResource("test-changelog.xml", "db/changelog.xml")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Test
    public void unannotatedContainerNoExceptionTest() throws LiquibaseException {

        UnannotatedLiquibaseContainer bean = CDI.current().select(UnannotatedLiquibaseContainer.class).get();
        Liquibase liquibase = bean.getLiquibase();

        Assert.assertNotNull(liquibase);

        Contexts initContext = new Contexts("init");
        liquibase.update(initContext);
        liquibase.dropAll();
    }

    @Test
    public void annotatedContainerWithNoParameterNoExceptionTest() throws LiquibaseException {

        EmptyAnnotatedLiquibaseContainer liquibaseContainer = CDI.current().select(EmptyAnnotatedLiquibaseContainer.class).get();
        Liquibase liquibase = liquibaseContainer.getLiquibase();

        Assert.assertNotNull(liquibase);

        Contexts initContext = new Contexts("init");
        liquibase.update(initContext);
        liquibase.dropAll();
    }

    @Test
    public void annotatedContainerNoExceptionTest() throws LiquibaseException {

        AnnotatedLiquibaseContainer liquibaseContainer = CDI.current().select(AnnotatedLiquibaseContainer.class).get();
        Liquibase liquibase = liquibaseContainer.getLiquibase();

        Assert.assertNotNull(liquibase);

        Contexts initContext = new Contexts("init");
        liquibase.update(initContext);
        liquibase.dropAll();
    }

    @Test(expectedExceptions = LiquibaseException.class)
    public void noTableExceptionTest() throws LiquibaseException {

        System.out.println("Executing test...");

        AnnotatedLiquibaseContainer liquibaseContainer = CDI.current().select(AnnotatedLiquibaseContainer.class).get();
        Liquibase liquibase = liquibaseContainer.getLiquibase();

        Assert.assertNotNull(liquibase);

        Contexts errorContext = new Contexts("error");
        System.out.println("Dropping...");
        liquibase.dropAll();
        System.out.println("All dropped.");
        System.out.println("Updating...");
        liquibase.update(errorContext);
        System.out.println("Updated.");

        System.out.println("Test executed");
    }

}
