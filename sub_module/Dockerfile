# docker build -t training:0.0.1 .
# docker run --name test-module -p 80:8080 training:0.0.1

# Sets the base image for subsequent instructions
# A valid Dockerfile must start with a FROM
# Only ARG may precede this instruction
#FROM openjdk:8-jdk-alpine
#FROM postgres:12
# https://hub.docker.com/_/postgres
# Postgres Dockerfile
# https://github.com/docker-library/postgres/blob/88173efa530f1a174a7ea311c5b6ee5e383f68bd/12/Dockerfile
# Run with -e POSTGRES_PASSWORD=<password> -p <host_port>:5432

#ARG POSTGRES_PASSWORD=testpswd
#RUN /docker-entrypoint.sh

FROM postest:12

USER root

RUN apt-get update && \
    DEBIAN_FRONTEND=noninteractive \
    apt-get -y install default-jre-headless && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

RUN addgroup spring && adduser --ingroup spring --gecos "" --disabled-password spring
COPY --chown=spring:spring entrypoint.sh /usr/local/bin/

USER spring:spring

# A build-time variable that can be passed via
#    docker build --build-arg <varname>=<value>
# Visible for all subsequent instructions
#  during the current (!) build stage
# Running containers, CMD and ENTRYPOINT
#  can’t access values of ARG variables
# Accepts default value
ARG JAR_FILE=build/libs/*.jar

WORKDIR /opt/app

# Copies new files to the filesystem of the container
COPY ${JAR_FILE} app.jar

# An environment variable that can be passed via
#    docker run -e "<env_varname>=<value>"
# Persists for all subsequent instructions in the build stage
#  and in a container created from the resulting image
ENV LOG_PATH="/log" \
    CONF_PATH="/config"

# Creates a mount point and marks it as holding externally
#  mounted volumes from native host or other containers
# Identical to -v HOST_DIR:CON_DIR without specifying the host dir
#  meaning a new directory wil be created for the volume
# Underlying folder path can be found via
#    docker inspect -f {{.Mounts}} <container_id>
VOLUME $LOG_PATH
VOLUME $CONF_PATH

# Serves as documentation about which ports are intended to be published
# Listens on TCP (default) or UDP e.g. EXPOSE 80/udp
# Actually published when running the container using
#  1. docker run -p <host_port>:<container_port>/tcp (map ports manually)
#  2. docker run -P (publish all exposed ports to a random port on the host)
EXPOSE 8080

# Allows you configure a container that will run as an executable
# The exec form (preferred):
#    ENTRYPOINT ["executable", "param1", "param2"]
# The shell form:
#    ENTRYPOINT command param1 param2
#    - executes as a subcommand of /bin/sh -c
#    - does not receive Unix signals
#    - ignores any CMD or docker run command line arguments
# Only the last ENTRYPOINT instruction will have an effect
ENTRYPOINT ["/usr/local/bin/entrypoint.sh"]
