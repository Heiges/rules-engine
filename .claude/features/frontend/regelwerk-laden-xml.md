# Regelwerk laden (XML-Dateiauswahl)

## Ziel

Ermöglicht dem Anwender, ein bestehendes Regelwerk als XML-Datei aus dem Dateisystem zu wählen. Nach der Auswahl ist das Regelwerk im Context aktiv und kann direkt bearbeitet und gespeichert werden.

## Anforderungen

- Klick auf die Kachel „Regelwerk laden" öffnet den nativen Dateiauswahldialog
- Nur Dateien mit der Endung `.xml` sind auswählbar
- Nach erfolgreicher Auswahl wird der Dateiname als `currentRuleset` und der Inhalt als `xmlContent` im Context gesetzt
- Wenn der Browser die File System Access API unterstützt (`showOpenFilePicker`): Das `FileSystemFileHandle` wird als `fileHandle` im Context gesetzt, so dass spätere Speicher-Operationen direkt in die Originaldatei schreiben können
- Wenn der Browser `showOpenFilePicker` nicht unterstützt (Firefox, Safari): Fallback auf `<input type="file">`; `fileHandle` bleibt null; Änderungen können nur per Download gespeichert werden
- Die Statuszeile zeigt `Aktuelles Regelwerk: <dateiname>`
- Es findet keine Validierung des XML-Inhalts statt

## Entscheidungen

- **`showOpenFilePicker` bevorzugt**: Liefert ein schreibbares `FileSystemFileHandle`, das direktes Zurückschreiben in die Originaldatei erlaubt — ohne Download-Umweg. Fallback auf `<input type="file">` für Browser ohne Unterstützung.
- **`fileHandle` im Context**: Wird einmalig beim Laden gesetzt und von allen Views (WerteView, AttributeView) zum Schreiben genutzt. Kein erneuter Dateidialog beim Speichern nötig.
- **Verstecktes `<input type="file">` als Fallback**: Für Browser ohne File System Access API; `ref.click()` triggert ihn aus der Kachel. `fileHandle` wird dabei nicht gesetzt.
- **Keine Validierung des XML-Inhalts**: Zu früh für Error-States; schlägt das spätere Parsen fehl, zeigen die Views entsprechende Fehlerzustände.

## Implementierung

| Artefakt | Pfad |
|---|---|
| Laden-Logik (`handleLoadRuleset`, Fallback `handleFileChange`) | `frontend/src/views/HomeView.tsx` |
| TypeScript-Deklaration für `showOpenFilePicker` | `frontend/src/file-system-access.d.ts` |
| Kachel-Komponente (optionaler `onClick`) | `frontend/src/components/Tile.tsx` |
| Globaler Regelwerk-Context | `frontend/src/context/RulesetContext.tsx` |
| Statuszeile | `frontend/src/components/StatusBar.tsx` |

## Rekonstruktion

```
Wenn der Anwender auf die Kachel „Regelwerk laden" klickt:
1. Falls showOpenFilePicker verfügbar:
   - showOpenFilePicker({ types: [{ accept: { 'application/xml': ['.xml'] } }] })
   - handle.getFile() → text lesen
   - setFileHandle(handle), setXmlContent(text), setCurrentRuleset(file.name)
   - AbortError ignorieren, andere Fehler als alert anzeigen
2. Fallback (kein showOpenFilePicker):
   - Verstecktes <input type="file" accept=".xml"> per ref.click() triggern
   - onChange: file.text() → setXmlContent(text), setCurrentRuleset(file.name)
   - fileHandle bleibt null → nur Download-Speicherung möglich
```

Kontext: `RulesetContext` stellt `currentRuleset`, `setCurrentRuleset`, `fileHandle`, `setFileHandle`, `xmlContent`, `setXmlContent` bereit. TypeScript-Deklaration für `showOpenFilePicker` und `showSaveFilePicker` liegt in `frontend/src/file-system-access.d.ts`.
