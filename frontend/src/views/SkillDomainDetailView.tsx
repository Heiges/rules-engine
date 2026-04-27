import { useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { useRuleset } from '../context/RulesetContext'
import { exportRuleset, saveRuleset } from '../api'
import type { SkillDomain } from '../api'
import './DetailView.css'
import './AttributeView.css'

export function SkillDomainDetailView() {
  const navigate = useNavigate()
  const { index } = useParams<{ index: string }>()
  const { rulesetData, setRulesetData, currentRuleset, fileHandle } = useRuleset()

  const isNew = index === 'neu'
  const domainIndex = isNew ? -1 : parseInt(index ?? '-1', 10)
  const existing = !isNew ? (rulesetData?.skillDomains[domainIndex] ?? null) : null

  const [name, setName] = useState(existing?.name ?? '')
  const [description, setDescription] = useState(existing?.description ?? '')
  const [saveError, setSaveError] = useState<string | null>(null)

  async function persist(updated: SkillDomain[]) {
    if (!rulesetData) return
    const updatedData = { ...rulesetData, skillDomains: updated }
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
    } catch (err) {
      setSaveError(err instanceof Error ? err.message : String(err))
    }
  }

  async function save() {
    const trimmedName = name.trim()
    if (!trimmedName) return

    const domains = rulesetData?.skillDomains ?? []
    const isDuplicate = domains.some((d, i) => d.name === trimmedName && i !== domainIndex)
    if (isDuplicate) {
      setSaveError('Eine Domäne mit diesem Namen existiert bereits.')
      return
    }

    let updated: SkillDomain[]
    if (isNew) {
      updated = [...domains, { name: trimmedName, description }]
    } else {
      updated = domains.map((d, i) => i === domainIndex ? { name: trimmedName, description } : d)
    }

    await persist(updated)
    navigate('/tile/skills')
  }

  return (
    <div className="detail-view">
      <button className="back-button" onClick={() => navigate('/tile/skills')}>← Zurück</button>
      <h1>{isNew ? 'Neue Domäne' : 'Domäne bearbeiten'}</h1>

      <div className="attr-form">
        <div className="attr-form-row">
          <label className="attr-form-label">Name</label>
          <input
            className="attr-form-input"
            value={name}
            onChange={e => setName(e.target.value)}
            onKeyDown={e => { if (e.key === 'Enter') save() }}
            autoFocus
          />
        </div>
        <div className="attr-form-row">
          <label className="attr-form-label">Beschreibung</label>
          <textarea
            className="attr-form-input attr-form-textarea"
            value={description}
            onChange={e => setDescription(e.target.value)}
            rows={3}
          />
        </div>

        {saveError && <p className="attr-error">{saveError}</p>}

        <div className="attr-form-actions">
          <button className="attr-cancel-btn" onClick={() => navigate('/tile/skills')}>Abbrechen</button>
          <button className="attr-save-btn" onClick={save} disabled={!name.trim()}>
            Speichern
          </button>
        </div>
      </div>
    </div>
  )
}
