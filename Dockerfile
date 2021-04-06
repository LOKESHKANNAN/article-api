FROM centos


RUN yum install -y java


RUN yum install sqlite


VOLUME /tmp

ADD article-api-0.0.1-SNAPSHOT.jar article-api-docker.jar

RUN sh -c 'touch /article-api-docker.jar'

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/article-api-docker.jar"] 