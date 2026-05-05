# Kachelnavigation — View-Spezifikation

## Navigationshierarchie

Die Anwendung ist in drei Ebenen gegliedert:

```
Startseite (HomeView)
├── [Aktion] Neue Referenzregeln      → /edit-ruleset
├── [Aktion] Referenzregeln laden     → /edit-ruleset
├── [konditional] Referenzregeln bearbeiten  → /edit-ruleset
└── [konditional] Spielwelt anlegen   → /create-world

Referenzregeln bearbeiten (EditRulesetView)  /edit-ruleset
├── Werte        → /tile/werte
├── Attribute    → /tile/attributes
├── Skills       → /tile/skills
└── Cheats       → /tile/cheats

Spielwelt anlegen (SpielweltView)  /create-world
├── Attribute    → /world/attributes
└── Charactereditor → /character-editor
```

## Kacheln

### Startseite (`HomeView`)

| ID | Name | Beschreibung | Sichtbarkeit | Ziel |
|----|------|-------------|--------------|------|
| `new-ruleset` | Neue Referenzregeln | Neue Referenzregeln erstellen | immer | Datei-Picker (neu) → `/edit-ruleset` |
| `load-ruleset` | Referenzregeln laden | Referenzregeln laden | immer | Datei-Picker (öffnen) → `/edit-ruleset` |
| `edit-ruleset` | Referenzregeln `{name}` bearbeiten | Die Referenzregeln `{name}` bearbeiten. | wenn `currentRuleset` gesetzt | `/edit-ruleset` |
| `create-world` | Spielwelt anlegen | Eine neue Spielwelt auf Basis der Referenzregeln `{name}` erstellen. | wenn `currentRuleset` gesetzt | `/create-world` |

### Referenzregeln bearbeiten (`EditRulesetView`)

| ID | Name | Beschreibung | Sichtbarkeit | Ziel |
|----|------|-------------|--------------|------|
| `werte` | Werte | Bearbeite den Wertebereich | immer | `/tile/werte` |
| `attributes` | Attribute | Attribute bearbeiten (`{n}` Attribute) | immer | `/tile/attributes` |
| `skills` | Skills | Skill anlegen und modifizieren (`{v}` Verben, `{d}` Domänen) | immer | `/tile/skills` |
| `cheats` | Cheats | Cheats anlegen und bearbeiten (`{c}` Cheats) | immer | `/tile/cheats` |

### Spielwelt anlegen (`SpielweltView`)

| ID | Name | Beschreibung | Sichtbarkeit | Ziel |
|----|------|-------------|--------------|------|
| `world-attributes` | Attribute | Attribute der Spielwelt verwalten und gruppieren. | immer | `/world/attributes` |
| `character-editor` | Charactereditor | Erstellt einen Character. | immer | `/character-editor` |

## Routing

Alle Routen sind in [App.tsx](../../../frontend/src/App.tsx) registriert:

| Route | Komponente | Hinweis |
|-------|-----------|---------|
| `/` | `HomeView` | |
| `/edit-ruleset` | `EditRulesetView` | |
| `/tile/werte` | `WerteView` | |
| `/tile/attributes` | `AttributeView` | `allowGrouping=false`, `backPath=/edit-ruleset` |
| `/tile/attributes/:index` | `AttributeDetailView` | |
| `/world/attributes` | `AttributeView` | `allowGrouping=true`, `backPath=/create-world` |
| `/world/attributes/:index` | `AttributeDetailView` | |
| `/tile/skills` | `SkillVerbView` | |
| `/tile/skills/domains/:index` | `SkillDomainDetailView` | |
| `/tile/skills/:index` | `SkillVerbDetailView` | |
| `/tile/cheats` | `CheatView` | |
| `/tile/cheats/:index` | `CheatDetailView` | |
| `/character-editor` | `CharacterEditorView` | |
| `/create-world` | `SpielweltView` | |
| `/tile/:id` | `DetailView` | Fallback für unbekannte Kacheln |

## Tile-Komponente

`Tile` ([frontend/src/components/Tile.tsx](../../../frontend/src/components/Tile.tsx)) akzeptiert:

| Prop | Typ | Pflicht | Bedeutung |
|------|-----|---------|-----------|
| `id` | `string` | ja | Eindeutige Kachel-ID; wird als Route-Segment verwendet (`/tile/{id}`) wenn kein `onClick` |
| `name` | `string` | ja | Überschrift der Kachel |
| `description` | `string` | ja | Kurzbeschreibung |
| `onClick` | `() => void` | nein | Überschreibt das Standard-Navigationsverhalten |

Standardverhalten ohne `onClick`: navigiert zu `/tile/{id}`.

## Konditionale Sichtbarkeit

Kacheln auf der Startseite, die ein geladenes Regelwerk voraussetzen, werden nur gerendert wenn `currentRuleset` im `RulesetContext` gesetzt ist.

## Zustand

Der `RulesetContext` ([frontend/src/context/RulesetContext.tsx](../../../frontend/src/context/RulesetContext.tsx)) hält:

| Feld | Typ | Bedeutung |
|------|-----|-----------|
| `currentRuleset` | `string \| null` | Name des aktuell geladenen Regelwerks |
| `rulesetData` | `RulesetData \| null` | Vollständige Daten des Regelwerks |
| `fileHandle` | `FileSystemFileHandle \| null` | Handle auf die geöffnete Datei (nur wenn File System Access API verfügbar) |

## Datei-Interaktion (Startseite)

- **Neu**: `showSaveFilePicker` → leeres Regelwerk anlegen → Datei schreiben → `/edit-ruleset`; Fallback: `window.prompt` für Name
- **Laden**: `showOpenFilePicker` → XML lesen → `importRuleset(xml)` → `/edit-ruleset`; Fallback: `<input type="file">`
