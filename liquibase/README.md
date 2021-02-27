# KumuluzEE Migrations

> KumuluzEE Migrations project for database migration using Liquibase.

KumuluzEE Migrations is a migration project for the KumuluzEE microservice framework.  

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

### Configuring migrations at startup

Liquibase migrations are configured with the common KumuluzEE configuration framework.
Configuration properties can be defined with the environment variables or with the configuration files.
For more details see the [KumuluzEE configuration wiki page](https://github.com/kumuluz/kumuluzee/wiki/Configuration) 
and [KumuluzEE Config](https://github.com/kumuluz/kumuluzee-config).

In order to be able to use Liquibase migrations, at least one datasource also needs to be configured.

KumuluzEE config example:
```yaml
kumuluzee:
  # Datasource configurations
  datasources:
    - jndi-name: jdbc/example-db
      connection-url: jdbc:postgresql://localhost:5432/example
      username: postgres
      password: postgres
  # Liquibase configurations 
  migrations:
    enabled: true                             # default: true
    liquibase:
      changelogs:
        - jndi-name: jdbc/example-db          # required, has to match with datasource's jndi-name
          file: db/changelog-master.xml       # default: "db/changelog-master.xml"
          startup:
            drop-all: false                   # default: false  
            update: true                      # default: false
```

Alternatively Liquibase actions can also be executed during application runtime by injecting `LiquibaseContainer` as 
it is shown in the following example:
```java
// Injects liquibase container if only 1 liquibase configuration is provided in config file
@Inject
private LiquibaseContainer liquibaseContainer;

public void dropAll(){

    Liquibase liquibase = liquibaseContainer.createLiquibase();

    try {
        liquibase.dropAll();
        liquibase.validate();
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
}
```