# Siteminder Email Sender Exercise
## How to Run Locally
Use your IDE to build the project or run `mvn clean install`


Execute the main class `EmailSenderApplication` to allow the API to listen and respond to requests

## How to Deploy Using Docker, Docker Hub and Render

### Generating the jar file
To package your application with Maven run: `mvn clean package`

Maven will create your jar file in a directory called `/target`

### Adding / Modifying the Docker file
This dockerfile will be used to create the docker image that we will deploy on Render
```
FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

### Building a docker image from our Spring project
It is recommended to have Docker or Docker Desktop installed separately

Run: `docker build -t emailsender .` 

Once successful, your image will also show up on Docker Desktop:

![](docker-image.png)

### Uploading our docker image to Docker Hub
Create an account on Docker Hub (same credentials as Docker works) and also create a repository

Login to Docker Hub using the terminal in docker desktop ```login -u YOUR-USER-NAME```

Alternatively you can also click login when prompted in docker desktop

After that use the docker tag command to give our emailsender service image a new name.

```
docker tag emailsender YOUR-USER-NAME/sm
```

We can then push our renamed image onto Docker Hub

```
docker push YOUR-USER-NAME/sm:latest
```

### Deploying on Render
Create an account and click
