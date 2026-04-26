import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useRuleset } from '../context/RulesetContext'
import { exportRuleset, saveRuleset } from '../api'
import type { ValueRange } from '../api'
import './DetailView.css'
import './WerteView.css'

export function WerteView() {
  const navigate = useNavigate()
  const { rulesetData, setRulesetData, currentRuleset, fileHandle } = useRuleset()
  const [werte, setWerte] = useState<ValueRange>(() => rulesetData?.valueRange ?? { min: -10, average: 0, max: 10 })
  const [saved, setSaved] = useState(false)
  const [saveError, setSaveError] = useState<string | null>(null)

  const isValid = werte.min <= werte.average && werte.average <= werte.max

  async function save() {
    if (!rulesetData || !isValid) return
    const updatedData = { ...rulesetData, valueRange: werte }
    setRulesetData(updatedData)
    setSaveError(null)
    try {
      if (fileHandle) {
        const xml = await exportRuleset(updatedData)
        const writable = await fileHandle.createWritable()
        await writable.write(xml)
        await writable.close()
      } else if (currentRuleset) {
        await saveRuleset(currentRuleset, updatedData)
      }
      setSaved(true)
      setTimeout(() => setSaved(false), 1800)
    } catch (err) {
      setSaveError(err instanceof Error ? err.message : String(err))
    }
  }

  function update(field: keyof ValueRange, raw: string) {
    const val = parseInt(raw, 10)
    if (!isNaN(val)) setWerte(prev => ({ ...prev, [field]: val }))
    setSaved(false)
  }

  return (
    <div className="detail-view">
      <button className="back-button" onClick={() => navigate('/edit-ruleset')}>← Zurück</button>
      <h1>Wertebereich</h1>

      <div className="werte-form">
        <div className="werte-row">
          <label className="werte-label" htmlFor="werte-min">Unterster Wert</label>
          <input
            id="werte-min"
            type="number"
            className="werte-input"
            value={werte.min}
            onChange={e => update('min', e.target.value)}
          />
        </div>

        <div className="werte-row">
          <label className="werte-label" htmlFor="werte-avg">Durchschnittswert</label>
          <input
            id="werte-avg"
            type="number"
            className="werte-input"
            value={werte.average}
            onChange={e => update('average', e.target.value)}
          />
        </div>

        <div className="werte-row">
          <label className="werte-label" htmlFor="werte-max">Oberster Wert</label>
          <input
            id="werte-max"
            type="number"
            className="werte-input"
            value={werte.max}
            onChange={e => update('max', e.target.value)}
          />
        </div>

        {!isValid && (
          <p className="werte-error">
            Ungültige Werte: unterster ≤ Durchschnitt ≤ oberster Wert erforderlich.
          </p>
        )}
        {saveError && <p className="werte-error">{saveError}</p>}

        <button
          className="werte-save-btn"
          onClick={save}
          disabled={!isValid || !rulesetData}
        >
          {saved ? '✓ Gespeichert' : 'Speichern'}
        </button>
      </div>
    </div>
  )
}
