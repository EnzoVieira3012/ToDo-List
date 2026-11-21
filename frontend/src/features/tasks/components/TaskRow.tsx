import { formatDate } from '@/utils/formatters'
import { Badge } from '@/components/ui'
import { Edit, Trash2, CheckCircle2 } from 'lucide-react'
import { cn } from '@/utils/classnames'
import type { Task } from '@/types/task'

interface TaskRowProps {
  task: Task
  onToggle: (id: string) => void
  onEdit: (task: Task) => void
  onDelete: (id: string) => void
}

export function TaskRow({ task, onToggle, onEdit, onDelete }: TaskRowProps) {
  const isOverdue = task.overdue && task.status === 'PENDING'

  return (
    <tr className={cn('border-b border-gray-100 hover:bg-gray-50 transition-colors', task.status === 'COMPLETED' && 'bg-green-50/50')}>
      <td className="px-6 py-4">
        <div className="flex items-center gap-3">
          <button
            onClick={() => onToggle(task.id)}
            className={cn(
              'w-5 h-5 rounded border-2 flex items-center justify-center transition-colors',
              task.status === 'COMPLETED'
                ? 'bg-green-500 border-green-500 text-white'
                : 'border-gray-300 text-transparent hover:border-primary-500',
            )}
            aria-label={task.status === 'COMPLETED' ? 'Marcar como pendente' : 'Marcar como concluída'}
            aria-pressed={task.status === 'COMPLETED'}
          >
            {task.status === 'COMPLETED' && <CheckCircle2 className="w-4 h-4" />}
          </button>
          <div className="min-w-0">
            <h3 className={cn(
              'font-medium text-gray-900 truncate',
              task.status === 'COMPLETED' && 'line-through text-gray-500',
              isOverdue && 'text-red-600',
            )}>
              {task.title}
            </h3>
            {task.description && (
              <p className={cn('text-sm text-gray-500 truncate mt-0.5', task.status === 'COMPLETED' && 'line-through')}>
                {task.description}
              </p>
            )}
          </div>
        </div>
      </td>
      <td className="px-6 py-4 hidden md:table-cell">
        <Badge variant={task.priority.toLowerCase() as 'low' | 'medium' | 'high'}>
          {task.priority === 'LOW' ? 'Baixa' : task.priority === 'MEDIUM' ? 'Média' : 'Alta'}
        </Badge>
      </td>
      <td className="px-6 py-4 hidden lg:table-cell">
        <Badge variant={task.status.toLowerCase() as 'pending' | 'completed'}>
          {task.status === 'PENDING' ? 'Pendente' : 'Concluída'}
        </Badge>
      </td>
      <td className="px-6 py-4 hidden md:table-cell">
        {task.dueDate ? (
          <span className={cn('text-sm', isOverdue ? 'text-red-600 font-medium' : 'text-gray-500')}>
            {formatDate(task.dueDate)}
            {isOverdue && ' (atrasada)'}
          </span>
        ) : (
          <span className="text-sm text-gray-400">-</span>
        )}
      </td>
      <td className="px-6 py-4 text-right">
        <div className="flex items-center justify-end gap-1">
          <button
            onClick={() => onEdit(task)}
            className="p-2 text-gray-400 hover:text-gray-600 hover:bg-gray-100 rounded-lg transition-colors"
            aria-label="Editar tarefa"
            disabled={task.status === 'COMPLETED'}
          >
            <Edit className="w-4 h-4" />
          </button>
          <button
            onClick={() => onDelete(task.id)}
            className="p-2 text-gray-400 hover:text-red-600 hover:bg-red-50 rounded-lg transition-colors"
            aria-label="Excluir tarefa"
          >
            <Trash2 className="w-4 h-4" />
          </button>
        </div>
      </td>
    </tr>
  )
}