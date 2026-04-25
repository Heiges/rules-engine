# Edit-Regelwerk-Kachel

## Ziel

Sobald ein Regelwerk geladen ist, erscheint auf der Startseite eine dritte Kachel, die den Nutzer direkt zur Bearbeitung des aktuell geladenen Regelwerks führt. Ohne geladenes Regelwerk ist die Kachel nicht sichtbar.

## Anforderungen

- Die Kachel erscheint nur, wenn `currentRuleset !== null` (Regelwerk geladen).
- Kachel-Name: `Regelwerk <Name> bearbeiten` — der Dateiname ohne `.xml`-Endung (Groß-/Kleinschreibung egal).
- Kachel-Beschreibung: `Bearbeite das aktuell geladene Regelwerk` (statisch).
- Die Kachel-ID ist `edit-ruleset`; sie navigiert über den Standard-Tile-Mechanismus zu `/tile/edit-ruleset`.
- Die zwei Basiskacheln („Neues Regelwerk", „Regelwerk laden") sind immer sichtbar und bleiben unverändert.

## Entscheidungen

- **Statisches Array ausgelagert**: Die Basiskacheln werden in `staticTiles` extrahiert; `tiles` wird zur berechneten Variable. Das hält die Logik lesbar ohne zusätzliche Komponente.
- **`.xml`-Endung entfernen**: Regex `/\.xml$/i` — case-insensitiv, damit auch `.XML`-Dateien korrekt angezeigt werden.
- **Kein neuer Context-State**: `currentRuleset` aus `RulesetContext` reicht aus; kein eigener State nötig.
- **Kein neues Routing**: Die Kachel navigiert zu `/tile/edit-ruleset` via Default-Tile-Verhalten. Eine dedizierte Route für diese View existiert noch nicht.

## Implementierung

| Artefakt | Pfad |
|---|---|
| View (geändert) | `frontend/src/views/HomeView.tsx` |
| Context (unverändert) | `frontend/src/context/RulesetContext.tsx` |
| Tile-Komponente (unverändert) | `frontend/src/components/Tile.tsx` |

## Rekonstruktion

```
Wenn ein Regelwerk geladen wurde, zeige auf der Startseite (HomeView) eine dritte Kachel an:
- Name: "Regelwerk <Name des Regelwerks> bearbeiten" (Dateiname ohne .xml-Endung)
- Beschreibung: "Bearbeite das aktuell geladene Regelwerk"
- Die Kachel ist nur sichtbar, wenn currentRuleset im RulesetContext gesetzt ist.
- Die zwei bestehenden Kacheln ("Neues Regelwerk", "Regelwerk laden") bleiben immer sichtbar.
```

Kontext: `RulesetContext` liefert `currentRuleset: string | null` (Dateiname inkl. `.xml`). Die `Tile`-Komponente navigiert per Default zu `/tile/<id>`. Basiskacheln waren bisher als statisches `const tiles`-Array definiert.
