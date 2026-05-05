# Skills — View-Spezifikation

## Views

| View | Route | Datei |
|------|-------|-------|
| `SkillVerbView` | `/tile/skills` | [frontend/…/SkillVerbView.tsx](../../../frontend/src/views/SkillVerbView.tsx) |
| `SkillVerbDetailView` | `/tile/skills/:index` | [frontend/…/SkillVerbDetailView.tsx](../../../frontend/src/views/SkillVerbDetailView.tsx) |
| `SkillDomainDetailView` | `/tile/skills/domains/:index` | [frontend/…/SkillDomainDetailView.tsx](../../../frontend/src/views/SkillDomainDetailView.tsx) |

## SkillVerbView

Kombinierte Listenansicht für `SkillVerb` (Sektion „Verb") und `SkillDomain` (Sektion „Domäne") in einer einzigen View. Beide Sektionen haben unabhängigen Zustand und persistieren separat.

### Sektion „Verb"

| Funktion | Beschreibung |
|----------|-------------|
| Sortieren | Spalte „Verb" klickbar: `null → asc → desc → null` |
| Einzelauswahl / Alle auswählen | Checkbox pro Zeile und im Header |
| Löschen (einzeln / Mehrfach) | Sofort ohne Bestätigung |
| Inline-Formular „Neues Verb" | Name + Beschreibung; wird über „+ Neues Verb" eingeblendet; Enter im Name-Feld fügt hinzu |
| Anzeigen / Bearbeiten | Navigiert zu `/tile/skills/{originalIndex}` |

### Sektion „Domäne"

Strukturell identisch zur Sektion „Verb", mit eigenen unabhängigen States.

| Funktion | Beschreibung |
|----------|-------------|
| Inline-Formular „Neue Domäne" | Analog zu „Neues Verb" |
| Anzeigen / Bearbeiten | Navigiert zu `/tile/skills/domains/{originalIndex}` |

### Hinweis

Für neue Einträge gibt es kein separates Detailformular — das Anlegen erfolgt direkt inline in der Listenansicht.

## SkillVerbDetailView

Formular zum Bearbeiten eines einzelnen `SkillVerb`.

### Felder

| Feld | Label | Pflicht | Validierung |
|------|-------|---------|------------|
| `name` | Name | ja | Nicht leer; kein Duplikat |
| `description` | Beschreibung | nein | — |

### Verhalten

- `index === 'neu'` → Neuanlage (Route erreichbar, aber kein Einstiegspunkt in der aktuellen Navigation).
- Nach Speichern und „Abbrechen": navigiert zu `/tile/skills`.
- Duplikatprüfung im Frontend.
- Enter im Name-Feld löst Speichern aus.

## SkillDomainDetailView

Formular zum Bearbeiten einer einzelnen `SkillDomain`. Struktur und Verhalten identisch zu `SkillVerbDetailView`.

### Felder

| Feld | Label | Pflicht |
|------|-------|---------|
| `name` | Name | ja |
| `description` | Beschreibung | nein |

### Verhalten

- Nach Speichern und „Abbrechen": navigiert zu `/tile/skills`.

## Persistenz (alle Skill-Views)

1. `fileHandle` vorhanden → XML exportieren, in Datei schreiben
2. sonst → `saveRuleset(currentRuleset, updatedData)` via API

`SkillVerbView` persistiert Verben und Domänen über separate Helfer (`persistSkills`, `persistDomains`), die jeweils nur das betroffene Feld im `rulesetData` ersetzen.

## Referenzen

- [skill/domain.md](domain.md) — Domänenklassen `Skill`, `SkillVerb`, `SkillDomain`
