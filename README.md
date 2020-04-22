# EasyBreezy

Create ```local.properties``` in the resources of src/main and put there the following settings

```
easybreezy.jdbc.url = jdbc:postgresql://localhost:5432/easybreezy
easybreezy.jdbc.user = <your_db_username_or_skip>
easybreezy.jdbc.password = <password_or_skip>

easybreezy.rabbit.uri=amqp://login:password@localhost:5672
easybreezy.rabbit.api=http://localhost:15672
```

## Migrations

#### Generate migration

```bash
./gradlew migrationsGenerate -Pmigname=CreateUsers
```

#### Run migrations in development

```bash
./gradlew migrationsMigrate
```

### Create Default Admin

```bash
./gradlew createDefaultUser
```

### Reformat code
```bash
./gradlew ktlintFormat
```
### Build Doc from Postman
```
docgen build -i input-postman-collection.json -o ~/Downloads/index.md -m
```
