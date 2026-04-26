import { useState } from 'react'
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
  const [selected, setSelected] = useState<Set<number>>(new Set())
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

  function toggleSelect(i: number) {
    setSelected(prev => {
      const next = new Set(prev)
      if (next.has(i)) next.delete(i)
      else next.add(i)
      return next
    })
  }

  function toggleAll() {
    if (selected.size === attrs.length) {
      setSelected(new Set())
    } else {
      setSelected(new Set(attrs.map((_, i) => i)))
    }
  }

  function del(i: number) {
    const updated = attrs.filter((_, j) => j !== i)
    setAttrs(updated)
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
    const updated = attrs.filter((_, i) => !selected.has(i))
    setAttrs(updated)
    setSelected(new Set())
    persist(updated)
  }

  const allSelected = attrs.length > 0 && selected.size === attrs.length

  return (
    <div className="detail-view">
      <button className="back-button" onClick={() => navigate('/edit-ruleset')}>← Zurück</button>
      <div className="attr-header">
        <h1>Attribute</h1>
        <div className="attr-header-actions">
          {selected.size > 0 && (
            <button className="attr-delete-selected-btn" onClick={deleteSelected}>
              {selected.size} löschen
            </button>
          )}
          <button className="attr-new-btn" onClick={() => navigate('/tile/attributes/neu')}>
            + Neues Attribut
          </button>
        </div>
      </div>

      {saveError && <p className="attr-error">{saveError}</p>}

      {attrs.length === 0 ? (
        <p className="attr-empty">Noch keine Attribute vorhanden.</p>
      ) : (
        <div className="attr-table-wrapper">
        <table className="attr-table">
          <thead>
            <tr>
              <th className="attr-col-check">
                <input type="checkbox" checked={allSelected} onChange={toggleAll} />
              </th>
              <th className="attr-col-name">Name</th>
              <th className="attr-col-actions">Aktionen</th>
            </tr>
          </thead>
          <tbody>
            {attrs.map((attr, i) => (
              <tr key={i} className={selected.has(i) ? 'attr-row-selected' : ''}>
                <td className="attr-col-check">
                  <input type="checkbox" checked={selected.has(i)} onChange={() => toggleSelect(i)} />
                </td>
                <td className="attr-col-name">{attr.name}</td>
                <td className="attr-col-actions">
                  <button className="attr-action-link" onClick={() => navigate(`/tile/attributes/${i}`)}>Anzeigen</button>
                  <button className="attr-action-link" onClick={() => navigate(`/tile/attributes/${i}`)}>Bearbeiten</button>
                  <button className="attr-action-link attr-action-delete" onClick={() => del(i)}>Löschen</button>
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
