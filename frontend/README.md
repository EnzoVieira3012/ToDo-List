# To-Do List Frontend

## Stack Tecnológica

- **React 18** (Functional Components + Hooks)
- **TypeScript 5+** (Strict mode)
- **Vite 5** (Build tool + Dev server)
- **TanStack Query v5** (Server state management)
- **React Router v6** (Routing)
- **Axios** (HTTP client)
- **React Hook Form + Zod** (Forms + Validation)
- **Tailwind CSS 3** (Utility-first styling)
- **Lucide React** (Icons)
- **date-fns** (Date formatting)
- **Vitest + React Testing Library** (Unit/Integration tests)
- **ESLint + Prettier** (Code quality)

## Estrutura do Projeto

```
src/
├── api/                    # Camada de comunicação HTTP
│   ├── axios.ts           # Instância Axios configurada
│   └── endpoints.ts       # Funções de API tipadas
├── components/            # Componentes UI reutilizáveis (atoms/molecules)
│   ├── ui/                # Button, Input, Select, Modal, Toast, etc
│   └── layout/            # Header, Footer, Container
├── features/              # Features organizadas por domínio
│   └── tasks/
│       ├── components/    # TaskForm, TaskList, TaskRow, TaskFilterBar, TaskStats
│       ├── hooks/         # useTasks, useTaskFilters, useTaskMutations
│       ├── types.ts       # Types TypeScript específicos de tasks
│       └── api.ts         # Endpoints específicos de tasks
├── hooks/                 # Custom hooks compartilhados
│   ├── useModal.ts
│   ├── useToast.ts
│   └── useDebounce.ts
├── pages/                 # Páginas/Rotas
│   └── TasksPage.tsx
├── types/                 # Types globais compartilhados
│   ├── api.ts             # ApiError, PaginatedResponse, etc
│   └── task.ts            # Task, TaskPriority, TaskStatus, DTOs
├── utils/                 # Helpers puros
│   ├── formatters.ts
│   ├── validators.ts
│   └── constants.ts
├── styles/                # Estilos globais
│   ├── globals.css
│   └── tailwind.css
├── App.tsx
├── main.tsx
└── vite-env.d.ts
```

## Pré-requisitos

- Node.js 20+ (LTS)
- npm 10+ (ou pnpm/yarn)
- Backend rodando em `http://localhost:8080`

## Como Rodar

### Desenvolvimento

```bash
# 1. Instalar dependências
npm install

# 2. Configurar variáveis de ambiente
cp .env.example .env

# 3. Iniciar dev server
npm run dev
```

Aplicação disponível em: `http://localhost:5173`

### Build de Produção

```bash
npm run build
```

Gera pasta `dist/` pronta para deploy estático (nginx, Vercel, Netlify, etc).

### Preview do Build

```bash
npm run preview
```

## Scripts Disponíveis

| Script | Descrição |
|--------|-----------|
| `dev` | Inicia servidor de desenvolvimento (Vite) |
| `build` | Build de produção (TypeScript + Vite) |
| `preview` | Preview local do build de produção |
| `lint` | ESLint + Prettier check |
| `lint:fix` | Auto-fix ESLint/Prettier issues |
| `test` | Roda testes (Vitest) |
| `test:ui` | Interface visual dos testes |
| `test:coverage` | Coverage report |
| `typecheck` | TypeScript type checking (no emit) |

## Variáveis de Ambiente

Ver `.env.example`:

| Variável | Descrição | Default |
|----------|-----------|---------|
| `VITE_API_BASE_URL` | URL base da API backend | `http://localhost:8080/api` |
| `VITE_API_TIMEOUT` | Timeout requests (ms) | `10000` |
| `VITE_ENABLE_MOCK_API` | Usar mock API (desenvolvimento) | `false` |
| `VITE_DEFAULT_PAGE_SIZE` | Paginação padrão | `10` |
| `VITE_DATE_FORMAT` | Formato de data exibição | `dd/MM/yyyy` |

## Funcionalidades

- ✅ Listar tarefas com paginação
- ✅ Filtrar por status (Pendente/Concluída) e prioridade (Baixa/Média/Alta)
- ✅ Criar tarefa (título obrigatório, descrição, prioridade, data limite)
- ✅ Editar tarefa inline
- ✅ Alternar status (Pendente ↔ Concluída) com um clique
- ✅ Excluir tarefa com confirmação
- ✅ Feedback visual: loading, erros, sucesso (toasts)
- ✅ Estados vazios (empty states)
- ✅ Responsivo (mobile, tablet, desktop)
- ✅ Validação client-side (Zod) + server-side
- ✅ Acessibilidade básica (ARIA, focus management)

## Integração com Backend

A comunicação segue o contrato REST definido no backend:

```typescript
// Exemplo de uso via hooks
const { data, isLoading, error } = useTasks({ status: 'PENDING', priority: 'HIGH' })
const createTask = useCreateTask()
const toggleTask = useToggleTaskStatus()
const deleteTask = useDeleteTask()
```

Endpoints consumidos:
- `GET /api/tasks?status=&priority=&page=&size=`
- `POST /api/tasks`
- `PUT /api/tasks/{id}`
- `PATCH /api/tasks/{id}/toggle`
- `DELETE /api/tasks/{id}`

## Testes

```bash
# Rodar todos os testes
npm test

# Com coverage
npm run test:coverage

# Interface visual
npm run test:ui
```

## Docker

```bash
# Build da imagem
docker build -t todo-frontend .

# Rodar container (servindo build estático via nginx)
docker run -p 80:80 todo-frontend
```

## Deploy

O build estático (`dist/`) pode ser deployado em:
- **Vercel/Netlify**: Conectar repositório, build command `npm run build`, output `dist`
- **Nginx**: Copiar `dist/` para `/var/www/html`, configurar `try_files` para SPA
- **Docker**: Usar `Dockerfile` multi-stage (build + nginx)
- **AWS S3 + CloudFront**: Static website hosting

## Decisões de Design

1. **TanStack Query**: Cache, dedup, retry, invalidation automática - evita estado manual
2. **React Hook Form + Zod**: Validação declarativa, performance, DX
3. **Feature-based structure**: Colocação por domínio (tasks) facilita manutenção
4. **Tailwind CSS**: Utility-first, design system consistente, bundle pequeno
5. **TypeScript strict**: Tipagem end-to-end (API → UI), refactoring seguro
6. **Axios interceptors**: Tratamento centralizado de erros, auth, logging
7. **Componentes UI isolados**: Reutilizáveis, testáveis, documentados