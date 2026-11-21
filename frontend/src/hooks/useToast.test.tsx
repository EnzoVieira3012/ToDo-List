import { render, screen, act } from '@testing-library/react'
import { ToastProvider, useToast, Toaster } from './useToast'

describe('useToast', () => {
  const wrapper = ({ children }: { children: React.ReactNode }) => (
    <ToastProvider>
      {children}
      <Toaster />
    </ToastProvider>
  )

  it('shows toast', () => {
    function TestComponent() {
      const { showToast } = useToast()
      return <button onClick={() => act(() => showToast('success', 'Test'))}>Show</button>
    }
    render(<TestComponent />, { wrapper })
    act(() => {
      screen.getByRole('button').click()
    })
    expect(screen.getByRole('alert')).toHaveTextContent('Test')
  })

  it('shows multiple toasts', () => {
    function TestComponent() {
      const { showToast } = useToast()
      return (
        <div>
          <button onClick={() => act(() => showToast('success', 'First'))}>First</button>
          <button onClick={() => act(() => showToast('error', 'Second'))}>Second</button>
        </div>
      )
    }
    render(<TestComponent />, { wrapper })
    act(() => {
      screen.getByRole('button', { name: 'First' }).click()
      screen.getByRole('button', { name: 'Second' }).click()
    })
    expect(screen.getAllByRole('alert')).toHaveLength(2)
  })

  it('dismisses toast by clicking close button', () => {
    function TestComponent() {
      const { showToast, dismissToast, toasts } = useToast()
      return (
        <div>
          <button onClick={() => act(() => showToast('info', 'Test'))}>Show</button>
          {toasts.length > 0 && (
            <button onClick={() => act(() => dismissToast(toasts[0].id))}>Dismiss Toast</button>
          )}
        </div>
      )
    }
    render(<TestComponent />, { wrapper })
    act(() => {
      screen.getByRole('button', { name: 'Show' }).click()
    })
    expect(screen.getByRole('alert')).toBeInTheDocument()
    act(() => {
      screen.getByRole('button', { name: 'Dismiss Toast' }).click()
    })
    expect(screen.queryByRole('alert')).not.toBeInTheDocument()
  })
})