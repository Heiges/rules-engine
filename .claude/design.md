# Designrichtlinien

## Coding-Style

- Java 21, keine Wildcardimporte
- Records für reine Datenhaltung (`Value`, `ValueRange`)
- Domänenklassen ohne Framework-Annotationen (kein JAXB, kein Spring)
- Keine Kommentare außer wenn das *Warum* nicht offensichtlich ist

## Domain-Modell

Alle Domänenklassen liegen in `coreElements/src/main/java/de/heiges/rulesengine/coreelements/domain/model/`.

- **Value** — Record; kapselt einen `int`-Betrag
- **ValueRange** — Record; min ≤ average ≤ max (wird beim Konstruktor geprüft)
- **Attribute** — Name (unveränderlich) + Beschreibung + `Value`
- **AttributeSet** — geordnete Sammlung von Attributen; kein Duplikat-Name erlaubt
- **Skill** — Name (unveränderlich) + Verweis auf ein Attribut + Level (int ≥ 0)

## API-DTOs

DTOs liegen in `api/src/main/java/de/heiges/rulesengine/api/dto/` und werden ausschließlich für die REST-Kommunikation genutzt. Domänenobjekte werden nie direkt serialisiert.

| DTO                 | Zweck                                      |
|---------------------|--------------------------------------------|
| `RulesetApiDto`     | Vollständiges Regelwerk (ValueRange + Attribute + Skills) |
| `AttributeApiDto`   | Einzelnes Attribut                         |
| `SkillApiDto`       | Einzelner Skill mit `linkedAttributeName`  |
| `ValueRangeApiDto`  | Wertebereich (min / average / max)         |
| `RollRequestDto`    | Würfelanfrage (`value`)                    |
| `RollResultApiDto`  | Würfelergebnis (dice[], success, pasch)    |

## Muster & Konventionen

- **Frontend-Struktur**: Komponenten unter `frontend/src/components/`, Views unter `frontend/src/views/`
- **API-Kommunikation**: zentralisiert in `frontend/src/api.ts`; Views rufen ausschließlich darüber das Backend auf
- **Globaler Zustand**: `RulesetContext` (`frontend/src/context/RulesetContext.tsx`) hält `currentRuleset`, `rulesetData` und `fileHandle`; abgeleitete/geteilte Daten gehören in den Context, nicht in lokale View-States
- **Navigation**: Kacheln auf der Startseite (`HomeView`) navigieren per React Router zu Detail-Views
- **Kacheln (Tiles)**: Name + Kurzbeschreibung; als `<Tile>`-Komponente in `HomeView.tsx` zusammengesetzt
- **Kachel-Styling**: Leicht abgehobener Hintergrund (`#edeaf3` light / `#1e1f27` dark), dezenter grauer Rahmen, subtiler Schatten, Hover-Effekt mit stärkerem Schatten und `translateY(-2px)`

## Was zu vermeiden ist

- JAXB- oder Spring-Annotationen im Domänenmodell (`coreElements`)
- Direkte `fetch`-Aufrufe in Views — immer über `api.ts`
- Geteilten Zustand in lokalen View-States statt im `RulesetContext`

## Beispiele

- API-Aufruf: `saveRuleset` / `importRuleset` in `frontend/src/api.ts`
- Domänenklasse: `ValueRange` als kompaktes Record mit Validierung im Compact Constructor
- Controller: `RulesetController` als Referenz für REST-Endpunkte mit DTO-Mapping
