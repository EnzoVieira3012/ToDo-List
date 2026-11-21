import { EmptyState } from '@/components/ui'
import { TaskRow } from './TaskRow'
import type { Task } from '@/types/task'

interface TaskListProps {
  tasks: Task[]
  isLoading: boolean
  onToggle: (id: string) => void
  onEdit: (task: Task) => void
  onDelete: (id: string) => void
}

export function TaskList({ tasks, isLoading, onToggle, onEdit, onDelete }: TaskListProps) {
  if (isLoading) {
    return (
      <div className="overflow-x-auto">
        <table className="w-full">
          <thead>
            <tr className="border-b border-gray-200">
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Tarefa</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider hidden md:table-cell">Prioridade</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider hidden lg:table-cell">Status</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider hidden md:table-cell">Data Limite</th>
              <th className="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">Ações</th>
            </tr>
          </thead>
          <tbody>
            {[...Array(5)].map((_, i) => (
              <tr key={i} className="border-b border-gray-100">
                <td className="px-6 py-4">
                  <LoadingSkeleton className="h-5 w-3/4 rounded" />
                </td>
                <td className="px-6 py-4 hidden md:table-cell">
                  <LoadingSkeleton className="h-5 w-20 rounded-full" />
                </td>
                <td className="px-6 py-4 hidden lg:table-cell">
                  <LoadingSkeleton className="h-5 w-24 rounded-full" />
                </td>
                <td className="px-6 py-4 hidden md:table-cell">
                  <LoadingSkeleton className="h-5 w-24 rounded" />
                </td>
                <td className="px-6 py-4 text-right">
                  <LoadingSkeleton className="w-8 h-8 rounded-lg mx-auto" />
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    )
  }

  if (tasks.length === 0) {
    return (
      <EmptyState
        icon="tasks"
        title="Nenhuma tarefa encontrada"
        description="Comece criando sua primeira tarefa ou ajuste os filtros."
      />
    )
  }

  return (
    <div className="overflow-x-auto">
      <table className="w-full">
        <thead>
          <tr className="border-b border-gray-200 bg-gray-50">
            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Tarefa</th>
            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider hidden md:table-cell">Prioridade</th>
            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider hidden lg:table-cell">Status</th>
            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider hidden md:table-cell">Data Limite</th>
            <th className="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">Ações</th>
          </tr>
        </thead>
        <tbody className="divide-y divide-gray-100">
          {tasks.map((task) => (
            <TaskRow
              key={task.id}
              task={task}
              onToggle={onToggle}
              onEdit={onEdit}
              onDelete={onDelete}
            />
          ))}
        </tbody>
      </table>
    </div>
  )
}

function LoadingSkeleton({ className }: { className?: string }) {
  return <div className={`animate-pulse bg-gray-200 rounded ${className || ''}`} />
}