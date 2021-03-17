package com.kumuluz.ee.migrations.liquibase;

import com.kumuluz.ee.migrations.liquibase.cdi.LiquibaseCdiExtension;
import org.jboss.arquillian.container.test.spi.client.deployment.CachedAuxilliaryArchiveAppender;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

/**
 * Packages KumuluzEE Liquibase library as a ShrinkWrap archive and adds it to deployments.
 *
 * @author Din Music
 * @since 1.0.0
 */
public class LiquibaseLibraryAppender extends CachedAuxilliaryArchiveAppender {

    @Override
    protected Archive<?> buildArchive() {

        return ShrinkWrap.create(JavaArchive.class, "kumuluzee-migrations-liquibase.jar")
                .addClass(LiquibaseExtension.class)
//                .addPackages(true, "com.kumuluz.ee.migrations.liquibase")
//                .addPackages(true, "com.kumuluz.ee.migrations.common")
                .addAsServiceProvider(com.kumuluz.ee.common.Extension.class, LiquibaseExtension.class)
                .addAsServiceProvider(javax.enterprise.inject.spi.Extension.class, LiquibaseCdiExtension.class)
                .addAsResource("META-INF/beans.xml");
    }

}
