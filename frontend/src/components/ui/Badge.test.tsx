import { render, screen } from '@testing-library/react'
import { Badge } from './Badge'

describe('Badge', () => {
  it('renders children', () => {
    render(<Badge>Test</Badge>)
    expect(screen.getByText('Test')).toBeInTheDocument()
  })

  it('applies default variant', () => {
    render(<Badge>Default</Badge>)
    expect(screen.getByText('Default')).toHaveClass('bg-gray-100', 'text-gray-800')
  })

  it('applies low variant', () => {
    render(<Badge variant="low">Baixa</Badge>)
    expect(screen.getByText('Baixa')).toHaveClass('bg-green-100', 'text-green-800')
  })

  it('applies medium variant', () => {
    render(<Badge variant="medium">Média</Badge>)
    expect(screen.getByText('Média')).toHaveClass('bg-amber-100', 'text-amber-800')
  })

  it('applies high variant', () => {
    render(<Badge variant="high">Alta</Badge>)
    expect(screen.getByText('Alta')).toHaveClass('bg-red-100', 'text-red-800')
  })

  it('applies pending variant', () => {
    render(<Badge variant="pending">Pendente</Badge>)
    expect(screen.getByText('Pendente')).toHaveClass('bg-gray-100', 'text-gray-800')
  })

  it('applies completed variant', () => {
    render(<Badge variant="completed">Concluída</Badge>)
    expect(screen.getByText('Concluída')).toHaveClass('bg-green-100', 'text-green-800')
  })
})