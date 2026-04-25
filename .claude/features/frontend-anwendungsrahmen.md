# Frontend-Anwendungsrahmen

## Ziel

Grundgerüst der React-Anwendung: Entry Point, Routing, globales Theming. Gibt der Anwendung ihre Struktur und definiert, wie neue Seiten eingehängt werden.

## Anforderungen

- Single-Page-App mit clientseitigem Routing (React Router)
- Zwei Routen: `/` (HomeView) und `/tile/:id` (DetailView)
- Globales Theming über CSS Custom Properties — automatische Umschaltung Light/Dark via `prefers-color-scheme`
- Farbpalette: Lila-Akzent (`#aa3bff` light / `#c084fc` dark), neutrale Text- und Hintergrundfarben
- Responsive Basis: 18px Schriftgröße, 16px auf Mobile (max-width 1024px), max-width 1126px zentriert

## Entscheidungen

- React Router statt Next.js o. ä.: keine Server-Komponenten nötig, reine Client-App
- CSS Custom Properties statt Styling-Library: kein Framework-Lock-in, vollständige Kontrolle über Designtoken
- `prefers-color-scheme` statt manuellem Toggle: reduziert Komplexität, respektiert Systemeinstellung

## Implementierung

| Artefakt | Pfad |
|---|---|
| Entry Point | `frontend/src/main.tsx` |
| Router + Layout | `frontend/src/App.tsx` |
| Globales Theming | `frontend/src/index.css` |
| App-CSS (Vorlage, größtenteils ungenutzt) | `frontend/src/App.css` |

## Rekonstruktion

```
Erstelle das Grundgerüst einer React 19 + TypeScript + Vite App mit React Router.
Routen: / → HomeView, /tile/:id → DetailView.
Globales Theming via CSS Custom Properties mit Light/Dark-Unterstützung,
Lila-Akzent (#aa3bff / #c084fc), max-width 1126px, responsive ab 1024px.
```
