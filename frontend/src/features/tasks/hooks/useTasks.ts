import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { taskApi } from '../api/taskApi'
import type { CreateTaskDTO, UpdateTaskDTO, TaskFilters } from '@/types/task'

const TASKS_QUERY_KEY = ['tasks'] as const

export function useTasks(filters: TaskFilters) {
  return useQuery({
    queryKey: [...TASKS_QUERY_KEY, filters],
    queryFn: () => taskApi.list(filters),
    placeholderData: (previousData) => previousData,
  })
}

export function useTask(id: string) {
  return useQuery({
    queryKey: [...TASKS_QUERY_KEY, id],
    queryFn: () => taskApi.getById(id),
    enabled: !!id,
  })
}

export function useCreateTask() {
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: (task: CreateTaskDTO) => taskApi.create(task),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: TASKS_QUERY_KEY })
    },
  })
}

export function useUpdateTask() {
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: ({ id, task }: { id: string; task: UpdateTaskDTO }) => taskApi.update(id, task),
    onSuccess: (updatedTask) => {
      queryClient.invalidateQueries({ queryKey: TASKS_QUERY_KEY })
      queryClient.setQueryData([...TASKS_QUERY_KEY, updatedTask.id], updatedTask)
    },
  })
}

export function useToggleTaskStatus() {
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: (id: string) => taskApi.toggleStatus(id),
    onSuccess: (updatedTask) => {
      queryClient.invalidateQueries({ queryKey: TASKS_QUERY_KEY })
      queryClient.setQueryData([...TASKS_QUERY_KEY, updatedTask.id], updatedTask)
    },
  })
}

export function useDeleteTask() {
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: (id: string) => taskApi.delete(id),
    onSuccess: (_, deletedId) => {
      queryClient.invalidateQueries({ queryKey: TASKS_QUERY_KEY })
      queryClient.removeQueries({ queryKey: [...TASKS_QUERY_KEY, deletedId] })
    },
  })
}

export function useTaskStats() {
  const { data } = useTasks({
    status: undefined,
    priority: undefined,
    page: 0,
    size: 1000,
    sortBy: 'createdAt',
    sortDir: 'desc',
  })

  const tasks = data?.content ?? []

  return {
    total: tasks.length,
    pending: tasks.filter((t) => t.status === 'PENDING').length,
    completed: tasks.filter((t) => t.status === 'COMPLETED').length,
    overdue: tasks.filter((t) => t.overdue).length,
  }
}