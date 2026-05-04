# Schicht-Spec: Views (Frontend)

## Zweck

Muster für alle Views unter `frontend/src/views/`. Views kommunizieren ausschließlich über `api.ts` mit dem Backend und lesen/schreiben Zustand über `RulesetContext`.

## Pflichtimporte

```tsx
import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useRuleset } from '../context/RulesetContext'
import { exportRuleset, saveRuleset } from '../api'
import type { FooType } from '../api'
import './DetailView.css'
import './FooView.css'     // view-spezifisches CSS, falls nötig
```

## Grundstruktur

Jede View beginnt mit dem `detail-view`-Wrapper, einem Zurück-Button und einem Titel:

```tsx
export function FooView() {
  const navigate = useNavigate()
  const { rulesetData, setRulesetData, currentRuleset, fileHandle } = useRuleset()

  return (
    <div className="detail-view">
      <button className="back-button" onClick={() => navigate('/pfad-zurück')}>← Zurück</button>
      <h1>Titel</h1>
      {/* Inhalt */}
    </div>
  )
}
```

## Persist-Muster (identisch in allen Views)

Lokaler Zustand wird nach jeder Änderung sofort in den Context und in die Datei geschrieben:

```tsx
const [saveError, setSaveError] = useState<string | null>(null)

async function persist(updated: FooType[]) {
  if (!rulesetData) return
  const updatedData = { ...rulesetData, foos: updated }
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
```

Fehleranzeige direkt unter dem Header: `{saveError && <p className="attr-error">{saveError}</p>}`

---

## View-Typ 1: Listenansicht

Für Views, die eine Sammlung von Elementen verwalten (anlegen, löschen, navigieren).

### Zustand

```tsx
const [items, setItems] = useState<FooType[]>(() => rulesetData?.foos ?? [])
const [selected, setSelected] = useState<Set<number>>(new Set())
const [sortDir, setSortDir] = useState<'asc' | 'desc' | null>(null)
const [showAdd, setShowAdd] = useState(false)   // optional: Inline-Formular
```

### Standardfunktionen

```tsx
function toggleSelect(i: number) {
  setSelected(prev => { const n = new Set(prev); n.has(i) ? n.delete(i) : n.add(i); return n })
}
function toggleAll() {
  setSelected(selected.size === items.length ? new Set() : new Set(items.map((_, i) => i)))
}
function del(i: number) {
  const updated = items.filter((_, j) => j !== i)
  setItems(updated)
  setSelected(prev => {
    const n = new Set<number>()
    for (const s of prev) { if (s < i) n.add(s); else if (s > i) n.add(s - 1) }
    return n
  })
  persist(updated)
}
function deleteSelected() {
  const updated = items.filter((_, i) => !selected.has(i))
  setItems(updated); setSelected(new Set()); persist(updated)
}
function toggleSort() {
  setSortDir(d => d === null ? 'asc' : d === 'asc' ? 'desc' : null)
}
```

### Sortierung

```tsx
const rows = items
  .map((item, originalIndex) => ({ item, originalIndex }))
  .sort((a, b) => sortDir === null ? 0
    : a.item.name.localeCompare(b.item.name, 'de') * (sortDir === 'asc' ? 1 : -1))
```

Sorting arbeitet auf Kopien mit `originalIndex`, damit Lösch- und Navigations-Operationen den richtigen Index treffen.

### JSX-Struktur

```tsx
<div className="detail-view">
  <button className="back-button" onClick={() => navigate('/pfad')}>← Zurück</button>

  <div className="attr-header">
    <h1>Foo-Liste</h1>
    <div className="attr-header-actions">
      {selected.size > 0 && (
        <button className="attr-delete-selected-btn" onClick={deleteSelected}>
          {selected.size} löschen
        </button>
      )}
      <button className="attr-new-btn" onClick={() => setShowAdd(true)}>+ Neues Foo</button>
    </div>
  </div>

  {saveError && <p className="attr-error">{saveError}</p>}

  {/* Inline-Formular (optional) */}
  {showAdd && (
    <div className="attr-form">
      <div className="attr-form-row">
        <label className="attr-form-label">Name</label>
        <input className="attr-form-input" ... autoFocus />
      </div>
      <div className="attr-form-row">
        <label className="attr-form-label">Beschreibung</label>
        <textarea className="attr-form-input attr-form-textarea" rows={3} ... />
      </div>
      <div className="attr-form-actions">
        <button className="attr-save-btn" onClick={add} disabled={!newName.trim()}>Hinzufügen</button>
        <button className="attr-cancel-btn" onClick={() => setShowAdd(false)}>Abbrechen</button>
      </div>
    </div>
  )}

  {items.length === 0 ? (
    <p className="attr-empty">Noch keine Foos vorhanden.</p>
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
          {rows.map(({ item, originalIndex }) => (
            <tr key={originalIndex} className={selected.has(originalIndex) ? 'attr-row-selected' : ''}>
              <td className="attr-col-check">
                <input type="checkbox" checked={selected.has(originalIndex)} onChange={() => toggleSelect(originalIndex)} />
              </td>
              <td className="attr-col-name">{item.name}</td>
              <td>{item.description || <span className="attr-no-value">–</span>}</td>
              <td className="attr-col-actions">
                <button className="attr-action-link" onClick={() => navigate(`/pfad/${originalIndex}`)}>Anzeigen</button>
                <button className="attr-action-link" onClick={() => navigate(`/pfad/${originalIndex}`)}>Bearbeiten</button>
                <button className="attr-action-link attr-action-delete" onClick={() => del(originalIndex)}>Löschen</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  )}
</div>
```

---

## View-Typ 2: Formularansicht

Für Views, die ein einzelnes Objekt mit mehreren Feldern bearbeiten (kein Liste, kein Löschen).

### Zustand

```tsx
const [formData, setFormData] = useState<FooType>(() => rulesetData?.foo ?? defaultValue)
```

### Funktionen

```tsx
function update(field: keyof FooType, raw: string) {
  const val = parseInt(raw, 10)          // oder direkter String-Wert
  if (!isNaN(val)) setFormData(prev => ({ ...prev, [field]: val }))
}

function commit(field: keyof FooType, raw: string) {
  const val = parseInt(raw, 10)
  if (!isNaN(val)) {
    const updated = { ...formData, [field]: val }
    setFormData(updated)
    persist(updated)
  }
}
```

`update` aktualisiert nur lokalen Zustand (onChange). `commit` schreibt zusätzlich in Context + Datei (onBlur).

### JSX-Struktur

```tsx
<div className="detail-view">
  <button className="back-button" onClick={() => navigate('/pfad')}>← Zurück</button>
  <h1>Foo bearbeiten</h1>

  <div className="foo-form">
    <div className="foo-row">
      <label className="foo-label" htmlFor="foo-field">Feldname</label>
      <input
        id="foo-field"
        type="number"
        className="foo-input"
        value={formData.field}
        onChange={e => update('field', e.target.value)}
        onBlur={e => commit('field', e.target.value)}
      />
    </div>
  </div>
</div>
```

---

## CSS-Dateien

| Datei | Inhalt |
|-------|--------|
| `DetailView.css` | Geteilt: `detail-view`, `back-button` |
| `AttributeView.css` | Geteilt für Listenansichten: alle `attr-*`-Klassen |
| `<Name>View.css` | View-spezifische Stile (nur wenn nötig) |

Listenansichten importieren immer beide: `DetailView.css` + `AttributeView.css`.

## Verbote

- Kein direkter `fetch`-Aufruf — immer über `api.ts`
- Kein geteilter Zustand in lokalen View-States — abgeleitete Daten gehören in `RulesetContext`
- Keine Inline-Styles — CSS-Klassen verwenden
