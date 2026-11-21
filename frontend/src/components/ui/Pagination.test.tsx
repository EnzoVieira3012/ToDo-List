import { render, screen, fireEvent } from '@testing-library/react'
import { Pagination } from './Pagination'

describe('Pagination', () => {
  it('does not render when totalPages <= 1', () => {
    render(<Pagination currentPage={0} totalPages={1} onPageChange={vi.fn()} />)
    expect(screen.queryByRole('navigation')).not.toBeInTheDocument()
  })

  it('renders pagination controls', () => {
    render(<Pagination currentPage={0} totalPages={5} onPageChange={vi.fn()} />)
    expect(screen.getByText('Página 1 de 5')).toBeInTheDocument()
    expect(screen.getByRole('button', { name: /página 1/i })).toBeInTheDocument()
  })

  it('highlights current page', () => {
    render(<Pagination currentPage={2} totalPages={5} onPageChange={vi.fn()} />)
    const currentPageBtn = screen.getByRole('button', { name: /página 3/i })
    expect(currentPageBtn).toHaveClass('btn-primary')
  })

  it('calls onPageChange when page button clicked', () => {
    const handlePageChange = vi.fn()
    render(<Pagination currentPage={0} totalPages={5} onPageChange={handlePageChange} />)
    fireEvent.click(screen.getByRole('button', { name: /página 2/i }))
    expect(handlePageChange).toHaveBeenCalledWith(1)
  })

  it('disables previous button on first page', () => {
    render(<Pagination currentPage={0} totalPages={5} onPageChange={vi.fn()} />)
    expect(screen.getByRole('button', { name: /página anterior/i })).toBeDisabled()
  })

  it('disables next button on last page', () => {
    render(<Pagination currentPage={4} totalPages={5} onPageChange={vi.fn()} />)
    expect(screen.getByRole('button', { name: /próxima página/i })).toBeDisabled()
  })

  it('shows ellipsis for gaps', () => {
    render(<Pagination currentPage={5} totalPages={10} onPageChange={vi.fn()} />)
    const ellipsis = screen.getAllByText('...')
    expect(ellipsis.length).toBeGreaterThan(0)
  })

  it('shows page size selector when enabled', () => {
    const handleSizeChange = vi.fn()
    render(<Pagination currentPage={0} totalPages={5} onPageChange={vi.fn()} showPageSize pageSize={10} onPageSizeChange={handleSizeChange} />)
    const select = screen.getByRole('combobox')
    expect(select).toBeInTheDocument()
    fireEvent.change(select, { target: { value: '25' } })
    expect(handleSizeChange).toHaveBeenCalledWith(25)
  })
})