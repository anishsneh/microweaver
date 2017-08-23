FROM openjdk:8-jdk-alpine
RUN apk add --no-cache htop
RUN apk add --no-cache bind
RUN apk add --no-cache wget
RUN apk add --no-cache python
RUN apk add --no-cache py-pip
RUN apk add --no-cache supervisor
RUN apk add --no-cache mysql
RUN pip install -U pip
RUN pip install kubernetes
MAINTAINER Anish Sneh email: anishsneh@anishsneh.com