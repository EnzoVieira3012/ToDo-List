/// <reference types="vite/client" />

interface ImportMetaEnv {
  readonly VITE_API_BASE_URL: string
  readonly VITE_API_TIMEOUT: string
  readonly VITE_ENABLE_MOCK_API: string
  readonly VITE_ENABLE_REACT_QUERY_DEVTOOLS: string
  readonly VITE_DEFAULT_PAGE_SIZE: string
  readonly VITE_DATE_FORMAT: string
}

interface ImportMeta {
  readonly env: ImportMetaEnv
}