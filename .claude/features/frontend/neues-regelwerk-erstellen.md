# Neues Regelwerk erstellen

## Ziel

Ermöglicht dem Nutzer, per Klick auf die Kachel „Neues Regelwerk" eine leere XML-Persistenzdatei im selbst gewählten Verzeichnis anzulegen. Die Datei wird sofort als aktives Regelwerk gesetzt und in der Statuszeile angezeigt.

## Anforderungen

- Klick auf die Kachel „Neues Regelwerk" öffnet den nativen Speichern-Dialog des Betriebssystems
- Vorgeschlagener Dateiname: `regelwerk.xml`; Filter beschränkt Auswahl auf `.xml`
- Die erzeugte Datei enthält ein gültiges, leeres XML-Regelwerk (kompatibel mit `XmlRulesetRepository.load()`)
- Nach erfolgreichem Erstellen wird `currentRuleset` auf den Dateinamen gesetzt (Anzeige in Statuszeile)
- Der `FileSystemFileHandle` wird im `RulesetContext` gespeichert (für späteres Speichern von Änderungen)
- Bricht der Nutzer den Dialog ab (`AbortError`), passiert nichts
- Andere Fehler werden in der Konsole protokolliert, aber nicht dem Nutzer angezeigt
- Funktioniert nur in Browsern mit File System Access API (Chromium-basiert)

## Entscheidungen

- **File System Access API (`showSaveFilePicker`) statt Browser-Download**: Der native Dialog erlaubt Verzeichniswahl + Dateiname in einem Schritt. Ein einfacher Download würde die Datei ins Download-Verzeichnis legen und keine Kontrolle über den Speicherort bieten.
- **`FileSystemFileHandle` im Context speichern**: Wird für zukünftiges Schreiben von Änderungen in dieselbe Datei benötigt. Ohne Handle müsste der Nutzer die Datei beim Speichern erneut suchen.
- **Leeres XML direkt im Frontend erzeugen** (kein Backend-Aufruf): Es gibt noch keine API. Das XML-Format ist trivial und stabil genug, um es als Konstante im Frontend zu halten.
- **`SaveFilePickerOptions`-Typdeklaration als lokale `.d.ts`-Datei**: TypeScript 6.0.3 enthält `FileSystemFileHandle` im DOM-Lib, aber `showSaveFilePicker` auf `Window` fehlt. Statt einer externen `@types`-Abhängigkeit wird die minimale Deklaration lokal gepflegt.

## Implementierung

| Artefakt | Pfad |
|---|---|
| View mit Handler | `frontend/src/views/HomeView.tsx` |
| React Context (erweitert um `fileHandle`) | `frontend/src/context/RulesetContext.tsx` |
| Typ-Deklaration File System Access API | `frontend/src/file-system-access.d.ts` |

### Leeres XML-Format (Konstante in `HomeView.tsx`)

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<ruleset>
    <attributeSet/>
    <skills/>
</ruleset>
```

Dieses Format ist identisch mit dem, was `XmlRulesetRepository.save()` für ein leeres `AttributeSet` und leere Skills-Liste erzeugt.

## Rekonstruktion

```
Implementiere das Feature "Neues Regelwerk erstellen" im Frontend:
Klick auf die Kachel "Neues Regelwerk" öffnet über window.showSaveFilePicker()
einen nativen Speichern-Dialog (suggestedName "regelwerk.xml", Filter .xml).
Die ausgewählte Datei erhält ein leeres XML-Regelwerk (kompatibel mit
XmlRulesetRepository). Danach wird der FileSystemFileHandle im RulesetContext
gespeichert und currentRuleset auf den Dateinamen gesetzt.
```

Kontext: `RulesetContext` (`frontend/src/context/RulesetContext.tsx`) muss `fileHandle: FileSystemFileHandle | null` und `setFileHandle` als neue Felder erhalten. TypeScript 6.0.3 kennt `window.showSaveFilePicker` nicht — es braucht eine Deklarationsdatei `frontend/src/file-system-access.d.ts` mit `interface Window { showSaveFilePicker(...): Promise<FileSystemFileHandle> }`.
