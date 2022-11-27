FROM maven:3.8.4-openjdk-11 AS builder
COPY . .
RUN mvn package

FROM openjdk:11-jre-slim
RUN echo '#!/bin/sh\nexec java -jar /opt/LintedData.jar "$@"' >> /bin/LintedData && chmod +x /bin/LintedData
COPY --from=builder target/LintedData.jar /opt/LintedData.jar
