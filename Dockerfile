FROM gradle:jdk11 as build

#expose running port of application.
EXPOSE 9000

#Create base app folder
ENV APP_HOME /app
RUN mkdir $APP_HOME

#copy source code
COPY --chown=gradle:gradle . $APP_HOME
WORKDIR $APP_HOME

RUN bu

#Copy executable jar
RUN cp build/libs/buaansach-0.0.1-SNAPSHOT.war buaansach-0.0.1-SNAPSHOT.war
ENTRYPOINT java -jar -Dspring.profiles.active=prod buaansach-0.0.1-SNAPSHOT.war