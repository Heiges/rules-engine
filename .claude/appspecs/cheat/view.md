# Cheats — View-Spezifikation

## Views

| View | Route | Datei |
|------|-------|-------|
| `CheatView` | `/tile/cheats` | [frontend/…/CheatView.tsx](../../../frontend/src/views/CheatView.tsx) |
| `CheatDetailView` | `/tile/cheats/:index` | [frontend/…/CheatDetailView.tsx](../../../frontend/src/views/CheatDetailView.tsx) |

## CheatView

Listenansicht aller Cheats mit Inline-Formular zum Anlegen.

### Funktionen

| Funktion | Beschreibung |
|----------|-------------|
| Sortieren | Spalte „Name" klickbar: `null → asc → desc → null` |
| Einzelauswahl / Alle auswählen | Checkbox pro Zeile und im Header |
| Löschen (einzeln / Mehrfach) | Sofort ohne Bestätigung |
| Inline-Formular „Neuer Cheat" | Name + Beschreibung; wird über „+ Neuer Cheat" eingeblendet; Enter im Name-Feld fügt hinzu |
| Anzeigen / Bearbeiten | Navigiert zu `/tile/cheats/{originalIndex}` |

### Hinweis

Das Inline-Formular wird ein- und ausgeblendet (`showAdd`-State). Abbrechen setzt Name und Beschreibung zurück.

## CheatDetailView

Formular zum Bearbeiten eines einzelnen `Cheat`.

### Felder

| Feld | Label | Pflicht | Validierung |
|------|-------|---------|------------|
| `name` | Name | ja | Nicht leer; kein Duplikat |
| `description` | Beschreibung | nein | — |

### Verhalten

- `index === 'neu'` → Neuanlage (Route erreichbar, aber kein direkter Einstiegspunkt in der Navigation — Anlegen erfolgt inline in `CheatView`).
- Nach Speichern und „Abbrechen": navigiert zu `/tile/cheats`.
- Duplikatprüfung im Frontend.
- Enter im Name-Feld löst Speichern aus.

## Persistenz

1. `fileHandle` vorhanden → XML exportieren, in Datei schreiben
2. sonst → `saveRuleset(currentRuleset, updatedData)` via API

## Referenzen

- [cheat/domain.md](domain.md) — Domänenklasse `Cheat`
