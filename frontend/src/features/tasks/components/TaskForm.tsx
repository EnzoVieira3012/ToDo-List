import { useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import { z } from 'zod'
import { Button, Input, Textarea, Select, Modal } from '@/components/ui'
import type { CreateTaskDTO, UpdateTaskDTO, TaskPriority } from '@/types/task'

const taskSchema = z.object({
  title: z.string().min(1, 'Título é obrigatório').max(200, 'Título deve ter no máximo 200 caracteres'),
  description: z.string().max(1000, 'Descrição deve ter no máximo 1000 caracteres').optional(),
  priority: z.enum(['LOW', 'MEDIUM', 'HIGH'], { required_error: 'Prioridade é obrigatória' }),
  dueDate: z.string().optional().nullable(),
})

type TaskFormData = z.infer<typeof taskSchema>

interface TaskFormProps {
  isOpen: boolean
  onClose: () => void
  onSubmit: (data: CreateTaskDTO | UpdateTaskDTO) => Promise<void>
  initialData?: Partial<TaskFormData> | null
  mode: 'create' | 'edit'
  loading?: boolean
}

type TaskFormSubmitData = CreateTaskDTO | UpdateTaskDTO

const PRIORITY_OPTIONS: { value: TaskPriority; label: string }[] = [
  { value: 'LOW', label: 'Baixa' },
  { value: 'MEDIUM', label: 'Média' },
  { value: 'HIGH', label: 'Alta' },
]

export function TaskForm({ isOpen, onClose, onSubmit, initialData, mode, loading }: TaskFormProps) {
  const {
    register,
    handleSubmit,
    reset,
    formState: { errors },
  } = useForm<TaskFormData>({
    resolver: zodResolver(taskSchema),
    defaultValues: {
      title: '',
      description: '',
      priority: 'MEDIUM',
      dueDate: null,
      ...initialData,
    },
  })

  const onFormSubmit = async (data: TaskFormData) => {
    const payload: TaskFormSubmitData = {
      title: data.title,
      description: data.description || undefined,
      priority: data.priority as TaskPriority,
      dueDate: data.dueDate || null,
    }
    await onSubmit(payload)
    reset()
    onClose()
  }

  return (
    <Modal
      isOpen={isOpen}
      onClose={onClose}
      title={mode === 'create' ? 'Nova Tarefa' : 'Editar Tarefa'}
      size="md"
      closeOnOverlayClick={!loading}
      closeOnEscape={!loading}
    >
      <form onSubmit={handleSubmit(onFormSubmit)} className="space-y-4">
        <Input
          label="Título *"
          placeholder="Digite o título da tarefa"
          error={errors.title?.message}
          {...register('title')}
          disabled={loading}
          autoFocus
        />

        <Textarea
          label="Descrição"
          placeholder="Descrição opcional"
          error={errors.description?.message}
          {...register('description')}
          disabled={loading}
          rows={3}
        />

        <div className="grid grid-cols-2 gap-4">
          <Select
            label="Prioridade *"
            options={PRIORITY_OPTIONS}
            placeholder="Selecione"
            error={errors.priority?.message}
            {...register('priority')}
            disabled={loading}
          />

          <div>
            <label htmlFor="dueDate" className="label">
              Data Limite
            </label>
            <input
              type="date"
              id="dueDate"
              className="input"
              {...register('dueDate')}
              disabled={loading}
              min={new Date().toISOString().split('T')[0]}
            />
          </div>
        </div>

        <div className="flex justify-end gap-3 pt-4 border-t border-gray-200">
          <Button type="button" variant="ghost" onClick={onClose} disabled={loading}>
            Cancelar
          </Button>
          <Button type="submit" loading={loading}>
            {mode === 'create' ? 'Criar' : 'Salvar'}
          </Button>
        </div>
      </form>
    </Modal>
  )
}