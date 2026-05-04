# Komponenten-Spec: Navigationssystem

## Konzept

Die Anwendung ist als Kachel-Baum organisiert. Jede View kann ein Kachel-Raster zeigen; ein Klick auf eine Kachel navigiert zur nächsten View. Die Navigationstiefe ist nicht festgelegt — eine Kachelview kann zu einer weiteren Kachelview führen, bevor eine Detailseite erreicht wird.

```
HomeView  (/)
├── KachelView
│   ├── KachelView          ← weitere Ebene möglich
│   │   └── DetailView
│   └── DetailView
│       └── EinzelView
└── ...
```

## Routen-Konvention

| Ebene | Muster | Beispiel |
|-------|--------|---------|
| Home | `/` | `/` |
| Kachelbereich | `/<bereich>` | `/edit-ruleset` |
| Listenansicht | `/tile/<name>` | `/tile/attributes` |
| Detailansicht | `/tile/<name>/:index` | `/tile/attributes/3` |
| Weltkontext | `/world/<name>` | `/world/attributes` |

Die `Tile`-Komponente navigiert standardmäßig zu `/tile/${id}`. Views, die einen anderen Pfad brauchen, übergeben einen expliziten `onClick`-Handler.

## Tile-Definition (deklaratives Muster)

Kacheln werden nicht direkt als JSX gestreut, sondern als typisiertes Array definiert. Die View rendert nur, was sichtbar ist.

```tsx
interface TileDefinition {
  id: string
  name: string
  description: string
  path?: string           // Navigate-Ziel; wenn gesetzt, wird onClick generiert
  onClick?: () => void    // Überschreibt path vollständig
  visible: () => boolean  // Sichtbarkeitsbedingung; () => true wenn immer sichtbar
}
```

### Verwendung in einer View

```tsx
const tiles: TileDefinition[] = [
  {
    id: 'immer-sichtbar',
    name: 'Immer da',
    description: '...',
    path: '/ziel',
    visible: () => true,
  },
  {
    id: 'bedingt',
    name: 'Nur wenn geladen',
    description: '...',
    path: '/anderes-ziel',
    visible: () => !!currentRuleset,
  },
]

return (
  <div className="tile-grid">
    {tiles
      .filter(t => t.visible())
      .map(t => (
        <Tile
          key={t.id}
          id={t.id}
          name={t.name}
          description={t.description}
          onClick={t.onClick ?? (t.path ? () => navigate(t.path!) : undefined)}
        />
      ))}
  </div>
)
```

## Sichtbarkeits-Bedingungen

`visible` ist ein boolesches Prädikat ohne Argumente. Es kann jeden im View-Scope verfügbaren Zustand auswerten — Context, lokalen State, abgeleitete Werte oder Kombinationen davon.

```tsx
visible: () => true                          // immer sichtbar
visible: () => bedingung                     // einfache Bedingung
visible: () => bedingungA && bedingungB      // kombinierte Bedingungen
```

Die Bedingung gehört in die `TileDefinition`, nicht ins JSX der View.

## Zurück-Navigation

Jede View unterhalb der HomeView hat einen Zurück-Button:

```tsx
<button className="back-button" onClick={() => navigate('/übergeordneter-pfad')}>← Zurück</button>
```

Der Zielpfad des Zurück-Buttons entspricht immer der übergeordneten Ebene im Navigationsbaum.

## Dateien

| Artefakt | Pfad |
|----------|------|
| Routen-Definition | `frontend/src/App.tsx` |
| Tile-Komponente | `frontend/src/components/Tile.tsx` |
| Tile-Styling | `frontend/src/components/Tile.css` |
| Kachel-Grid-Styling | `frontend/src/views/HomeView.css` |

## Referenzen

- [tile.md](tile.md) — Tile-Komponente im Detail
- [views.md](../layers/views.md) — View-Aufbau und Persist-Muster
