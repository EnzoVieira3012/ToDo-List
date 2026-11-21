import { useState, useCallback, useMemo } from 'react'
import { Plus } from 'lucide-react'
import { useTasks, useCreateTask, useUpdateTask, useToggleTaskStatus, useDeleteTask, useTaskStats } from '@/features/tasks/hooks/useTasks'
import { TaskForm } from '@/features/tasks/components/TaskForm'
import { TaskList } from '@/features/tasks/components/TaskList'
import { TaskFilterBar } from '@/features/tasks/components/TaskFilterBar'
import { TaskStats } from '@/features/tasks/components/TaskStats'
import { Pagination, Button } from '@/components/ui'
import type { Task, TaskFilters, CreateTaskDTO, UpdateTaskDTO } from '@/types/task'

export function TasksPage() {
  const [filters, setFilters] = useState<TaskFilters>({
    status: undefined,
    priority: undefined,
    page: 0,
    size: 10,
    sortBy: 'createdAt',
    sortDir: 'desc',
  })
  const [search, setSearch] = useState('')
  const [isFormOpen, setIsFormOpen] = useState(false)
  const [editingTask, setEditingTask] = useState<Task | null>(null)

  const { data, isLoading, isError, error } = useTasks(filters)
  const stats = useTaskStats()

  const createMutation = useCreateTask()
  const updateMutation = useUpdateTask()
  const toggleMutation = useToggleTaskStatus()
  const deleteMutation = useDeleteTask()

  const isMutating = createMutation.isPending || updateMutation.isPending || toggleMutation.isPending || deleteMutation.isPending

  const handleCreate = useCallback(async (data: CreateTaskDTO | UpdateTaskDTO) => {
    await createMutation.mutateAsync(data as CreateTaskDTO)
  }, [createMutation])

  const handleUpdate = useCallback(async (data: CreateTaskDTO | UpdateTaskDTO) => {
    if (editingTask) {
      await updateMutation.mutateAsync({ id: editingTask.id, task: data as UpdateTaskDTO })
    }
  }, [editingTask, updateMutation])

  const handleToggle = useCallback((id: string) => {
    toggleMutation.mutate(id)
  }, [toggleMutation])

  const handleDelete = useCallback((id: string) => {
    if (window.confirm('Tem certeza que deseja excluir esta tarefa?')) {
      deleteMutation.mutate(id)
    }
  }, [deleteMutation])

  const handleEdit = useCallback((task: Task) => {
    setEditingTask(task)
    setIsFormOpen(true)
  }, [])

  const handleFiltersChange = useCallback((newFilters: Partial<TaskFilters>) => {
    setFilters((prev) => ({ ...prev, ...newFilters, page: 0 }))
  }, [])

  const handleClearFilters = useCallback(() => {
    setFilters((prev) => ({ ...prev, status: undefined, priority: undefined, page: 0 }))
    setSearch('')
  }, [])

  const handlePageChange = useCallback((page: number) => {
    setFilters((prev) => ({ ...prev, page }))
  }, [])

  const handlePageSizeChange = useCallback((size: number) => {
    setFilters((prev) => ({ ...prev, size, page: 0 }))
  }, [])

  const hasActiveFilters = useMemo(() => {
    return !!(filters.status || filters.priority || search)
  }, [filters.status, filters.priority, search])

  const paginatedData = data
  const tasks = paginatedData?.content ?? []
  const totalPages = paginatedData?.totalPages ?? 0
  const currentPage = paginatedData?.page ?? 0

  return (
    <div className="min-h-screen bg-gray-50">
      <header className="bg-white border-b border-gray-200 sticky top-0 z-10">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex items-center justify-between h-16">
            <div className="flex items-center gap-3">
              <div className="w-10 h-10 rounded-xl bg-primary-600 flex items-center justify-center">
                <Plus className="w-6 h-6 text-white" />
              </div>
              <div>
                <h1 className="text-xl font-bold text-gray-900">To-Do List</h1>
                <p className="text-sm text-gray-500">Gerencie suas tarefas com facilidade</p>
              </div>
            </div>
            <Button onClick={() => { setEditingTask(null); setIsFormOpen(true); }}>
              <Plus className="w-4 h-4 mr-2" />
              Nova Tarefa
            </Button>
          </div>
        </div>
      </header>

      <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {isError && (
          <div className="mb-6 p-4 bg-red-50 border border-red-200 rounded-lg text-red-700">
            Erro ao carregar tarefas: {(error as Error)?.message || 'Erro desconhecido'}
          </div>
        )}

        <TaskStats
          total={stats.total}
          pending={stats.pending}
          completed={stats.completed}
          overdue={stats.overdue}
        />

        <TaskFilterBar
          filters={{ status: filters.status, priority: filters.priority, search }}
          onFiltersChange={handleFiltersChange}
          onClear={handleClearFilters}
          hasActiveFilters={hasActiveFilters}
        />

        <div className="card">
          <TaskList
            tasks={tasks}
            isLoading={isLoading}
            onToggle={handleToggle}
            onEdit={handleEdit}
            onDelete={handleDelete}
          />
          {paginatedData && (
            <Pagination
              currentPage={currentPage}
              totalPages={totalPages}
              onPageChange={handlePageChange}
              showPageSize
              pageSize={filters.size}
              onPageSizeChange={handlePageSizeChange}
            />
          )}
        </div>
      </main>

      <TaskForm
        isOpen={isFormOpen}
        onClose={() => { setIsFormOpen(false); setEditingTask(null); }}
        onSubmit={editingTask ? handleUpdate : handleCreate}
        initialData={editingTask ? {
          title: editingTask.title,
          description: editingTask.description || '',
          priority: editingTask.priority,
          dueDate: editingTask.dueDate,
        } : null}
        mode={editingTask ? 'edit' : 'create'}
        loading={isMutating}
      />
    </div>
  )
}