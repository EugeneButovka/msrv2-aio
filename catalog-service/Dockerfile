FROM fabric8/java-alpine-openjdk8-jre
ADD ["target/catalog-service-0.0.1-SNAPSHOT-exec.jar", "app.jar"]
EXPOSE 8181 8787
ENV JAVA_OPTS="-Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=8787,suspend=n"
RUN sh -c 'touch /app.jar'
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Dspring.profiles.active=docker -jar /app.jar" ]
