# Regelwerk laden (XML-Dateiauswahl)

## Ziel

Ermöglicht dem Anwender, ein bestehendes Regelwerk als XML-Datei aus dem Dateisystem zu wählen. Nach der Auswahl wird der Dateiname in der Statuszeile angezeigt, sodass klar ist, welches Regelwerk aktiv ist.

## Anforderungen

- Klick auf die Kachel „Regelwerk laden" öffnet den nativen Dateiauswahldialog des Browsers
- Nur Dateien mit der Endung `.xml` sind auswählbar (`accept=".xml"`)
- Nach erfolgreicher Auswahl wird der Dateiname (nicht der vollständige Pfad) als `currentRuleset` im globalen Context gesetzt
- Die Statuszeile zeigt daraufhin `Aktuelles Regelwerk: <dateiname>`
- Es findet keine Validierung des XML-Inhalts statt — wenn die Datei kein gültiges Regelwerk enthält, schlägt das spätere Laden einfach fehl
- Nach der Auswahl wird `e.target.value` zurückgesetzt, damit dieselbe Datei erneut auswählbar ist
- Die Kachel navigiert nicht zur DetailView; der Dateidialog ersetzt diese Aktion vollständig

## Entscheidungen

- **Verstecktes `<input type="file">`**: Nativer Dateidialog ohne eigene UI-Komponente; `ref.click()` triggert ihn programmatisch aus der Kachel heraus. Einfachste browserkompatible Lösung.
- **`onClick`-Prop am `Tile`**: Statt einer separaten Komponente bekommt `Tile` ein optionales `onClick`-Prop. Ist es gesetzt, ersetzt es die Navigation (`onClick ?? navigate`). Kein Breaking Change für bestehende Kacheln.
- **Nur Dateiname speichern, kein Inhalt**: Für die Statuszeile reicht der Name. Die eigentliche Deserialisierung des XML findet zu einem späteren Zeitpunkt statt.
- **Keine Fehlerbehandlung**: Explizit ausgelassen — zu früh für Error-States, da der Lade-Flow noch nicht vollständig ist.

## Implementierung

| Artefakt | Pfad |
|---|---|
| Kachel-Übersicht (Dateidialog-Logik) | `frontend/src/views/HomeView.tsx` |
| Kachel-Komponente (optionaler `onClick`) | `frontend/src/components/Tile.tsx` |
| Globaler Regelwerk-Context | `frontend/src/context/RulesetContext.tsx` |
| Statuszeile | `frontend/src/components/StatusBar.tsx` |

## Rekonstruktion

```
Wenn der Anwender auf die Kachel „Regelwerk laden" klickt, soll ein nativer Dateiauswahldialog
öffnen (nur .xml). Nach Auswahl wird der Dateiname in der Statuszeile angezeigt.

Umsetzung:
- Tile-Komponente bekommt optionales onClick-Prop; wenn gesetzt, ersetzt es navigate().
- HomeView: useRef auf verstecktes <input type="file" accept=".xml">, onClick der load-ruleset-Kachel
  ruft fileInputRef.current?.click() auf.
- onChange des Inputs: setCurrentRuleset(file.name), danach e.target.value = '' zurücksetzen.
- Keine Validierung des XML-Inhalts.
```

Kontext: `RulesetContext` (`frontend/src/context/RulesetContext.tsx`) stellt `currentRuleset` und `setCurrentRuleset` bereit. `StatusBar` liest `currentRuleset` und zeigt es an. `Tile` nutzt bereits `useNavigate`; das neue `onClick`-Prop überschreibt nur die Navigationslogik.
