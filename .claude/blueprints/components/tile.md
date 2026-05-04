# Komponenten-Spec: Tile

## Zweck

Wiederverwendbare Navigationskachel. Zeigt einen Funktionsbereich mit Name und Kurzbeschreibung; navigiert per Klick zur zugehörigen Seite.

## Interface (Props)

| Prop          | Typ        | Pflicht | Beschreibung |
|---------------|------------|---------|--------------|
| `id`          | `string`   | ja      | Eindeutige Kennung; wird als URL-Segment verwendet (`/tile/:id`) |
| `name`        | `string`   | ja      | Angezeigter Titel der Kachel (`<h2>`) |
| `description` | `string`   | ja      | Kurzbeschreibung unter dem Titel (`<p>`) |
| `onClick`     | `() => void` | nein  | Überschreibt die Standard-Navigation vollständig |

## Verhalten

- **Standard:** Klick navigiert zu `/tile/${id}` via `useNavigate()` (React Router).
- **Override:** Ist `onClick` gesetzt, ersetzt es die Navigation vollständig — kein `navigate`-Aufruf.

## JSX-Struktur

```tsx
<button className="tile" onClick={onClick ?? (() => navigate(`/tile/${id}`))}>
  <h2>{name}</h2>
  <p>{description}</p>
</button>
```

## Styling (`Tile.css`)

| Eigenschaft        | Light-Mode    | Dark-Mode     |
|--------------------|---------------|---------------|
| Hintergrund        | `#edeaf3`     | `#1e1f27`     |
| Rahmen             | dezenter grauer `border` | dezenter grauer `border` |
| Schatten           | subtiler `box-shadow` | subtiler `box-shadow` |
| Hover-Transform    | `translateY(-2px)` | `translateY(-2px)` |
| Hover-Schatten     | stärker als Ruhezustand | stärker als Ruhezustand |

Cursor: `pointer`. Transition auf `transform` und `box-shadow`.

## Dateien

| Artefakt   | Pfad |
|------------|------|
| Komponente | `frontend/src/components/Tile.tsx` |
| Styling    | `frontend/src/components/Tile.css` |

## Verwendung (Beispiel)

```tsx
import { Tile } from '../components/Tile'

// Standard-Navigation zu /tile/mein-bereich
<Tile id="mein-bereich" name="Mein Bereich" description="Kurzbeschreibung" />

// Benutzerdefinierter Klick-Handler
<Tile id="laden" name="Laden" description="Datei öffnen" onClick={handleLoad} />
```

## Einschränkungen / Nicht-Ziele

- Keine Icons, keine Badges, kein Ladezustand — die Komponente bleibt bewusst schlank.
- Sichtbarkeitssteuerung (z. B. nur wenn Regelwerk geladen) liegt beim Aufrufer, nicht in `Tile`.
