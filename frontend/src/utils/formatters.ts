import { format, parseISO, isValid } from 'date-fns'
import { ptBR } from 'date-fns/locale'

export function formatDate(dateString: string | null, pattern = 'dd/MM/yyyy'): string {
  if (!dateString) return '-'
  try {
    const date = parseISO(dateString)
    return isValid(date) ? format(date, pattern, { locale: ptBR }) : '-'
  } catch {
    return '-'
  }
}

export function formatDateTime(dateString: string, pattern = 'dd/MM/yyyy HH:mm'): string {
  try {
    const date = parseISO(dateString)
    return isValid(date) ? format(date, pattern, { locale: ptBR }) : '-'
  } catch {
    return '-'
  }
}

export function isOverdue(dueDate: string | null): boolean {
  if (!dueDate) return false
  const today = new Date()
  today.setHours(0, 0, 0, 0)
  const due = parseISO(dueDate)
  return due < today
}