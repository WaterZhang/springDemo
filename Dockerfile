FROM adoptopenjdk/openjdk11:latest

RUN mkdir /opt/app
WORKDIR /opt/app
COPY /build/libs/springdemo-1.0.war /opt/app/springdemo.war
COPY zzm.jks /opt/app/zzm.jks
EXPOSE 8443
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=dev", "/opt/app/springdemo.war"]