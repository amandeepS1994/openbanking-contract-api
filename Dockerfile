FROM gradle:7.5.0-jdk11-alpine as build
RUN mkdir -p /home/spring/banking
WORKDIR /home/spring/banking
COPY --chown=gradle:gradle . .
RUN gradle build --no-daemon -x test


FROM amazoncorretto:11-alpine3.15
ENV port=8081
ENV dbuser=
ENV dbpass=
ENV dbhost=MySQL-Database
ENV dbport=3306
ENV khost=keycloak
ENV kport=8080
ENV kurl=auth
ENV redishost=redis-store
ENV redisport=6379
ENV testnethost=open-banking-testnet
ENV testnetport=8081
RUN mkdir -p /open_banking/
WORKDIR /open_banking/
RUN  ls
RUN cd /open_banking/
COPY --from=build /home/spring/banking/build/libs/ .
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "contract-0.0.1-SNAPSHOT.jar"]
