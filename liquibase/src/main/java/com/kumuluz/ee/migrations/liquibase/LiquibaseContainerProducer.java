package com.kumuluz.ee.migrations.liquibase;

import com.kumuluz.ee.migrations.liquibase.annotations.LiquibaseChangelog;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

@ApplicationScoped
public class LiquibaseContainerProducer {

    @Produces
    public LiquibaseContainer produceLiquibaseContainer(InjectionPoint injectionPoint) {

        String jndiName = "";

        if (injectionPoint.getAnnotated().isAnnotationPresent(LiquibaseChangelog.class)) {
            jndiName = injectionPoint.getAnnotated().getAnnotation(LiquibaseChangelog.class).jndiName();
        }

        return new LiquibaseContainer(jndiName);
    }

}
