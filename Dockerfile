FROM openjdk:11-jdk

COPY build /build

EXPOSE 8080

ENTRYPOINT ["java","-jar","/build/libs/euro20_21-0.0.1-SNAPSHOT.jar", "-Xmx256m"]