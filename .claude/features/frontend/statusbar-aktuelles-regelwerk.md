# Statuszeile – Aktuelles Regelwerk

## Ziel

Zeigt am unteren Rand des Anwendungsrahmens dauerhaft an, welches Regelwerk aktuell geladen ist. Gibt dem Anwender jederzeit Orientierung, ohne dass er in eine Detail-Ansicht navigieren muss.

## Anforderungen

- Statuszeile erscheint auf allen Seiten (nicht nur auf HomeView)
- Wenn ein Regelwerk geladen ist: `Aktuelles Regelwerk: <name>` (Name fett)
- Wenn kein Regelwerk geladen ist: `Kein Regelwerk geladen`
- Statuszeile sitzt am unteren Ende des `#root`-Flex-Containers (nicht `position: fixed`)
- Der Name des Regelwerks wird global über einen React Context gehalten und kann von jeder Komponente gesetzt werden

## Entscheidungen

- **React Context statt lokalem State**: Der Regelwerk-Name muss von verschiedenen Stellen (z. B. „Regelwerk laden"-Flow) schreibbar und von der immer sichtbaren StatusBar lesbar sein — daher globaler Context.
- **`null` als Initialzustand**: Unterscheidet sauber zwischen „noch nicht geladen" und einem leeren String-Namen.
- **`position: fixed; bottom: 0`**: Die StatusBar ist immer am unteren Viewport-Rand sichtbar und wandert mit der Fensterhöhe mit — unabhängig von der Content-Höhe. `#root` bekommt `padding-bottom: 30px`, damit kein Content unter der Statuszeile versteckt wird.
- **Styling mit CSS Custom Properties**: Passt sich automatisch in Light- und Dark-Mode ein, ohne eigene Medienabfragen.

## Implementierung

| Artefakt | Pfad |
|---|---|
| Context (State + Provider + Hook) | `frontend/src/context/RulesetContext.tsx` |
| StatusBar-Komponente | `frontend/src/components/StatusBar.tsx` |
| StatusBar-Styles | `frontend/src/components/StatusBar.css` |
| App-Integration (Provider + StatusBar) | `frontend/src/App.tsx` |

## Rekonstruktion

```
Füge der React-App eine Statuszeile am unteren Rand hinzu.
Die Statuszeile zeigt "Aktuelles Regelwerk: <name>" (Name fett) wenn ein Regelwerk geladen ist,
sonst "Kein Regelwerk geladen".
Den Namen global als React Context speichern (RulesetContext mit useRuleset-Hook).
StatusBar außerhalb der Routes in App.tsx einbinden, damit sie auf allen Seiten sichtbar ist.
Das Content-Div der Routes erhält flex: 1, damit die StatusBar ans Ende gedrückt wird.
```

Kontext: `#root` in `index.css` ist `display: flex; flex-direction: column; min-height: 100svh`. Die StatusBar nutzt CSS Custom Properties (`--border`, `--text`, `--bg`) aus dem globalen Theme.
