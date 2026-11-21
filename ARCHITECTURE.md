# Architecture Decision Records (ADRs)

Este documento registra as decisões arquiteturais significativas do projeto To-Do List.

---

## ADR-001: Domain-Driven Design no Backend

**Status**: Aceito  
**Data**: 2025-01-15

### Contexto
O desafio exige aplicação de princípios DDD no backend para demonstrar modelagem de domínio, separação de responsabilidades e código limpo.

### Decisão
Adotar arquitetura DDD com 4 camadas bem definidas:
1. **Domain** - Entidades, Value Objects, Domain Events, Repository Interfaces, Domain Services
2. **Application** - Use Cases, DTOs, Mappers (MapStruct)
3. **Infrastructure** - JPA Entities, Spring Data Repositories, Configs, Exception Handler
3. **Presentation** - REST Controllers, API DTOs

### Consequências
- ✅ Domínio isolado de frameworks (testável sem Spring)
- ✅ Regras de negócio encapsuladas na entidade `Task` (não anêmica)
- ✅ Separação clara: Use Cases orquestram, Domain decide
- ⚠️ Mais boilerplate inicial vs arquitetura anêmica
- ⚠️ Curva de aprendizado para time não familiarizado

---

## ADR-002: Value Objects para Tipos de Domínio

**Status**: Aceito  
**Data**: 2025-01-15

### Contexto
Tipos primitivos (`String`, `UUID`, `LocalDate`) não expressam intenção de domínio e permitem estados inválidos.

### Decisão
Criar Value Objects imutáveis com validação no construtor:
- `TaskId` (UUID wrapper)
- `TaskTitle` (not blank, max 200)
- `TaskDescription` (nullable, max 1000)
- `DueDate` (LocalDate wrapper + métodos `isOverdue`, `isToday`, `isFuture`)
- `Priority` (Enum: LOW/MEDIUM/HIGH com peso e label)
- `TaskStatus` (Enum: PENDING/COMPLETED com toggle)

### Consequências
- ✅ Validação centralizada (fail-fast)
- ✅ Imutabilidade garantida
- ✅ Type safety (não passa `String` onde espera `TaskTitle`)
- ✅ Métodos de domínio expressivos (`dueDate.isOverdue()`)
- ⚠️ Mais classes para manter

---

## ADR-003: MapStruct para Mapeamento (Compile-time)

**Status**: Aceito  
**Data**: 2025-01-15

### Contexto
Precisa converter entre: Domain ↔ DTO (Application) ↔ JPA Entity ↔ API DTO.

### Decisão
Usar MapStruct (processador de anotações compile-time) ao invés de reflection runtime (ModelMapper) ou manual.

### Consequências
- ✅ Performance: zero reflection, código gerado otimizado
- ✅ Type safety: erros de mapeamento pegos em build
- ✅ Debugging: código gerado legível
- ✅ Integração Lombok via `lombok-mapstruct-binding`
- ⚠️ Requer configuração `annotationProcessorPaths` no Maven

---

## ADR-004: RFC 7807 ProblemDetail para Erros HTTP

**Status**: Aceito  
**Data**: 2025-01-15

### Contexto
APIs REST precisam de formato de erro padronizado, interoperável e informativo.

### Decisão
Implementar `GlobalExceptionHandler` retornando `application/problem+json` (RFC 7807) via `ProblemDetail` do Spring Boot 3.

### Consequências
- ✅ Formato padrão da indústria
- ✅ Campos estruturados: `timestamp`, `status`, `error`, `message`, `path`, `validationErrors`
- ✅ Integração nativa Spring Boot 3 (`ErrorResponse` / `ProblemDetail`)
- ✅ Ferramentas clientes podem parsear automaticamente

---

## ADR-005: Testcontainers para Testes de Integração

**Status**: Aceito  
**Data**: 2025-01-15

### Contexto
Testes de integração precisam de banco real (PostgreSQL) para validar queries, constraints, triggers.

### Decisão
Usar Testcontainers (JUnit 5 + `@Testcontainers`) com container PostgreSQL real para testes de Repository e Controller.

### Consequências
- ✅ Fidelidade 100% produção (mesmo dialect, constraints, triggers)
- ✅ Isolamento: container limpo por classe de teste
- ✅ CI/CD confiável (GitHub Actions, GitLab CI suportam Docker-in-Docker)
- ⚠️ Requer Docker rodando (recursos: RAM/CPU)
- ⚠️ Testes mais lentos (~10-30s por classe)

---

## ADR-006: Flyway para Database Migrations

**Status**: Aceito  
**Data**: 2025-01-15

### Contexto
Evolução de schema precisa ser versionada, reprodutível e auditável.

### Decisão
Flyway com migrations SQL versionadas em `src/main/resources/db/migration/V{version}__{description}.sql`.

### Consequências
- ✅ Versionamento linear (V1, V2, V3...)
- ✅ Checksum validation (detecta drift)
- ✅ Baseline para ambientes existentes
- ✅ SQL puro (portável, revisável)
- ⚠️ Rollback manual (scripts `U` ou undo não recomendados)

---

## ADR-007: TanStack Query v5 para Server State (Frontend)

**Status**: Aceito  
**Data**: 2025-01-15

### Contexto
Gerenciar estado assíncrono do servidor (cache, loading, error, retry, dedup, invalidação).

### Decisão
TanStack Query (React Query) v5 como única fonte de verdade para server state. Zero Redux/Zustand/Context para dados da API.

### Consequências
- ✅ Cache inteligente (staleTime, gcTime)
- ✅ Deduplication automática (mesma query = 1 request)
- ✅ Retry configurável + exponential backoff
- ✅ Invalidação declarativa (`invalidateQueries`)
- ✅ DevTools visuais (queries, mutations, cache)
- ✅ TypeScript first-class (typed hooks)
- ⚠️ Curva de aprendizado (queries vs mutations, keys)

---

## ADR-008: React Hook Form + Zod (Frontend)

**Status**: Aceito  
**Data**: 2025-01-15

### Contexto
Formulários com validação complexa, performance e DX.

### Decisão
React Hook Form (uncontrolled, minimal re-renders) + Zod (schema validation + type inference).

### Consequências
- ✅ Performance: inputs não controlados, re-render apenas no submit
- ✅ DX: `register()`, `handleSubmit()`, `formState.errors`
- ✅ Type safety: `z.infer<typeof schema>` → tipos automáticos
- ✅ Reutilização: schemas compartilhados frontend/backend (futuro)
- ✅ Resolvers: `zodResolver` integra nativamente

---

## ADR-009: Feature-based Structure (Frontend)

**Status**: Aceito  
**Data**: 2025-01-15

### Contexto
Organização de arquivos escalável para time crescente.

### Decisão
Estrutura por feature (`features/tasks/`) em vez de por tipo técnico (`components/`, `hooks/`, `api/` misturados).

```
features/
  tasks/
    components/  # TaskForm, TaskList, TaskRow, TaskFilterBar, TaskStats
    hooks/       # useTasks, useCreateTask, useTaskStats...
    api.ts       # Endpoints específicos
    types.ts     # Types locais
```

### Consequências
- ✅ Colocation: tudo relacionado a "tasks" junto
- ✅ Escalabilidade: nova feature = nova pasta
- ✅ Onboarding: contexto completo em um lugar
- ✅ Tree-shaking: imports explícitos
- ⚠️ Shared components/hooks ainda em `src/components/`, `src/hooks/`

---

## ADR-010: Tailwind CSS + Design Tokens Customizados

**Status**: Aceito  
**Data**: 2025-01-15

### Contexto
Estilização consistente, responsiva, com bundle mínimo.

### Decisão
Tailwind CSS v3 com tokens customizados em `tailwind.config.js`:
- Cores `primary`, `priority.*`, `status.*`
- Font `Inter` via Google Fonts
- `@layer components` para primitivos (`.btn`, `.input`, `.card`, `.badge`)

### Consequências
- ✅ Design system consistente (tokens = single source of truth)
- ✅ Bundle mínimo (purge unused em build)
- ✅ Responsividade nativa (`md:`, `lg:`, `xl:`)
- ✅ Dark mode ready (`dark:` variant)
- ✅ Customização via config (não CSS-in-JS runtime)
- ⚠️ HTML verboso (classes utilitárias)

---

## ADR-011: Multi-stage Docker Builds

**Status**: Aceito  
**Data**: 2025-01-15

### Contexto
Imagens Docker pequenas, seguras e rápidas para produção.

### Decisão
Backend: Maven build stage → JRE runtime (eclipse-temurin:21-jre-alpine)  
Frontend: Node build stage → Nginx static serve (nginx:alpine)

### Consequências
- ✅ Imagens finais ~50-80MB (vs 500MB+ single stage)
- ✅ Segurança: sem build tools, source code, node_modules no runtime
- ✅ Cache de layers otimizado (deps copiadas antes do source)
- ✅ Reprodutibilidade: mesma imagem dev/staging/prod

---

## ADR-012: Docker Compose para Desenvolvimento

**Status**: Aceito  
**Data**: 2025-01-15

### Contexto
Ambiente de dev consistente, zero config manual.

### Decisão
`docker-compose.yml` na pasta `backend/` com:
- PostgreSQL 15 (healthcheck + volume persistido)
- pgAdmin 4 (UI para inspecionar DB)

### Consequências
- ✅ `docker-compose up -d` = ambiente pronto
- ✅ Dados persistem entre restarts (volume `postgres_data`)
- ✅ Healthcheck garante DB pronto antes da app
- ⚠️ Porta 5432/5050 no host (conflito se já em uso)

---

## ADR-013: TypeScript Strict Mode

**Status**: Aceito  
**Data**: 2025-01-15

### Contexto
Type safety end-to-end (API → UI).

### Decisão
`tsconfig.json` com:
```json
{
  "strict": true,
  "noUnusedLocals": true,
  "noUnusedParameters": true,
  "exactOptionalPropertyTypes": true,
  "noImplicitReturns": true,
  "noFallthroughCasesInSwitch": true
}
```

### Consequências
- ✅ Bugs pegos em build (não runtime)
- ✅ Refactoring seguro (rename, delete, move)
- ✅ Documentação viva (tipos = contratos)
- ⚠️ Mais verboso inicial
- ⚠️ Requer tipos corretos em libs externas (`@types/*`)

---

## ADR-014: Conventional Commits + Branch Strategy

**Status**: Aceito  
**Data**: 2025-01-15

### Contexto
Histórico legível, automação de release/changelog.

### Decisão
- **Commits**: `feat:`, `fix:`, `docs:`, `style:`, `refactor:`, `test:`, `chore:`, `perf:`
- **Branches**: `main` (produção) ← `develop` (integração) ← `feature/*` (features) ← `hotfix/*` (urgente)
- **PR**: feature → develop → main (via merge squash ou rebase)

### Consequências
- ✅ Changelog automático possível (`standard-version`, `semantic-release`)
- ✅ Histórico limpo e navegável
- ✅ CI/CD pode filtrar por tipo de commit

---

## ADR-015: OpenAPI/Swagger para Documentação de API

**Status**: Aceito  
**Data**: 2025-01-15

### Contexto
Documentação de API sempre sincronizada com código.

### Decisão
SpringDoc OpenAPI v2 com:
- Anotações em Controllers (`@Operation`, `@Parameter`, `@Tag`)
- Configuração centralizada (`OpenApiConfig`)
- Swagger UI em `/swagger-ui.html`
- Spec JSON em `/v3/api-docs`

### Consequências
- ✅ Documentação viva (code-first)
- ✅ Client generation possível (OpenAPI Generator, Swagger Codegen)
- ✅ Testável via UI
- ✅ Contrato para frontend/backend alignment

---

## Resumo de Tecnologias por Camada

| Camada | Backend | Frontend |
|--------|---------|----------|
| **Domain/State** | DDD Entities, VOs, Enums | TanStack Query (server), RHF+Zod (form), Custom Hooks (UI) |
| **Application/Logic** | Use Cases, DTOs, Mappers | Feature hooks (`useTasks`, `useCreateTask`...) |
| **Infrastructure/Data** | JPA, Spring Data, Flyway, Testcontainers | Axios, API endpoints, React Query |
| **Presentation/UI** | REST Controllers, Global Exception Handler | React Components, Tailwind, Feature Components |
| **Quality** | JUnit 5, Mockito, AssertJ, JaCoCo | Vitest, RTL, ESLint, Prettier, TypeScript Strict |
| **DevOps** | Maven, Docker Multi-stage, Docker Compose | Vite, Docker Multi-stage, Nginx |

---

## Diagramas

### Arquitetura Backend (DDD)
```
┌─────────────────────────────────────────────────────────────┐
│                    PRESENTATION LAYER                        │
│  TaskController (REST) → API DTOs → Validation → 200/201/400 │
└──────────────────────────┬──────────────────────────────────┘
                           │ delegates
┌──────────────────────────▼──────────────────────────────────┐
│                    APPLICATION LAYER                         │
│  Use Cases (Create/Get/List/Update/Toggle/Delete)           │
│  → Domain → MapStruct → App DTOs                            │
└──────────────────────────┬──────────────────────────────────┘
                           │ uses
┌──────────────────────────▼──────────────────────────────────┐
│                      DOMAIN LAYER                            │
│  Task (Entity) ← TaskRepository (Interface)                 │
│  Value Objects: TaskId, TaskTitle, TaskDescription,         │
│                 DueDate, Priority, TaskStatus               │
│  Domain Services (se necessário)                            │
└──────────────────────────┬──────────────────────────────────┘
                           │ implements
┌──────────────────────────▼──────────────────────────────────┐
│                   INFRASTRUCTURE LAYER                       │
│  TaskRepositoryImpl → SpringDataTaskRepository → PostgreSQL  │
│  TaskJpaEntity, TaskMapper (MapStruct)                      │
│  Flyway Migrations, GlobalExceptionHandler, CORS, OpenAPI   │
└─────────────────────────────────────────────────────────────┘
```

### Fluxo Frontend (Feature Tasks)
```
┌─────────────────────────────────────────────────────────────┐
│                      PAGES                                   │
│  TasksPage (Route "/")                                       │
│  → useTasks(filters) → TaskList + TaskFilterBar + TaskStats  │
│  → useCreateTask / useUpdateTask / useToggleTaskStatus       │
│  → useDeleteTask                                             │
└──────────────────────────┬──────────────────────────────────┘
                           │ composes
┌──────────────────────────▼──────────────────────────────────┐
│                    FEATURES/TASKS                            │
│  components/  ← TaskForm, TaskRow, TaskList...               │
│  hooks/       ← useTasks, useTaskStats, mutations            │
│  api.ts       ← taskApi (Axios + types)                      │
│  types.ts     ← Task, TaskFilters, DTOs, Labels              │
└──────────────────────────┬──────────────────────────────────┘
                           │ uses
┌──────────────────────────▼──────────────────────────────────┐
│                      SHARED                                  │
│  components/ui/  ← Button, Input, Modal, Badge, Pagination   │
│  hooks/          ← useModal, useToast, useDebounce           │
│  utils/          ← formatters, cn (clsx+tw-merge)            │
│  types/api.ts    ← ApiError, PaginatedResponse               │
└─────────────────────────────────────────────────────────────┘
```

---

## Evolução Futura (Backlog Arquitetural)

| Item | Prioridade | Descrição |
|------|------------|-----------|
| **CQRS** | Média | Separar Read/Write models para queries complexas |
| **Domain Events** | Média | `TaskCompletedEvent` → notifications, audit log |
| **Event Sourcing** | Baixa | Audit trail completo de mudanças de tarefa |
| **API Versioning** | Alta | `/api/v1/tasks` header/URL versioning |
| **Authentication** | Alta | JWT/OIDC (Spring Security + React context) |
| **Authorization** | Alta | Roles: ADMIN, USER (resource ownership) |
| **Rate Limiting** | Média | Bucket4j / Redis |
| **Observability** | Média | OpenTelemetry → Jaeger/Prometheus/Grafana |
| **Feature Flags** | Baixa | LaunchDarkly / Unleash para rollout gradual |
| **Micro-frontends** | Baixa | Module Federation se app crescer |

---

*Documento vivo - atualizar a cada decisão arquitetural significativa.*