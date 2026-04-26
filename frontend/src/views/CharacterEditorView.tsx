import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useRuleset } from '../context/RulesetContext'
import './DetailView.css'
import './CharacterEditorView.css'

export function CharacterEditorView() {
  const navigate = useNavigate()
  const { rulesetData, currentRuleset } = useRuleset()
  const { valueRange, attributes, skills } = rulesetData ?? {
    valueRange: { min: 0, average: 0, max: 0 },
    attributes: [],
    skills: [],
  }

  const [charName, setCharName] = useState('')
  const [attrValues, setAttrValues] = useState<Record<string, number>>(() =>
    Object.fromEntries(attributes.map(a => [a.name, valueRange.average]))
  )
  const [skillLevels, setSkillLevels] = useState<Record<string, number>>(() =>
    Object.fromEntries(skills.map(s => [s.name, 0]))
  )

  function setAttr(name: string, raw: string) {
    const val = parseInt(raw, 10)
    if (isNaN(val)) return
    setAttrValues(prev => ({ ...prev, [name]: Math.min(valueRange.max, Math.max(valueRange.min, val)) }))
  }

  function setSkill(name: string, raw: string) {
    const val = parseInt(raw, 10)
    if (!isNaN(val)) setSkillLevels(prev => ({ ...prev, [name]: Math.max(0, val) }))
  }

  return (
    <div className="detail-view">
      <button className="back-button" onClick={() => navigate('/')}>← Zurück</button>
      <h1>Charactereditor</h1>
      {currentRuleset && <p className="char-ruleset-label">Regelwerk: {currentRuleset}</p>}

      <div className="char-section">
        <h2>Name</h2>
        <input
          className="char-name-input"
          type="text"
          placeholder="Name des Charakters …"
          value={charName}
          onChange={e => setCharName(e.target.value)}
        />
      </div>

      {attributes.length > 0 && (
        <div className="char-section">
          <h2>Attribute</h2>
          <p className="char-range-hint">Wertebereich: {valueRange.min} bis {valueRange.max} (Ø {valueRange.average})</p>
          <div className="char-attr-grid">
            {attributes.map(attr => (
              <div key={attr.name} className="char-attr-row">
                <span className="char-attr-name">{attr.name}</span>
                <input
                  type="range"
                  className="char-slider"
                  min={valueRange.min}
                  max={valueRange.max}
                  value={attrValues[attr.name] ?? valueRange.average}
                  onChange={e => setAttr(attr.name, e.target.value)}
                />
                <input
                  type="number"
                  className="char-value-input"
                  min={valueRange.min}
                  max={valueRange.max}
                  value={attrValues[attr.name] ?? valueRange.average}
                  onChange={e => setAttr(attr.name, e.target.value)}
                />
              </div>
            ))}
          </div>
        </div>
      )}

      {skills.length > 0 && (
        <div className="char-section">
          <h2>Fertigkeiten</h2>
          <div className="char-skill-grid">
            {skills.map(skill => (
              <div key={skill.name} className="char-skill-row">
                <span className="char-skill-name">{skill.name}</span>
                <span className="char-skill-linked">({skill.linkedAttributeName})</span>
                <input
                  type="number"
                  className="char-value-input"
                  min={0}
                  value={skillLevels[skill.name] ?? 0}
                  onChange={e => setSkill(skill.name, e.target.value)}
                />
              </div>
            ))}
          </div>
        </div>
      )}

      {attributes.length === 0 && skills.length === 0 && (
        <p className="char-empty">Das Regelwerk enthält noch keine Attribute oder Fertigkeiten.</p>
      )}
    </div>
  )
}
