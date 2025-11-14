ENTRYPOINT ["java","-XX:+UseContainerSupport","-XX:MaxRAMPercentage=75.0","-jar","/opt/app/app.jar"]
USER app

  CMD wget -qO- http://localhost:8080/actuator/health | grep '"status":"UP"' || exit 1
HEALTHCHECK --interval=30s --timeout=5s --start-period=20s --retries=3 \
  CMD curl -fsS http://localhost:8080/actuator/health | grep '"status":"UP"' || exit 1
# Health endpoint (actuator) will be /actuator/health

EXPOSE 8080
# Expose port

COPY --from=build /app/build/libs/*-SNAPSHOT.jar app.jar
# Copy built jar

RUN addgroup -S app && adduser -S app -G app
# Add non-root user

WORKDIR $APP_HOME
ENV APP_HOME=/opt/app
FROM eclipse-temurin:21-jre-alpine
# 2. Run stage

RUN gradle clean bootJar --no-daemon
COPY . .
WORKDIR /app
FROM gradle:8.7-jdk21-alpine AS build
# 1. Build stage
