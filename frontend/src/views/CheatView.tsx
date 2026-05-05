import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useRuleset } from '../context/RulesetContext'
import { exportRuleset, saveRuleset } from '../api'
import type { Cheat } from '../api'
import './DetailView.css'
import './AttributeView.css'

export function CheatView() {
  const navigate = useNavigate()
  const { rulesetData, setRulesetData, currentRuleset, fileHandle } = useRuleset()

  const [cheats, setCheats] = useState<Cheat[]>(() => rulesetData?.cheats ?? [])
  const [selected, setSelected] = useState<Set<number>>(new Set())
  const [sortDir, setSortDir] = useState<'asc' | 'desc' | null>(null)
  const [showAdd, setShowAdd] = useState(false)
  const [newName, setNewName] = useState('')
  const [newDesc, setNewDesc] = useState('')
  const [saveError, setSaveError] = useState<string | null>(null)

  async function persist(updated: Cheat[]) {
    if (!rulesetData) return
    const updatedData = { ...rulesetData, cheats: updated }
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

  function toggleSelect(i: number) {
    setSelected(prev => { const n = new Set(prev); n.has(i) ? n.delete(i) : n.add(i); return n })
  }
  function toggleAll() {
    setSelected(selected.size === cheats.length ? new Set() : new Set(cheats.map((_, i) => i)))
  }
  function del(i: number) {
    const updated = cheats.filter((_, j) => j !== i)
    setCheats(updated)
    setSelected(prev => {
      const n = new Set<number>()
      for (const s of prev) { if (s < i) n.add(s); else if (s > i) n.add(s - 1) }
      return n
    })
    persist(updated)
  }
  function deleteSelected() {
    const updated = cheats.filter((_, i) => !selected.has(i))
    setCheats(updated); setSelected(new Set()); persist(updated)
  }
  function add() {
    const name = newName.trim(); if (!name) return
    const updated = [...cheats, { name, description: newDesc.trim() }]
    setCheats(updated); setNewName(''); setNewDesc(''); setShowAdd(false); persist(updated)
  }
  function toggleSort() { setSortDir(d => d === null ? 'asc' : d === 'asc' ? 'desc' : null) }

  const rows = cheats
    .map((cheat, originalIndex) => ({ cheat, originalIndex }))
    .sort((a, b) => sortDir === null ? 0 : a.cheat.name.localeCompare(b.cheat.name, 'de') * (sortDir === 'asc' ? 1 : -1))
  const allSelected = cheats.length > 0 && selected.size === cheats.length

  return (
    <div className="detail-view">
      <div className="attr-header">
        <h1>Cheats</h1>
        <div className="attr-header-actions">
          {selected.size > 0 && (
            <button className="attr-delete-selected-btn" onClick={deleteSelected}>
              {selected.size} löschen
            </button>
          )}
          <button className="attr-new-btn" onClick={() => setShowAdd(true)}>+ Neuer Cheat</button>
        </div>
      </div>

      {saveError && <p className="attr-error">{saveError}</p>}

      {showAdd && (
        <div className="attr-form" style={{ marginBottom: 16 }}>
          <div className="attr-form-row">
            <label className="attr-form-label">Name</label>
            <input className="attr-form-input" value={newName} onChange={e => setNewName(e.target.value)}
              onKeyDown={e => e.key === 'Enter' && add()} autoFocus />
          </div>
          <div className="attr-form-row">
            <label className="attr-form-label">Beschreibung</label>
            <textarea className="attr-form-input attr-form-textarea" value={newDesc} onChange={e => setNewDesc(e.target.value)} rows={3} />
          </div>
          <div className="attr-form-actions">
            <button className="attr-save-btn" onClick={add} disabled={!newName.trim()}>Hinzufügen</button>
            <button className="attr-cancel-btn" onClick={() => { setShowAdd(false); setNewName(''); setNewDesc('') }}>Abbrechen</button>
          </div>
        </div>
      )}

      {cheats.length === 0 ? (
        <p className="attr-empty">Noch keine Cheats vorhanden.</p>
      ) : (
        <div className="attr-table-wrapper">
          <table className="attr-table">
            <thead>
              <tr>
                <th className="attr-col-check">
                  <input type="checkbox" checked={allSelected} onChange={toggleAll} />
                </th>
                <th className="attr-col-name attr-col-sortable" onClick={toggleSort}>
                  Name {sortDir === 'asc' ? '▲' : sortDir === 'desc' ? '▼' : '⇅'}
                </th>
                <th>Beschreibung</th>
                <th className="attr-col-actions">Aktionen</th>
              </tr>
            </thead>
            <tbody>
              {rows.map(({ cheat, originalIndex }) => (
                <tr key={originalIndex} className={selected.has(originalIndex) ? 'attr-row-selected' : ''}>
                  <td className="attr-col-check">
                    <input type="checkbox" checked={selected.has(originalIndex)} onChange={() => toggleSelect(originalIndex)} />
                  </td>
                  <td className="attr-col-name">{cheat.name}</td>
                  <td>{cheat.description || <span className="attr-no-value">–</span>}</td>
                  <td className="attr-col-actions">
                    <button className="attr-action-link" onClick={() => navigate(`/tile/cheats/${originalIndex}`)}>Anzeigen</button>
                    <button className="attr-action-link" onClick={() => navigate(`/tile/cheats/${originalIndex}`)}>Bearbeiten</button>
                    <button className="attr-action-link attr-action-delete" onClick={() => del(originalIndex)}>Löschen</button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  )
}
