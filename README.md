# splitly-core

Stack: Java 21, Spring-Boot 3.1.5, Gradle

## Getting Started

Build the project with:

```shell
./gradlew jar
```

Create the docker image with:

```shell
docker build -t splitly-core:latest .
```
Run db image

```shell
docker-compose -f docker-compose-dev.yml up -d
```

Run the docker image with:

```shell
docker run -e "HELLO_WORLD=bla-bla-bla" --name splitly-core -p 8080:8080 --rm splitly-core:latest
```

Ckeck the app with:

```shell
curl http://localhost:8080
```

## Environment Variables

| Name              | Default value | Required | Description                                                             |
|:------------------|:--------------|:--------:|:------------------------------------------------------------------------|
| HELLO_WORLD       | default_value |  false   | Test value to check if env vars works                                   |
| DB_URL            |               |   true   | DB url for localhost jdbc:postgresql://localhost:5432/splitly_db        |
| DB_DDL_USERNAME   |               |   true   | User to change the database structure                                   |
| DB_DDL_PASSWORD   |               |   true   | password for  "DB_DDL_USERNAME"                                         |
| DB_DML_PASSWORD   |               |   true   | User for data manipulation                                              |
| DB_DML_PASSWORD   |               |   true   | password for  "DB_DML_PASSWORD"                                         |
| DB_ADMIN_PASSWORD |               |   true   | the password for the database administrator is the default user "admin" |


