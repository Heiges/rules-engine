# App-Spezifikationen

Dieses Verzeichnis enthält konkrete Spezifikationen der Anwendung — eine pro fachlicher Domäne.

## Wozu dienen diese Specs?

Jede Spec beschreibt eine konkrete Domäne: welche Klassen es gibt, wie sie gespeichert werden, welche Views und Kacheln existieren. Sie sind die Grundlage für die Implementierung und referenzieren für das *Wie* die passenden Blaupausen in `blueprints/`.

## Struktur

Jede Domäne bekommt einen eigenen Unterordner. Innerhalb des Ordners gibt es je nach Bedarf Specs pro Schicht:

```
<domäne>/
  domain.md        ← Domänenklassen
  persistence.md   ← Persistierung
  view.md          ← Views und Kacheln
```

Nicht jede Domäne braucht alle Schichten — nur was tatsächlich existiert wird beschrieben.

## Bestehende Specs

| Domäne | Schicht | Datei |
|--------|---------|-------|
| Navigation | Frontend | [navigation/tile-navigation.md](navigation/tile-navigation.md) |

## Referenzen

- [blueprints/](../blueprints/README.md) — Blaupausen für Muster und Bauregeln
