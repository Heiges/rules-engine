import { Tile } from '../components/Tile'
import { useRuleset } from '../context/RulesetContext'
import './DetailView.css'
import './HomeView.css'

export function EditRulesetView() {
  const { rulesetData, currentRuleset } = useRuleset()
  const attributeCount = rulesetData?.attributes.length ?? 0
  const skillCount = rulesetData?.skills.length ?? 0
  const domainCount = rulesetData?.skillDomains.length ?? 0
  const cheatCount = rulesetData?.cheats.length ?? 0
  const rulesetName = currentRuleset ?? ''

  return (
    <div className="detail-view">
      <h1>{rulesetName} bearbeiten</h1>
      <div className="tile-grid">
        <Tile
          id="werte"
          name="Werte"
          description="Bearbeite den Wertebereich"
        />
        <Tile
          id="attributes"
          name="Attribute"
          description={`Attribute bearbeiten (${attributeCount} Attribute)`}
        />
        <Tile
          id="skills"
          name="Skills"
          description={`Skill anlegen und modifizieren (${skillCount} Verben, ${domainCount} Domänen)`}
        />
        <Tile
          id="cheats"
          name="Cheats"
          description={`Cheats anlegen und bearbeiten (${cheatCount} Cheats)`}
        />
      </div>
    </div>
  )
}
