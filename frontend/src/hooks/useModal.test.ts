import { renderHook, act } from '@testing-library/react'
import { useModal } from './useModal'

describe('useModal', () => {
  it('returns closed state initially', () => {
    const { result } = renderHook(() => useModal<string>())
    expect(result.current.isOpen).toBe(false)
    expect(result.current.data).toBeNull()
  })

  it('opens modal with data', () => {
    const { result } = renderHook(() => useModal<string>())

    act(() => {
      result.current.open('test data')
    })

    expect(result.current.isOpen).toBe(true)
    expect(result.current.data).toBe('test data')
  })

  it('opens modal without data', () => {
    const { result } = renderHook(() => useModal<string>())

    act(() => {
      result.current.open()
    })

    expect(result.current.isOpen).toBe(true)
    expect(result.current.data).toBeNull()
  })

  it('closes modal and clears data', () => {
    const { result } = renderHook(() => useModal<string>())

    act(() => {
      result.current.open('data')
    })
    expect(result.current.isOpen).toBe(true)

    act(() => {
      result.current.close()
    })

    expect(result.current.isOpen).toBe(false)
    expect(result.current.data).toBeNull()
  })

  it('can reopen with new data after close', () => {
    const { result } = renderHook(() => useModal<string>())

    act(() => { result.current.open('first') })
    act(() => { result.current.close() })
    act(() => { result.current.open('second') })

    expect(result.current.isOpen).toBe(true)
    expect(result.current.data).toBe('second')
  })
})