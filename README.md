# Issue Tracking System

An API for an issue tracking system with users able to create,update,delete,assign,comment, and view issues with
multiple
search filters.

Used GitHub Actions to automatically build the project using Maven and build/push the docker image of the project to
DockerHub.

## Docker Command

`docker run -e DATABASE_URL=jdbc:postgresql://172.17.0.2:5432/issueTracker -e DATABASE_USERNAME=postgres -e DATABASE_PASSWORD=postgres -p 8080:8080 hamaady786/issue-tracker-images`

## Tech

- `Spring Boot`
- `Maven`
- `PostgreSQL`
- `JUnit/Mockito`
- `Spring Data JPA`
- `Criteria Builder`
- `Docker`
- `GitHub Actions`
