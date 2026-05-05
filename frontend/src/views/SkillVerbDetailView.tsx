import { useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { useRuleset } from '../context/RulesetContext'
import { exportRuleset, saveRuleset } from '../api'
import type { SkillVerb } from '../api'
import './DetailView.css'
import './AttributeView.css'

export function SkillVerbDetailView() {
  const navigate = useNavigate()
  const { index } = useParams<{ index: string }>()
  const { rulesetData, setRulesetData, currentRuleset, fileHandle } = useRuleset()

  const isNew = index === 'neu'
  const skillIndex = isNew ? -1 : parseInt(index ?? '-1', 10)
  const existing = !isNew ? (rulesetData?.skills[skillIndex] ?? null) : null

  const [name, setName] = useState(existing?.name ?? '')
  const [description, setDescription] = useState(existing?.description ?? '')
  const [saveError, setSaveError] = useState<string | null>(null)

  async function persist(updated: SkillVerb[]) {
    if (!rulesetData) return
    const updatedData = { ...rulesetData, skills: updated }
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

    const skills = rulesetData?.skills ?? []
    const isDuplicate = skills.some((s, i) => s.name === trimmedName && i !== skillIndex)
    if (isDuplicate) {
      setSaveError('Ein Skill mit diesem Namen existiert bereits.')
      return
    }

    let updated: SkillVerb[]
    if (isNew) {
      updated = [...skills, { name: trimmedName, description }]
    } else {
      updated = skills.map((s, i) => i === skillIndex ? { name: trimmedName, description } : s)
    }

    await persist(updated)
    navigate('/tile/skills')
  }

  return (
    <div className="detail-view">
      <h1>{isNew ? 'Neuer Skill' : 'Skill bearbeiten'}</h1>

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
