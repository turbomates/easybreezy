# EasyBreezy
# ApiDoc
{domain}/webjars/swagger-ui/index.html?url=/api/openapi.json

# Configuration

Create ```local.properties``` in the resources of src/main and put there the following settings

```
easybreezy.jdbc.url = jdbc:postgresql://localhost:5432/easybreezy
easybreezy.jdbc.user = <your_db_username_or_skip>
easybreezy.jdbc.password = <password_or_skip>

easybreezy.rabbit.uri=amqp://login:password@localhost:5672
easybreezy.rabbit.api=http://localhost:15672
```

#### [Setup Ide Code style](https://github.com/pinterest/ktlint#option-3)

## Useful commands

#### Generate migration

```bash
./gradlew migrationsGenerate -Pmigname=CreateUsers
```

#### Run migrations in development

```bash
./gradlew migrationsMigrate
```

#### Create Default Admin

```bash
./gradlew createDefaultAdmin
```

#### Format code
```bash
./gradlew ktlintFormat
```


## Conventions
### Exceptions
```
Use require() to check condition and throw exception
Use your custom exceptions if you need to throw it more than once
```
