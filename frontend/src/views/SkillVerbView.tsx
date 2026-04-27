import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useRuleset } from '../context/RulesetContext'
import { exportRuleset, saveRuleset } from '../api'
import type { SkillVerb } from '../api'
import './DetailView.css'
import './AttributeView.css'

export function SkillVerbView() {
  const navigate = useNavigate()
  const { rulesetData, setRulesetData, currentRuleset, fileHandle } = useRuleset()
  const [skills, setSkills] = useState<SkillVerb[]>(() => rulesetData?.skills ?? [])
  const [selected, setSelected] = useState<Set<number>>(new Set())
  const [sortDir, setSortDir] = useState<'asc' | 'desc' | null>(null)
  const [saveError, setSaveError] = useState<string | null>(null)
  const [showAdd, setShowAdd] = useState(false)
  const [newName, setNewName] = useState('')
  const [newDesc, setNewDesc] = useState('')

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

  function toggleSelect(i: number) {
    setSelected(prev => {
      const next = new Set(prev)
      if (next.has(i)) next.delete(i)
      else next.add(i)
      return next
    })
  }

  function toggleAll() {
    if (selected.size === skills.length) {
      setSelected(new Set())
    } else {
      setSelected(new Set(skills.map((_, i) => i)))
    }
  }

  function del(i: number) {
    const updated = skills.filter((_, j) => j !== i)
    setSkills(updated)
    setSelected(prev => {
      const next = new Set<number>()
      for (const s of prev) {
        if (s < i) next.add(s)
        else if (s > i) next.add(s - 1)
      }
      return next
    })
    persist(updated)
  }

  function deleteSelected() {
    const updated = skills.filter((_, i) => !selected.has(i))
    setSkills(updated)
    setSelected(new Set())
    persist(updated)
  }

  function toggleSort() {
    setSortDir(d => d === null ? 'asc' : d === 'asc' ? 'desc' : null)
  }

  function add() {
    const name = newName.trim()
    if (!name) return
    const updated = [...skills, { name, description: newDesc.trim() }]
    setSkills(updated)
    setNewName('')
    setNewDesc('')
    setShowAdd(false)
    persist(updated)
  }

  const rows = skills
    .map((skill, originalIndex) => ({ skill, originalIndex }))
    .sort((a, b) => {
      if (sortDir === null) return 0
      const cmp = a.skill.name.localeCompare(b.skill.name, 'de')
      return sortDir === 'asc' ? cmp : -cmp
    })

  const allSelected = skills.length > 0 && selected.size === skills.length

  return (
    <div className="detail-view">
      <button className="back-button" onClick={() => navigate('/edit-ruleset')}>← Zurück</button>
      <div className="attr-header">
        <h1>Skills</h1>
        <div className="attr-header-actions">
          {selected.size > 0 && (
            <button className="attr-delete-selected-btn" onClick={deleteSelected}>
              {selected.size} löschen
            </button>
          )}
          <button className="attr-new-btn" onClick={() => setShowAdd(true)}>+ Neuer Skill</button>
        </div>
      </div>

      {saveError && <p className="attr-error">{saveError}</p>}

      {showAdd && (
        <div className="attr-form" style={{ marginBottom: 24 }}>
          <div className="attr-form-row">
            <label className="attr-form-label">Name</label>
            <input
              className="attr-form-input"
              value={newName}
              onChange={e => setNewName(e.target.value)}
              onKeyDown={e => e.key === 'Enter' && add()}
              autoFocus
            />
          </div>
          <div className="attr-form-row">
            <label className="attr-form-label">Beschreibung</label>
            <textarea
              className="attr-form-input attr-form-textarea"
              value={newDesc}
              onChange={e => setNewDesc(e.target.value)}
            />
          </div>
          <div className="attr-form-actions">
            <button className="attr-save-btn" onClick={add} disabled={!newName.trim()}>
              Hinzufügen
            </button>
            <button className="attr-cancel-btn" onClick={() => { setShowAdd(false); setNewName(''); setNewDesc('') }}>
              Abbrechen
            </button>
          </div>
        </div>
      )}

      {skills.length === 0 ? (
        <p className="attr-empty">Noch keine Skills vorhanden.</p>
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
              {rows.map(({ skill, originalIndex }) => (
                <tr key={originalIndex} className={selected.has(originalIndex) ? 'attr-row-selected' : ''}>
                  <td className="attr-col-check">
                    <input type="checkbox" checked={selected.has(originalIndex)} onChange={() => toggleSelect(originalIndex)} />
                  </td>
                  <td className="attr-col-name">{skill.name}</td>
                  <td>{skill.description || <span className="attr-no-value">–</span>}</td>
                  <td className="attr-col-actions">
                    <button className="attr-action-link" onClick={() => navigate(`/tile/skills/${originalIndex}`)}>Anzeigen</button>
                    <button className="attr-action-link" onClick={() => navigate(`/tile/skills/${originalIndex}`)}>Bearbeiten</button>
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
