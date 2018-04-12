#
# Scala and sbt Dockerfile
#
# original file from
# https://github.com/hseeberger/scala-sbt
#

# Pull base image
FROM openjdk:8u162

# Env variables
ENV SCALA_VERSION 2.12.5
ENV SBT_VERSION   1.1.2
ENV APP_NAME      timeslot
ENV APP_VERSION   1.4

# Scala expects this file
RUN touch /usr/lib/jvm/java-8-openjdk-amd64/release

# Install Scala
## Piping curl directly in tar
RUN \
  curl -fsL https://downloads.typesafe.com/scala/$SCALA_VERSION/scala-$SCALA_VERSION.tgz | tar xfz - -C /root/ && \
  echo >> /root/.bashrc && \
  echo "export PATH=~/scala-$SCALA_VERSION/bin:$PATH" >> /root/.bashrc

# Install sbt
RUN \
  curl -L -o sbt-$SBT_VERSION.deb https://dl.bintray.com/sbt/debian/sbt-$SBT_VERSION.deb && \
  dpkg -i sbt-$SBT_VERSION.deb && \
  rm sbt-$SBT_VERSION.deb && \
  apt-get update && \
  apt-get install sbt && \
  sbt sbtVersion

# Define working directory
WORKDIR /root

ENV PROJECT_HOME /usr/src
COPY ["build.sbt", "/tmp/build/"]
COPY ["project/plugins.sbt", "project/build.properties", "/tmp/build/project/"]
RUN cd /tmp/build && \
 sbt update && \
 sbt compile && \
 sbt test && \
 sbt dist

EXPOSE 9000 5005

RUN unzip /tmp/build/target/universal/$APP_NAME-$APP_VERSION.zip
RUN chmod +x $APP_NAME-$APP_VERSION/bin/$APP_NAME
ENTRYPOINT $APP_NAME-$APP_VERSION/bin/$APP_NAME -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005