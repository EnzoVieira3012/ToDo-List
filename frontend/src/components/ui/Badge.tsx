import { cn } from '@/utils/classnames'

interface BadgeProps {
  children: React.ReactNode
  variant?: 'default' | 'low' | 'medium' | 'high' | 'pending' | 'completed'
  className?: string
}

export function Badge({ children, variant = 'default', className }: BadgeProps) {
  const variantClasses = {
    default: 'bg-gray-100 text-gray-800',
    low: 'bg-green-100 text-green-800',
    medium: 'bg-amber-100 text-amber-800',
    high: 'bg-red-100 text-red-800',
    pending: 'bg-gray-100 text-gray-800',
    completed: 'bg-green-100 text-green-800',
  }

  return (
    <span className={cn('inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium', variantClasses[variant], className)}>
      {children}
    </span>
  )
}