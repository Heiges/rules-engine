# Komponenten-Spec: Breadcrumb

## Zweck

Zeigt den aktuellen Navigationspfad als klickbare Segmentreihe. Ersetzt den `← Zurück`-Button in Views — jedes Segment außer dem letzten ist klickbar und navigiert zur entsprechenden Route.

## Konzept

Die Komponente liest die aktuelle URL via `useLocation()` und baut daraus eine geordnete Liste von `{ label, path }`-Einträgen. Labels werden in zwei Stufen aufgelöst:

1. **Statische Auflösung** — feste Route-Segmente werden über eine Map auf einen lesbaren Namen abgebildet.
2. **Dynamische Auflösung** — variable Segmente (z. B. `:index`, `:id`) werden über einen injizierten Resolver auf einen lesbaren Namen abgebildet.

## Hook: `useBreadcrumbs`

Kapselt die Pfad-Auflösung. Nimmt eine statische Label-Map und einen optionalen Resolver entgegen, gibt ein Array von Einträgen zurück:

```ts
interface BreadcrumbEntry {
  label: string
  path: string        // vollständiger Pfad bis zu diesem Segment
  isLast: boolean     // letztes Segment = aktuell aktiv
}

type LabelMap = Record<string, string>

type DynamicResolver = (segment: string, fullPath: string) => string | null

function useBreadcrumbs(labels: LabelMap, resolve?: DynamicResolver): BreadcrumbEntry[]
```

Der Hook liegt in `frontend/src/hooks/useBreadcrumbs.ts`.

## Interface (Props)

```ts
interface BreadcrumbProps {
  labels: LabelMap
  resolve?: DynamicResolver
}
```

Label-Map und Resolver werden von außen übergeben — die Komponente selbst ist anwendungsunabhängig.

## JSX-Struktur

```tsx
<nav className="breadcrumb">
  {entries.map((entry, i) => (
    <span key={entry.path}>
      {i > 0 && <span className="breadcrumb-separator">›</span>}
      {entry.isLast
        ? <span className="breadcrumb-current">{entry.label}</span>
        : <button className="breadcrumb-link" onClick={() => navigate(entry.path)}>{entry.label}</button>
      }
    </span>
  ))}
</nav>
```

## Styling (`Breadcrumb.css`)

| Element | Beschreibung |
|---------|-------------|
| `.breadcrumb` | Horizontale Reihe, `display: flex`, `gap`, kleine Schriftgröße, gedämpfte Farbe |
| `.breadcrumb-separator` | Trennzeichen `›`, nicht klickbar, gedämpfter als Labels |
| `.breadcrumb-link` | Klickbares Segment; Hover: Unterstreichung, volle Textfarbe |
| `.breadcrumb-current` | Letztes Segment; volle Textfarbe, nicht klickbar |

## Integration

- Die Komponente wird **einmalig** in einem gemeinsamen Layout-Bereich eingebunden.
- Der `← Zurück`-Button wird in allen Views **entfernt**.
- Auf der Einstiegsseite ohne übergeordneten Pfad wird kein Breadcrumb angezeigt.

## Dateien

| Artefakt | Pfad |
|----------|------|
| Komponente | `frontend/src/components/Breadcrumb.tsx` |
| Styling | `frontend/src/components/Breadcrumb.css` |
| Hook | `frontend/src/hooks/useBreadcrumbs.ts` |

## Referenzen

- [navigation.md](navigation.md) — Kachel-Baum und Routen-Konventionen
