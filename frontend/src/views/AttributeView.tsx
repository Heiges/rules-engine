import { useRef, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useRuleset } from '../context/RulesetContext'
import { exportRuleset, saveRuleset } from '../api'
import type { Attribute } from '../api'
import './DetailView.css'
import './AttributeView.css'

export function AttributeView() {
  const navigate = useNavigate()
  const { rulesetData, setRulesetData, currentRuleset, fileHandle } = useRuleset()
  const [attrs, setAttrs] = useState<Attribute[]>(() => rulesetData?.attributes ?? [])
  const [editIdx, setEditIdx] = useState<number | null>(null)
  const [editName, setEditName] = useState('')
  const [newName, setNewName] = useState('')
  const [saveError, setSaveError] = useState<string | null>(null)
  const skipCommit = useRef(false)

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

  function startEdit(i: number) {
    setEditIdx(i)
    setEditName(attrs[i].name)
  }

  function commitEdit() {
    if (skipCommit.current) {
      skipCommit.current = false
      setEditIdx(null)
      return
    }
    if (editIdx === null) return
    const name = editName.trim()
    if (name && !attrs.some((a, j) => j !== editIdx && a.name === name)) {
      const updated = attrs.map((a, j) => j === editIdx ? { ...a, name } : a)
      setAttrs(updated)
      persist(updated)
    }
    setEditIdx(null)
  }

  function del(i: number) {
    if (editIdx === i) setEditIdx(null)
    const updated = attrs.filter((_, j) => j !== i)
    setAttrs(updated)
    persist(updated)
  }

  function add() {
    const name = newName.trim()
    if (!name || attrs.some(a => a.name === name)) return
    const updated = [...attrs, { name, description: '', value: 0 }]
    setAttrs(updated)
    setNewName('')
    persist(updated)
  }

  return (
    <div className="detail-view">
      <button className="back-button" onClick={() => navigate('/edit-ruleset')}>← Zurück</button>
      <h1>Attribute</h1>
      {attrs.length > 0 && <p className="attr-subtitle">{attrs.length} Attribute</p>}

      <div className="attr-list">
        {attrs.length === 0 && (
          <p className="attr-empty">Noch keine Attribute vorhanden.</p>
        )}
        {attrs.map((attr, i) => (
          <div key={i} className="attr-row">
            {editIdx === i ? (
              <input
                className="attr-name-input"
                value={editName}
                autoFocus
                onChange={e => setEditName(e.target.value)}
                onBlur={commitEdit}
                onKeyDown={e => {
                  if (e.key === 'Enter') (e.target as HTMLInputElement).blur()
                  if (e.key === 'Escape') {
                    skipCommit.current = true;
                    (e.target as HTMLInputElement).blur()
                  }
                }}
              />
            ) : (
              <span className="attr-name" onClick={() => startEdit(i)}>{attr.name}</span>
            )}
            <button className="attr-delete-btn" onClick={() => del(i)} title="Löschen">✕</button>
          </div>
        ))}
      </div>

      {saveError && <p className="attr-error">{saveError}</p>}

      <div className="attr-add-section">
        <input
          className="attr-name-input"
          placeholder="Name des neuen Attributs …"
          value={newName}
          onChange={e => setNewName(e.target.value)}
          onKeyDown={e => { if (e.key === 'Enter') add() }}
        />
        <button
          className="attr-add-btn"
          onClick={add}
          disabled={!newName.trim() || attrs.some(a => a.name === newName.trim())}
        >
          + Hinzufügen
        </button>
      </div>
    </div>
  )
}
