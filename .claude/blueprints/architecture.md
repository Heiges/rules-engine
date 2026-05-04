# Architektur-Blaupause

Einstiegspunkt für alle Blaupausen und Spezifikationen. Von hier aus navigierst du gezielt zu der Schicht oder Domäne, die du brauchst.

## Architekturstil

Modulares Backend mit strikter Abhängigkeitsrichtung + React-Frontend, das ausschließlich über eine REST-API kommuniziert. Das Domänenmodell ist framework-frei und bildet die Basis aller anderen Schichten.

## Generische Modulstruktur

| Rolle | Beschreibung |
|-------|--------------|
| **Domain** | Framework-freies Domänenmodell; keine Abhängigkeit nach außen |
| **Fachlogik** | Fachlogik auf Basis des Domänenmodells (optional, kann entfallen) |
| **Persistence** | Datenhaltung; hängt nur vom Domänenmodell ab |
| **API** | REST-Schicht; verbindet alle Backend-Module, stellt Endpunkte bereit |
| **Frontend** | React-UI; kommuniziert ausschließlich über die REST-API |

Jedes Projekt belegt diese Rollen mit konkreten Modulen. Die Rollen selbst und ihre Abhängigkeitsrichtung sind unveränderlich.

## Abhängigkeitsrichtung

```
API → Persistence → Domain
API → Fachlogik  → Domain
Frontend → API (REST)
```

Abhängigkeiten nur nach unten. Das Domänenmodell kennt keine andere Schicht.

## Integrationspunkte (Muster)

- **Persistenz**: eine Datei oder ein Datensatz pro Entität; Speicherort konfigurierbar
- **REST-API**: Endpunkte unter `/api/`; CRUD + domänenspezifische Operationen
- **CORS**: Frontend-Origin explizit freigegeben für alle `/api/**`-Endpunkte

## Entscheidungen (projektübergreifend)

- **Domänenmodell bleibt annotation-frei** — technische Annotationen (JAXB, Spring) nur in den jeweiligen Schichten
- **API-DTOs getrennt vom Domänenmodell** — kein direktes Exponieren der Domänenobjekte
- **CORS-Konfiguration in eigener Klasse** — nicht inline im Application-Bootstrap

## Navigation zu den Blaupausen

### Schicht-Blaupausen (`blueprints/layers/`)

| Blaupause | Rolle | Wann lesen |
|-----------|-------|------------|
| [domain.md](layers/domain.md) | Domain | Neue Domänenklasse anlegen |
| [persistence.md](layers/persistence.md) | Persistence | Domänenobjekt persistieren |
| [views.md](layers/views.md) | Frontend | Neue View anlegen |

### Komponenten-Blaupausen (`blueprints/components/`)

| Blaupause | Wann lesen |
|-----------|------------|
| [navigation.md](components/navigation.md) | Navigationsstruktur, Kacheln, Sichtbarkeitslogik |
| [tile.md](components/tile.md) | Tile-Komponente im Detail |

## Konkrete Architektur

Die projektspezifische Ausprägung dieser Blaupause — welche Module, Pfade und Integrationspunkte konkret existieren — ist in den App-Spezifikationen beschrieben: [appspecs/](../appspecs/README.md)
