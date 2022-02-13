FROM openjdk:11
WORKDIR /
ADD /build/libs/university-service-0.0.1-SNAPSHOT.jar university-service.jar
USER root
EXPOSE 8080
CMD java -Xms512M -Xmx512M -jar -Dspring.profiles.active=prod university-service.jar