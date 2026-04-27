import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useRuleset } from '../context/RulesetContext'
import { exportRuleset, saveRuleset } from '../api'
import type { SkillVerb, SkillDomain } from '../api'
import './DetailView.css'
import './AttributeView.css'

export function SkillVerbView() {
  const navigate = useNavigate()
  const { rulesetData, setRulesetData, currentRuleset, fileHandle } = useRuleset()

  // ── SkillVerb state ──────────────────────────────────────────────────────
  const [skills, setSkills] = useState<SkillVerb[]>(() => rulesetData?.skills ?? [])
  const [selected, setSelected] = useState<Set<number>>(new Set())
  const [sortDir, setSortDir] = useState<'asc' | 'desc' | null>(null)
  const [showAdd, setShowAdd] = useState(false)
  const [newName, setNewName] = useState('')
  const [newDesc, setNewDesc] = useState('')

  // ── SkillDomain state ────────────────────────────────────────────────────
  const [domains, setDomains] = useState<SkillDomain[]>(() => rulesetData?.skillDomains ?? [])
  const [domainSelected, setDomainSelected] = useState<Set<number>>(new Set())
  const [domainSortDir, setDomainSortDir] = useState<'asc' | 'desc' | null>(null)
  const [showDomainAdd, setShowDomainAdd] = useState(false)
  const [newDomainName, setNewDomainName] = useState('')
  const [newDomainDesc, setNewDomainDesc] = useState('')

  const [saveError, setSaveError] = useState<string | null>(null)

  // ── Persist helpers ──────────────────────────────────────────────────────
  async function persistSkills(updated: SkillVerb[]) {
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

  async function persistDomains(updated: SkillDomain[]) {
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

  // ── SkillVerb CRUD ───────────────────────────────────────────────────────
  function toggleSelect(i: number) {
    setSelected(prev => { const n = new Set(prev); n.has(i) ? n.delete(i) : n.add(i); return n })
  }
  function toggleAll() {
    setSelected(selected.size === skills.length ? new Set() : new Set(skills.map((_, i) => i)))
  }
  function delSkill(i: number) {
    const updated = skills.filter((_, j) => j !== i)
    setSkills(updated)
    setSelected(prev => {
      const n = new Set<number>()
      for (const s of prev) { if (s < i) n.add(s); else if (s > i) n.add(s - 1) }
      return n
    })
    persistSkills(updated)
  }
  function deleteSelectedSkills() {
    const updated = skills.filter((_, i) => !selected.has(i))
    setSkills(updated); setSelected(new Set()); persistSkills(updated)
  }
  function addSkill() {
    const name = newName.trim(); if (!name) return
    const updated = [...skills, { name, description: newDesc.trim() }]
    setSkills(updated); setNewName(''); setNewDesc(''); setShowAdd(false); persistSkills(updated)
  }
  function toggleSort() { setSortDir(d => d === null ? 'asc' : d === 'asc' ? 'desc' : null) }

  const skillRows = skills
    .map((skill, originalIndex) => ({ skill, originalIndex }))
    .sort((a, b) => sortDir === null ? 0 : a.skill.name.localeCompare(b.skill.name, 'de') * (sortDir === 'asc' ? 1 : -1))
  const allSelected = skills.length > 0 && selected.size === skills.length

  // ── SkillDomain CRUD ─────────────────────────────────────────────────────
  function toggleDomainSelect(i: number) {
    setDomainSelected(prev => { const n = new Set(prev); n.has(i) ? n.delete(i) : n.add(i); return n })
  }
  function toggleAllDomains() {
    setDomainSelected(domainSelected.size === domains.length ? new Set() : new Set(domains.map((_, i) => i)))
  }
  function delDomain(i: number) {
    const updated = domains.filter((_, j) => j !== i)
    setDomains(updated)
    setDomainSelected(prev => {
      const n = new Set<number>()
      for (const s of prev) { if (s < i) n.add(s); else if (s > i) n.add(s - 1) }
      return n
    })
    persistDomains(updated)
  }
  function deleteSelectedDomains() {
    const updated = domains.filter((_, i) => !domainSelected.has(i))
    setDomains(updated); setDomainSelected(new Set()); persistDomains(updated)
  }
  function addDomain() {
    const name = newDomainName.trim(); if (!name) return
    const updated = [...domains, { name, description: newDomainDesc.trim() }]
    setDomains(updated); setNewDomainName(''); setNewDomainDesc(''); setShowDomainAdd(false); persistDomains(updated)
  }
  function toggleDomainSort() { setDomainSortDir(d => d === null ? 'asc' : d === 'asc' ? 'desc' : null) }

  const domainRows = domains
    .map((domain, originalIndex) => ({ domain, originalIndex }))
    .sort((a, b) => domainSortDir === null ? 0 : a.domain.name.localeCompare(b.domain.name, 'de') * (domainSortDir === 'asc' ? 1 : -1))
  const allDomainsSelected = domains.length > 0 && domainSelected.size === domains.length

  // ── Render ────────────────────────────────────────────────────────────────
  return (
    <div className="detail-view">
      <button className="back-button" onClick={() => navigate('/edit-ruleset')}>← Zurück</button>
      <h1>Skills</h1>
      {saveError && <p className="attr-error">{saveError}</p>}

      {/* ── Tabelle: Verb ── */}
      <div className="attr-header" style={{ marginTop: 24 }}>
        <h2 style={{ margin: 0 }}>Verb</h2>
        <div className="attr-header-actions">
          {selected.size > 0 && (
            <button className="attr-delete-selected-btn" onClick={deleteSelectedSkills}>
              {selected.size} löschen
            </button>
          )}
          <button className="attr-new-btn" onClick={() => setShowAdd(true)}>+ Neues Verb</button>
        </div>
      </div>

      {showAdd && (
        <div className="attr-form" style={{ marginBottom: 16 }}>
          <div className="attr-form-row">
            <label className="attr-form-label">Name</label>
            <input className="attr-form-input" value={newName} onChange={e => setNewName(e.target.value)}
              onKeyDown={e => e.key === 'Enter' && addSkill()} autoFocus />
          </div>
          <div className="attr-form-row">
            <label className="attr-form-label">Beschreibung</label>
            <textarea className="attr-form-input attr-form-textarea" value={newDesc} onChange={e => setNewDesc(e.target.value)} />
          </div>
          <div className="attr-form-actions">
            <button className="attr-save-btn" onClick={addSkill} disabled={!newName.trim()}>Hinzufügen</button>
            <button className="attr-cancel-btn" onClick={() => { setShowAdd(false); setNewName(''); setNewDesc('') }}>Abbrechen</button>
          </div>
        </div>
      )}

      {skills.length === 0 ? (
        <p className="attr-empty">Noch keine Verben vorhanden.</p>
      ) : (
        <div className="attr-table-wrapper">
          <table className="attr-table">
            <thead>
              <tr>
                <th className="attr-col-check">
                  <input type="checkbox" checked={allSelected} onChange={toggleAll} />
                </th>
                <th className="attr-col-name attr-col-sortable" onClick={toggleSort}>
                  Verb {sortDir === 'asc' ? '▲' : sortDir === 'desc' ? '▼' : '⇅'}
                </th>
                <th>Beschreibung</th>
                <th className="attr-col-actions">Aktionen</th>
              </tr>
            </thead>
            <tbody>
              {skillRows.map(({ skill, originalIndex }) => (
                <tr key={originalIndex} className={selected.has(originalIndex) ? 'attr-row-selected' : ''}>
                  <td className="attr-col-check">
                    <input type="checkbox" checked={selected.has(originalIndex)} onChange={() => toggleSelect(originalIndex)} />
                  </td>
                  <td className="attr-col-name">{skill.name}</td>
                  <td>{skill.description || <span className="attr-no-value">–</span>}</td>
                  <td className="attr-col-actions">
                    <button className="attr-action-link" onClick={() => navigate(`/tile/skills/${originalIndex}`)}>Anzeigen</button>
                    <button className="attr-action-link" onClick={() => navigate(`/tile/skills/${originalIndex}`)}>Bearbeiten</button>
                    <button className="attr-action-link attr-action-delete" onClick={() => delSkill(originalIndex)}>Löschen</button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      {/* ── Tabelle: Domäne ── */}
      <div className="attr-header" style={{ marginTop: 40 }}>
        <h2 style={{ margin: 0 }}>Domäne</h2>
        <div className="attr-header-actions">
          {domainSelected.size > 0 && (
            <button className="attr-delete-selected-btn" onClick={deleteSelectedDomains}>
              {domainSelected.size} löschen
            </button>
          )}
          <button className="attr-new-btn" onClick={() => setShowDomainAdd(true)}>+ Neue Domäne</button>
        </div>
      </div>

      {showDomainAdd && (
        <div className="attr-form" style={{ marginBottom: 16 }}>
          <div className="attr-form-row">
            <label className="attr-form-label">Name</label>
            <input className="attr-form-input" value={newDomainName} onChange={e => setNewDomainName(e.target.value)}
              onKeyDown={e => e.key === 'Enter' && addDomain()} autoFocus />
          </div>
          <div className="attr-form-row">
            <label className="attr-form-label">Beschreibung</label>
            <textarea className="attr-form-input attr-form-textarea" value={newDomainDesc} onChange={e => setNewDomainDesc(e.target.value)} />
          </div>
          <div className="attr-form-actions">
            <button className="attr-save-btn" onClick={addDomain} disabled={!newDomainName.trim()}>Hinzufügen</button>
            <button className="attr-cancel-btn" onClick={() => { setShowDomainAdd(false); setNewDomainName(''); setNewDomainDesc('') }}>Abbrechen</button>
          </div>
        </div>
      )}

      {domains.length === 0 ? (
        <p className="attr-empty">Noch keine Domänen vorhanden.</p>
      ) : (
        <div className="attr-table-wrapper">
          <table className="attr-table">
            <thead>
              <tr>
                <th className="attr-col-check">
                  <input type="checkbox" checked={allDomainsSelected} onChange={toggleAllDomains} />
                </th>
                <th className="attr-col-name attr-col-sortable" onClick={toggleDomainSort}>
                  Domäne {domainSortDir === 'asc' ? '▲' : domainSortDir === 'desc' ? '▼' : '⇅'}
                </th>
                <th>Beschreibung</th>
                <th className="attr-col-actions">Aktionen</th>
              </tr>
            </thead>
            <tbody>
              {domainRows.map(({ domain, originalIndex }) => (
                <tr key={originalIndex} className={domainSelected.has(originalIndex) ? 'attr-row-selected' : ''}>
                  <td className="attr-col-check">
                    <input type="checkbox" checked={domainSelected.has(originalIndex)} onChange={() => toggleDomainSelect(originalIndex)} />
                  </td>
                  <td className="attr-col-name">{domain.name}</td>
                  <td>{domain.description || <span className="attr-no-value">–</span>}</td>
                  <td className="attr-col-actions">
                    <button className="attr-action-link" onClick={() => navigate(`/tile/skills/domains/${originalIndex}`)}>Anzeigen</button>
                    <button className="attr-action-link" onClick={() => navigate(`/tile/skills/domains/${originalIndex}`)}>Bearbeiten</button>
                    <button className="attr-action-link attr-action-delete" onClick={() => delDomain(originalIndex)}>Löschen</button>
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
