# To-Do List Frontend

Interface web moderna para gerenciamento de tarefas construída com **React 18**, **TypeScript 5**, **Vite 5**, **TanStack Query v5** e **Tailwind CSS 3**.

## Stack Tecnológica

| Tecnologia | Versão | Descrição |
|------------|--------|-----------|
| **React** | 18.2 | Functional Components + Hooks |
| **TypeScript** | 5.2 | Strict mode, type safety |
| **Vite** | 5.0 | Build tool + Dev server (HMR) |
| **TanStack Query** | 5.17 | Server state management (cache, retry, dedup) |
| **React Router** | 6.22 | Routing SPA |
| **Axios** | 1.6 | HTTP client com interceptors |
| **React Hook Form** | 7.49 | Formulários performáticos |
| **Zod** | 3.22 | Schema validation |
| **Tailwind CSS** | 3.4 | Utility-first styling |
| **Lucide React** | 0.303 | Ícones SVG otimizados |
| **date-fns** | 3.2 | Formatação de datas |
| **Vitest** | 1.2 | Unit/Integration testing |
| **React Testing Library** | 14.1 | Component testing |
| **ESLint + Prettier** | 8.56 / 3.2 | Code quality + formatting |

## Estrutura do Projeto

```
src/
├── api/                    # Camada de comunicação HTTP
│   ├── axios.ts           # Instância Axios configurada (interceptors)
│   └── endpoints.ts       # Funções de API tipadas (genéricas)
├── components/            # Componentes UI reutilizáveis (atoms/molecules)
│   └── ui/                # Button, Input, Textarea, Select, Modal, Badge,
│                          # Pagination, LoadingSpinner, EmptyState, Toaster
├── features/              # Features organizadas por domínio (feature-based)
│   └── tasks/
│       ├── components/    # TaskForm, TaskList, TaskRow, TaskFilterBar, TaskStats
│       ├── hooks/         # useTasks, useTaskStats, useCreateTask, etc.
│       ├── api.ts         # Endpoints específicos de tasks
│       └── types.ts       # Types TypeScript específicos de tasks
├── hooks/                 # Custom hooks compartilhados
│   ├── useModal.ts        # Controle de modais
│   ├── useToast.ts        # Sistema de notificações (toasts)
│   └── useDebounce.ts     # Debounce para inputs
├── pages/                 # Páginas/Rotas (page-level components)
│   └── TasksPage.tsx      # Página principal (lista + filtros + CRUD)
├── types/                 # Types globais compartilhados
│   ├── api.ts             # ApiError, PaginatedResponse, TaskFilterParams
│   └── task.ts            # Task, TaskPriority, TaskStatus, DTOs, Labels
├── utils/                 # Helpers puros (sem side effects)
│   ├── formatters.ts      # formatDate, formatDateTime, isOverdue
│   ├── classnames.ts      # cn() = clsx + tailwind-merge
│   └── validators.ts      # Validadores Zod reutilizáveis
├── styles/                # Estilos globais
│   └── globals.css        # @tailwind + @layer components/utilities
├── test/                  # Configuração de testes
│   └── setup.ts           # jest-dom, mocks globais
├── App.tsx                # Root component + Routes + ToastProvider
├── main.tsx               # Entry point + QueryClientProvider
└── vite-env.d.ts          # TypeScript env declarations
```

## Pré-requisitos

- **Node.js 20+** (LTS - recomendado via nvm/fnm)
- **npm 10+** (ou pnpm/yarn)
- **Backend rodando** em `http://localhost:8080` (ou configurar `VITE_API_BASE_URL`)

## Como Rodar

### Desenvolvimento

```bash
# 1. Instalar dependências
npm install

# 2. Configurar variáveis de ambiente
cp .env.example .env
# Editar .env se necessário

# 3. Iniciar dev server (HMR)
npm run dev
```

**Aplicação**: `http://localhost:5173`  
**Proxy API**: `/api/*` → `http://localhost:8080/api/*` (configurado em `vite.config.ts`)

### Build de Produção

```bash
npm run build
```

Gera pasta `dist/` otimizada (minified, hashed, gzip/brotli ready).

### Preview do Build

```bash
npm run preview
```

---

## Scripts Disponíveis

| Script | Comando | Descrição |
|--------|---------|-----------|
| `dev` | `vite` | Dev server com HMR |
| `build` | `tsc && vite build` | Type check + build produção |
| `preview` | `vite preview` | Preview local do build |
| `lint` | `eslint . --ext ts,tsx` | Lint check |
| `lint:fix` | `eslint . --ext ts,tsx --fix` | Auto-fix lint |
| `format` | `prettier --write "src/**/*.{ts,tsx,css,md}"` | Formatar código |
| `test` | `vitest` | Testes unitários (watch mode) |
| `test:run` | `vitest --run` | Testes unitários (CI mode) |
| `test:ui` | `vitest --ui` | Interface visual (Vitest UI) |
| `test:coverage` | `vitest --coverage` | Coverage report (v8) |
| `typecheck` | `tsc --noEmit` | Type checking apenas |

---

## Variáveis de Ambiente

Arquivo `.env` (baseado em `.env.example`):

| Variável | Descrição | Default | Obrigatório |
|----------|-----------|---------|-------------|
| `VITE_API_BASE_URL` | URL base da API backend | `http://localhost:8080/api` | ✅ |
| `VITE_API_TIMEOUT` | Timeout requests (ms) | `10000` | ❌ |
| `VITE_ENABLE_MOCK_API` | Usar mock API (dev) | `false` | ❌ |
| `VITE_ENABLE_REACT_QUERY_DEVTOOLS` | React Query DevTools | `true` | ❌ |
| `VITE_DEFAULT_PAGE_SIZE` | Paginação padrão | `10` | ❌ |
| `VITE_DATE_FORMAT` | Formato de data (date-fns) | `dd/MM/yyyy` | ❌ |

---

## Funcionalidades Implementadas

### CRUD Completo
- ✅ **Listar** tarefas com paginação server-side
- ✅ **Filtrar** por status (`PENDING`/`COMPLETED`) e prioridade (`LOW`/`MEDIUM`/`HIGH`)
- ✅ **Criar** tarefa (título obrigatório, descrição opcional, prioridade, data limite)
- ✅ **Editar** tarefa via modal (pré-preenchido)
- ✅ **Alternar Status** (Pendente ↔ Concluída) com 1 clique (checkbox na tabela)
- ✅ **Excluir** com confirmação (modal)

### UX & Feedback
- ✅ **Loading states**: Skeletons na tabela, spinners em botões
- ✅ **Error handling**: Toasts para erros de rede/validação/API
- ✅ **Success toasts**: Confirmação visual após operações
- ✅ **Empty states**: Ilustração + CTA quando sem tarefas/filtrado
- ✅ **Confirmação de exclusão**: Modal nativo `window.confirm`

### Interface
- ✅ **Responsivo**: Mobile-first (tailwind breakpoints: sm, md, lg, xl)
- ✅ **Tabela adaptativa**: Colunas ocultas em mobile (`hidden md:table-cell`)
- ✅ **Badges coloridos**: Prioridade (verde/âmbar/vermelho) + Status (cinza/verde)
- ✅ **Datas inteligentes**: Formato `dd/MM/yyyy` + badge "atrasada" em vermelho
- ✅ **Acessibilidade**: ARIA labels, focus management, semantic HTML

### Validação
- ✅ **Client-side**: Zod schema (título 1-200 chars, descrição max 1000, prioridade obrigatória)
- ✅ **Server-side**: Backend valida + retorna RFC 7807 errors
- ✅ **Form UX**: Inline errors, disabled submit enquanto loading, auto-focus

---

## Arquitetura de Estado

### TanStack Query (Server State)
```typescript
// Query keys centralizadas
const TASKS_QUERY_KEY = ['tasks'] as const

// Hooks gerados
useTasks(filters)           // Lista paginada + filtros
useTask(id)                 // Detalhe único
useCreateTask()             // Mutation create
useUpdateTask()             // Mutation update
useToggleTaskStatus()       // Mutation toggle
useDeleteTask()             // Mutation delete
useTaskStats()              // Agregados (total, pendentes, concluídas, atrasadas)

// Features:
// - Cache 5min (staleTime) + GC 24h (gcTime)
// - Auto-refetch on window focus: false
// - Retry: 1x
// - Optimistic updates via onMutate/onError/onSettled
// - Invalidação automática: queryClient.invalidateQueries({ queryKey: TASKS_QUERY_KEY })
```

### React Hook Form + Zod (Form State)
```typescript
const schema = z.object({
  title: z.string().min(1).max(200),
  description: z.string().max(1000).optional(),
  priority: z.enum(['LOW', 'MEDIUM', 'HIGH']),
  dueDate: z.string().optional().nullable(),
})

const { register, handleSubmit, setValue, reset, formState: { errors } } = useForm({
  resolver: zodResolver(schema),
  defaultValues: { title: '', description: '', priority: 'MEDIUM', dueDate: null }
})
```

### Custom Hooks (UI State)
| Hook | Responsabilidade |
|------|------------------|
| `useModal<T>()` | Abrir/fechar modal com data tipada |
| `useToast()` | Toast global (success/error/warning/info) + auto-dismiss |
| `useDebounce<T>(value, delay)` | Debounce para search inputs |

---

## Componentes UI (`src/components/ui/`)

| Componente | Props Principais | Variants |
|------------|------------------|----------|
| `Button` | `variant`, `size`, `loading`, `disabled` | primary, secondary, danger, ghost \| sm, md, lg |
| `Input` | `label`, `error`, `hint`, `type` | - |
| `Textarea` | `label`, `error`, `hint`, `rows` | - |
| `Select` | `label`, `error`, `options[]`, `placeholder` | - |
| `Modal` | `isOpen`, `onClose`, `title`, `size`, `closeOnOverlayClick` | sm, md, lg, xl, full |
| `Badge` | `variant` | default, low, medium, high, pending, completed |
| `Pagination` | `currentPage`, `totalPages`, `onPageChange`, `showPageSize` | - |
| `LoadingSpinner` | `size` | sm, md, lg |
| `EmptyState` | `icon`, `title`, `description`, `action` | tasks, completed, default |
| `Toaster` | (context) | success, error, warning, info |

---

## Tipagem TypeScript (`src/types/`)

### Task Types (`task.ts`)
```typescript
type TaskPriority = 'LOW' | 'MEDIUM' | 'HIGH'
type TaskStatus = 'PENDING' | 'COMPLETED'

interface Task {
  id: string
  title: string
  description: string | null
  priority: TaskPriority
  status: TaskStatus
  dueDate: string | null
  createdAt: string
  updatedAt: string
  overdue: boolean
}

interface CreateTaskDTO { title, description?, priority, dueDate? }
interface UpdateTaskDTO { title?, description?, priority?, dueDate? }

interface TaskFilters {
  status?: TaskStatus
  priority?: TaskPriority
  page: number
  size: number
  sortBy: string
  sortDir: 'asc' | 'desc'
}
```

### API Types (`api.ts`)
```typescript
interface ApiError { timestamp, status, error, message, path, validationErrors? }
interface PaginatedResponse<T> { content: T[], page, size, totalElements, totalPages, first, last, empty }
interface TaskFilterParams { status?, priority?, page?, size?, sortBy?, sortDir? }
```

---

## Testes

### Configuração
- **Framework**: Vitest + React Testing Library
- **Environment**: jsdom
- **Coverage**: v8 (text, json, html)
- **Setup**: `src/test/setup.ts` (jest-dom, matchMedia mock, localStorage mock)

### Executar
```bash
# Watch mode (desenvolvimento)
npm test

# CI mode (single run)
npm run test:run

# Com coverage
npm run test:coverage
# Abre: coverage/index.html

# UI visual
npm run test:ui
```

### Cobertura Atual (85 testes)

| Categoria | Arquivos | Testes |
|-----------|----------|--------|
| **Hooks** | `useDebounce`, `useModal`, `useToast` | 13 |
| **UI Components** | Button, Input, Badge, Modal, EmptyState, Pagination | 47 |
| **Feature Tasks** | TaskForm | 14 |
| **Utils** | formatters | 11 |
| **Total** | **11 arquivos** | **85** |

### Padrões de Teste
```typescript
// Component test
import { render, screen, fireEvent, waitFor } from '@testing-library/react'
import { Button } from './Button'

describe('Button', () => {
  it('calls onClick handler', () => {
    const handleClick = vi.fn()
    render(<Button onClick={handleClick}>Click</Button>)
    fireEvent.click(screen.getByRole('button'))
    expect(handleClick).toHaveBeenCalledTimes(1)
  })
})

// Hook test
import { renderHook, act } from '@testing-library/react'
import { useDebounce } from './useDebounce'

describe('useDebounce', () => {
  it('updates value after delay', () => {
    const { result, rerender } = renderHook(
      ({ value }) => useDebounce(value, 500),
      { initialProps: { value: 'initial' } }
    )
    rerender({ value: 'updated' })
    expect(result.current).toBe('initial')
    act(() => vi.advanceTimersByTime(500))
    expect(result.current).toBe('updated')
  })
})
```

---

## Docker

### Multi-stage Dockerfile
```dockerfile
# Build stage
FROM node:20-alpine AS builder
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
RUN npm run build

# Production stage (nginx)
FROM nginx:alpine
COPY --from=builder /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
```

### nginx.conf (SPA routing)
```nginx
server {
    listen 80;
    server_name localhost;
    root /usr/share/nginx/html;
    index index.html;

    location / {
        try_files $uri $uri/ /index.html;
    }

    # Cache static assets
    location ~* \.(js|css|png|jpg|jpeg|gif|ico|svg|woff|woff2)$ {
        expires 1y;
        add_header Cache-Control "public, immutable";
    }

    # Gzip
    gzip on;
    gzip_types text/css application/javascript application/json;
}
```

### Build & Run
```bash
# Build
docker build -t todo-frontend .

# Run
docker run -d -p 80:80 --name todo-frontend todo-frontend

# Ou com docker-compose (raiz do projeto)
docker-compose up -d frontend
```

---

## Deploy

### Vercel / Netlify (Recomendado)
```bash
# Conectar repositório GitHub
# Build Command: npm run build
# Output Directory: dist
# Environment Variables: configurar VITE_API_BASE_URL
```

### Nginx (VPS/EC2)
```bash
# 1. Build local
npm run build

# 2. Copiar para servidor
scp -r dist/* user@server:/var/www/html/

# 3. Configurar nginx (try_files para SPA)
```

### Docker (Kubernetes/ECS/Cloud Run)
```bash
# Build multi-arch
docker buildx build --platform linux/amd64,linux/arm64 -t registry/todo-frontend:latest --push .

# Deploy manifest
kubectl apply -f k8s/
```

### AWS S3 + CloudFront
```bash
aws s3 sync dist/ s3://meu-bucket --delete
aws cloudfront create-invalidation --distribution-id ID --paths "/*"
```

---

## Decisões de Design (ADRs)

### ADR-FE-001: TanStack Query para Server State
**Decisão**: Usar TanStack Query v5 em vez de Redux/Zustand/Context para dados do servidor.
**Justificativa**: Cache automático, dedup, retry, stale-while-revalidate, devtools, zero boilerplate.

### ADR-FE-002: React Hook Form + Zod
**Decisão**: RHF (uncontrolled) + Zod para validação.
**Justificativa**: Performance (menos re-renders), DX excelente, schema reutilizável, type inference.

### ADR-FE-003: Feature-based Structure
**Decisão**: Agrupar por domínio (`features/tasks/`) em vez de tipo técnico.
**Justificativa**: Colocation, escalabilidade, onboarding, manutenção facilitada.

### ADR-FE-004: Tailwind CSS
**Decisão**: Utility-first CSS com design tokens customizados.
**Justificativa**: Consistência, bundle pequeno (purge), responsividade nativa, dark mode ready.

### ADR-FE-005: TypeScript Strict
**Decisão**: `strict: true`, `noUnusedLocals`, `noUnusedParameters`, `exactOptionalPropertyTypes`.
**Justificativa**: Catch bugs em build, refactoring seguro, documentação viva.

### ADR-FE-006: Axios Interceptors Centralizados
**Decisão**: Single axios instance com interceptors para auth, errors, logging.
**Justificativa**: DRY, consistência, fácil adição de auth tokens, monitoring.

### ADR-FE-007: Componentes UI Atômicos
**Decisão**: Biblioteca própria de primitivos (`Button`, `Input`, `Modal`, etc).
**Justificativa**: Design system consistente, testáveis isoladamente, customizáveis, sem dependência externa pesada.

---

## Troubleshooting

### Proxy não funciona (dev)
```bash
# Verificar vite.config.ts
server: {
  proxy: {
    '/api': {
      target: 'http://localhost:8080',
      changeOrigin: true,
    },
  },
}
```

### CORS Error
- Verificar backend `CorsConfig` permite `http://localhost:5173`
- Em produção: configurar domínio real

### Build falha (TypeScript)
```bash
npm run typecheck  # Ver erros sem emitir
# Corrigir types antes de build
```

### Testes falham (act/vi.useFakeTimers)
```bash
# Resetar timers entre testes
afterEach(() => vi.useRealTimers())
```

### Variáveis de ambiente não carregam
- Devem iniciar com `VITE_`
- Reiniciar dev server após alterar `.env`
- `import.meta.env.VAR_NAME` (não `process.env`)

---

## Contribuição

1. Fork → Branch: `git checkout -b feature/nome`
2. Código + Testes (`npm run test:run`)
3. Lint + Format (`npm run lint:fix && npm run format`)
4. Type check (`npm run typecheck`)
5. Commit convencional: `feat: descrição`
6. Push → PR

### Commits Conventionais
```
feat: nova funcionalidade
fix: correção de bug
docs: documentação
style: formatação (prettier)
refactor: refatoração sem mudança de comportamento
test: adição/alteração de testes
chore: manutenção (deps, configs)
perf: melhoria de performance
```

---

## Licença

MIT License - veja [LICENSE](../LICENSE) para detalhes.