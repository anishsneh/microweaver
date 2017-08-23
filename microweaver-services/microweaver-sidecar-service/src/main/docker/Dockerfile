FROM microweaver/base:0.0.1-SNAPSHOT
ARG finalName
ARG wheelName
ADD $finalName /application.jar
COPY $wheelName /$wheelName
COPY ./etc /etc
RUN pip install /$wheelName
ENTRYPOINT ["supervisord", "-c", "/etc/supervisord.conf"]