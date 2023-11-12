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
Run the docker image with:
```shell
docker run -e "HELLO_WORLD=bla-bla-bla" --name splitly-core -p 8080:8080 --rm splitly-core:latest
```
Ckeck the app with:
```shell
curl http://localhost:8080
```

## Environment Variables
| Name         | Default value  | Required | Description                           |
|:-------------|:---------------|:--------:|:--------------------------------------|
| HELLO_WORLD  | default_value  |  false   | Test value to check if env vars works |


