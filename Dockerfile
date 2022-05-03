FROM openjdk:11
EXPOSE  8086
WORKDIR /app
ADD   ./target/*.jar /app/account-service.jar
ENTRYPOINT ["java","-jar","/app/account-service.jar"]
