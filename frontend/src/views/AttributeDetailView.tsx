import { useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { useRuleset } from '../context/RulesetContext'
import { exportRuleset, saveRuleset } from '../api'
import type { Attribute } from '../api'
import './DetailView.css'
import './AttributeView.css'

interface Props {
  listPath: string
}

export function AttributeDetailView({ listPath }: Props) {
  const navigate = useNavigate()
  const { index } = useParams<{ index: string }>()
  const { rulesetData, setRulesetData, currentRuleset, fileHandle } = useRuleset()

  const isNew = index === 'neu'
  const attrIndex = isNew ? -1 : parseInt(index ?? '-1', 10)
  const existing = !isNew ? (rulesetData?.attributes[attrIndex] ?? null) : null

  const [name, setName] = useState(existing?.name ?? '')
  const [description, setDescription] = useState(existing?.description ?? '')
  const [value, setValue] = useState(existing?.value ?? 0)
  const [saveError, setSaveError] = useState<string | null>(null)

  async function persist(updated: Attribute[]) {
    if (!rulesetData) return
    const updatedData = { ...rulesetData, attributes: updated }
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

    const attrs = rulesetData?.attributes ?? []
    const isDuplicate = attrs.some((a, i) => a.name === trimmedName && i !== attrIndex)
    if (isDuplicate) {
      setSaveError('Ein Attribut mit diesem Namen existiert bereits.')
      return
    }

    let updated: Attribute[]
    if (isNew) {
      updated = [...attrs, { name: trimmedName, description, value }]
    } else {
      updated = attrs.map((a, i) => i === attrIndex ? { name: trimmedName, description, value } : a)
    }

    await persist(updated)
    navigate(listPath)
  }

  return (
    <div className="detail-view">
      <h1>{isNew ? 'Neues Attribut' : 'Attribut bearbeiten'}</h1>

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
        <div className="attr-form-row">
          <label className="attr-form-label">Wert</label>
          <input
            className="attr-form-input attr-form-input-number"
            type="number"
            value={value}
            onChange={e => setValue(parseInt(e.target.value, 10) || 0)}
          />
        </div>

        {saveError && <p className="attr-error">{saveError}</p>}

        <div className="attr-form-actions">
          <button className="attr-cancel-btn" onClick={() => navigate(listPath)}>Abbrechen</button>
          <button className="attr-save-btn" onClick={save} disabled={!name.trim()}>
            Speichern
          </button>
        </div>
      </div>
    </div>
  )
}
