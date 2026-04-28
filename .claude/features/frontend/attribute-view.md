# Attribute-View (CRUD)

## Ziel

Kompakte Tabellenübersicht aller Attribute eines Regelwerks. Der View wird in zwei Kontexten genutzt: Referenzregeln (`/tile/attributes`) und Spielwelt (`/world/attributes`). Im Spielwelt-Kontext ist zusätzlich die Gruppierung von Attributen möglich. Jede Mutation wird sofort persistiert.

## Anforderungen

- `AttributeView` akzeptiert Props: `allowGrouping: boolean`, `backPath: string`, `detailBasePath: string`
- Routen: `/tile/attributes` (Referenzregeln, kein Gruppieren) und `/world/attributes` (Spielwelt, Gruppieren erlaubt)
- Tabelle mit Spalten: Checkbox | Name (sortierbar) | Gruppe | Aktionen (Anzeigen, Bearbeiten, Löschen)
- **Checkbox-Spalte**: Einzel-Auswahl pro Zeile; Kopfzeilen-Checkbox wählt alle aus / ab. Wenn ≥1 selektiert: Button „N löschen" und (wenn `allowGrouping`) Button „N gruppieren" erscheinen im Header
- **Gruppieren**: nur wenn `allowGrouping=true`; `window.prompt` für Gruppenname; setzt `groupName` auf den selektierten Attributen
- **Sortierung Name**: Klick auf Spaltenheader „Name" wechselt zyklisch ⇅ → ▲ (A–Z, `localeCompare 'de'`) → ▼ (Z–A) → ⇅. Original-Indizes bleiben erhalten, damit Aktions-Links nach dem Sortieren korrekt navigieren
- **Aktionen pro Zeile**: Anzeigen / Bearbeiten (navigieren zu `{detailBasePath}/:index`), Löschen (sofort, kein Dialog)
- Button „+ Neues Attribut" oben rechts navigiert zu `{detailBasePath}/neu`
- Leerzustand: Hinweistext „Noch keine Attribute vorhanden."
- Schreibfehler: roter Fehlertext unterhalb des Headers
- Zurück-Button navigiert zu `backPath`
- `AttributeDetailView` akzeptiert Prop `listPath: string`; Zurück, Abbrechen und nach Speichern navigieren zu `listPath`
- Routen für Detail: `/tile/attributes/:index` und `/world/attributes/:index`
- Beide Views persistieren sofort: `fileHandle` vorhanden → `exportRuleset` → `fileHandle.createWritable()`; sonst `saveRuleset` → `PUT /api/rulesets/{name}`

## Entscheidungen

- **Tabelle statt Inline-Edit-Liste**: Kompakter, übersichtlicher; Aktionen als Textlinks statt Icon-Buttons. Inline-Edit (`skipCommit`-Ref-Pattern) wurde durch den Detail-View ersetzt.
- **Wrapper-Div für Tabellenrahmen**: `border-collapse: collapse` auf `<table>` verhindert, dass `border-radius` + `overflow: hidden` direkt am `<table>` funktioniert. Der `div.attr-table-wrapper` trägt Rahmen und Radius.
- **`originalIndex` beim Sortieren mitführen**: Die Sortierung arbeitet auf einer abgeleiteten `rows`-Liste (`{ attr, originalIndex }`). Aktions-Links und Checkbox-Selection referenzieren immer den Original-Index im `attrs`-Array — verhindert falsche Navigation nach Sortierung.
- **`text-align: left` auf `.detail-view`**: Der globale `#root`-Style setzt `text-align: center`; das wird in `DetailView.css` überschrieben, damit alle Detail-Views linksbündig sind.
- **Index-basiertes Routing** (`/tile/attributes/:index`, `/world/attributes/:index`): Einfacher als Name-Encoding; stabil solange kein Reorder zwischen Navigation und Bearbeitung stattfindet.
- **Kein separater Anzeigen-only-Modus**: Anzeigen und Bearbeiten zeigen denselben Detail-View. Ein Read-only-Modus kann später ergänzt werden.
- **Props statt Duplizierung**: `allowGrouping`, `backPath`, `detailBasePath` in `AttributeView` und `listPath` in `AttributeDetailView` ermöglichen die Wiederverwendung in Referenzregeln- und Spielwelt-Kontext ohne Code-Duplizierung.

## Implementierung

| Artefakt | Pfad |
|---|---|
| Listen-View | `frontend/src/views/AttributeView.tsx` |
| Detail-View (Anlage/Bearbeitung) | `frontend/src/views/AttributeDetailView.tsx` |
| Styles (Liste + Formular) | `frontend/src/views/AttributeView.css` |
| Globale `text-align: left`-Korrektur | `frontend/src/views/DetailView.css` |
| Routing (Routen vor `/tile/:id`) | `frontend/src/App.tsx` |
| API-Funktionen `exportRuleset`, `saveRuleset` | `frontend/src/api.ts` |
| Context (`rulesetData`, `setRulesetData`, `currentRuleset`, `fileHandle`) | `frontend/src/context/RulesetContext.tsx` |

## Rekonstruktion

```
Erstelle zwei Views für Attribute-Verwaltung:

1. AttributeView (/tile/attributes):
   - Tabelle: Checkbox | Name (sortierbar ⇅/▲/▼ via localeCompare 'de') | Aktionen
   - Sortierung: rows = attrs.map((attr, originalIndex) => ...).sort(...); Aktions-Links nutzen originalIndex
   - Massenauswahl: Set<number> selected; Kopfzeilen-Checkbox; "N löschen"-Button wenn selected.size > 0
   - Aktionen: Anzeigen + Bearbeiten → navigate(`/tile/attributes/${originalIndex}`); Löschen → sofort + persist
   - Tabellenrahmen: div.attr-table-wrapper (border + border-radius + overflow:hidden); table selbst ohne border
   - "Neues Attribut"-Button → navigate('/tile/attributes/neu')

2. AttributeDetailView (/tile/attributes/neu und /tile/attributes/:index):
   - useParams({ index }); isNew = index === 'neu'
   - Felder: Name (input), Beschreibung (textarea), Wert (number input)
   - save(): Duplikatprüfung (gleicher Name, anderer Index) → persist → navigate('/tile/attributes')
   - persist(): identisch zu AttributeView (fileHandle oder saveRuleset)

3. App.tsx: Route /tile/attributes/:index vor /tile/:id eintragen

4. DetailView.css: text-align: left auf .detail-view (überschreibt #root text-align: center)
```

Kontext: `Attribute`-Interface in `api.ts` hat `name: string`, `description: string`, `value: number`. Persistenz via `saveRuleset` (PUT) oder `exportRuleset` + `fileHandle`. `RulesetContext` liefert `rulesetData`, `setRulesetData`, `currentRuleset`, `fileHandle`.
