import { cn } from '@/utils/classnames'
import { ClipboardList, Clock, CheckCircle2, AlertTriangle } from 'lucide-react'

interface StatCardProps {
  icon: React.ReactNode
  label: string
  value: number
  color: string
  bgColor: string
}

function StatCard({ icon, label, value, color, bgColor }: StatCardProps) {
  return (
    <div className="card p-4">
      <div className="flex items-center justify-between">
        <div>
          <p className="text-sm font-medium text-gray-600">{label}</p>
          <p className="text-2xl font-bold text-gray-900 mt-1">{value}</p>
        </div>
        <div className={cn('p-3 rounded-xl', bgColor)}>
          <span className={cn('text-2xl', color)}>{icon}</span>
        </div>
      </div>
    </div>
  )
}

interface TaskStatsProps {
  total: number
  pending: number
  completed: number
  overdue: number
}

export function TaskStats({ total, pending, completed, overdue }: TaskStatsProps) {
  return (
    <div className="grid grid-cols-2 md:grid-cols-4 gap-4 mb-6">
      <StatCard
        icon={<ClipboardList className="w-6 h-6" />}
        label="Total"
        value={total}
        color="text-primary-600"
        bgColor="bg-primary-100"
      />
      <StatCard
        icon={<Clock className="w-6 h-6" />}
        label="Pendentes"
        value={pending}
        color="text-amber-600"
        bgColor="bg-amber-100"
      />
      <StatCard
        icon={<CheckCircle2 className="w-6 h-6" />}
        label="Concluídas"
        value={completed}
        color="text-green-600"
        bgColor="bg-green-100"
      />
      <StatCard
        icon={<AlertTriangle className="w-6 h-6" />}
        label="Atrasadas"
        value={overdue}
        color="text-red-600"
        bgColor="bg-red-100"
      />
    </div>
  )
}