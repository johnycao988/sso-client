FROM docker.registry.cscloud.com/frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD ./target/sso-client.jar app.jar   
EXPOSE 3000
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]