FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY ./target/SystemManager-0.0.1.jar ./SystemManager.jar

EXPOSE 8081

#TODO: just for testing purposes
RUN useradd test1
RUN useradd test2
RUN groupadd group1
RUN groupadd group2


CMD ["java", "-jar", "SystemManager.jar"]