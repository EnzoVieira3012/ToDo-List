import { render, screen, fireEvent } from '@testing-library/react'
import { Input, Textarea } from './Input'

describe('Input', () => {
  it('renders label', () => {
    render(<Input label="Nome" />)
    expect(screen.getByLabelText('Nome')).toBeInTheDocument()
  })

  it('renders error message', () => {
    render(<Input error="Campo obrigatório" />)
    expect(screen.getByRole('alert')).toHaveTextContent('Campo obrigatório')
  })

  it('shows hint', () => {
    render(<Input hint="Dica útil" />)
    expect(screen.getByText('Dica útil')).toBeInTheDocument()
  })

  it('calls onChange', () => {
    const handleChange = vi.fn()
    render(<Input onChange={handleChange} />)
    fireEvent.change(screen.getByRole('textbox'), { target: { value: 'teste' } })
    expect(handleChange).toHaveBeenCalled()
  })

  it('applies error class when error prop is set', () => {
    const { container } = render(<Input error="Erro" />)
    expect(container.querySelector('input')).toHaveClass('input-error')
  })

  it('is disabled when disabled prop is true', () => {
    render(<Input disabled />)
    expect(screen.getByRole('textbox')).toBeDisabled()
  })
})

describe('Textarea', () => {
  it('renders textarea with label', () => {
    render(<Textarea label="Descrição" />)
    expect(screen.getByLabelText('Descrição')).toBeInTheDocument()
    const textarea = screen.getByRole('textbox')
    expect(textarea.tagName).toBe('TEXTAREA')
  })

  it('shows error', () => {
    render(<Textarea error="Muito longo" />)
    expect(screen.getByRole('alert')).toHaveTextContent('Muito longo')
  })

  it('has correct rows', () => {
    render(<Textarea rows={5} />)
    expect(screen.getByRole('textbox')).toHaveAttribute('rows', '5')
  })
})