# Liquibase migrations

Example KumuluzEE configuration in `config.yaml`:
```yaml
kumuluzee:
  datasources:
    - jndi-name: jdbc/example-db
      connection-url: jdbc:postgresql://localhost:5432/example
      username: postgres
      password: postgres
      pool:
        max-size: 20
  migrations:
    enabled: true
    liquibase:
      changelogs:
        - file: db/db.changelog-master.xml
          jndi-name: jdbc/example-db
          startup:
            drop-all: false
            update: true
```