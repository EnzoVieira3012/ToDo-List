import { Search, X } from 'lucide-react'
import { Select, Button } from '@/components/ui'
import type { TaskStatus, TaskPriority } from '@/types/task'

interface TaskFiltersType {
  status?: TaskStatus
  priority?: TaskPriority
  search?: string
}

interface TaskFilterBarProps {
  filters: TaskFiltersType
  onFiltersChange: (filters: Partial<TaskFiltersType>) => void
  onClear: () => void
  hasActiveFilters: boolean
}

const STATUS_OPTIONS = [
  { value: '', label: 'Todos os status' },
  { value: 'PENDING', label: 'Pendente' },
  { value: 'COMPLETED', label: 'Concluída' },
]

const PRIORITY_OPTIONS = [
  { value: '', label: 'Todas as prioridades' },
  { value: 'LOW', label: 'Baixa' },
  { value: 'MEDIUM', label: 'Média' },
  { value: 'HIGH', label: 'Alta' },
]

export function TaskFilterBar({ filters, onFiltersChange, onClear, hasActiveFilters }: TaskFilterBarProps) {
  return (
    <div className="card mb-6">
      <div className="card-body">
        <div className="flex flex-col sm:flex-row gap-4">
          <div className="relative flex-1">
            <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-400" />
            <input
              type="text"
              placeholder="Buscar por título..."
              value={filters.search || ''}
              onChange={(e) => onFiltersChange({ search: e.target.value })}
              className="input pl-10"
            />
          </div>

          <Select
            label="Status"
            options={STATUS_OPTIONS}
            value={filters.status || ''}
            onChange={(e) => onFiltersChange({ status: (e.target.value as TaskStatus) || undefined })}
          />

          <Select
            label="Prioridade"
            options={PRIORITY_OPTIONS}
            value={filters.priority || ''}
            onChange={(e) => onFiltersChange({ priority: (e.target.value as TaskPriority) || undefined })}
          />

          {hasActiveFilters && (
            <Button
              variant="ghost"
              size="sm"
              onClick={onClear}
              className="self-end mt-4 sm:mt-0"
            >
              <X className="w-4 h-4 mr-1" />
              Limpar
            </Button>
          )}
        </div>
      </div>
    </div>
  )
}