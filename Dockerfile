FROM gradle:jdk11 as build

#Create base app folder
ENV APP_HOME /app
RUN mkdir $APP_HOME

#copy source code
COPY --chown=gradle:gradle . $APP_HOME
WORKDIR $APP_HOME

RUN gradle build -x test --no-daemon

#Copy executable jar
FROM gradle:jdk11
ENV TZ 'Asia/Ho_Chi_Minh'

#expose running port of application.
EXPOSE 9000

#Create base app folder
ENV APP_HOME /app
RUN mkdir $APP_HOME

#copy source code
COPY --chown=gradle:gradle . $APP_HOME
WORKDIR $APP_HOME

COPY --from=build /app/build/libs/buaansach-0.0.1-SNAPSHOT.war buaansach-0.0.1-SNAPSHOT.war
ENTRYPOINT java -jar buaansach-0.0.1-SNAPSHOT.war