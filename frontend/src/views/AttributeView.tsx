import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useRuleset } from '../context/RulesetContext'
import { exportRuleset, saveRuleset } from '../api'
import type { Attribute } from '../api'
import './DetailView.css'
import './AttributeView.css'

interface Props {
  allowGrouping: boolean
  backPath: string
  detailBasePath: string
}

export function AttributeView({ allowGrouping, backPath, detailBasePath }: Props) {
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

  function groupSelected() {
    const groupName = window.prompt('Gruppenname:')
    if (groupName === null) return
    const trimmed = groupName.trim()
    if (!trimmed) return
    const updated = attrs.map((attr, i) =>
      selected.has(i) ? { ...attr, groupName: trimmed } : attr
    )
    setAttrs(updated)
    setSelected(new Set())
    persist(updated)
  }

  const [sortDir, setSortDir] = useState<'asc' | 'desc' | null>(null)

  function toggleSort() {
    setSortDir(d => d === null ? 'asc' : d === 'asc' ? 'desc' : null)
  }

  const rows = attrs
    .map((attr, originalIndex) => ({ attr, originalIndex }))
    .sort((a, b) => {
      if (sortDir === null) return 0
      const cmp = a.attr.name.localeCompare(b.attr.name, 'de')
      return sortDir === 'asc' ? cmp : -cmp
    })

  const allSelected = attrs.length > 0 && selected.size === attrs.length

  return (
    <div className="detail-view">
      <button className="back-button" onClick={() => navigate(backPath)}>← Zurück</button>
      <div className="attr-header">
        <h1>Attribute</h1>
        <div className="attr-header-actions">
          {selected.size > 0 && (
            <>
              {allowGrouping && (
                <button className="attr-group-selected-btn" onClick={groupSelected}>
                  {selected.size} gruppieren
                </button>
              )}
              <button className="attr-delete-selected-btn" onClick={deleteSelected}>
                {selected.size} löschen
              </button>
            </>
          )}
          <button className="attr-new-btn" onClick={() => navigate(`${detailBasePath}/neu`)}>
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
              <th className="attr-col-name attr-col-sortable" onClick={toggleSort}>
                Name {sortDir === 'asc' ? '▲' : sortDir === 'desc' ? '▼' : '⇅'}
              </th>
              {allowGrouping && <th className="attr-col-group">Gruppe</th>}
              <th className="attr-col-actions">Aktionen</th>
            </tr>
          </thead>
          <tbody>
            {rows.map(({ attr, originalIndex }) => (
              <tr key={originalIndex} className={selected.has(originalIndex) ? 'attr-row-selected' : ''}>
                <td className="attr-col-check">
                  <input type="checkbox" checked={selected.has(originalIndex)} onChange={() => toggleSelect(originalIndex)} />
                </td>
                <td className="attr-col-name">{attr.name}</td>
                {allowGrouping && <td className="attr-col-group">{(attr.groupName && attr.groupName !== 'Allgemein') ? attr.groupName : <span className="attr-no-value">–</span>}</td>}
                <td className="attr-col-actions">
                  <button className="attr-action-link" onClick={() => navigate(`${detailBasePath}/${originalIndex}`)}>Anzeigen</button>
                  <button className="attr-action-link" onClick={() => navigate(`${detailBasePath}/${originalIndex}`)}>Bearbeiten</button>
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
