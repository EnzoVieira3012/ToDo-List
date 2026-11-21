import { render, screen, fireEvent } from '@testing-library/react'
import { EmptyState } from './EmptyState'

describe('EmptyState', () => {
  it('renders title', () => {
    render(<EmptyState title="Sem tarefas" />)
    expect(screen.getByText('Sem tarefas')).toBeInTheDocument()
  })

  it('renders description', () => {
    render(<EmptyState title="Title" description="Descrição aqui" />)
    expect(screen.getByText('Descrição aqui')).toBeInTheDocument()
  })

  it('renders action button', () => {
    const handleClick = vi.fn()
    render(<EmptyState title="Title" action={{ label: 'Criar', onClick: handleClick }} />)
    const button = screen.getByRole('button', { name: /criar/i })
    expect(button).toBeInTheDocument()
    fireEvent.click(button)
    expect(handleClick).toHaveBeenCalledTimes(1)
  })

  it('renders icon', () => {
    render(<EmptyState icon="tasks" title="Title" />)
    expect(screen.getByTestId('empty-state-icon')).toBeInTheDocument()
  })

  it('renders completed icon', () => {
    render(<EmptyState icon="completed" title="Title" />)
    expect(screen.getByTestId('empty-state-icon')).toBeInTheDocument()
  })
})