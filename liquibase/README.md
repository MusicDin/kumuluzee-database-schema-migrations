# Liquibase migrations

Example KumuluzEE configuration in `config.yaml`:
```yaml
kumuluzee:
  # Datasource configurations
  datasources:
    - jndi-name: jdbc/example-db-1
      connection-url: jdbc:postgresql://localhost:5432/example1
      username: postgres
      password: postgres
      pool:
        max-size: 20
    - jndi-name: jdbc/example-db-2
      connection-url: jdbc:postgresql://localhost:5433/example2
      username: postgres
      password: postgres
      pool:
        max-size: 20
  # Liquibase configurations 
  migrations:
    enabled: true                             # default: true
    liquibase:
      changelogs:
          # Note: Liquibase "jndi-name" has to match with datasource's jndi-name
        - jndi-name: jdbc/example-db-1
          file: db/db.changelog-master-1.xml  # default: "db/changelog-master.xml"
          startup:
            drop-all: false                   # default: false  
            update: true                      # default: false
```

Example of `LiquibaseContainer` injection:
```java

// Injects liquibase configuration if only 1 liquibase configuration is provided 
@Inject
private LiquibaseContainer firstLiquibaseConfigContainer;

// Injects liquibase configuration if only 1 liquibase configuration is provided
@Inject
@LiquibaseChangelog
private LiquibaseContainer alsoFirstLiquibaseConfigContainer;

// Injects container from configuration with provided jndi name
@Inject
@LiquibaseChangelog(jndiName = "jdbc/example-db-1")
private LiquibaseContainer liquibaseContainer;

public void dropAll(){
    Liquibase liquibase = liquibaseContainer.createLiquibase();
    try {
        liquibase.dropAll();
        liquibase.validate();
        liquibase.getDatabase().getConnection().close();
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
}
```