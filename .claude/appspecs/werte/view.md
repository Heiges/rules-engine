# Werte — View-Spezifikation

## Views

| View | Route | Datei |
|------|-------|-------|
| `WerteView` | `/tile/werte` | [frontend/…/WerteView.tsx](../../../frontend/src/views/WerteView.tsx) |

## WerteView

Einfaches Formular zur Bearbeitung des Wertebereichs (`ValueRange`). Keine Listenansicht, kein Detailnavigation.

### Felder

| Feld | Label | Typ | Quelle |
|------|-------|-----|--------|
| `min` | Unterster Wert | `number` | `rulesetData.valueRange.min` |
| `average` | Durchschnittswert | `number` | `rulesetData.valueRange.average` |
| `max` | Oberster Wert | `number` | `rulesetData.valueRange.max` |

### Verhalten

- Lokaler State wird bei `onChange` aktualisiert.
- Persistenz erfolgt bei `onBlur` — nicht bei jedem Tastendruck.
- Validierung (min ≤ average ≤ max) liegt in der Domäne und im Backend; die View selbst zeigt keine Fehlermeldung bei Validierungsfehlern.
- Fehler beim Speichern werden still verschluckt.

### Persistenz

1. `fileHandle` vorhanden → XML exportieren, in Datei schreiben (`fileHandle.createWritable()`)
2. sonst → `saveRuleset(currentRuleset, updatedData)` via API

## Referenzen

- [werte/domain.md](domain.md) — Domänenklassen `Value`, `ValueRange`
