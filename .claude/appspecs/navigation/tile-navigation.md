# Kachelnavigation — View-Spezifikation

## Navigationshierarchie

```
Rollenwahl (RoleSelectionView)  /              ← Einstiegsseite
├── Spielleiter  → /home       (setzt role=spielleiter)
└── Spieler      → /player     (setzt role=spieler)

Startseite (HomeView)  /home                   ← nur Spielleiter
├── [konditional] Neue Referenzregeln      → /edit-ruleset
├── [konditional] Referenzregeln laden     → /edit-ruleset
├── [konditional + currentRuleset] Referenzregeln bearbeiten  → /edit-ruleset
└── [konditional + currentRuleset] Spielwelt anlegen          → /create-world

Spielerbereich (PlayerView)  /player           ← nur Spieler
└── Charactereditor → /character-editor

Referenzregeln bearbeiten (EditRulesetView)  /edit-ruleset   ← nur Spielleiter
├── Werte        → /tile/werte
├── Attribute    → /tile/attributes
├── Skills       → /tile/skills
└── Cheats       → /tile/cheats

Spielwelt anlegen (SpielweltView)  /create-world             ← nur Spielleiter
├── Attribute    → /world/attributes
└── Charactereditor → /character-editor
```

## Kacheln

### Rollenwahl (`RoleSelectionView`)

Einstiegsseite unter `/`. Zeigt zwei Kacheln zur Rollenauswahl.

| ID | Name | Beschreibung | Sichtbarkeit | Aktion |
|----|------|-------------|--------------|--------|
| `spielleiter` | Spielleiter | Ich will Regeln anpassen und meine Spielwelt definieren | immer | setzt `role=spielleiter` → `/home` |
| `spieler` | Spieler | Spass, ich will Spass | immer | setzt `role=spieler` → `/player` |

### Startseite (`HomeView`)  `/home`

Nur über die Rolle Spielleiter erreichbar.

| ID | Name | Beschreibung | Sichtbarkeit | Ziel |
|----|------|-------------|--------------|------|
| `new-ruleset` | Neue Referenzregeln | Neue Referenzregeln erstellen | immer | Datei-Picker (neu) → `/edit-ruleset` |
| `load-ruleset` | Referenzregeln laden | Referenzregeln laden | immer | Datei-Picker (öffnen) → `/edit-ruleset` |
| `edit-ruleset` | Referenzregeln `{name}` bearbeiten | Die Referenzregeln `{name}` bearbeiten. | wenn `currentRuleset` gesetzt | `/edit-ruleset` |
| `create-world` | Spielwelt anlegen | Eine neue Spielwelt auf Basis der Referenzregeln `{name}` erstellen. | wenn `currentRuleset` gesetzt | `/create-world` |

### Spielerbereich (`PlayerView`)  `/player`

Nur über die Rolle Spieler erreichbar.

| ID | Name | Beschreibung | Sichtbarkeit | Ziel |
|----|------|-------------|--------------|------|
| `character-editor` | Charactereditor | Erstellt einen Character. | immer | `/character-editor` |

### Referenzregeln bearbeiten (`EditRulesetView`)

Nur über die Rolle Spielleiter erreichbar.

| ID | Name | Beschreibung | Sichtbarkeit | Ziel |
|----|------|-------------|--------------|------|
| `werte` | Werte | Bearbeite den Wertebereich | immer | `/tile/werte` |
| `attributes` | Attribute | Attribute bearbeiten (`{n}` Attribute) | immer | `/tile/attributes` |
| `skills` | Skills | Skill anlegen und modifizieren (`{v}` Verben, `{d}` Domänen) | immer | `/tile/skills` |
| `cheats` | Cheats | Cheats anlegen und bearbeiten (`{c}` Cheats) | immer | `/tile/cheats` |

### Spielwelt anlegen (`SpielweltView`)

Nur über die Rolle Spielleiter erreichbar.

| ID | Name | Beschreibung | Sichtbarkeit | Ziel |
|----|------|-------------|--------------|------|
| `world-attributes` | Attribute | Attribute der Spielwelt verwalten und gruppieren. | immer | `/world/attributes` |
| `character-editor` | Charactereditor | Erstellt einen Character. | immer | `/character-editor` |

## Routing

Alle Routen sind in [App.tsx](../../../frontend/src/App.tsx) registriert:

| Route | Komponente | Hinweis |
|-------|-----------|---------|
| `/` | `RoleSelectionView` | Einstiegsseite |
| `/home` | `HomeView` | Spielleiter-Bereich |
| `/player` | `PlayerView` | Spieler-Bereich (neu) |
| `/edit-ruleset` | `EditRulesetView` | nur Spielleiter |
| `/tile/werte` | `WerteView` | nur Spielleiter |
| `/tile/attributes` | `AttributeView` | nur Spielleiter; `allowGrouping=false`, `backPath=/edit-ruleset` |
| `/tile/attributes/:index` | `AttributeDetailView` | nur Spielleiter |
| `/world/attributes` | `AttributeView` | nur Spielleiter; `allowGrouping=true`, `backPath=/create-world` |
| `/world/attributes/:index` | `AttributeDetailView` | nur Spielleiter |
| `/tile/skills` | `SkillVerbView` | nur Spielleiter |
| `/tile/skills/domains/:index` | `SkillDomainDetailView` | nur Spielleiter |
| `/tile/skills/:index` | `SkillVerbDetailView` | nur Spielleiter |
| `/tile/cheats` | `CheatView` | nur Spielleiter |
| `/tile/cheats/:index` | `CheatDetailView` | nur Spielleiter |
| `/character-editor` | `CharacterEditorView` | |
| `/create-world` | `SpielweltView` | nur Spielleiter |
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

## Zustand

Der `RulesetContext` ([frontend/src/context/RulesetContext.tsx](../../../frontend/src/context/RulesetContext.tsx)) hält:

| Feld | Typ | Bedeutung |
|------|-----|-----------|
| `currentRuleset` | `string \| null` | Name des aktuell geladenen Regelwerks |
| `rulesetData` | `RulesetData \| null` | Vollständige Daten des Regelwerks |
| `fileHandle` | `FileSystemFileHandle \| null` | Handle auf die geöffnete Datei (nur wenn File System Access API verfügbar) |
| `role` | `'spielleiter' \| 'spieler' \| null` | Gewählte Benutzerrolle; wird auf der Rollenwahl-Seite gesetzt |

## Datei-Interaktion (Startseite, nur Spielleiter)

- **Neu**: `showSaveFilePicker` → leeres Regelwerk anlegen → Datei schreiben → `/edit-ruleset`; Fallback: `window.prompt` für Name
- **Laden**: `showOpenFilePicker` → XML lesen → `importRuleset(xml)` → `/edit-ruleset`; Fallback: `<input type="file">`
