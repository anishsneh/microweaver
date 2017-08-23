FROM centos:centos7.3.1611
RUN yum install -y epel-release
RUN yum install -y htop
RUN yum install -y bind-utils
RUN yum install -y wget
RUN yum install -y telnet
RUN yum install -y python
RUN yum install -y python-pip
RUN yum install -y supervisor
RUN yum install -y mysql
RUN yum install -y java-1.8.0-openjdk
#RUN wget --no-cookies --no-check-certificate --header "Cookie: oraclelicense=accept-securebackup-cookie" "http://download.oracle.com/otn-pub/java/jdk/8u144-b01/090f390dda5b47b9b721c7dfaa008135/jdk-8u144-linux-x64.rpm" -O jdk-8-linux-x64.rpm
#RUN yum --nogpgcheck localinstall -y jdk-8-linux-x64.rpm
#RUN rm -f jdk-8-linux-x64.rpm
RUN pip install -U pip
RUN pip install kubernetes
MAINTAINER Anish Sneh email: anishsneh@anishsneh.com