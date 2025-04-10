FROM container-registry.oracle.com/graalvm/native-image:23-ol8 AS builder

WORKDIR /build

COPY . .

# Usa secrets seguros (não visíveis nos layers da imagem final)
RUN --mount=type=secret,id=aws_access_key,target=/run/secrets/aws_access_key \
    --mount=type=secret,id=aws_secret_key,target=/run/secrets/aws_secret_key \
    --mount=type=secret,id=aws_region,target=/run/secrets/aws_region \
    --mount=type=secret,id=dynamo_table,target=/run/secrets/dynamo_table \
    --mount=type=secret,id=sqs_queue,target=/run/secrets/sqs_queue \
    bash -c "\
      export AWS_CLOUD_ACCESS_KEY=\$(cat /run/secrets/aws_access_key) && \
      export AWS_CLOUD_SECRET_KEY=\$(cat /run/secrets/aws_secret_key) && \
      export AWS_CLOUD_REGION=\$(cat /run/secrets/aws_region) && \
      export AWS_CLOUD_DYNAMO_PERSON=\$(cat /run/secrets/dynamo_table) && \
      export AWS_CLOUD_SQS_PERSON=\$(cat /run/secrets/sqs_queue) && \
      ./mvnw --no-transfer-progress -pl domain clean install && \
      ./mvnw --no-transfer-progress -pl springframework -am native:compile -Pnative"

FROM gcr.io/distroless/java-base-debian12

EXPOSE 8080

COPY --from=builder /build/springframework/target/springframework /app

ENTRYPOINT ["/app"]
