# To-Do List — Full Stack DDD Application

[![Java](https://img.shields.io/badge/Java-21-blue.svg)](https://openjdk.org/projects/jdk/21/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![React](https://img.shields.io/badge/React-18.2-61dafb.svg)](https://react.dev/)
[![TypeScript](https://img.shields.io/badge/TypeScript-5.2-3178c6.svg)](https://www.typescriptlang.org/)
[![Vite](https://img.shields.io/badge/Vite-5.0-646cff.svg)](https://vitejs.dev/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-336791.svg)](https://www.postgresql.org/)
[![Docker](https://img.shields.io/badge/Docker-Ready-2496ed.svg)](https://www.docker.com/)
[![Tests](https://img.shields.io/badge/Tests-209%20passing-success.svg)]()

> Aplicação completa de gerenciamento de tarefas (To-Do List) construída com **Domain-Driven Design (DDD)** no backend e **boas práticas modernas** no frontend.

---

## 📋 Visão Geral

| Camada | Tecnologias Principais |
|--------|------------------------|
| **Backend** | Java 21, Spring Boot 3.2, Spring Data JPA, PostgreSQL, Flyway, MapStruct, SpringDoc OpenAPI |
| **Frontend** | React 18, TypeScript 5, Vite 5, TanStack Query v5, React Hook Form + Zod, Tailwind CSS 3 |
| **Testes** | JUnit 5 + Mockito + Testcontainers (Backend), Vitest + React Testing Library (Frontend) |
| **DevOps** | Docker Multi-stage, Docker Compose, Maven, GitHub Actions ready |

---

## 🏗️ Arquitetura

### Backend — DDD (Domain-Driven Design)

```
backend/src/main/java/com/todo
├── domain              # 🎯 Núcleo puro (zero dependências Spring)
│   ├── model           # Entidades, Value Objects, Enums, Domain Events
│   ├── repository      # Interfaces (contratos)
│   └── service         # Domain Services
├── application         # ⚙️ Orquestração (Use Cases)
│   ├── dto             # DTOs entrada/saída
│   ├── service         # Create/Get/List/Update/Toggle/Delete Task
│   └── mapper          # MapStruct Domain ↔ DTO
├── infrastructure      # 🔧 Implementações técnicas
│   ├── persistence     # JPA Entities, Spring Data, Repository Impl
│   ├── config          # CORS, OpenAPI, Flyway, Exception Handler
│   └── exception       # Global Exception Handler (RFC 7807)
└── presentation        # 🌐 API REST
    ├── controller      # TaskController (6 endpoints)
    └── dto             # API Request/Response
```

### Frontend — Feature-Based Architecture

```
frontend/src
├── api/                    # Axios + interceptors
├── components/ui/          # Design System (Button, Input, Modal, Badge, Pagination...)
├── features/tasks/         # 🎯 Feature completa isolada
│   ├── components/         # TaskForm, TaskList, TaskRow, TaskFilterBar, TaskStats
│   ├── hooks/              # useTasks, useCreateTask, useToggleTaskStatus...
│   ├── api.ts              # Endpoints tipados
│   └── types.ts            # Types locais
├── hooks/                  # useModal, useToast, useDebounce
├── pages/                  # TasksPage (rota principal)
├── types/                  # Types globais (Task, ApiError, PaginatedResponse)
├── utils/                  # formatters, cn (clsx+tailwind-merge)
└── styles/                 # Tailwind globals + @layer components
```

---

## 🚀 Quick Start

### Pré-requisitos
- **Docker & Docker Compose** (recomendado)
- Ou: Java 21 + Maven 3.9 + Node 20 + PostgreSQL 15

### Opção 1: Docker Compose (Tudo em 1 comando)

```bash
# Na raiz do projeto
cd backend
docker-compose up -d

# Verificar se subiu
docker-compose ps
# postgres   Up (healthy)
# pgadmin    Up
```

**Serviços:**
| Serviço | URL | Credenciais |
|---------|-----|-------------|
| **Backend API** | http://localhost:8080/api | - |
| **Swagger UI** | http://localhost:8080/swagger-ui.html | - |
| **PostgreSQL** | localhost:5432 | `todo_user` / `todo_pass` / `todo_db` |
| **pgAdmin** | http://localhost:5050 | `admin@todo.local` / `admin` |

### Opção 2: Desenvolvimento Local

#### Backend
```bash
cd backend

# 1. Subir apenas PostgreSQL
docker run -d --name todo-postgres \
  -e POSTGRES_DB=todo_db -e POSTGRES_USER=todo_user -e POSTGRES_PASSWORD=todo_pass \
  -p 5432:5432 postgres:15-alpine

# 2. Configurar env
cp .env.example .env

# 3. Rodar migrations + app
./mvnw flyway:migrate
./mvnw spring-boot:run
```

#### Frontend
```bash
cd frontend

# 1. Instalar deps
npm install

# 2. Configurar env
cp .env.example .env

# 3. Dev server (com proxy /api → backend)
npm run dev
# Abre: http://localhost:5173
```

---

## 📚 Documentação

| Arquivo | Descrição |
|---------|-----------|
| [`backend/README.md`](backend/README.md) | Documentação completa do backend (endpoints, testes, Docker, ADRs) |
| [`frontend/README.md`](frontend/README.md) | Documentação completa do frontend (arquitetura, componentes, deploy) |
| [`ARCHITECTURE.md`](ARCHITECTURE.md) | **15 ADRs** documentados + diagramas + decisões por camada |
| [`CONTRIBUTING.md`](CONTRIBUTING.md) | Guia de contribuição (branches, commits, code style, CI/CD) |
| [`docs/postman-collection.json`](docs/postman-collection.json) | Coleção Postman pronta para importar |

---

## 🎯 Funcionalidades

### Backend (API REST)
- ✅ **CRUD completo** de tarefas (6 endpoints RESTful)
- ✅ **Filtros**: status (PENDING/COMPLETED), prioridade (LOW/MEDIUM/HIGH)
- ✅ **Paginação** server-side + ordenação
- ✅ **Validação** Bean Validation + Domain (Value Objects)
- ✅ **Tratamento de erros** RFC 7807 (ProblemDetail)
- ✅ **Documentação** Swagger UI + OpenAPI 3
- ✅ **Health checks** Spring Actuator
- ✅ **CORS** configurado para frontend

### Frontend (SPA)
- ✅ **Listagem** tabela responsiva com skeleton loading
- ✅ **Filtros** em tempo real (status, prioridade, busca)
- ✅ **Criar/Editar** via Modal com React Hook Form + Zod
- ✅ **Toggle status** 1-clique (checkbox na linha)
- ✅ **Excluir** com confirmação
- ✅ **Estatísticas** cards (total, pendentes, concluídas, atrasadas)
- ✅ **Toasts** success/error/warning/info auto-dismiss
- ✅ **Estados vazios** ilustrados
- ✅ **Totalmente responsivo** (mobile → desktop)

### Testes (209 testes passando)
| Camada | Testes | Framework |
|--------|--------|-----------|
| **Domain** | 63 | JUnit 5 + AssertJ |
| **Application** | 42 | JUnit 5 + Mockito |
| **Integration (Repo)** | 7 | Spring Data JPA + Testcontainers |
| **Integration (Controller)** | 12 | MockMvc + Testcontainers |
| **Frontend Unit** | 85 | Vitest + React Testing Library |

---

## 🐳 Docker (Produção)

### Backend — Multi-stage
```dockerfile
# Build: Maven + JDK 21
# Runtime: eclipse-temurin:21-jre-alpine (~80MB)
```

### Frontend — Multi-stage
```dockerfile
# Build: Node 20 + Vite (dist/)
# Runtime: nginx:alpine servindo estáticos (~25MB)
```

### Build & Push
```bash
# Backend
cd backend && docker build -t todo-backend:latest .

# Frontend
cd frontend && docker build -t todo-frontend:latest .
```

---

## 🧪 Executar Testes

### Backend
```bash
cd backend

# Unitários (rápidos)
./mvnw test

# Integração (precisa Docker)
./mvnw verify -Dtest=*IntegrationTest

# Coverage (JaCoCo)
./mvnw verify jacoco:report
# Abrir: target/site/jacoco/index.html
```

### Frontend
```bash
cd frontend

# Watch mode
npm test

# CI mode
npm run test:run

# Coverage
npm run test:coverage
# Abrir: coverage/index.html
```

---

## 📦 Estrutura do Projeto

```
ToDo-List/
├── .github/workflows/        # CI/CD (GitHub Actions ready)
├── backend/                  # Spring Boot 3.2 + DDD
│   ├── src/main/java/...     # Código fonte
│   ├── src/test/java/...     # 124 testes
│   ├── src/main/resources/
│   │   ├── db/migration/     # Flyway SQL (V1__create_tasks_table.sql)
│   │   └── application.yml   # Config profiles (dev/test/prod)
│   ├── docker-compose.yml    # PostgreSQL + pgAdmin
│   ├── Dockerfile            # Multi-stage
│   ├── pom.xml               # Maven dependencies
│   └── README.md             # Docs backend
├── frontend/                 # React 18 + TS + Vite
│   ├── src/                  # Feature-based structure
│   ├── public/               # Assets estáticos
│   ├── Dockerfile            # Multi-stage (nginx)
│   ├── package.json          # Dependencies + scripts
│   ├── tailwind.config.js    # Design tokens
│   └── README.md             # Docs frontend
├── docs/
│   └── postman-collection.json
├── ARCHITECTURE.md           # 15 ADRs + diagramas
├── CONTRIBUTING.md           # Guia contribuição
├── PLANO_ACAO.md             # Plano de desenvolvimento (ignorado no git)
├── .gitignore                # Ignora .env, .idea, PLANO_ACAO.md
└── README.md                 # Este arquivo
```

---

## 🔧 Configuração Avançada

### Variáveis de Ambiente (Backend)
```bash
# backend/.env
SPRING_PROFILES_ACTIVE=dev
SERVER_PORT=8080
DB_HOST=localhost
DB_PORT=5432
DB_NAME=todo_db
DB_USERNAME=todo_user
DB_PASSWORD=todo_pass
CORS_ALLOWED_ORIGINS=http://localhost:5173,http://localhost:3000
```

### Variáveis de Ambiente (Frontend)
```bash
# frontend/.env
VITE_API_BASE_URL=http://localhost:8080/api
VITE_API_TIMEOUT=10000
VITE_ENABLE_REACT_QUERY_DEVTOOLS=true
VITE_DEFAULT_PAGE_SIZE=10
```

---

## 🛠️ Scripts Úteis

| Comando | Descrição |
|---------|-----------|
| `cd backend && ./mvnw spring-boot:run` | Roda backend dev |
| `cd backend && ./mvnw clean package -DskipTests` | Build JAR |
| `cd frontend && npm run dev` | Frontend dev server |
| `cd frontend && npm run build` | Build produção |
| `cd frontend && npm run lint:fix` | Auto-fix ESLint |
| `cd frontend && npm run format` | Prettier format |
| `cd backend && docker-compose up -d` | Sobe DB + pgAdmin |
| `cd backend && docker-compose down -v` | Para + remove volumes |

---

## 📖 Convenções

### Commits (Conventional Commits)
```
feat(tasks): adicionar filtro por prioridade
fix(api): corrigir validação data nula
docs(readme): atualizar instruções Docker
test(domain): cobrir Value Object DueDate
refactor(frontend): extrair hook useTaskStats
chore(deps): atualizar Spring Boot 3.2.5
```

### Branches
```
main ← develop ← feature/* | fix/* | docs/* | test/*
```

---

## 🤝 Contribuindo

1. **Fork** → **Branch** (`feature/nova-funcionalidade`)
2. **Code** + **Testes** (`npm run test:run` / `./mvnw test`)
3. **Quality** (`npm run lint:fix` / `./mvnw checkstyle:check`)
4. **Commit** convencional
5. **Push** → **Pull Request** para `develop`

Ver [`CONTRIBUTING.md`](CONTRIBUTING.md) para detalhes completos.

---

## 📄 Licença

**MIT License** — Livre para uso comercial, modificação, distribuição.

---

## 👨‍💻 Autor

**Enzo Vieira** — [GitHub](https://github.com/EnzoVieira3012)

---

## 🙏 Agradecimentos

- **Eric Evans** — Domain-Driven Design (livro base)
- **Spring Team** — Spring Boot 3, Spring Data, SpringDoc
- **React Team** — React 18, Hooks, Concurrent Features
- **TanStack** — React Query v5 (server state perfeito)
- **Tailwind Labs** — Tailwind CSS (styling utility-first)
- **Testcontainers** — Testes de integração com DB real

---

> **Status do Projeto**: ✅ **Completo** — Todas as 10 fases entregues (DDD Backend + React Frontend + Testes + Docker + Documentação)