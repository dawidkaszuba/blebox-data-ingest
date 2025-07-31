spring:
  kafka:
    bootstrap-servers: localhost:9092
    admin:
      properties:
        bootstrap.servers: localhost:9092

data-ingest:
  kafka:
    raw-data-topic:
      name: "raw-data"
      partitions: "1"
      replicas: "1"
