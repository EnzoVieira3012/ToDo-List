import { render, screen, fireEvent } from '@testing-library/react'
import { Modal } from './Modal'

describe('Modal', () => {
  it('does not render when closed', () => {
    render(<Modal isOpen={false} onClose={() => {}}>Content</Modal>)
    expect(screen.queryByRole('dialog')).not.toBeInTheDocument()
  })

  it('renders when open', () => {
    render(<Modal isOpen={true} onClose={() => {}}>Content</Modal>)
    expect(screen.getByRole('dialog')).toBeInTheDocument()
    expect(screen.getByText('Content')).toBeInTheDocument()
  })

  it('renders title', () => {
    render(<Modal isOpen={true} onClose={() => {}} title="Meu Modal">Content</Modal>)
    expect(screen.getByText('Meu Modal')).toBeInTheDocument()
  })

  it('renders description', () => {
    render(<Modal isOpen={true} onClose={() => {}} description="Descrição do modal">Content</Modal>)
    expect(screen.getByText('Descrição do modal')).toBeInTheDocument()
  })

  it('calls onClose when close button clicked', () => {
    const handleClose = vi.fn()
    render(<Modal isOpen={true} onClose={handleClose}>Content</Modal>)
    fireEvent.click(screen.getByLabelText('Close modal'))
    expect(handleClose).toHaveBeenCalledTimes(1)
  })

  it('calls onClose when overlay clicked', () => {
    const handleClose = vi.fn()
    render(<Modal isOpen={true} onClose={handleClose} closeOnOverlayClick>Content</Modal>)
    const overlay = screen.getByTestId('modal-overlay')
    fireEvent.click(overlay)
    expect(handleClose).toHaveBeenCalledTimes(1)
  })

  it('does not call onClose when overlay clicked and closeOnOverlayClick is false', () => {
    const handleClose = vi.fn()
    render(<Modal isOpen={true} onClose={handleClose} closeOnOverlayClick={false}>Content</Modal>)
    const overlay = screen.getByTestId('modal-overlay')
    fireEvent.click(overlay)
    expect(handleClose).not.toHaveBeenCalled()
  })

  it('closes on Escape key', () => {
    const handleClose = vi.fn()
    render(<Modal isOpen={true} onClose={handleClose} closeOnEscape>Content</Modal>)
    fireEvent.keyDown(screen.getByRole('dialog'), { key: 'Escape' })
    expect(handleClose).toHaveBeenCalledTimes(1)
  })

  it('does not close on Escape when closeOnEscape is false', () => {
    const handleClose = vi.fn()
    render(<Modal isOpen={true} onClose={handleClose} closeOnEscape={false}>Content</Modal>)
    fireEvent.keyDown(screen.getByRole('dialog'), { key: 'Escape' })
    expect(handleClose).not.toHaveBeenCalled()
  })

  it('applies size classes', () => {
    const { rerender } = render(<Modal isOpen={true} onClose={() => {}} size="sm">Content</Modal>)
    expect(screen.getByRole('dialog')).toHaveClass('max-w-sm')

    rerender(<Modal isOpen={true} onClose={() => {}} size="lg">Content</Modal>)
    expect(screen.getByRole('dialog')).toHaveClass('max-w-lg')

    rerender(<Modal isOpen={true} onClose={() => {}} size="full">Content</Modal>)
    expect(screen.getByRole('dialog')).toHaveClass('max-w-4xl')
  })
})