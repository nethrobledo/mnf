FROM eclipse-temurin:17
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} recipe.jar
ENTRYPOINT ["java","-jar","/recipe.jar"]