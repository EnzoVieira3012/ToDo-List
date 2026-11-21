import { Fragment } from 'react'
import { ChevronLeft, ChevronRight } from 'lucide-react'
import { Button } from './Button'


interface PaginationProps {
  currentPage: number
  totalPages: number
  onPageChange: (page: number) => void
  showPageSize?: boolean
  pageSize?: number
  onPageSizeChange?: (size: number) => void
}

export function Pagination({ currentPage, totalPages, onPageChange, showPageSize, pageSize, onPageSizeChange }: PaginationProps) {
  if (totalPages <= 1) return null

  const pages = Array.from({ length: totalPages }, (_, i) => i)
  const visiblePages = pages.filter(
    (page) =>
      page === 0 ||
      page === totalPages - 1 ||
      (page >= currentPage - 1 && page <= currentPage + 1)
  )

  return (
    <div className="flex items-center justify-between px-6 py-4 border-t border-gray-200 bg-gray-50 rounded-b-xl">
      <div className="flex items-center gap-4 text-sm text-gray-600">
        <span>Página {currentPage + 1} de {totalPages}</span>
        {showPageSize && onPageSizeChange && (
          <select
            value={pageSize}
            onChange={(e) => onPageSizeChange(Number(e.target.value))}
            className="input w-auto py-1.5 text-sm"
          >
            <option value={5}>5 por página</option>
            <option value={10}>10 por página</option>
            <option value={25}>25 por página</option>
            <option value={50}>50 por página</option>
          </select>
        )}
      </div>

      <div className="flex items-center gap-1">
        <Button
          variant="ghost"
          size="sm"
          onClick={() => onPageChange(0)}
          disabled={currentPage === 0}
          aria-label="Primeira página"
        >
          <ChevronLeft className="w-4 h-4" />
        </Button>
        <Button
          variant="ghost"
          size="sm"
          onClick={() => onPageChange(currentPage - 1)}
          disabled={currentPage === 0}
          aria-label="Página anterior"
        >
          <ChevronLeft className="w-4 h-4" />
        </Button>

        {visiblePages.map((page, index) => {
          const prevPage = visiblePages[index - 1]
          const showEllipsis = prevPage !== undefined && page - prevPage > 1

          return (
            <Fragment key={page}>
              {showEllipsis && <span className="px-2 text-gray-400">...</span>}
              <Button
                variant={page === currentPage ? 'primary' : 'ghost'}
                size="sm"
                onClick={() => onPageChange(page)}
                className="w-8 h-8 px-0"
                aria-label={`Página ${page + 1}`}
                aria-current={page === currentPage ? 'page' : undefined}
              >
                {page + 1}
              </Button>
            </Fragment>
          )
        })}

        <Button
          variant="ghost"
          size="sm"
          onClick={() => onPageChange(currentPage + 1)}
          disabled={currentPage === totalPages - 1}
          aria-label="Próxima página"
        >
          <ChevronRight className="w-4 h-4" />
        </Button>
        <Button
          variant="ghost"
          size="sm"
          onClick={() => onPageChange(totalPages - 1)}
          disabled={currentPage === totalPages - 1}
          aria-label="Última página"
        >
          <ChevronRight className="w-4 h-4" />
        </Button>
      </div>
    </div>
  )
}