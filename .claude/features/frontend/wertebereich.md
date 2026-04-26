# Wertebereich

## Ziel

Der Nutzer kann unter `/tile/werte` den numerischen Wertebereich eines Regelwerks festlegen: Minimum, Durchschnitt und Maximum. Gespeichert wird zurück in die Originaldatei (via `fileHandle`) oder bei fehlendem Handle via API.

## Anforderungen

- Route: `/tile/werte` (spezifische Route vor dem generischen `/tile/:id`)
- Drei bearbeitbare Ganzzahl-Felder: **Unterster Wert** (`min`), **Durchschnittswert** (`average`), **Oberster Wert** (`max`)
- Auto-Persist beim Verlassen eines Feldes (`onBlur`) — analog zu `AttributeView`
- Kein expliziter Speichern-Button, keine Validierungsprüfung
- Standardwerte wenn kein `valueRange` im Context: `min=-10, average=0, max=10`
- Zurück-Button navigiert zu `/edit-ruleset`

## Entscheidungen

- **Auto-Persist auf `onBlur`**: Analog zu `AttributeView` — jedes Feld speichert beim Verlassen eigenständig. Kein Speichern-Button, keine Validierungsinvariante.
- **Speicherpfad folgt dem Ladepfad**: Wenn `fileHandle` im Context vorhanden (Datei wurde per `showOpenFilePicker` geöffnet), wird zurück in die Originaldatei geschrieben. Fallback: `PUT /api/rulesets/{name}` → `~/.rules-engine/data/`.

## Implementierung

| Artefakt | Pfad |
|---|---|
| View | `frontend/src/views/WerteView.tsx` |
| Styles | `frontend/src/views/WerteView.css` |
| Routing (Route `/tile/werte` vor `/tile/:id`) | `frontend/src/App.tsx` |
| API-Funktion `saveRuleset` | `frontend/src/api.ts` |
| Context (`rulesetData`, `setRulesetData`, `currentRuleset`, `fileHandle`) | `frontend/src/context/RulesetContext.tsx` |

## Rekonstruktion

```
WerteView liest werte aus rulesetData.valueRange (Fallback: min=-10, average=0, max=10).

update(field, raw):   // onChange — aktualisiert nur lokalen State
  val = parseInt(raw)
  if !isNaN(val): setWerte(prev => { ...prev, [field]: val })

commit(field, raw):   // onBlur — persistiert
  val = parseInt(raw)
  if !isNaN(val):
    updated = { ...werte, [field]: val }
    setWerte(updated)
    persist(updated)

persist(updated):
  updatedData = { ...rulesetData, valueRange: updated }
  setRulesetData(updatedData)
  if (fileHandle):
    xml = await exportRuleset(updatedData)
    await fileHandle.createWritable() → write → close   // schreibt Originaldatei
  else if (currentRuleset):
    await saveRuleset(currentRuleset, updatedData)       // PUT /api/rulesets/{name}
```
