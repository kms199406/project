# Step 1: Use an official Gradle image to build the project
FROM gradle:8.5-jdk17 AS builder

# Set the working directory
WORKDIR /app

# 빌드에 필요한 파일들 복사
COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./
COPY src ./src

# Gradle 파일 복사
COPY gradle/gradle-8.5-bin.zip /app/gradle/gradle-8.5-bin.zip

# Firebase 설정 파일 복사
COPY src/main/resources/superb-analog-439512-g8-firebase-adminsdk-l7nbt-2305deb251.json /app/serviceAccountKey.json
COPY src/main/resources/superb-analog-439512-g8-e7979f6854cd.json /usr/share/springboot/superb-analog-439512-g8-e7979f6854cd.json


# gradle-wrapper.properties의 distributionUrl을 로컬 파일 경로로 변경
RUN sed -i 's|https://services.gradle.org/distributions/gradle-8.5-bin.zip|file:///app/gradle/gradle-8.5-bin.zip|' gradle/wrapper/gradle-wrapper.properties

# Gradle Wrapper를 사용하여 빌드 (항상 테스트를 제외)
RUN --mount=type=cache,target=/root/.gradle ./gradlew build -x test --no-daemon

# Step 2: Use an official OpenJDK runtime image to run the app
FROM openjdk:17-jdk-slim

# Set the working directory in the runtime container
WORKDIR /app

# Copy the JAR file from the builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Copy Firebase config from builder stage
COPY --from=builder /app/serviceAccountKey.json /app/serviceAccountKey.json
# Google 인증서 추가
# Google API 인증서 추가
COPY googleapis-root.crt /etc/google/googleapis-root.crt

# Java keystore에 인증서 추가
RUN keytool -importcert -file /etc/google/googleapis-root.crt -alias googleapis-root \
    -keystore $JAVA_HOME/lib/security/cacerts -storepass changeit -noprompt

COPY google.crt /tmp/google.crt

RUN keytool -importcert -file /tmp/google.crt -alias google-cert \
    -keystore $JAVA_HOME/lib/security/cacerts \
    -storepass changeit -noprompt \
    && rm /tmp/google.crt
# wait-for-it.sh 스크립트를 복사
COPY scripts/wait-for-it.sh /app/wait-for-it.sh

RUN mkdir -p /usr/share/elasticsearch/config \
    /usr/share/kibana/config \
    /usr/share/logstash/config \
    /usr/share/logstash/pipeline \
    /usr/share/kafka/config \
    /usr/share/springboot/config

RUN chown -R 1000:1000 /usr/share/elasticsearch/config \
    /usr/share/kibana/config \
    /usr/share/logstash/config \
    /usr/share/logstash/pipeline \
    /usr/share/kafka/config \
    /usr/share/springboot/config

# SSL 인증서 복사
COPY www.projectkkk.pkcs12 /usr/share/elasticsearch/config/www.projectkkk.pkcs12
COPY www.projectkkk.com.pem /usr/share/elasticsearch/config/www.projectkkk.com.pem
COPY www.projectkkk.com.pem /usr/share/kibana/config/www.projectkkk.com.pem
COPY www.projectkkk.pkcs12 /usr/share/logstash/config/www.projectkkk.pkcs12
COPY www.projectkkk.pkcs12 /usr/share/kibana/config/www.projectkkk.pkcs12
COPY kibana.yml /usr/share/kibana/config/kibana.yml
#COPY r10.crt /usr/share/kibana/config/r10.crt
COPY www.projectkkk.pkcs12 /usr/share/kafka/config/www.projectkkk.pkcs12
COPY www.projectkkk.pkcs12 /usr/share/springboot/config/www.projectkkk.pkcs12
COPY www.projectkkk.pkcs12 /app/www.projectkkk.pkcs12
COPY logstash.conf /usr/share/logstash/pipeline/logstash.conf
COPY logstash.yml /usr/share/logstash/config/logstash.yml

RUN chown -R 1000:1000 \
    /usr/share/elasticsearch/config/* \
    /usr/share/kibana/config/* \
    /usr/share/logstash/config/* \
    /usr/share/logstash/pipeline/* \
    /usr/share/kafka/config/* \
    /usr/share/springboot/config/*

RUN chmod 755 /usr/share/elasticsearch/config \
    /usr/share/kibana/config \
    /usr/share/logstash/config \
    /usr/share/logstash/pipeline \
    /usr/share/kafka/config \
    /usr/share/springboot/config

RUN chmod +x /app/wait-for-it.sh
RUN chmod 644 /app/www.projectkkk.pkcs12

# 권한 설정
#RUN chmod 600 /usr/share/elasticsearch/config/elastic-truststore.p12
RUN chmod 600 /usr/share/elasticsearch/config/www.projectkkk.pkcs12
RUN chmod 600 /usr/share/elasticsearch/config/www.projectkkk.com.pem
RUN chmod 600 /usr/share/kibana/config/www.projectkkk.com.pem
RUN chmod 600 /usr/share/logstash/config/www.projectkkk.pkcs12
RUN chmod 600 /usr/share/kibana/config/www.projectkkk.pkcs12
RUN chmod 600 /usr/share/kibana/config/kibana.yml
#RUN chmod 600 /usr/share/kibana/config/r10.crt
RUN chmod 600 /usr/share/kafka/config/www.projectkkk.pkcs12
RUN chmod 600 /usr/share/springboot/config/www.projectkkk.pkcs12
RUN chmod 600 /usr/share/logstash/pipeline/logstash.conf
RUN chmod 600 /usr/share/logstash/config/logstash.yml


# Expose port 443 for the application
EXPOSE 443

# Run the Spring Boot application after waiting for Kafka and Elasticsearch to be ready
ENTRYPOINT ["/app/wait-for-it.sh", "kafka:9092", "--timeout=120", "--", "/app/wait-for-it.sh", "elasticsearch:9200", "--timeout=240", "--", "java", "-Dserver.port=443", "-Dserver.ssl.key-store=/app/www.projectkkk.pkcs12", "-Dserver.ssl.key-store-password=Ccenter123456!", "-Dserver.ssl.key-store-type=PKCS12", "-Djavax.net.ssl.trustStore=/usr/share/elasticsearch/config/www.projectkkk.pkcs12", "-Djavax.net.ssl.trustStorePassword=Ccenter123456!", "-Djavax.net.ssl.trustStoreType=PKCS12", "-jar", "app.jar"]
