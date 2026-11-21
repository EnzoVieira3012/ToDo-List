import api from '@/api/axios'
import type { Task, CreateTaskDTO, UpdateTaskDTO, TaskFilters, PaginatedResponse } from '@/types/task'

export const taskApi = {
  list: async (filters: TaskFilters): Promise<PaginatedResponse<Task>> => {
    const params = new URLSearchParams()
    if (filters.status) params.append('status', filters.status)
    if (filters.priority) params.append('priority', filters.priority)
    params.append('page', String(filters.page))
    params.append('size', String(filters.size))
    params.append('sortBy', filters.sortBy)
    params.append('sortDir', filters.sortDir)

    const { data } = await api.get<PaginatedResponse<Task>>(`/tasks?${params.toString()}`)
    return data
  },

  getById: async (id: string): Promise<Task> => {
    const { data } = await api.get<Task>(`/tasks/${id}`)
    return data
  },

  create: async (task: CreateTaskDTO): Promise<Task> => {
    const { data } = await api.post<Task>('/tasks', task)
    return data
  },

  update: async (id: string, task: UpdateTaskDTO): Promise<Task> => {
    const { data } = await api.put<Task>(`/tasks/${id}`, task)
    return data
  },

  toggleStatus: async (id: string): Promise<Task> => {
    const { data } = await api.patch<Task>(`/tasks/${id}/toggle`)
    return data
  },

  delete: async (id: string): Promise<void> => {
    await api.delete(`/tasks/${id}`)
  },
}