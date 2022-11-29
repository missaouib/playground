# ----------------------------------------------------------------------------------------------------------------------
# 预构建
# ----------------------------------------------------------------------------------------------------------------------
ARG BASE_IMG

FROM $BASE_IMG AS builder

WORKDIR /tmp

COPY *.jar app.jar

RUN java -Djarmode=layertools -jar /tmp/app.jar extract && \
    rm -rf /tmp/dependencies/BOOT-INF/lib/java-boot-jarmode-layertools-*.jar && \
    rm -rf /tmp/application/BOOT-INF/classpath.idx && \
    rm -rf /tmp/application/BOOT-INF/layers.idx && \
    rm -rf /tmp/application/META-INF/maven

# ----------------------------------------------------------------------------------------------------------------------
# 构建
# ----------------------------------------------------------------------------------------------------------------------
FROM $BASE_IMG AS main

LABEL maintainer="应卓 yingzhor@gmail.com"

USER root:root

WORKDIR /opt

COPY docker-entrypoint.sh /bin/docker-entrypoint.sh

RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.aliyun.com/g' /etc/apk/repositories && \
    apk add --no-cache fontconfig ttf-dejavu && \
    mkdir -p /opt/log/ && \
    mkdir -p /opt/lib/ && \
    mkdir -p /opt/data/ && \
    chmod +x /bin/docker-entrypoint.sh

COPY --from=builder /tmp/dependencies/ ./
COPY --from=builder /tmp/spring-boot-loader/ ./
COPY --from=builder /tmp/snapshot-dependencies/ ./
COPY --from=builder /tmp/spring-turbo/ ./
COPY --from=builder /tmp/application/ ./

ENV SPRING_PROFILES_ACTIVE=docker
ENV SPRING_PID_FILE=/opt/data/pid

STOPSIGNAL 15

EXPOSE 8080 8443

VOLUME /opt

ENTRYPOINT ["docker-entrypoint.sh"]