# Contributing Guide

Obrigado por contribuir com o To-Do List! Este guia ajuda a manter consistência e qualidade.

---

## Como Contribuir

### 1. Fork & Clone
```bash
git clone https://github.com/SEU_USUARIO/ToDo-List.git
cd ToDo-List
```

### 2. Branch Strategy
```bash
# Sempre partir da develop
git checkout develop
git pull origin develop
git checkout -b feature/nome-da-feature
# ou
git checkout -b fix/nome-do-bug
# ou
git checkout -b docs/atualizacao-readme
```

### 3. Desenvolvimento

#### Backend
```bash
cd backend

# Subir dependências (DB)
docker-compose up -d

# Rodar aplicação
./mvnw spring-boot:run

# Testes
./mvnw test           # Unitários
./mvnw verify         # Integração (Testcontainers)
./mvnw jacoco:report  # Coverage
```

#### Frontend
```bash
cd frontend

# Instalar deps
npm install

# Dev server
npm run dev

# Qualidade
npm run lint:fix      # Auto-fix ESLint
npm run format        # Prettier
npm run typecheck     # TypeScript strict
npm run test:run      # Testes (CI mode)
npm run test:coverage # Coverage
```

### 4. Commits (Conventional Commits)

```
<type>(<scope>): <subject>

<body>

<footer>
```

**Types:**
| Type | Descrição |
|------|-----------|
| `feat` | Nova funcionalidade |
| `fix` | Correção de bug |
| `docs` | Documentação |
| `style` | Formatação (prettier, sem lógica) |
| `refactor` | Refatoração sem mudança de comportamento |
| `test` | Adição/alteração de testes |
| `chore` | Manutenção (deps, configs, build) |
| `perf` | Melhoria de performance |
| `ci` | CI/CD pipelines |

**Exemplos:**
```bash
feat(tasks): adicionar filtro por prioridade na listagem
fix(api): corrigir validação de data limite nula
docs(readme): atualizar instruções de Docker
test(domain): adicionar testes para Value Object DueDate
refactor(frontend): extrair hook useTaskStats
chore(deps): atualizar Spring Boot para 3.2.5
```

### 5. Pull Request

#### Checklist antes de abrir PR:
- [ ] Testes passam (`./mvnw test` / `npm run test:run`)
- [ ] Lint limpo (`./mvnw checkstyle:check` / `npm run lint`)
- [ ] Type check passa (`npm run typecheck`)
- [ ] Build sucesso (`./mvnw package -DskipTests` / `npm run build`)
- [ ] Commits seguem Conventional Commits
- [ ] Branch atualizada com `develop` (`git rebase develop`)
- [ ] Documentação atualizada se necessário (README, ARCHITECTURE.md)

#### Template PR:
```markdown
## Descrição
Breve resumo das mudanças.

## Tipo de Mudança
- [ ] Bug fix
- [ ] Nova feature
- [ ] Breaking change
- [ ] Docs
- [ ] Refactor
- [ ] Testes

## Como Testar
1. Passo 1
2. Passo 2
3. Resultado esperado

## Screenshots (se UI)
...

## Checklist
- [ ] Testes adicionados/atualizados
- [ ] Documentação atualizada
- [ ] Sem breaking changes não documentados
```

### 6. Code Review

**O que revisamos:**
- ✅ Lógica correta e edge cases
- ✅ Testes cobrem novos caminhos
- ✅ Nomenclatura clara (variáveis, métodos, classes)
- ✅ Sem código duplicado (DRY)
- ✅ Performance considerada (N+1 queries, re-renders desnecessários)
- ✅ Segurança (validação, SQL injection, XSS)
- ✅ Acessibilidade (ARIA, focus, contraste)

**Tempo de resposta:** 24-48h úteis

---

## Padrões de Código

### Backend (Java)

#### Nomenclatura
| Elemento | Convenção | Exemplo |
|----------|-----------|---------|
| Package | lowercase, reverse domain | `com.todo.domain.model` |
| Class/Interface | PascalCase | `TaskRepository`, `CreateTaskUseCase` |
| Method | camelCase | `findById`, `execute` |
| Constant | UPPER_SNAKE_CASE | `MAX_TITLE_LENGTH` |
| Enum | PascalCase | `Priority`, `TaskStatus` |

#### Estrutura de Classe (Domain)
```java
public class Task {
    // 1. Fields (final preferivelmente)
    private final TaskId id;
    private TaskTitle title;
    
    // 2. Private constructor (factory methods públicos)
    private Task(TaskId id, TaskTitle title, ...) { ... }
    
    // 3. Factory methods
    public static Task create(...) { ... }
    public static Task reconstruct(...) { ... }
    
    // 4. Business methods (comportamento)
    public void complete() { ... }
    public void updateTitle(TaskTitle newTitle) { ... }
    
    // 5. Query methods (somente leitura)
    public boolean isOverdue() { ... }
    public boolean isCompleted() { ... }
    
    // 6. equals/hashCode (baseado em ID)
}
```

#### Exceptions
- Domain: `IllegalArgumentException`, `IllegalStateException` (validação)
- Application: não lança exceptions de negócio (retorna Result/Optional)
- Infrastructure: `ResourceNotFoundException`, `DataIntegrityViolationException`
- Presentation: `GlobalExceptionHandler` → RFC 7807

### Frontend (TypeScript/React)

#### Nomenclatura
| Elemento | Convenção | Exemplo |
|----------|-----------|---------|
| File/Component | PascalCase | `TaskForm.tsx`, `Button.tsx` |
| Hook | camelCase + `use` prefix | `useTasks.ts`, `useDebounce.ts` |
| Type/Interface | PascalCase | `Task`, `TaskFilters` |
| Function/Variable | camelCase | `handleSubmit`, `isLoading` |
| Constant | UPPER_SNAKE_CASE | `PRIORITY_OPTIONS`, `API_BASE_URL` |
| CSS Class | kebab-case (Tailwind) | `btn-primary`, `input-error` |

#### Componentes
```tsx
// Functional component com TypeScript
interface ButtonProps extends ButtonHTMLAttributes<HTMLButtonElement> {
  variant?: 'primary' | 'secondary' | 'danger' | 'ghost'
  size?: 'sm' | 'md' | 'lg'
  loading?: boolean
}

export const Button = forwardRef<HTMLButtonElement, ButtonProps>(
  ({ className, variant = 'primary', size = 'md', loading, disabled, children, ...props }, ref) => {
    // early returns, guards first
    // render
  }
)

Button.displayName = 'Button'
```

#### Hooks
```typescript
// Custom hook com tipos explícitos
export function useTasks(filters: TaskFilters) {
  return useQuery({
    queryKey: [...TASKS_QUERY_KEY, filters],
    queryFn: () => taskApi.list(filters),
    placeholderData: (previousData) => previousData,
  })
}
```

#### Imports (ordem)
```typescript
// 1. React & libraries
import { useState, useCallback } from 'react'
import { useQuery, useMutation } from '@tanstack/react-query'

// 2. Internal aliases (@/)
import { taskApi } from '@/features/tasks/api'
import { Button } from '@/components/ui'

// 3. Types
import type { Task, TaskFilters } from '@/types/task'

// 4. Relative (evitar se possível)
import { cn } from '../utils/classnames'
```

---

## Testes

### Backend
```java
// Unit Test (Domain)
@Test
@DisplayName("Deve lançar exceção ao concluir tarefa já concluída")
void shouldThrowWhenCompletingAlreadyCompletedTask() {
    Task task = Task.create(...);
    task.complete();
    
    assertThatThrownBy(task::complete)
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Task is already completed");
}

// Unit Test (Application - Mockito)
@ExtendWith(MockitoExtension.class)
class CreateTaskUseCaseTest {
    @Mock TaskRepository repository;
    @Mock TaskApplicationMapper mapper;
    @InjectMocks CreateTaskUseCase useCase;
    
    @Test void shouldCreateTask() { ... }
}

// Integration Test (Testcontainers)
@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = Replace.NONE)
class TaskRepositoryIntegrationTest {
    @Container static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");
    // ...
}
```

### Frontend
```tsx
// Component Test (RTL)
import { render, screen, fireEvent, waitFor } from '@testing-library/react'
import { TaskForm } from './TaskForm'

describe('TaskForm', () => {
  it('valida título obrigatório', async () => {
    render(<TaskForm {...defaultProps} />)
    fireEvent.click(screen.getByRole('button', { name: /criar/i }))
    await waitFor(() => {
      expect(screen.getByRole('alert')).toHaveTextContent(/título é obrigatório/i)
    })
  })
})

// Hook Test
import { renderHook, act } from '@testing-library/react'
import { useDebounce } from './useDebounce'

describe('useDebounce', () => {
  it('atualiza valor após delay', () => {
    const { result, rerender } = renderHook(
      ({ value }) => useDebounce(value, 500),
      { initialProps: { value: 'initial' } }
    )
    rerender({ value: 'updated' })
    act(() => vi.advanceTimersByTime(500))
    expect(result.current).toBe('updated')
  })
})
```

---

## CI/CD Pipeline

### GitHub Actions (`.github/workflows/ci.yml`)

```yaml
name: CI

on:
  push:
    branches: [main, develop]
  pull_request:
    branches: [main, develop]

jobs:
  backend:
    runs-on: ubuntu-latest
    services:
      postgres:
        image: postgres:15-alpine
        env: { POSTGRES_DB: todo_db, POSTGRES_USER: todo_user, POSTGRES_PASSWORD: todo_pass }
        ports: [5432:5432]
        options: --health-cmd="pg_isready" --health-interval=10s --health-timeout=5s --health-retries=5
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with: { distribution: 'temurin', java-version: '21', cache: 'maven' }
      - run: cd backend && ./mvnw verify jacoco:report

  frontend:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-node@v4
        with: { node-version: '20', cache: 'npm', cache-dependency-path: 'frontend/package-lock.json' }
      - run: cd frontend && npm ci
      - run: cd frontend && npm run lint
      - run: cd frontend && npm run typecheck
      - run: cd frontend && npm run test:run
      - run: cd frontend && npm run build

  docker:
    needs: [backend, frontend]
    if: github.ref == 'refs/heads/main'
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: docker/build-push-action@v5
        with:
          context: ./backend
          push: true
          tags: ghcr.io/${{ github.repository }}/backend:latest
      - uses: docker/build-push-action@v5
        with:
          context: ./frontend
          push: true
          tags: ghcr.io/${{ github.repository }}/frontend:latest
```

---

## Release Process

### Versionamento (SemVer)
```
MAJOR.MINOR.PATCH
```
- **MAJOR**: Breaking changes (API incompatível)
- **MINOR**: Novas features compatíveis
- **PATCH**: Bug fixes compatíveis

### Release (main branch)
1. Merge `develop` → `main` via PR
2. Tag: `git tag v1.2.0 && git push origin v1.2.0`
3. GitHub Actions: Build + Push Docker + Deploy
4. Changelog automático (conventional commits)

---

## Comunicação

- **Issues**: Bug reports, feature requests
- **Discussions**: Perguntas, ideias, RFCs
- **PRs**: Code review técnico
- **Slack/Discord**: (se configurado) para chat rápido

---

## Licença

Ao contribuir, você concorda que suas contribuições serão licenciadas sob a **MIT License** do projeto.

---

## Dúvidas?

Abra uma [Issue](https://github.com/EnzoVieira3012/ToDo-List/issues) com label `question` ou `help wanted`.