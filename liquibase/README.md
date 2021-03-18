# KumuluzEE Migrations

> KumuluzEE Migrations project for database schema migrations using Liquibase.

KumuluzEE Migrations is a migration project for the KumuluzEE microservice framework provides a simple way to migrate 
database schemas using [Liquibase](https://docs.liquibase.com). It supports migrations at application startup or 
in runtime when application is already running.

## Usage

You can enable KumuluzEE Migrations with Liquibase by adding the following dependency:
```xml
<dependency>
    <groupId>com.kumuluz.ee.migrations</groupId>
    <artifactId>kumuluzee-migrations-liquibase</artifactId>
    <version>${kumuluzee-migrations.version}</version>
</dependency>
```

This extension also requires **at least one data source** to be configured. 
For example PostgreSQL can be used by adding the following dependency:
```xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <version>${postgresql.version}</version>
</dependency>
```

### Configuring migrations

Liquibase migrations are configured with the common KumuluzEE configuration framework.
Configuration properties can be defined with the environment variables or with the configuration file.
For more details see the [KumuluzEE configuration wiki page](https://github.com/kumuluz/kumuluzee/wiki/Configuration) 
and [KumuluzEE Config](https://github.com/kumuluz/kumuluzee-config).

In order to be able to use Liquibase migrations, **at least one datasource** needs to be configured.

Only required Liquibase configuration property is `jndi-name` which has to match a JNDI name of the preconfigured 
datasource.

Minimum Liquibase migration configuration (excluding datasource configuration):
```yaml
kumuluzee:
  migrations:
    liquibase:
      changelogs:
        - jndi-name: jdbc/example-db    # Required
```

In order to provide custom changelog file's location, `file` property can be used (default value is 
`db/changelog-master.xml`). Location needs to be a relative to the `resource` directory.

Sample configuration:
```yaml
kumuluzee:
  migrations:
    liquibase:
      changelogs:
        - jndi-name: jdbc/example-db
          file: db/changelog-master.xml       # default: "db/changelog-master.xml"
```

#### Configuring migrations at startup

There are two actions that can be executed at application startup. One being `dropAll` and another one being `update`.
Action `dropAll` will drop the database and action `update` will update the database according to the changelog 
on location provided in `file` property. Note that action `dropAll` will be executed before `update` if both are enabled.

Example configuration:
```yaml
kumuluzee:
  migrations:
    liquibase:
      changelogs:
        - jndi-name: jdbc/example-db
          startup:
            drop-all: false                   # default: false  
            update: true                      # default: false
```

#### Disabling migrations

In order to disable application startup migrations, set `kumuluzee.migrations.enabled` to false (default is true). 
```yaml
kumuluzee:
  migrations:
    enabled: false  # default: true
```

#### Contexts and labels

Trough contexts and labels Liquibase provides a way to selectively execute changeSets.
Contexts allow selecting certain changeSets to be executed, while labels provide a way of 
selecting changeSets to be executed with complex expressions 
(note that *comma* (",") in labels means the same as operator *or*). 

Both contexts and labels can be configured in KumuluzEE configuration file.

Example configuration:
```yaml
kumuluzee:
  migrations:
    liquibase:
      changelogs:
        - jndi-name: jdbc/example-db
          labels: "label1 and !label2, label3"     # default: ""
          contexts: "context1, context2"          # default: ""
```

Note that if contexts are not specified they will be ignored by Liquibase. Same applies for labels.

### Migrations using CDI

KumuluzEE migrations provide LiquibaseContainer, a wrapper for Liquibase object, which can be injected using CDI. 
Liquibase object provides a way to execute schema migrations over connected data source in runtime. 

LiquibaseContainer is created by firstly selecting appropriate configuration based on provided JNDI name in 
@LiquibaseChangelog annotation and then connecting it to matching data source.
Annotation can also be omitted if only one Liquibase migration is specified in KumuluzEE configuration file.

Example:
```java
/*
 * Injects LiquibaseContainer if only 1 liquibase 
 * configuration is provided in config file.
 */
@Inject
private LiquibaseContainer liquibaseContainer;

/* 
 * Injects LiquibaseContainer if only 1 liquibase 
 * configuration is provided in config file.
 */
@Inject
@LiquibaseChangelog
private LiquibaseContainer liquibaseContainer2;

/* 
 * Injects LiquibaseContainer for changelog with JNDI
 * name provided in annotation's argument.
 */
@Inject
@LiquibaseChangelog(jndiName = "jdbc/example-db")
private LiquibaseContainer liquibaseContainer3;

public void dropAll() throws LiquibaseException {

    Liquibase liquibase = liquibaseContainer.createLiquibase();

    liquibase.dropAll();
    liquibase.validate();

}
```