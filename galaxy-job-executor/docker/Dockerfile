ARG JDK_IMAGE
FROM ${JDK_IMAGE}
MAINTAINER galaxy-data-platform
VOLUME /tmp
WORKDIR /home/galaxy/galaxy-job-executor
COPY config config
ARG JAR_FILE
COPY ${JAR_FILE} galaxy-job-executor.jar
ENV JAVA_OPTS=" -Xms1024m -Xmx2048m "
ENV LANG=en_US.UTF-8 LANGUAGE=en_US:en LC_ALL=en_US.UTF-8
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar galaxy-job-executor.jar" ]