export interface ApiError {
  timestamp: string
  status: number
  error: string
  message: string
  path: string
  validationErrors?: Record<string, string>
}



export interface TaskFilterParams {
  status?: string
  priority?: string
  page?: number
  size?: number
  sortBy?: string
  sortDir?: string
}