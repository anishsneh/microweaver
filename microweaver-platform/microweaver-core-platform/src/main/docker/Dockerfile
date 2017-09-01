FROM alpine:3.6
RUN apk update
RUN apk add --no-cache strace
RUN apk add --no-cache sysstat
RUN apk add --no-cache openjdk8
RUN apk add --no-cache htop
RUN apk add --no-cache bind
RUN apk add --no-cache wget
RUN apk add --no-cache tcpdump 
RUN apk add --no-cache bind-tools 
RUN apk add --no-cache curl
RUN apk add --no-cache python
RUN apk add --no-cache py-pip
RUN apk add --no-cache supervisor
RUN apk add --no-cache mysql
RUN pip install -U pip
RUN pip install kubernetes
MAINTAINER Anish Sneh email: anishsneh@anishsneh.com