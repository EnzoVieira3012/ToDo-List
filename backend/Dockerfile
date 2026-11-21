# =============================================================================
# Backend - Multi-stage Docker Build
# =============================================================================
# Stage 1: Build com Maven + JDK 21
# =============================================================================
FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /app

# Copiar apenas pom.xml primeiro para cache de dependências
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .
RUN chmod +x mvnw

# Baixar dependências (cache layer)
RUN ./mvnw dependency:go-offline -B

# Copiar código fonte
COPY src ./src

# Build do JAR (pular testes para build mais rápido)
RUN ./mvnw clean package -DskipTests -B

# =============================================================================
# Stage 2: Runtime com JRE 21 Alpine (imagem mínima ~80MB)
# =============================================================================
FROM eclipse-temurin:21-jre-alpine

# Metadados
LABEL maintainer="To-Do List Team"
LABEL description="To-Do List Backend API - Spring Boot 3.2 + DDD"
LABEL version="1.0.0"

# Variáveis de ambiente
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -Djava.security.egd=file:/dev/./urandom" \
    SPRING_PROFILES_ACTIVE=prod \
    SERVER_PORT=8080

# Criar usuário não-root para segurança
RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

WORKDIR /app

# Copiar JAR do stage de build
COPY --from=builder /app/target/todo-backend-*.jar app.jar

# Criar diretórios necessários e ajustar permissões
RUN mkdir -p /app/logs /app/tmp && \
    chown -R appuser:appgroup /app

# Mudar para usuário não-root
USER appuser

# Expor porta
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Entry point
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]