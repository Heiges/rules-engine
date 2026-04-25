# Frontend-Kacheln

## Ziel

Primäres Navigationsmuster der Anwendung. Die Startseite zeigt eine Kachel-Übersicht; jede Kachel repräsentiert einen Funktionsbereich und navigiert per Klick zur zugehörigen Detailseite.

## Anforderungen

- Kacheln als Eintrittspunkte: Name + Kurzbeschreibung, klickbar, navigieren zu `/tile/:id`
- `Tile` akzeptiert ein optionales `onClick`-Prop; ist es gesetzt, ersetzt es die Navigation vollständig
- Kacheldaten als Array in `HomeView.tsx` definiert (kein externe Datenhaltung)
- Responsive Grid: `auto-fill`, Minimalbreite 260px, 16px Gap
- Detailseite zeigt den Kachel-Namen als Überschrift und bietet einen Zurück-Button (`← Zurück`)
- Detailseite liest `id` per `useParams()` und schlägt Name in einer statischen Map nach

## Entscheidungen

- Kacheldaten hardcoded in HomeView: solange keine API existiert, kein Over-Engineering
- Tile als eigenständige Komponente: wiederverwendbar, Styling gekapselt
- DetailView mit statischer ID→Name-Map: einfachste Lösung bis eine echte Datenschicht steht
- Kachel-Styling: leicht abgehobener Hintergrund (`#edeaf3` / `#1e1f27` dark), dezenter Rahmen, Schatten, Hover mit `translateY(-2px)` und stärkerem Schatten

## Implementierung

| Artefakt | Pfad |
|---|---|
| Kachel-Übersicht | `frontend/src/views/HomeView.tsx` |
| Kachel-Grid-Styling | `frontend/src/views/HomeView.css` |
| Kachel-Komponente | `frontend/src/components/Tile.tsx` |
| Kachel-Komponenten-Styling | `frontend/src/components/Tile.css` |
| Detailseite | `frontend/src/views/DetailView.tsx` |
| Detailseiten-Styling | `frontend/src/views/DetailView.css` |

## Kacheln (aktueller Stand)

| ID | Name | Beschreibung |
|---|---|---|
| `new-ruleset` | Neues Regelwerk | Ein neues Regelwerk erstellen |
| `load-ruleset` | Regelwerk laden | ein bestehendes Regelwerk laden |

## Rekonstruktion

```
Erstelle eine HomeView mit einer Kachel-Übersicht (responsive Grid, min 260px).
Kacheldaten als Array { id, name, description } in der Komponente.
Tile-Komponente: klickbarer Button, navigiert zu /tile/{id}, zeigt Name + Beschreibung.
Hover-Effekt: translateY(-2px), stärkerer Schatten.
DetailView: liest id aus URL-Params, zeigt Name aus statischer Map, Zurück-Button.
```
