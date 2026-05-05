import { useState } from 'react'
import { useRuleset } from '../context/RulesetContext'
import { exportRuleset, saveRuleset } from '../api'
import type { ValueRange } from '../api'
import './DetailView.css'
import './WerteView.css'

export function WerteView() {
  const { rulesetData, setRulesetData, currentRuleset, fileHandle } = useRuleset()
  const [werte, setWerte] = useState<ValueRange>(() => rulesetData?.valueRange ?? { min: -10, average: 0, max: 10 })

  async function persist(updated: ValueRange) {
    if (!rulesetData) return
    const updatedData = { ...rulesetData, valueRange: updated }
    setRulesetData(updatedData)
    try {
      if (fileHandle) {
        const xml = await exportRuleset(updatedData)
        const writable = await fileHandle.createWritable()
        await writable.write(xml)
        await writable.close()
      } else if (currentRuleset) {
        await saveRuleset(currentRuleset, updatedData)
      }
    } catch (_) {}
  }

  function update(field: keyof ValueRange, raw: string) {
    const val = parseInt(raw, 10)
    if (!isNaN(val)) setWerte(prev => ({ ...prev, [field]: val }))
  }

  function commit(field: keyof ValueRange, raw: string) {
    const val = parseInt(raw, 10)
    if (!isNaN(val)) {
      const updated = { ...werte, [field]: val }
      setWerte(updated)
      persist(updated)
    }
  }

  return (
    <div className="detail-view">
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
            onBlur={e => commit('min', e.target.value)}
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
            onBlur={e => commit('average', e.target.value)}
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
            onBlur={e => commit('max', e.target.value)}
          />
        </div>
      </div>
    </div>
  )
}
