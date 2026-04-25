import { BrowserRouter, Routes, Route } from 'react-router-dom'
import { RulesetProvider } from './context/RulesetContext'
import { StatusBar } from './components/StatusBar'
import { HomeView } from './views/HomeView'
import { DetailView } from './views/DetailView'

function App() {
  return (
    <RulesetProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<HomeView />} />
          <Route path="/tile/:id" element={<DetailView />} />
        </Routes>
        <StatusBar />
      </BrowserRouter>
    </RulesetProvider>
  )
}

export default App
