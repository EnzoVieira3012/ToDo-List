import { Routes, Route } from 'react-router-dom'
import { TasksPage } from '@/pages/TasksPage'
import { Toaster } from '@/hooks/useToast'

function App() {
  return (
    <div className="min-h-screen bg-gray-50">
      <Routes>
        <Route path="/" element={<TasksPage />} />
      </Routes>
      <Toaster />
    </div>
  )
}

export default App