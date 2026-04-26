# Regelwerk laden – Dateidialog + API

## Ziel

Die Kachel „Regelwerk laden" öffnet einen Dateidialog (browsernativ oder Fallback) und schickt den XML-Inhalt über die REST API ans Backend zum Parsen.

## Anforderungen

- Klick auf „Regelwerk laden" öffnet einen Dateiauswahl-Dialog, gefiltert auf `.xml`
- Der XML-Inhalt wird per `POST /api/rulesets/import` ans Backend gesendet; das Backend gibt strukturierte Daten zurück
- Das geladene Regelwerk wird in `RulesetContext` gesetzt; Navigation zu `/edit-ruleset`
- Unterstützte Browser: alle (Chromium-Pfad und Fallback)

## Browser-Kompatibilität

| Browser | Methode |
|---|---|
| Chromium (Chrome, Edge) | `window.showOpenFilePicker` (File System Access API) – liefert ein `FileSystemFileHandle` |
| Brave, Firefox, Safari | `<input type="file">` (programmatisch erzeugt und geklickt) – kein `FileHandle` |

Im Fallback-Pfad wird `fileHandle` im Context auf `null` gesetzt; Speichern via Handle ist dann nicht möglich (Workaround nötig).

## Entscheidungen

- **Primärpfad**: `showOpenFilePicker` wenn verfügbar, sonst Fallback — kein Error-Abbruch
- **API für das Parsen**: das Frontend liest nur den XML-Text; das Parsen übernimmt `POST /api/rulesets/import`
- Kein direktes Lesen des Dateisystems im Backend (kein Pfad übergeben)

## Implementierung

| Artefakt | Pfad |
|---|---|
| View (Kacheln + Dialoge) | `frontend/src/views/HomeView.tsx` |
| API-Funktionen | `frontend/src/api.ts` (`importRuleset`) |
| Backend-Endpunkt | `api/src/main/java/de/heiges/rulesengine/api/controller/RulesetController.java` (`POST /api/rulesets/import`) |
| Context | `frontend/src/context/RulesetContext.tsx` |

## Rekonstruktion

```
HomeView: Klick auf "Regelwerk laden" → handleLoad()
  Wenn window.showOpenFilePicker verfügbar:
    showOpenFilePicker → handle → file.text() → importRuleset(xml)
    → setFileHandle(handle), setCurrentRuleset(handle.name), setRulesetData(data)
  Sonst (Brave, Firefox):
    <input type="file"> erzeugen, .click()
    onchange → file.text() → importRuleset(xml)
    → setFileHandle(null), setCurrentRuleset(file.name), setRulesetData(data)
  → navigate('/edit-ruleset')
```
