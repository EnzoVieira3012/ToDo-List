import { formatDate, formatDateTime, isOverdue } from './formatters'

describe('formatters', () => {
  describe('formatDate', () => {
    it('should format ISO date string to dd/MM/yyyy', () => {
      expect(formatDate('2025-12-31')).toBe('31/12/2025')
    })

    it('should return dash for null', () => {
      expect(formatDate(null)).toBe('-')
    })

    it('should return dash for empty string', () => {
      expect(formatDate('')).toBe('-')
    })

    it('should return dash for invalid date', () => {
      expect(formatDate('invalid')).toBe('-')
    })

    it('should use custom pattern', () => {
      expect(formatDate('2025-12-31', 'yyyy-MM-dd')).toBe('2025-12-31')
    })
  })

  describe('formatDateTime', () => {
    it('should format ISO datetime string', () => {
      const result = formatDateTime('2025-12-31T15:30:00')
      expect(result).toMatch(/31\/12\/2025 \d{2}:\d{2}/)
    })

    it('should return dash for invalid date', () => {
      expect(formatDateTime('invalid')).toBe('-')
    })
  })

  describe('isOverdue', () => {
    it('should return true for past date', () => {
      const yesterday = new Date()
      yesterday.setDate(yesterday.getDate() - 1)
      expect(isOverdue(yesterday.toISOString().split('T')[0])).toBe(true)
    })

    it('should return false for future date', () => {
      const tomorrow = new Date()
      tomorrow.setDate(tomorrow.getDate() + 1)
      expect(isOverdue(tomorrow.toISOString().split('T')[0])).toBe(false)
    })

    it('should return false for null', () => {
      expect(isOverdue(null)).toBe(false)
    })

    it('should return false for today', () => {
      const today = new Date().toISOString().split('T')[0]
      expect(isOverdue(today)).toBe(false)
    })
  })
})