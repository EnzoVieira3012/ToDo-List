import { render, screen, fireEvent, waitFor } from '@testing-library/react'
import { TaskForm } from './TaskForm'
import { vi } from 'vitest'

const mockOnSubmit = vi.fn().mockResolvedValue(undefined)
const mockOnClose = vi.fn()

const defaultProps = {
  isOpen: true,
  onClose: mockOnClose,
  onSubmit: mockOnSubmit,
  mode: 'create' as const,
}

describe('TaskForm', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('renders form when open', () => {
    render(<TaskForm {...defaultProps} />)
    expect(screen.getByRole('dialog')).toBeInTheDocument()
    expect(screen.getByText('Nova Tarefa')).toBeInTheDocument()
  })

  it('does not render when closed', () => {
    render(<TaskForm {...defaultProps} isOpen={false} />)
    expect(screen.queryByRole('dialog')).not.toBeInTheDocument()
  })

  it('shows title field with required validation', () => {
    render(<TaskForm {...defaultProps} />)
    expect(screen.getByLabelText(/título \*/i)).toBeInTheDocument()
  })

  it('shows description field', () => {
    render(<TaskForm {...defaultProps} />)
    expect(screen.getByLabelText(/descrição/i)).toBeInTheDocument()
  })

  it('shows priority select with options', () => {
    render(<TaskForm {...defaultProps} />)
    const select = screen.getByLabelText(/prioridade \*/i)
    expect(select).toBeInTheDocument()
    expect(screen.getByText('Baixa')).toBeInTheDocument()
    expect(screen.getByText('Média')).toBeInTheDocument()
    expect(screen.getByText('Alta')).toBeInTheDocument()
  })

  it('shows due date field', () => {
    render(<TaskForm {...defaultProps} />)
    expect(screen.getByLabelText(/data limite/i)).toBeInTheDocument()
  })

  it('validates required title', async () => {
    render(<TaskForm {...defaultProps} />)
    const submitBtn = screen.getByRole('button', { name: /criar/i })
    fireEvent.click(submitBtn)

    await waitFor(() => {
      expect(screen.getByRole('alert')).toHaveTextContent(/título é obrigatório/i)
    })
  })

  it('validates max title length', async () => {
    render(<TaskForm {...defaultProps} />)
    const titleInput = screen.getByLabelText(/título \*/i)
    const longTitle = 'a'.repeat(201)
    fireEvent.change(titleInput, { target: { value: longTitle } })

    const submitBtn = screen.getByRole('button', { name: /criar/i })
    fireEvent.click(submitBtn)

    await waitFor(() => {
      expect(screen.getByRole('alert')).toHaveTextContent(/200 caracteres/i)
    })
  })

  it('validates required priority by preventing submit', async () => {
    render(<TaskForm {...defaultProps} />)
    const titleInput = screen.getByLabelText(/título \*/i)
    fireEvent.change(titleInput, { target: { value: 'Tarefa válida' } })

    const submitBtn = screen.getByRole('button', { name: /criar/i })
    fireEvent.click(submitBtn)

    // The form should not submit because priority is not selected
    await waitFor(() => {
      expect(mockOnSubmit).not.toHaveBeenCalled()
    })
  })

  it('submits form on valid data', async () => {
    render(<TaskForm {...defaultProps} />)

    fireEvent.change(screen.getByLabelText(/título \*/i), { target: { value: 'Nova Tarefa' } })
    fireEvent.change(screen.getByLabelText(/prioridade \*/i), { target: { value: 'HIGH' } })

    const submitBtn = screen.getByRole('button', { name: /criar/i })
    fireEvent.click(submitBtn)

    await waitFor(() => {
      expect(mockOnSubmit).toHaveBeenCalledWith(
        expect.objectContaining({
          title: 'Nova Tarefa',
          priority: 'HIGH',
        })
      )
    })
  })

  it('calls onClose when cancel clicked', async () => {
    render(<TaskForm {...defaultProps} />)
    fireEvent.click(screen.getByRole('button', { name: /cancelar/i }))
    expect(mockOnClose).toHaveBeenCalledTimes(1)
  })

  it('pre-fills form in edit mode', () => {
    render(<TaskForm
      {...defaultProps}
      mode="edit"
      initialData={{
        title: 'Tarefa Existente',
        description: 'Descrição existente',
        priority: 'MEDIUM',
        dueDate: '2025-12-31',
      }}
    />)

    expect(screen.getByLabelText(/título \*/i)).toHaveValue('Tarefa Existente')
    expect(screen.getByLabelText(/descrição/i)).toHaveValue('Descrição existente')
    expect(screen.getByLabelText(/prioridade \*/i)).toHaveValue('MEDIUM')
    expect(screen.getByLabelText(/data limite/i)).toHaveValue('2025-12-31')
    expect(screen.getByText('Editar Tarefa')).toBeInTheDocument()
  })

  it('shows save button in edit mode', () => {
    render(<TaskForm {...defaultProps} mode="edit" />)
    expect(screen.getByRole('button', { name: /salvar/i })).toBeInTheDocument()
  })

  it('disables form when loading', () => {
    render(<TaskForm {...defaultProps} loading />)
    expect(screen.getByRole('button', { name: /criar/i })).toBeDisabled()
    expect(screen.getByLabelText(/título \*/i)).toBeDisabled()
  })
})