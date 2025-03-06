FROM ubuntu:24.04

# System installs
RUN apt-get update && apt-get install -y \
    build-essential \
    openjdk-21-jdk

RUN apt-get clean
