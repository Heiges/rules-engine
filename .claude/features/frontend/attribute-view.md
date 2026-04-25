# Attribute-View (CRUD)

## Ziel

Der Nutzer kann unter `/tile/attributes` die Attribute eines Regelwerks vollständig verwalten: bestehende umbenennen, löschen und neue anlegen. Jede Änderung wird sofort in den `RulesetContext` zurückgeschrieben und — sofern ein `fileHandle` vorhanden ist — automatisch in die XML-Datei persistiert.

## Anforderungen

- Route: `/tile/attributes` (spezifische Route vor dem generischen `/tile/:id`, damit `DetailView` nicht greift).
- Liste aller Attribute aus `xmlContent` im Context (Selektor `attributeSet > attribute`).
- **Umbenennen**: Klick auf einen Namen aktiviert ein Inline-Input-Feld. Bestätigung per Enter oder Blur. Escape bricht ab (kein Speichern). Leere Namen und Duplikate werden still verworfen.
- **Löschen**: Sofort per ✕-Button; kein Bestätigungsdialog.
- **Neu anlegen**: Eingabefeld am Ende der Liste; Bestätigung per Button oder Enter. „Hinzufügen"-Button ist deaktiviert, solange Name leer oder bereits vorhanden ist. Neue Attribute erhalten `value="0"`.
- Anzahl der Attribute wird als Untertitel angezeigt (`n Attribute`).
- Leerzustand: Hinweis „Noch keine Attribute vorhanden."
- Zurück-Button navigiert zu `/edit-ruleset`.

## Entscheidungen

- **`skipCommit`-Ref für Escape**: `onBlur` feuert immer (auch nach Escape). Ein `useRef`-Flag verhindert, dass Escape einen ungewollten Commit auslöst.
- **Kein Value-Editing**: Die Attributwerte sind im generischen Regelwerk alle `0`; sie werden bei der Charaktererstellung gesetzt. In diesem View werden nur Namen verwaltet.
- **`key={i}` (Index) statt `key={attr.name}`**: Würde der Name als Key verwendet, führt jeder Tastendruck zu einem Remount des Inputs (Key ändert sich mit dem State). Index-Keys sind bei kontrollierten Inputs ohne Reorder sicher.
- **Auto-Persist auf jede Mutation**: Löschen, Umbenennen, Hinzufügen schreiben sofort in Context und Datei — kein separater „Speichern"-Button nötig.
- **XML-Manipulation via DOMParser + XMLSerializer**: Attribute-Nodes werden ersetzt, der Rest der XML (Skills etc.) bleibt erhalten.
- **CSS-Wiederverwendung**: `DetailView.css` liefert `.back-button`; eigene `AttributeView.css` für Listenstile.

## Implementierung

| Artefakt | Pfad |
|---|---|
| View (neu) | `frontend/src/views/AttributeView.tsx` |
| Styles (neu) | `frontend/src/views/AttributeView.css` |
| Routing (Route `/tile/attributes` ergänzt) | `frontend/src/App.tsx` |
| Context (unverändert, liefert `xmlContent`, `setXmlContent`, `fileHandle`) | `frontend/src/context/RulesetContext.tsx` |

## Rekonstruktion

```
Erstelle eine View unter /tile/attributes mit der man Attribute eines Regelwerks verwalten kann:
- Liste aller Attribute (aus xmlContent im RulesetContext, Selektor "attributeSet > attribute")
- Klick auf Namen → Inline-Rename (Enter/Blur = speichern, Escape = abbrechen)
- ✕-Button pro Zeile zum Löschen
- Eingabefeld + "Hinzufügen"-Button für neue Attribute (value=0, Button disabled wenn leer/Duplikat)
- Änderungen sofort in xmlContent (Context) und via fileHandle in die Datei schreiben
- Zurück-Button navigiert zu /edit-ruleset
```

Kontext: `RulesetContext` (`frontend/src/context/RulesetContext.tsx`) stellt `xmlContent` (XML-String), `setXmlContent` und `fileHandle` bereit. Die Route muss vor `/tile/:id` in `App.tsx` registriert werden, sonst greift der generische `DetailView`. XML-Struktur: `<ruleset><attributeSet><attribute name="X" value="0"/></attributeSet><skills>…</skills></ruleset>`.
