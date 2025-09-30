FROM eclipse-temurin:21-jdk

RUN useradd -r -u 1001 -m -d /home/appuser appuser \
    && mkdir -p /opt/blebox-data-ingest \
    && chown -R appuser:appuser /opt/blebox-data-ingest

COPY --chown=appuser:appuser ./target/*.jar /opt/blebox-data-ingest/blebox-data-ingest.jar

USER appuser
EXPOSE 8080
WORKDIR /opt/blebox-data-ingest
ENTRYPOINT ["java", "-jar", "/opt/blebox-data-ingest/blebox-data-ingest.jar"]
