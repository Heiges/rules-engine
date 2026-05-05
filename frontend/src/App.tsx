import { BrowserRouter, Routes, Route } from 'react-router-dom'
import { RulesetProvider } from './context/RulesetContext'
import { StatusBar } from './components/StatusBar'
import { RequireRole } from './components/RequireRole'
import { AppBreadcrumb } from './components/AppBreadcrumb'
import { RoleSelectionView } from './views/RoleSelectionView'
import { HomeView } from './views/HomeView'
import { PlayerView } from './views/PlayerView'
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
        <AppBreadcrumb />
        <Routes>
          <Route path="/" element={<RoleSelectionView />} />
          <Route path="/home" element={<RequireRole role="spielleiter"><HomeView /></RequireRole>} />
          <Route path="/player" element={<RequireRole role="spieler"><PlayerView /></RequireRole>} />
          <Route path="/edit-ruleset" element={<RequireRole role="spielleiter"><EditRulesetView /></RequireRole>} />
          <Route path="/tile/werte" element={<RequireRole role="spielleiter"><WerteView /></RequireRole>} />
          <Route path="/tile/attributes" element={<RequireRole role="spielleiter"><AttributeView allowGrouping={false} detailBasePath="/tile/attributes" /></RequireRole>} />
          <Route path="/tile/attributes/:index" element={<RequireRole role="spielleiter"><AttributeDetailView listPath="/tile/attributes" /></RequireRole>} />
          <Route path="/world/attributes" element={<RequireRole role="spielleiter"><AttributeView allowGrouping={true} detailBasePath="/world/attributes" /></RequireRole>} />
          <Route path="/world/attributes/:index" element={<RequireRole role="spielleiter"><AttributeDetailView listPath="/world/attributes" /></RequireRole>} />
          <Route path="/tile/skills" element={<RequireRole role="spielleiter"><SkillVerbView /></RequireRole>} />
          <Route path="/tile/skills/domains/:index" element={<RequireRole role="spielleiter"><SkillDomainDetailView /></RequireRole>} />
          <Route path="/tile/skills/:index" element={<RequireRole role="spielleiter"><SkillVerbDetailView /></RequireRole>} />
          <Route path="/tile/cheats" element={<RequireRole role="spielleiter"><CheatView /></RequireRole>} />
          <Route path="/tile/cheats/:index" element={<RequireRole role="spielleiter"><CheatDetailView /></RequireRole>} />
          <Route path="/create-world" element={<RequireRole role="spielleiter"><SpielweltView /></RequireRole>} />
          <Route path="/character-editor" element={<CharacterEditorView />} />
          <Route path="/tile/:id" element={<DetailView />} />
        </Routes>
        <StatusBar />
      </BrowserRouter>
    </RulesetProvider>
  )
}

export default App
