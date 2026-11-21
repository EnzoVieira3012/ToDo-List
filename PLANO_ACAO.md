# Plano de Ação - To-Do List com Arquitetura DDD

## Visão Geral
- **Backend**: Java 21 + Spring Boot 3.x
- **Frontend**: React 18 + TypeScript + Vite
- **Database**: PostgreSQL (produção) / H2 (testes)
- **Arquitetura**: DDD (Domain-Driven Design) no backend

---

## FASE 1: Setup e Configuração Inicial

### 1.1 Backend - Spring Boot
- [ ] Criar projeto Spring Boot (start.spring.io)
  - Dependencies: Spring Web, Spring Data JPA, PostgreSQL Driver, H2, Validation, Lombok, SpringDoc OpenAPI (Swagger)
- [ ] Configurar `application.yml` (perfis: dev, test, prod)
- [ ] Configurar Docker Compose (PostgreSQL + pgAdmin)
- [ ] Estrutura de pacotes DDD:
  ```
  com.todo
  ├── domain
  │   ├── model (Entidades, Value Objects, Enums)
  │   ├── repository (Interfaces)
  │   ├── service (Domain Services)
  │   └── event (Domain Events)
  ├── application
  │   ├── dto (Input/Output DTOs)
  │   ├── service (Use Cases / Application Services)
  │   └── mapper (Domain <-> DTO)
  ├── infrastructure
  │   ├── persistence (JPA Entities, Repositories, Spring Data Repos)
  │   ├── config (Database, Security, etc)
  │   └── exception (Global Exception Handler)
  └── presentation
      ├── controller (REST Controllers)
      └── dto (API Request/Response)
  ```

### 1.2 Frontend - React + Vite + TypeScript
- [ ] Criar projeto: `npm create vite@latest frontend -- --template react-ts`
- [ ] Instalar dependências:
  - `axios`, `react-router-dom`, `@tanstack/react-query`
  - `zod` + `@hookform/resolvers` + `react-hook-form` (validação)
  - `lucide-react` (ícones), `date-fns` (datas)
  - `tailwindcss` + `@tailwindcss/vite` (estilização)
- [ ] Configurar estrutura:
  ```
  src/
  ├── api/ (axios instance, endpoints)
  ├── components/ (UI reutilizáveis)
  ├── features/
  │   └── tasks/ (components, hooks, types específicos de tarefas)
  ├── hooks/ (custom hooks)
  ├── pages/ (páginas/rotas)
  ├── types/ (types TypeScript globais)
  └── utils/ (helpers)
  ```

---

## FASE 2: Camada de Domínio (Backend)

### 2.1 Value Objects
- [ ] `TaskId` (UUID wrapper)
- [ ] `TaskTitle` (validação: não vazio, max 200 chars)
- [ ] `TaskDescription` (opcional, max 1000 chars)
- [ ] `Priority` (Enum: LOW, MEDIUM, HIGH)
- [ ] `TaskStatus` (Enum: PENDING, COMPLETED)
- [ ] `DueDate` (LocalDate wrapper, validação: não passado para tarefas novas)

### 2.2 Entidade Principal
- [ ] `Task` (Aggregate Root)
  - Atributos: id, title, description, priority, status, dueDate, createdAt, updatedAt
  - Métodos de negócio:
    - `complete()` - valida se já não está concluída
    - `reopen()` - valida se está concluída
    - `updateTitle()`, `updateDescription()`, `updatePriority()`, `updateDueDate()`
    - `isOverdue()` - verifica se passou da data limite e está pendente

### 2.3 Repository Interface
- [ ] `TaskRepository` (interface no domain)
  - `save(Task)`, `findById(TaskId)`, `findAll()`, `findByStatus(TaskStatus)`, `findByPriority(Priority)`, `deleteById(TaskId)`, `existsById(TaskId)`

### 2.4 Domain Events (Opcional - Diferencial)
- [ ] `TaskCreatedEvent`, `TaskCompletedEvent`, `TaskUpdatedEvent`, `TaskDeletedEvent`
- [ ] `DomainEventPublisher` interface

---

## FASE 3: Camada de Aplicação (Backend)

### 3.1 DTOs
- [ ] `CreateTaskRequest`, `UpdateTaskRequest`
- [ ] `TaskResponse`, `TaskListResponse`
- [ ] `TaskFilterRequest` (status, priority, page, size)

### 3.2 Use Cases / Application Services
- [ ] `CreateTaskUseCase`
- [ ] `GetTaskUseCase`
- [ ] `ListTasksUseCase` (com filtros e paginação)
- [ ] `UpdateTaskUseCase`
- [ ] `ToggleTaskStatusUseCase` (complete/reopen)
- [ ] `DeleteTaskUseCase`

### 3.3 Mappers
- [ ] `TaskMapper` (Domain <-> DTO <-> JPA Entity)

---

## FASE 4: Camada de Infraestrutura (Backend)

### 4.1 JPA Entity
- [ ] `TaskJpaEntity` (mapeamento tabela `tasks`)
- [ ] `SpringDataTaskRepository` (extends JpaRepository)

### 4.2 Repository Implementation
- [ ] `TaskRepositoryImpl` (implementa `TaskRepository` usando SpringDataTaskRepository + Mapper)

### 4.3 Configurações
- [ ] `DatabaseConfig`, `OpenApiConfig` (Swagger)
- [ ] `GlobalExceptionHandler` (ProblemDetail RFC 7807)
- [ ] Validation exception handler

---

## FASE 5: Camada de Apresentação (Backend)

### 5.1 REST Controllers
- [ ] `TaskController`
  - `POST /api/tasks` - Criar
  - `GET /api/tasks` - Listar (com query params: status, priority, page, size)
  - `GET /api/tasks/{id}` - Buscar por ID
  - `PUT /api/tasks/{id}` - Atualizar completa
  - `PATCH /api/tasks/{id}/toggle` - Alternar status (pendente/concluída)
  - `DELETE /api/tasks/{id}` - Excluir

### 5.2 Documentação
- [ ] Swagger UI acessível em `/swagger-ui.html`
- [ ] OpenAPI spec em `/v3/api-docs`

---

## FASE 6: Frontend - Core e Shared

### 6.1 Configurações Base
- [ ] Axios instance com interceptors (auth, error handling, baseURL)
- [ ] React Query provider + QueryClient
- [ ] React Router setup
- [ ] Tailwind CSS config
- [ ] Variáveis de ambiente (.env)

### 6.2 Componentes UI Base (Reutilizáveis)
- [ ] `Button`, `Input`, `Textarea`, `Select`, `Checkbox`
- [ ] `Modal`, `Toast/Alert`, `LoadingSpinner`, `EmptyState`
- [ ] `Table`, `Pagination`, `Badge` (priority/status)
- [ ] `DatePicker` (ou input type=date)

### 6.3 Hooks Customizados
- [ ] `useTasks` (React Query: list, create, update, delete, toggle)
- [ ] `useTaskFilters` (estado de filtros + URL sync)
- [ ] `useModal` (controle de modais)

---

## FASE 7: Frontend - Feature Tasks

### 7.1 Tipos TypeScript
- [ ] `Task`, `TaskPriority`, `TaskStatus`, `CreateTaskDTO`, `UpdateTaskDTO`, `TaskFilters`, `PaginatedResponse`

### 7.2 Componentes de Tarefa
- [ ] `TaskForm` (criar/editar - validação Zod + React Hook Form)
- [ ] `TaskList` (tabela/lista com ações)
- [ ] `TaskRow` / `TaskCard` (exibição individual)
- [ ] `TaskFilterBar` (filtros por status, prioridade, busca)
- [ ] `TaskStats` (contadores: total, pendentes, concluídas, atrasadas)

### 7.3 Páginas
- [ ] `TasksPage` (lista principal + filtros + criar)
- [ ] `TaskDetailModal` (ver detalhes + editar inline)

### 7.4 Estados e Feedback
- [ ] Loading states (skeleton, spinners)
- [ ] Error handling (toast messages)
- [ ] Confirmação de exclusão (modal)
- [ ] Empty states

---

## FASE 8: Testes (Diferencial)

### 8.1 Backend
- [ ] Testes unitários Domain (Entidade Task, Value Objects)
- [ ] Testes unitários Application (Use Cases com Mockito)
- [ ] Testes de integração Repository (Testcontainers PostgreSQL)
- [ ] Testes de contrato Controller (MockMvc)

### 8.2 Frontend
- [ ] Testes unitários componentes (Vitest + React Testing Library)
- [ ] Testes de hooks customizados
- [ ] Testes E2E básicos (Playwright ou Cypress)

---

## FASE 9: Docker e Deploy

### 9.1 Docker
- [ ] `Dockerfile.backend` (multi-stage: build + runtime)
- [ ] `Dockerfile.frontend` (nginx para servir build estático)
- [ ] `docker-compose.yml` (backend, frontend, postgres, pgadmin)
- [ ] `.dockerignore`

### 9.2 CI/CD (Opcional)
- [ ] GitHub Actions: build, test, docker push

---

## FASE 10: Documentação e Entrega

### 10.1 README.md
- [ ] Visão geral do projeto
- [ ] Decisões arquiteturais (DDD, por que Spring Boot, etc)
- [ ] Diagrama de camadas
- [ ] Pré-requisitos (Java 21, Node 20+, Docker)
- [ ] Como rodar localmente (com e sem Docker)
- [ ] Como rodar testes
- [ ] Endpoints da API (exemplos curl)
- [ ] Variáveis de ambiente
- [ ] Estrutura do projeto

### 10.2 Extras
- [ ] `ARCHITECTURE.md` (decisões detalhadas, ADRs)
- [ ] `CONTRIBUTING.md`
- [ ] Collection Postman/Insomnia

---

## ORDEM DE EXECUÇÃO RECOMENDADA

```
1.  Setup Backend (1.1) + Docker Compose
2.  Domain Layer (2.1 - 2.4)
3.  Application Layer (3.1 - 3.3)
4.  Infrastructure Layer (4.1 - 4.3)
5.  Presentation Layer (5.1 - 5.2)
6.  Testes Backend (8.1)
7.  Setup Frontend (1.2)
8.  Core Frontend (6.1 - 6.3)
9.  Feature Tasks Frontend (7.1 - 7.4)
10. Testes Frontend (8.2)
11. Docker (9.1)
12. Documentação (10.1 - 10.2)
```

---

## CRITÉRIOS DE PRONTO (Definition of Done)

- [ ] Backend compila e roda sem erros
- [ ] Swagger acessível e documentado
- [ ] Frontend builda sem erros TypeScript/ESLint
- [ ] CRUD completo funciona ponta a ponta
- [ ] Filtros funcionam (status, prioridade)
- [ ] Validações de domínio impedem estados inválidos
- [ ] Tratamento de erros amigável no frontend
- [ ] Responsivo (mobile, tablet, desktop)
- [ ] README completo com instruções claras
- [ ] Docker compose sobe stack completa com 1 comando