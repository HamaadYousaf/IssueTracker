FROM openjdk:17
EXPOSE 8080
ADD target/issue-tracker-images.jar issue-tracker-images.jar
ENTRYPOINT ["java", "-jar", "/issue-tracker-images.jar"]