# Blaupausen

Dieses Verzeichnis enthält wiederverwendbare Blaupausen für den Entwicklungsprozess.

## Wozu dienen diese Blaupausen?

Neue Features werden **blueprint-first** entwickelt: Zuerst entsteht eine Spezifikation, dann die Implementierung. Das hat zwei Vorteile:

1. **Weniger Codebase-Analyse** — Claude liest die passenden Blaupausen statt die gesamte Codebasis zu scannen.
2. **Explizite Entscheidungen** — Anforderungen und Designentscheidungen sind vor der Implementierung geklärt, nicht danach dokumentiert.

## Struktur

```
blueprints/
  layers/       Schicht-Blaupausen: Muster und Regeln pro technischer Schicht
  components/   Komponenten-Blaupausen: wiederverwendbare UI-Primitive
```

### `layers/`

Beschreibt **wie** man in einer Schicht arbeitet — unabhängig von konkreten Fachklassen.
Eine Layer-Spec enthält Klassenkategorien, Pflichtmuster, Verbote und Ablageorte.

| Datei | Schicht |
|-------|---------|
| `domain.md` | Domänenmodell (`coreElements`) |
| `persistence.md` | XML-Persistenz (`persistence`) |
| `views.md` | Frontend-Views (`frontend/src/views/`) |

### `components/`

Beschreibt **wie** eine UI-Komponente gebaut ist — Props, Verhalten, Rendering, Styling.
Eine Komponenten-Spec ist so vollständig, dass die Komponente ohne Blick in den Code neu gebaut werden kann.

| Datei | Komponente |
|-------|------------|
| `tile.md` | `Tile` — Navigationskachel |
| `navigation.md` | Navigationssystem — Kachel-Baum, Routen, Sichtbarkeitslogik |

