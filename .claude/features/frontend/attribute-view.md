# Attribute-View (CRUD)

## Ziel

Der Nutzer kann unter `/tile/attributes` die Attribute eines Regelwerks vollständig verwalten: bestehende umbenennen, löschen und neue anlegen. Jede Änderung wird sofort in den `RulesetContext` zurückgeschrieben und — sofern ein `fileHandle` vorhanden ist — direkt in die XML-Datei persistiert. Ohne `fileHandle` wird die geänderte XML als Download angeboten. Schreibfehler werden als roter Fehlertext angezeigt.

## Anforderungen

- Route: `/tile/attributes` (spezifische Route vor dem generischen `/tile/:id`, damit `DetailView` nicht greift).
- Liste aller Attribute aus `xmlContent` im Context (via `getElementsByTagName`, kein `querySelector`).
- **Umbenennen**: Klick auf einen Namen aktiviert ein Inline-Input-Feld. Bestätigung per Enter oder Blur. Escape bricht ab (kein Speichern). Leere Namen und Duplikate werden still verworfen.
- **Löschen**: Sofort per ✕-Button; kein Bestätigungsdialog.
- **Neu anlegen**: Eingabefeld am Ende der Liste; Bestätigung per Button oder Enter. „Hinzufügen"-Button ist deaktiviert, solange Name leer oder bereits vorhanden ist. Neue Attribute erhalten `value="0"`.
- Anzahl der Attribute wird als Untertitel angezeigt (`n Attribute`).
- Leerzustand: Hinweis „Noch keine Attribute vorhanden."
- Zurück-Button navigiert zu `/edit-ruleset`.
- Schreibfehler werden als roter Fehlertext über dem Hinzufügen-Bereich angezeigt.
- Ist kein `fileHandle` vorhanden, wird die geänderte XML als Download angeboten.

## Entscheidungen

- **`getElementsByTagName` statt `querySelector`**: Nach `XMLSerializer.serializeToString` können Browser `xmlns=""`-Namespace-Deklarationen einfügen, die `querySelector` in XML-Dokumenten unzuverlässig machen. `getElementsByTagName` matcht per Local Name, unabhängig vom Namespace.
- **`skipCommit`-Ref für Escape**: `onBlur` feuert immer (auch nach Escape). Ein `useRef`-Flag verhindert, dass Escape einen ungewollten Commit auslöst.
- **Kein Value-Editing**: Die Attributwerte sind im generischen Regelwerk alle `0`; sie werden bei der Charaktererstellung gesetzt. In diesem View werden nur Namen verwaltet.
- **`key={i}` (Index) statt `key={attr.name}`**: Würde der Name als Key verwendet, führt jeder Tastendruck zu einem Remount des Inputs (Key ändert sich mit dem State). Index-Keys sind bei kontrollierten Inputs ohne Reorder sicher.
- **Auto-Persist auf jede Mutation**: Löschen, Umbenennen, Hinzufügen schreiben sofort in Context und Datei — kein separater „Speichern"-Button nötig.
- **Download-Fallback wie `WerteView`**: Ohne `fileHandle` (z.B. Firefox oder Laden via `<input type="file">`) wird `<a download>` genutzt. Der Nutzer muss die Datei manuell ersetzen.
- **Fehleranzeige statt stiller Fehler**: `createWritable()` kann fehlschlagen (Berechtigungen, gesperrte Datei). Der Fehler wird als roter Text angezeigt.
- **XML-Manipulation via DOMParser + XMLSerializer**: Attribute-Nodes werden ersetzt, der Rest der XML (Skills etc.) bleibt erhalten.
- **CSS-Wiederverwendung**: `DetailView.css` liefert `.back-button`; eigene `AttributeView.css` für Listenstile inkl. `.attr-error`.

## Implementierung

| Artefakt | Pfad |
|---|---|
| View | `frontend/src/views/AttributeView.tsx` |
| Styles | `frontend/src/views/AttributeView.css` |
| Routing (Route `/tile/attributes` vor `/tile/:id`) | `frontend/src/App.tsx` |
| Context (`xmlContent`, `setXmlContent`, `fileHandle`, `currentRuleset`) | `frontend/src/context/RulesetContext.tsx` |

## Rekonstruktion

```
Erstelle eine View unter /tile/attributes mit der man Attribute eines Regelwerks verwalten kann:
- Liste aller Attribute (aus xmlContent im RulesetContext via getElementsByTagName — kein querySelector,
  da XMLSerializer Namespace-Deklarationen einfügen kann)
- Klick auf Namen → Inline-Rename (Enter/Blur = speichern, Escape = abbrechen via skipCommit-Ref)
- ✕-Button pro Zeile zum Löschen
- Eingabefeld + "Hinzufügen"-Button für neue Attribute (value=0, Button disabled wenn leer/Duplikat)
- Änderungen sofort in xmlContent (Context) schreiben
- Persistenz: fileHandle vorhanden → createWritable() mit try/catch, Fehler als roter Text anzeigen
  fileHandle null → Download-Fallback via <a download> (wie WerteView)
- Zurück-Button navigiert zu /edit-ruleset
```

Kontext: `RulesetContext` stellt `xmlContent`, `setXmlContent`, `fileHandle` und `currentRuleset` bereit. Die Route muss vor `/tile/:id` in `App.tsx` registriert werden. XML-Struktur: `<ruleset><attributeSet><attribute name="X" value="0"/></attributeSet><skills>…</skills></ruleset>`.
