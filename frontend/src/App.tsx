import { BrowserRouter, Routes, Route } from 'react-router-dom'
import { RulesetProvider } from './context/RulesetContext'
import { StatusBar } from './components/StatusBar'
import { HomeView } from './views/HomeView'
import { DetailView } from './views/DetailView'
import { EditRulesetView } from './views/EditRulesetView'
import { AttributeView } from './views/AttributeView'
import { AttributeDetailView } from './views/AttributeDetailView'
import { WerteView } from './views/WerteView'
import { CharacterEditorView } from './views/CharacterEditorView'

function App() {
  return (
    <RulesetProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<HomeView />} />
          <Route path="/edit-ruleset" element={<EditRulesetView />} />
          <Route path="/tile/werte" element={<WerteView />} />
          <Route path="/tile/attributes" element={<AttributeView />} />
          <Route path="/tile/attributes/:index" element={<AttributeDetailView />} />
          <Route path="/character-editor" element={<CharacterEditorView />} />
          <Route path="/tile/:id" element={<DetailView />} />
        </Routes>
        <StatusBar />
      </BrowserRouter>
    </RulesetProvider>
  )
}

export default App
