# To-Do List Backend

API REST para gerenciamento de tarefas construída com **Java 21**, **Spring Boot 3.2** e arquitetura **DDD (Domain-Driven Design)**.

## Stack Tecnológica

| Tecnologia | Versão | Descrição |
|------------|--------|-----------|
| **Java** | 21 (LTS) | Linguagem principal |
| **Spring Boot** | 3.2.5 | Framework de aplicação |
| **Spring Data JPA** | 3.2.5 | Persistência com Hibernate |
| **PostgreSQL** | 15 | Banco de produção |
| **H2** | 2.2 | Banco de testes (em memória) |
| **Flyway** | 10.15 | Migrations de banco |
| **SpringDoc OpenAPI** | 2.3 | Swagger UI + OpenAPI 3 |
| **Lombok** | 1.18.32 | Redução de boilerplate |
| **MapStruct** | 1.5.5 | Mapeamento DTO ↔ Domain |
| **Maven** | 3.9+ | Build e dependências |
| **Docker** | - | Containerização |

## Arquitetura: DDD (Domain-Driven Design)

```
src/main/java/com/todo
├── domain              # Camada de Domínio (pura, sem dependências de framework)
│   ├── model           # Entidades, Value Objects, Enums, Domain Events
│   ├── repository      # Interfaces de Repositório (contratos)
│   └── service         # Domain Services (regras de negócio complexas)
├── application         # Camada de Aplicação (orquestração)
│   ├── dto             # DTOs de entrada/saída
│   ├── service         # Use Cases / Application Services
│   └── mapper          # Conversão Domain <-> DTO (MapStruct)
├── infrastructure      # Camada de Infraestrutura (implementações)
│   ├── persistence     # JPA Entities, Spring Data Repositories, Impl Repositories
│   ├── config          # Configurações (DB, Security, OpenAPI, CORS)
│   └── exception       # Global Exception Handler (RFC 7807 ProblemDetail)
└── presentation        # Camada de Apresentação (API REST)
    ├── controller      # REST Controllers
    └── dto             # Request/Response DTOs da API
```

### Princípios Aplicados

1. **Domínio Isolado**: Zero dependências de Spring/framework na camada `domain`
2. **Entidades Ricas**: Regras de negócio dentro da entidade `Task` (não anêmica)
3. **Value Objects Imutáveis**: `TaskId`, `TaskTitle`, `TaskDescription`, `DueDate`, `Priority`, `TaskStatus`
4. **Repository Pattern**: Interface no domain, implementação na infrastructure
5. **Use Cases**: Um por operação, orquestram domain, retornam DTOs
6. **Mappers**: MapStruct para conversão automática Domain ↔ DTO ↔ JPA Entity

## Pré-requisitos

- **Java 21+** (Oracle JDK, OpenJDK, Temurin)
- **Maven 3.9+** (ou use `./mvnw` wrapper)
- **PostgreSQL 15+** (ou Docker)
- **Docker & Docker Compose** (recomendado)

## Como Rodar

### Opção 1: Docker Compose (Recomendado)

```bash
# Na pasta backend/
cd backend
docker-compose up -d

# Verificar logs
docker-compose logs -f postgres
```

Serviços disponíveis:
- **PostgreSQL**: `localhost:5432` (db: `todo_db`, user: `todo_user`, pass: `todo_pass`)
- **pgAdmin**: `localhost:5050` (login: `admin@todo.local` / `admin`)

### Opção 2: Local (sem Docker Compose)

```bash
# 1. Subir apenas PostgreSQL
docker run -d \
  --name todo-postgres \
  -e POSTGRES_DB=todo_db \
  -e POSTGRES_USER=todo_user \
  -e POSTGRES_PASSWORD=todo_pass \
  -p 5432:5432 \
  postgres:15-alpine

# 2. Configurar variáveis de ambiente
cp .env.example .env
# Edite .env se necessário

# 3. Rodar migrations (Flyway)
./mvnw flyway:migrate

# 4. Iniciar aplicação
./mvnw spring-boot:run
```

### Opção 3: Perfil de Teste (H2 em memória)

```bash
./mvnw spring-boot:run -Dspring.profiles.active=test
```

- H2 Console: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:testdb`
- User: `sa` / Password: (vazio)

---

## URLs da Aplicação

| Serviço | URL |
|---------|-----|
| **API Base** | `http://localhost:8080/api` |
| **Swagger UI** | `http://localhost:8080/swagger-ui.html` |
| **OpenAPI Spec** | `http://localhost:8080/v3/api-docs` |
| **Health Check** | `http://localhost:8080/actuator/health` |
| **Metrics** | `http://localhost:8080/actuator/metrics` |

---

## Endpoints da API

### Tarefas (`/api/tasks`)

| Método | Endpoint | Descrição | Status Code |
|--------|----------|-----------|-------------|
| `POST` | `/tasks` | Criar nova tarefa | `201 Created` |
| `GET` | `/tasks` | Listar com filtros/paginação | `200 OK` |
| `GET` | `/tasks/{id}` | Buscar por ID | `200 OK` / `404` |
| `PUT` | `/tasks/{id}` | Atualização completa | `200 OK` |
| `PATCH` | `/tasks/{id}/toggle` | Alternar status | `200 OK` |
| `DELETE` | `/tasks/{id}` | Excluir tarefa | `204 No Content` |

### Criar Tarefa

```bash
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Estudar DDD",
    "description": "Ler livro do Eric Evans",
    "priority": "HIGH",
    "dueDate": "2025-12-31"
  }'
```

**Request Body:**
```json
{
  "title": "string (obrigatório, max 200)",
  "description": "string (opcional, max 1000)",
  "priority": "LOW | MEDIUM | HIGH (obrigatório)",
  "dueDate": "YYYY-MM-DD (opcional)"
}
```

### Listar Tarefas

```bash
# Todas
curl http://localhost:8080/api/tasks

# Com filtros
curl "http://localhost:8080/api/tasks?status=PENDING&priority=HIGH&page=0&size=20&sortBy=createdAt&sortDir=desc"
```

**Query Parameters:**
| Parâmetro | Tipo | Default | Descrição |
|-----------|------|---------|-----------|
| `status` | string | - | `PENDING` ou `COMPLETED` |
| `priority` | string | - | `LOW`, `MEDIUM`, `HIGH` |
| `page` | int | `0` | Página (0-based) |
| `size` | int | `10` | Itens por página (max 100) |
| `sortBy` | string | `createdAt` | Campo para ordenação |
| `sortDir` | string | `desc` | `asc` ou `desc` |

**Response (Paginated):**
```json
{
  "content": [...],
  "page": 0,
  "size": 10,
  "totalElements": 42,
  "totalPages": 5,
  "first": true,
  "last": false,
  "empty": false
}
```

### Buscar por ID

```bash
curl http://localhost:8080/api/tasks/550e8400-e29b-41d4-a716-446655440000
```

### Atualizar Tarefa (PUT - completa)

```bash
curl -X PUT http://localhost:8080/api/tasks/{id} \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Novo título",
    "description": "Nova descrição",
    "priority": "MEDIUM",
    "dueDate": "2025-11-30"
  }'
```

### Alternar Status (PATCH)

```bash
curl -X PATCH http://localhost:8080/api/tasks/{id}/toggle
```

### Excluir Tarefa

```bash
curl -X DELETE http://localhost:8080/api/tasks/{id}
```

---

## Exemplos de Resposta

### Task Response
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "title": "Estudar DDD",
  "description": "Ler livro do Eric Evans",
  "priority": "HIGH",
  "status": "PENDING",
  "dueDate": "2025-12-31",
  "createdAt": "2025-01-15T10:30:00",
  "updatedAt": "2025-01-15T10:30:00",
  "overdue": false
}
```

### Error Response (RFC 7807)
```json
{
  "timestamp": "2025-01-15T10:30:00",
  "status": 400,
  "error": "Validation Error",
  "message": "Invalid request parameters",
  "path": "/api/tasks",
  "validationErrors": {
    "title": "Title is required",
    "priority": "Priority is required"
  }
}
```

---

## Testes

### Executar Todos os Testes
```bash
./mvnw test
```

### Testes Unitários (Domain + Application)
```bash
./mvnw test -Dtest=*Test
```

### Testes de Integração (Testcontainers)
```bash
./mvnw verify -Dtest=*IntegrationTest
```

### Coverage Report (JaCoCo)
```bash
./mvnw verify jacoco:report
# Abrir: target/site/jacoco/index.html
```

### Estrutura de Testes

| Tipo | Localização | Framework |
|------|-------------|-----------|
| **Domain Unit** | `src/test/java/com/todo/domain/model/` | JUnit 5 + AssertJ |
| **Application Unit** | `src/test/java/com/todo/application/service/` | JUnit 5 + Mockito |
| **Repository Integration** | `src/test/java/com/todo/infrastructure/persistence/` | Spring Data JPA + Testcontainers |
| **Controller Integration** | `src/test/java/com/todo/presentation/controller/` | MockMvc + Testcontainers |

---

## Variáveis de Ambiente

Arquivo `.env` (baseado em `.env.example`):

| Variável | Descrição | Default |
|----------|-----------|---------|
| `SPRING_PROFILES_ACTIVE` | Perfil ativo: `dev`, `test`, `prod` | `dev` |
| `SERVER_PORT` | Porta do servidor | `8080` |
| `DB_HOST` | Host do PostgreSQL | `localhost` |
| `DB_PORT` | Porta do PostgreSQL | `5432` |
| `DB_NAME` | Nome do banco | `todo_db` |
| `DB_USERNAME` | Usuário do banco | `todo_user` |
| `DB_PASSWORD` | Senha do banco | `todo_pass` |
| `SPRING_JPA_SHOW_SQL` | Log SQL do Hibernate | `true` |
| `CORS_ALLOWED_ORIGINS` | Origens CORS permitidas | `http://localhost:5173` |
| `LOGGING_LEVEL_COM_TODO` | Log level da aplicação | `DEBUG` |

---

## Build para Produção

### JAR Executável
```bash
./mvnw clean package -DskipTests
# Gera: target/todo-backend-1.0.0-SNAPSHOT.jar
```

### Docker Image (Multi-stage)
```bash
# Build
docker build -t todo-backend:latest .

# Run
docker run -d \
  --name todo-backend \
  -p 8080:8080 \
  --env-file .env \
  todo-backend:latest
```

### Dockerfile (Multi-stage)
```dockerfile
# Build stage
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

---

## Banco de Dados

### Tabela `tasks`

```sql
CREATE TABLE tasks (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    title VARCHAR(200) NOT NULL,
    description VARCHAR(1000),
    priority VARCHAR(10) NOT NULL CHECK (priority IN ('LOW', 'MEDIUM', 'HIGH')),
    status VARCHAR(10) NOT NULL CHECK (status IN ('PENDING', 'COMPLETED')),
    due_date DATE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Índices
CREATE INDEX idx_tasks_status ON tasks(status);
CREATE INDEX idx_tasks_priority ON tasks(priority);
CREATE INDEX idx_tasks_due_date ON tasks(due_date);
CREATE INDEX idx_tasks_created_at ON tasks(created_at);
CREATE INDEX idx_tasks_status_priority ON tasks(status, priority);

-- Trigger updated_at
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_tasks_updated_at
    BEFORE UPDATE ON tasks
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();
```

### Migrations (Flyway)

Localização: `src/main/resources/db/migration/`

| Arquivo | Descrição |
|---------|-----------|
| `V1__create_tasks_table.sql` | Schema inicial |

---

## Decisões Arquiteturais (ADRs)

### ADR-001: Domain-Driven Design
**Decisão**: Usar DDD com camadas bem definidas (domain, application, infrastructure, presentation).
**Justificativa**: Isolamento de regras de negócio, testabilidade, manutenibilidade a longo prazo.

### ADR-002: Value Objects para Tipos Primitivos
**Decisão**: `TaskId`, `TaskTitle`, `TaskDescription`, `DueDate`, `Priority`, `TaskStatus` como Value Objects.
**Justificativa**: Validação no construtor, imutabilidade, type safety, expressividade.

### ADR-003: MapStruct para Mapeamento
**Decisão**: Usar MapStruct (compile-time) em vez de reflection runtime.
**Justificativa**: Performance, type safety, detecção de erros em build.

### ADR-004: RFC 7807 ProblemDetail para Erros
**Decisão**: Padronizar erros HTTP com `application/problem+json`.
**Justificativa**: Interoperabilidade, consistência, ferramentas existentes.

### ADR-005: Testcontainers para Integração
**Decisão**: PostgreSQL real em container para testes de integração.
**Justificativa**: Fidelidade produção, isolamento, CI/CD confiável.

### ADR-006: Flyway para Migrations
**Decisão**: Versionamento de schema com Flyway.
**Justificativa**: Reprodutibilidade, rollback controlado, auditoria.

---

## Qualidade de Código

### Static Analysis
```bash
# SpotBugs (se configurado)
./mvnw spotbugs:check

# Checkstyle (se configurado)
./mvnw checkstyle:check
```

### Formatação
```bash
# Google Java Format (via plugin IDE ou CLI)
```

---

## Monitoramento

### Actuator Endpoints
| Endpoint | Descrição |
|----------|-----------|
| `/actuator/health` | Health check (liveness/readiness) |
| `/actuator/info` | Info da aplicação |
| `/actuator/metrics` | Métricas JVM/HTTP/DB |
| `/actuator/prometheus` | Métricas Prometheus |

### Logs Estruturados
- **Console**: Formato texto (dev)
- **Produção**: JSON (via Logstash/ELK)

---

## Segurança

- **CORS**: Configurado via `CorsConfig` (origens permitidas)
- **Validação**: Bean Validation (JSR-380) em DTOs + Domain
- **SQL Injection**: Prevenido por JPA/Hibernate (parameter binding)
- **Headers**: Security headers via Spring Boot defaults

---

## CI/CD (GitHub Actions)

Ver `.github/workflows/ci.yml`:
- Build + Test em PR
- Docker build + push em merge para `main`
- Deploy automático (configurar)

---

## Troubleshooting

### Porta 8080 em uso
```bash
# Encontrar processo
lsof -i :8080
# ou no Windows
netstat -ano | findstr :8080
```

### Flyway Migration Failed
```bash
# Reparar (cuidado em prod!)
./mvnw flyway:repair
```

### Testcontainers não inicia
- Verificar Docker rodando
- Verificar recursos (RAM/CPU)
- `docker system prune` se necessário

---

## Contribuição

1. Fork o projeto
2. Crie branch: `git checkout -b feature/nova-funcionalidade`
3. Commit: `git commit -m 'feat: descrição'`
4. Push: `git push origin feature/nova-funcionalidade`
5. Abra Pull Request

### Padrão de Commits (Conventional Commits)
```
feat: nova funcionalidade
fix: correção de bug
docs: documentação
style: formatação
refactor: refatoração
test: testes
chore: manutenção
```

---

## Licença

MIT License - veja [LICENSE](../LICENSE) para detalhes.