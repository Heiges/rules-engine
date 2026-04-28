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
import { SkillVerbView } from './views/SkillVerbView'
import { SkillVerbDetailView } from './views/SkillVerbDetailView'
import { SkillDomainDetailView } from './views/SkillDomainDetailView'
import { CheatView } from './views/CheatView'
import { CheatDetailView } from './views/CheatDetailView'
import { SpielweltView } from './views/SpielweltView'

function App() {
  return (
    <RulesetProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<HomeView />} />
          <Route path="/edit-ruleset" element={<EditRulesetView />} />
          <Route path="/tile/werte" element={<WerteView />} />
          <Route path="/tile/attributes" element={<AttributeView allowGrouping={false} backPath="/edit-ruleset" detailBasePath="/tile/attributes" />} />
          <Route path="/tile/attributes/:index" element={<AttributeDetailView listPath="/tile/attributes" />} />
          <Route path="/world/attributes" element={<AttributeView allowGrouping={true} backPath="/create-world" detailBasePath="/world/attributes" />} />
          <Route path="/world/attributes/:index" element={<AttributeDetailView listPath="/world/attributes" />} />
          <Route path="/tile/skills" element={<SkillVerbView />} />
          <Route path="/tile/skills/domains/:index" element={<SkillDomainDetailView />} />
          <Route path="/tile/skills/:index" element={<SkillVerbDetailView />} />
          <Route path="/tile/cheats" element={<CheatView />} />
          <Route path="/tile/cheats/:index" element={<CheatDetailView />} />
          <Route path="/character-editor" element={<CharacterEditorView />} />
          <Route path="/create-world" element={<SpielweltView />} />
          <Route path="/tile/:id" element={<DetailView />} />
        </Routes>
        <StatusBar />
      </BrowserRouter>
    </RulesetProvider>
  )
}

export default App
