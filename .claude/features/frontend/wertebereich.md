# Wertebereich

## Ziel

Der Nutzer kann unter `/tile/werte` den numerischen Wertebereich eines Regelwerks festlegen: Minimum, Durchschnitt und Maximum. Gespeichert wird zurück in die Originaldatei (via `fileHandle`) oder bei fehlendem Handle via API.

## Anforderungen

- Route: `/tile/werte` (spezifische Route vor dem generischen `/tile/:id`)
- Drei bearbeitbare Ganzzahl-Felder: **Unterster Wert** (`min`), **Durchschnittswert** (`average`), **Oberster Wert** (`max`)
- Invariante: `min ≤ average ≤ max`. Bei Verletzung: Fehlermeldung + Speichern-Button deaktiviert
- Expliziter Speichern-Button. Nach Erfolg kurz „✓ Gespeichert" (1800 ms)
- Kein Regelwerk geladen (`rulesetData === null`): Speichern-Button deaktiviert
- Standardwerte wenn kein `valueRange` im Context: `min=-10, average=0, max=10`
- Zurück-Button navigiert zu `/edit-ruleset`
- Schreibfehler als roter Text unterhalb des Formulars

## Entscheidungen

- **Expliziter Speichern-Button**: Bei Zahlenfeldern ist Auto-Persist auf jeden Keystroke störend (Zwischenzustand `-` beim Eintippen von `-5` wäre ungültig).
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
save():
  const updatedData = { ...rulesetData, valueRange: werte }
  setRulesetData(updatedData)
  if (fileHandle):
    xml = await exportRuleset(updatedData)
    await fileHandle.createWritable() → write → close   // schreibt Originaldatei
  else if (currentRuleset):
    await saveRuleset(currentRuleset, updatedData)       // PUT /api/rulesets/{name}
  bei Erfolg: setSaved(true), setTimeout 1800ms
  bei Fehler: saveError-State setzen
```
