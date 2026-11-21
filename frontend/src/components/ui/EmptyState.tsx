import { cn } from '@/utils/classnames'
import { CheckCircle2, ClipboardList, PlusCircle } from 'lucide-react'
import { Button } from './Button'

interface EmptyStateProps {
  icon?: 'tasks' | 'completed' | 'default'
  title: string
  description?: string
  action?: {
    label: string
    onClick: () => void
  }
  className?: string
}



const ICONS = {
  tasks: ClipboardList,
  completed: CheckCircle2,
  default: PlusCircle,
}

export function EmptyState({ icon = 'default', title, description, action, className }: EmptyStateProps) {
  const Icon = ICONS[icon]

  return (
    <div className={cn('flex flex-col items-center justify-center py-12 px-4 text-center', className)}>
      <div className="w-16 h-16 rounded-full bg-gray-100 flex items-center justify-center mb-4 text-gray-400">
        <Icon className="w-8 h-8" />
      </div>
      <h3 className="text-lg font-medium text-gray-900">{title}</h3>
      {description && (
        <p className="mt-1 text-sm text-gray-500 max-w-sm">{description}</p>
      )}
      {action && (
        <Button onClick={action.onClick} className="mt-4" size="sm">
          <PlusCircle className="w-4 h-4" />
          {action.label}
        </Button>
      )}
    </div>
  )
}