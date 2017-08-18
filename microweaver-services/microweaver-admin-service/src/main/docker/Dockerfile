FROM microweaver/base:0.0.1-SNAPSHOT
ARG finalName
ADD $finalName /application.jar
ENTRYPOINT ["java","-jar","/application.jar"]