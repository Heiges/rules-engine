import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useRuleset } from '../context/RulesetContext'
import { rollDice } from '../api'
import type { Attribute, RollResult } from '../api'
import './DetailView.css'
import './CharacterEditorView.css'

interface EffectiveAttr {
  key: string
  isGroup: boolean
}

function buildEffectiveAttrs(attributes: Attribute[]): EffectiveAttr[] {
  const seenGroups = new Set<string>()
  const result: EffectiveAttr[] = []
  for (const attr of attributes) {
    const group = attr.groupName && attr.groupName !== 'Allgemein' ? attr.groupName : null
    if (group) {
      if (!seenGroups.has(group)) {
        seenGroups.add(group)
        result.push({ key: group, isGroup: true })
      }
    } else {
      result.push({ key: attr.name, isGroup: false })
    }
  }
  return result
}

export function CharacterEditorView() {
  const navigate = useNavigate()
  const { rulesetData, currentRuleset } = useRuleset()
  const { valueRange, attributes, skills } = rulesetData ?? {
    valueRange: { min: 0, average: 0, max: 0 },
    attributes: [],
    skills: [],
  }

  const effectiveAttrs = buildEffectiveAttrs(attributes)

  const [charName, setCharName] = useState('')
  const [attrValues, setAttrValues] = useState<Record<string, number>>(() =>
    Object.fromEntries(effectiveAttrs.map(e => [e.key, valueRange.average]))
  )
  const [rollResults, setRollResults] = useState<Record<string, RollResult>>({})
  const [rollingFor, setRollingFor] = useState<string | null>(null)

  async function roll(attrName: string) {
    setRollingFor(attrName)
    try {
      const result = await rollDice(attrValues[attrName] ?? valueRange.average)
      setRollResults(prev => ({ ...prev, [attrName]: result }))
    } finally {
      setRollingFor(null)
    }
  }

  function setAttr(name: string, raw: string) {
    const val = parseInt(raw, 10)
    if (isNaN(val)) return
    setAttrValues(prev => ({ ...prev, [name]: Math.min(valueRange.max, Math.max(valueRange.min, val)) }))
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

      {effectiveAttrs.length > 0 && (
        <div className="char-section">
          <h2>Attribute</h2>
          <p className="char-range-hint">Wertebereich: {valueRange.min} bis {valueRange.max} (Ø {valueRange.average})</p>
          <div className="char-attr-grid">
            {effectiveAttrs.map(eff => {
              const result = rollResults[eff.key]
              return (
                <div key={eff.key} className="char-attr-row">
                  <span className="char-attr-name">{eff.key}</span>
                  <input
                    type="range"
                    className="char-slider"
                    min={valueRange.min}
                    max={valueRange.max}
                    value={attrValues[eff.key] ?? valueRange.average}
                    onChange={e => setAttr(eff.key, e.target.value)}
                  />
                  <input
                    type="number"
                    className="char-value-input"
                    min={valueRange.min}
                    max={valueRange.max}
                    value={attrValues[eff.key] ?? valueRange.average}
                    onChange={e => setAttr(eff.key, e.target.value)}
                  />
                  <button
                    className="char-roll-btn"
                    onClick={() => roll(eff.key)}
                    disabled={rollingFor === eff.key}
                  >
                    {rollingFor === eff.key ? '…' : 'Würfeln'}
                  </button>
                  {result && (
                    <span className="char-roll-result">
                      <span className="char-dice">
                        {result.dice.map((d, i) => (
                          <span
                            key={i}
                            className={`char-die${result.success && d === result.paschValue ? ' char-die--pasch' : ''}`}
                          >
                            {d}
                          </span>
                        ))}
                      </span>
                      <span className={`char-outcome char-outcome--${result.success ? 'success' : 'failure'}`}>
                        {result.success
                          ? `Erfolg (${result.paschCount}× ${result.paschValue})`
                          : 'Misserfolg'}
                      </span>
                    </span>
                  )}
                </div>
              )
            })}
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
                {skill.description && (
                  <span className="char-skill-linked">{skill.description}</span>
                )}
              </div>
            ))}
          </div>
        </div>
      )}

      {effectiveAttrs.length === 0 && skills.length === 0 && (
        <p className="char-empty">Das Regelwerk enthält noch keine Attribute oder Fertigkeiten.</p>
      )}
    </div>
  )
}
