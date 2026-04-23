import { BrowserRouter, Routes, Route } from 'react-router-dom'
import { HomeView } from './views/HomeView'
import { DetailView } from './views/DetailView'

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<HomeView />} />
        <Route path="/tile/:id" element={<DetailView />} />
      </Routes>
    </BrowserRouter>
  )
}

export default App
