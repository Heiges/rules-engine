# Edit-Regelwerk-View

## Ziel

Nach dem Klick auf die „Regelwerk bearbeiten"-Kachel gelangt der Nutzer in eine dedizierte Bearbeitungsansicht. Dort sieht er Kacheln für die einzelnen Bestandteile des Regelwerks (Werte, Attribute, Skills). Die Kachel-Beschreibung zeigt direkt, wie viele Einträge bereits vorhanden sind, sodass der Nutzer auf einen Blick den aktuellen Stand erkennt.

## Anforderungen

- Route: `/edit-ruleset` — eigene Route, nicht über den generischen `/tile/:id`-Mechanismus.
- Überschrift: `<Regelwerkname> bearbeiten` (Dateiname ohne `.xml`-Endung, case-insensitiv).
- Zurück-Button navigiert zu `/` (HomeView).
- Kachel „Werte": navigiert zu `/tile/werte`.
- Kachel „Attribute":
  - Beschreibung: `Attribute bearbeiten (<n> Attribute)` — `n` aus `rulesetData.attributes.length`.
  - Navigiert zu `/tile/attributes`.
- Kachel „Skills":
  - Beschreibung: `Skill anlegen und modifizieren (<n> Skills)` — `n` aus `rulesetData.skills.length`.
  - Navigiert zu `/tile/skills`.
- Anzahlen werden aus dem im RulesetContext gespeicherten `rulesetData` ermittelt; bei `null` wird `0` angezeigt.

## Entscheidungen

- **Anzahl direkt aus `rulesetData`**: Kein separates XML-Parsing mehr — `rulesetData` im Context ist die kanonische Quelle für Anzahlen.
- **CSS-Wiederverwendung**: `EditRulesetView` importiert `DetailView.css` (`.back-button`) und `HomeView.css` (`.tile-grid`) — keine separate CSS-Datei nötig.
- **Explizites `onClick` statt Default-Tile-Navigation in HomeView**: Die Edit-Kachel in `HomeView` bekommt `onClick={() => navigate('/edit-ruleset')}`, weil der Default-Mechanismus zu `/tile/edit-ruleset` navigieren würde.

## Implementierung

| Artefakt | Pfad |
|---|---|
| View | `frontend/src/views/EditRulesetView.tsx` |
| Routing (Route `/edit-ruleset` + `/tile/skills`) | `frontend/src/App.tsx` |
| CSS (wiederverwendet) | `frontend/src/views/DetailView.css` |
| CSS (wiederverwendet) | `frontend/src/views/HomeView.css` |

## Rekonstruktion

```
Die View /edit-ruleset zeigt drei Kacheln:
- "Werte" → /tile/werte
- "Attribute" → /tile/attributes, Beschreibung mit Attributanzahl aus rulesetData
- "Skills" → /tile/skills, Beschreibung mit Skillanzahl aus rulesetData
Zurück-Button zur Startseite, Überschrift "<Regelwerkname> bearbeiten".
```

Kontext: `RulesetContext` stellt `currentRuleset`, `rulesetData` und `fileHandle` bereit. `rulesetData.attributes` und `rulesetData.skills` liefern die Anzahlen. Die `Tile`-Komponente navigiert per Default zu `/tile/<id>`. Routing in `App.tsx` via React Router.
