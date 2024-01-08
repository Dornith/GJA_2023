FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY ./target/SystemManager-0.0.1.jar ./SystemManager.jar

EXPOSE 8081

#TODO: just for testing purposes
RUN useradd -p passwd test1
RUN useradd -p passwd test2
RUN groupadd group1
RUN groupadd group2
RUN usermod -a -G group1 test1
RUN usermod -a -G group2 test2

CMD ["java", "-jar", "SystemManager.jar"]