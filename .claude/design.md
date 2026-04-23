# Designrichtlinien

## Coding-Style

<!-- Formatierung, Benennung, Kommentarregeln -->

## Domain-Modell

<!-- Wichtige Entitäten, Aggregate, Value Objects -->
Attribute
Skills

## Muster & Konventionen

- **Frontend-Struktur**: Komponenten unter `frontend/src/components/`, Views unter `frontend/src/views/`
- **Navigation**: Kacheln auf der Startseite (`HomeView`) navigieren per React Router zu Detail-Views (`/tile/:id`)
- **Kacheln (Tiles)**: Name + Kurzbeschreibung; Kacheldaten als Array in `HomeView.tsx` definiert
- **Kachel-Styling**: Leicht abgehobener Hintergrund (`#edeaf3` light / `#1e1f27` dark), dezenter grauer Rahmen, subtiler Schatten, Hover-Effekt mit stärkerem Schatten und `translateY(-2px)`

## Was zu vermeiden ist

<!-- Anti-Patterns, die in diesem Projekt nicht verwendet werden sollen -->

## Beispiele

<!-- Referenzimplementierungen auf die Claude verweisen soll -->
