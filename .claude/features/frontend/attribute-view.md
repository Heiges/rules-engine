# Attribute-View (CRUD)

## Ziel

Der Nutzer kann unter `/tile/attributes` die Attribute eines Regelwerks vollständig verwalten: bestehende umbenennen, löschen und neue anlegen. Jede Änderung wird sofort via `PUT /api/rulesets/{name}` persistiert — kein Download, kein Dateidialog.

## Anforderungen

- Route: `/tile/attributes` (spezifische Route vor dem generischen `/tile/:id`)
- Liste aller Attribute aus `rulesetData.attributes` im Context
- **Umbenennen**: Klick auf einen Namen aktiviert ein Inline-Input-Feld. Bestätigung per Enter oder Blur. Escape bricht ab (`skipCommit`-Ref). Leere Namen und Duplikate werden still verworfen.
- **Löschen**: Sofort per ✕-Button; kein Bestätigungsdialog
- **Neu anlegen**: Eingabefeld am Ende; Bestätigung per Button oder Enter. Button deaktiviert wenn Name leer oder bereits vorhanden. Neue Attribute: `description: ''`, `value: 0`
- Anzahl der Attribute als Untertitel (`n Attribute`)
- Leerzustand: „Noch keine Attribute vorhanden."
- Zurück-Button navigiert zu `/edit-ruleset`
- Schreibfehler werden als roter Fehlertext angezeigt

## Entscheidungen

- **Auto-Persist auf jede Mutation**: Löschen, Umbenennen, Hinzufügen rufen sofort `saveRuleset(currentRuleset, updatedData)` → `PUT /api/rulesets/{name}` → `~/.rules-engine/data/`. Das Backend ist die kanonische Speicherstelle, unabhängig vom Ladepfad.
- **Vollständiges `rulesetData` an `saveRuleset` übergeben**: Die API nimmt immer das komplette Regelwerk entgegen (`PUT`); der Controller ersetzt die Datei atomar. Partial-Update-Endpunkte wären Mehraufwand ohne Mehrwert.
- **`skipCommit`-Ref für Escape**: `onBlur` feuert immer (auch nach Escape). Ein `useRef`-Flag verhindert, dass Escape einen ungewollten Commit auslöst.
- **`key={i}` (Index)**: Bei kontrollierten Inputs ohne Reorder sind Index-Keys sicher und verhindern ungewollte Remounts beim Tippen.
- **Kein Value-Editing**: Attributwerte werden bei der Charaktererstellung gesetzt; hier werden nur Namen verwaltet.

## Implementierung

| Artefakt | Pfad |
|---|---|
| View | `frontend/src/views/AttributeView.tsx` |
| Styles | `frontend/src/views/AttributeView.css` |
| Routing (Route `/tile/attributes` vor `/tile/:id`) | `frontend/src/App.tsx` |
| API-Funktion `saveRuleset` | `frontend/src/api.ts` |
| Context (`rulesetData`, `setRulesetData`, `currentRuleset`) | `frontend/src/context/RulesetContext.tsx` |

## Rekonstruktion

```
AttributeView liest attrs aus rulesetData.attributes (Context-Initialwert).
persist(updated: Attribute[]):
  const updatedData = { ...rulesetData, attributes: updated }
  setRulesetData(updatedData)
  await saveRuleset(currentRuleset, updatedData)   // PUT /api/rulesets/{name}
  bei Fehler: saveError-State setzen

Umbenennen: Inline-Input; skipCommit-Ref verhindert Blur-Commit nach Escape.
Löschen: filter + persist.
Hinzufügen: { name, description: '', value: 0 } + persist.
```
