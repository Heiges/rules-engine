import { useNavigate } from 'react-router-dom'
import { Tile } from '../components/Tile'
import { useRuleset } from '../context/RulesetContext'
import './DetailView.css'
import './HomeView.css'

function countAttributes(xml: string | null): number {
  if (!xml) return 0
  const doc = new DOMParser().parseFromString(xml, 'application/xml')
  return doc.querySelectorAll('attributeSet > attribute').length
}

export function EditRulesetView() {
  const navigate = useNavigate()
  const { xmlContent, currentRuleset } = useRuleset()
  const attributeCount = countAttributes(xmlContent)
  const rulesetName = currentRuleset?.replace(/\.xml$/i, '') ?? ''

  return (
    <div className="detail-view">
      <button className="back-button" onClick={() => navigate('/')}>← Zurück</button>
      <h1>{rulesetName} bearbeiten</h1>
      <div className="tile-grid">
        <Tile
          id="attributes"
          name="Attribute"
          description={`Attribute bearbeiten (${attributeCount} Attribute)`}
        />
      </div>
    </div>
  )
}
