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
        - jndi-name: jdbc/example-db-1        # required, has to match with datasource's jndi-name
          file: db/db.changelog-master-1.xml  # default: "db/changelog-master.xml"
          contexts: context1, context2        # default: ""
          labels: label1 and label2, label3   # default: "" ("," (comma) is same as "or")
          startup:
            drop-all: false                   # default: false  
            update: true                      # default: false
```

Example of `LiquibaseContainer` injection:
```java

// Injects liquibase container if only 1 liquibase configuration is provided in config file
@Inject
private LiquibaseContainer firstLiquibaseConfigContainer;

// Also injects liquibase container if only 1 liquibase configuration is provided in config file
@Inject
@LiquibaseChangelog
private LiquibaseContainer alsoFirstLiquibaseConfigContainer;

// Injects liquibase container from configuration with provided jndi name
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