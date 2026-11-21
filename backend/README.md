# To-Do List Backend

## Stack Tecnológica

- **Java 21** (LTS)
- **Spring Boot 3.2+**
- **Spring Data JPA** (Hibernate)
- **PostgreSQL** (produção) / **H2** (testes)
- **Flyway** (migrations)
- **SpringDoc OpenAPI** (Swagger UI)
- **Lombok** (boilerplate reduction)
- **Bean Validation** (JSR-380)
- **Maven** (build)
- **Docker** (containerização)

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
│   └── mapper          # Conversão Domain <-> DTO
├── infrastructure      # Camada de Infraestrutura (implementações)
│   ├── persistence     # JPA Entities, Spring Data Repositories, Impl Repositories
│   ├── config          # Configurações (DB, Security, OpenAPI, etc)
│   └── exception       # Global Exception Handler (RFC 7807)
└── presentation        # Camada de Apresentação (API REST)
    ├── controller      # REST Controllers
    └── dto             # Request/Response DTOs da API
```

## Pré-requisitos

- Java 21+
- Maven 3.9+
- PostgreSQL 15+ (ou Docker)
- Docker & Docker Compose (opcional)

## Como Rodar

### Com Docker (Recomendado)

```bash
# Na raiz do projeto
docker-compose up -d
```

### Local (sem Docker)

```bash
# 1. Subir apenas o PostgreSQL
docker run -d \
  --name todo-postgres \
  -e POSTGRES_DB=todo_db \
  -e POSTGRES_USER=todo_user \
  -e POSTGRES_PASSWORD=todo_pass \
  -p 5432:5432 \
  postgres:15-alpine

# 2. Configurar variáveis de ambiente (copiar .env.example para .env e ajustar)
cp .env.example .env

# 3. Rodar migrations
./mvnw flyway:migrate

# 4. Iniciar aplicação
./mvnw spring-boot:run
```

A API estará disponível em: `http://localhost:8080`

- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI Spec**: `http://localhost:8080/v3/api-docs`
- **Health Check**: `http://localhost:8080/actuator/health`

## Endpoints Principais

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/tasks` | Criar tarefa |
| GET | `/api/tasks` | Listar tarefas (com filtros) |
| GET | `/api/tasks/{id}` | Buscar tarefa por ID |
| PUT | `/api/tasks/{id}` | Atualizar tarefa completa |
| PATCH | `/api/tasks/{id}/toggle` | Alternar status (pendente/concluída) |
| DELETE | `/api/tasks/{id}` | Excluir tarefa |

### Query Parameters (Listagem)

- `status`: `PENDING` | `COMPLETED`
- `priority`: `LOW` | `MEDIUM` | `HIGH`
- `page`: número da página (default: 0)
- `size`: tamanho da página (default: 10, max: 100)
- `sort`: campo,direção (ex: `createdAt,desc`)

## Testes

```bash
# Unitários + Integração
./mvnw test

# Apenas unitários
./mvnw test -Dtest=*UnitTest

# Com coverage
./mvnw verify jacoco:report
```

Relatório de coverage em: `target/site/jacoco/index.html`

## Variáveis de Ambiente

Ver arquivo `.env.example` para todas as opções.

Principais:
- `SPRING_PROFILES_ACTIVE`: `dev` | `test` | `prod`
- `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USERNAME`, `DB_PASSWORD`
- `SERVER_PORT`: porta do servidor (default: 8080)
- `CORS_ALLOWED_ORIGINS`: origens permitidas para CORS

## Build para Produção

```bash
./mvnw clean package -DskipTests
```

Gera `target/todo-backend-*.jar`

### Docker Image

```bash
docker build -t todo-backend .
docker run -p 8080:8080 --env-file .env todo-backend
```

## Estrutura de Banco de Dados

Tabela principal: `tasks`

| Coluna | Tipo | Descrição |
|--------|------|-----------|
| id | UUID | Primary Key |
| title | VARCHAR(200) | Título (obrigatório) |
| description | VARCHAR(1000) | Descrição (opcional) |
| priority | VARCHAR(10) | LOW, MEDIUM, HIGH |
| status | VARCHAR(10) | PENDING, COMPLETED |
| due_date | DATE | Data limite (opcional) |
| created_at | TIMESTAMP | Criação |
| updated_at | TIMESTAMP | Última atualização |

## Decisões Arquiteturais

1. **DDD**: Domínio isolado de frameworks, regras de negócio nas entidades (não anêmicas)
2. **Value Objects**: Imutáveis, validação no construtor (Title, Priority, Status, DueDate)
3. **Repository Pattern**: Interface no domain, implementação na infrastructure
4. **Use Cases**: Um por operação de negócio, retornam DTOs, não expõem domínio
5. **Global Exception Handler**: RFC 7807 (ProblemDetail) para erros padronizados
6. **Flyway**: Migrations versionadas para schema evolution
7. **OpenAPI**: Documentação automática e contract-first