# Frontend-Patterns

## Struktur

- Komponenten unter `frontend/src/components/`
- Views unter `frontend/src/views/`
- Hooks unter `frontend/src/hooks/`

## API-Kommunikation

Alle Backend-Aufrufe laufen ausschließlich über `frontend/src/api.ts`. Views rufen nie direkt `fetch` auf.

## Globaler Zustand

`RulesetContext` (`frontend/src/context/RulesetContext.tsx`) hält:

| Feld | Typ | Beschreibung |
|------|-----|-------------|
| `currentRuleset` | `string \| null` | Name des geladenen Regelwerks |
| `rulesetData` | `RulesetData \| null` | Vollständige Regelwerk-Daten |
| `fileHandle` | `FileSystemFileHandle \| null` | Datei-Handle (nur wenn File System Access API verfügbar) |
| `role` | `'spielleiter' \| 'spieler' \| null` | Gewählte Benutzerrolle |

Abgeleitete oder geteilte Daten gehören in den Context, nicht in lokale View-States.

## Kachel-Styling

Leicht abgehobener Hintergrund (`#edeaf3` light / `#1e1f27` dark), dezenter grauer Rahmen, subtiler Schatten, Hover-Effekt mit stärkerem Schatten und `translateY(-2px)`.

## Referenzen

- [blueprints/components/tile.md](../blueprints/components/tile.md) — Tile-Komponente
- [blueprints/components/navigation.md](../blueprints/components/navigation.md) — Navigationssystem
