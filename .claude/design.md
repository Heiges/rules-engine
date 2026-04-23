# Designrichtlinien

## Coding-Style

<!-- Formatierung, Benennung, Kommentarregeln -->

## Domain-Modell

Alle Domänenklassen liegen in `coreElements/src/main/java/de/heiges/rulesengine/coreelements/domain/model/`.

- **Attribute** — Name (unveränderlich) + Wert (int ≥ 0)
- **AttributeSet** — geordnete Sammlung von Attributen; kein Duplikat-Name erlaubt
- **Skill** — Name (unveränderlich) + Verweis auf ein Attribut + Level (int ≥ 0)

## Muster & Konventionen

- **Frontend-Struktur**: Komponenten unter `frontend/src/components/`, Views unter `frontend/src/views/`
- **Navigation**: Kacheln auf der Startseite (`HomeView`) navigieren per React Router zu Detail-Views (`/tile/:id`)
- **Kacheln (Tiles)**: Name + Kurzbeschreibung; Kacheldaten als Array in `HomeView.tsx` definiert
- **Kachel-Styling**: Leicht abgehobener Hintergrund (`#edeaf3` light / `#1e1f27` dark), dezenter grauer Rahmen, subtiler Schatten, Hover-Effekt mit stärkerem Schatten und `translateY(-2px)`

## Was zu vermeiden ist

<!-- Anti-Patterns, die in diesem Projekt nicht verwendet werden sollen -->

## Beispiele

<!-- Referenzimplementierungen auf die Claude verweisen soll -->
