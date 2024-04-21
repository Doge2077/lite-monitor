FROM ubuntu:20.04
RUN apt update
RUN apt install -y openjdk-17-jdk
COPY lite-monitor-server/target/lite-monitor-server-0.0.1-SNAPSHOT.jar /home/lite-monitor/server/server.jar
CMD java -jar /home/lite-monitor/server/server.jar
EXPOSE 8080