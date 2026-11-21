export type TaskPriority = 'LOW' | 'MEDIUM' | 'HIGH'
export type TaskStatus = 'PENDING' | 'COMPLETED'

export interface Task {
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

export interface CreateTaskDTO {
  title: string
  description?: string
  priority: TaskPriority
  dueDate?: string | null
}

export interface UpdateTaskDTO {
  title?: string
  description?: string
  priority?: TaskPriority
  dueDate?: string | null
}

export interface TaskFilters {
  status?: TaskStatus
  priority?: TaskPriority
  page: number
  size: number
  sortBy: string
  sortDir: 'asc' | 'desc'
}

export const PRIORITY_LABELS: Record<TaskPriority, string> = {
  LOW: 'Baixa',
  MEDIUM: 'Média',
  HIGH: 'Alta',
}

export const PRIORITY_COLORS: Record<TaskPriority, string> = {
  LOW: 'badge-low',
  MEDIUM: 'badge-medium',
  HIGH: 'badge-high',
}

export const STATUS_LABELS: Record<TaskStatus, string> = {
  PENDING: 'Pendente',
  COMPLETED: 'Concluída',
}

export const STATUS_COLORS: Record<TaskStatus, string> = {
  PENDING: 'badge-pending',
  COMPLETED: 'badge-completed',
}

export interface PaginatedResponse<T> {
  content: T[]
  page: number
  size: number
  totalElements: number
  totalPages: number
  first: boolean
  last: boolean
  empty: boolean
}