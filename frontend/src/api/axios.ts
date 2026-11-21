import axios from 'axios'

const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '/api',
  timeout: Number(import.meta.env.VITE_API_TIMEOUT) || 10000,
  headers: {
    'Content-Type': 'application/json',
  },
})

api.interceptors.request.use(
  (config) => {
    return config
  },
  (error) => Promise.reject(error),
)

api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response) {
      const { status, data } = error.response
      switch (status) {
        case 400:
          console.error('Validation Error:', data)
          break
        case 404:
          console.error('Not Found:', data)
          break
        case 500:
          console.error('Server Error:', data)
          break
      }
    } else if (error.request) {
      console.error('Network Error:', error.message)
    }
    return Promise.reject(error)
  },
)

export default api