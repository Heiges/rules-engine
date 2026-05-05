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
| Navigation | Frontend | [navigation/breadcrumb.md](navigation/breadcrumb.md) |
| Werte | Domain | [werte/domain.md](werte/domain.md) |
| Werte | Frontend | [werte/view.md](werte/view.md) |
| Attribute | Domain | [attribute/domain.md](attribute/domain.md) |
| Attribute | Frontend | [attribute/view.md](attribute/view.md) |
| Skills | Domain | [skill/domain.md](skill/domain.md) |
| Skills | Frontend | [skill/view.md](skill/view.md) |
| Cheats | Domain | [cheat/domain.md](cheat/domain.md) |
| Cheats | Frontend | [cheat/view.md](cheat/view.md) |

## Referenzen

- [blueprints/](../blueprints/README.md) — Blaupausen für Muster und Bauregeln
