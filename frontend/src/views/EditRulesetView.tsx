import { useNavigate } from 'react-router-dom'
import { Tile } from '../components/Tile'
import { useRuleset } from '../context/RulesetContext'
import './DetailView.css'
import './HomeView.css'

export function EditRulesetView() {
  const navigate = useNavigate()
  const { rulesetData, currentRuleset } = useRuleset()
  const attributeCount = rulesetData?.attributes.length ?? 0
  const skillCount = rulesetData?.skills.length ?? 0
  const rulesetName = currentRuleset ?? ''

  return (
    <div className="detail-view">
      <button className="back-button" onClick={() => navigate('/')}>← Zurück</button>
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
          description={`Skill anlegen und modifizieren (${skillCount} Skills)`}
        />
      </div>
    </div>
  )
}
