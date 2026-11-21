import { renderHook, act, waitFor } from '@testing-library/react'
import { useDebounce } from './useDebounce'

describe('useDebounce', () => {
  beforeEach(() => {
    vi.useFakeTimers()
  })

  afterEach(() => {
    vi.useRealTimers()
  })

  it('returns initial value immediately', () => {
    const { result } = renderHook(() => useDebounce('initial', 500))
    expect(result.current).toBe('initial')
  })

  it('updates value after delay', () => {
    const { result, rerender } = renderHook(
      ({ value }) => useDebounce(value, 500),
      { initialProps: { value: 'initial' } }
    )

    rerender({ value: 'updated' })
    expect(result.current).toBe('initial')

    act(() => {
      vi.advanceTimersByTime(500)
    })

    expect(result.current).toBe('updated')
  })

  it('only updates after delay completes', () => {
    const { result, rerender } = renderHook(
      ({ value }) => useDebounce(value, 500),
      { initialProps: { value: 'initial' } }
    )

    rerender({ value: 'first' })
    act(() => vi.advanceTimersByTime(200))
    expect(result.current).toBe('initial')

    rerender({ value: 'second' })
    act(() => vi.advanceTimersByTime(200))
    expect(result.current).toBe('initial')

    act(() => vi.advanceTimersByTime(300))
    expect(result.current).toBe('second')
  })

  it('works with numbers', () => {
    const { result, rerender } = renderHook(
      ({ value }) => useDebounce(value, 300),
      { initialProps: { value: 0 } }
    )

    rerender({ value: 42 })
    act(() => vi.advanceTimersByTime(300))
    expect(result.current).toBe(42)
  })

  it('works with objects', () => {
    const { result, rerender } = renderHook(
      ({ value }) => useDebounce(value, 300),
      { initialProps: { value: { a: 1 } } }
    )

    const newObj = { a: 2 }
    rerender({ value: newObj })
    act(() => vi.advanceTimersByTime(300))
    expect(result.current).toBe(newObj)
  })
})