FROM eclipse-temurin:21-jdk

WORKDIR /app

# Must manually copy the jar from the build context
# to the file where this dockerfile is located for the next line to work
# TODO - maybe figure out how to do this automatically maven package outputDir or custom plugin?
COPY ./SystemManager-0.0.1.jar ./SystemManager.jar

EXPOSE 8081

CMD ["java", "-jar", "SystemManager.jar"]