FROM docker.registry.cscloud.com/frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD ./target/sso-client.jar app.jar
ENV TEST.ENV0 TEST.JAVA1
ENV TEST.ENV1 TEST.ENV1
ENV TEST.ENV2 TEST.ENV2
EXPOSE 3000
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom -DTEST.ENV0=TEST.JAVA2","-jar","/app.jar"]