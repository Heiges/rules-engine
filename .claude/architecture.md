# Architekturvorgaben

## Architekturstil

<!-- z.B. Hexagonale Architektur, Clean Architecture, Schichtenmodell -->

## Modulstruktur

| Modul            | Verzeichnis      | Beschreibung                                                               |
|------------------|------------------|----------------------------------------------------------------------------|
| `coreElements`   | `coreElements/`  | Domänenmodell (Attribute, AttributeSet, Skill, ValueRange)                 |
| `coreMechanics`  | `coreMechanics/` | Kernmechaniken (Würfelwürfe, Proben, Modifikatoren etc.)                   |
| `persistence`    | `persistence/`   | XML-Persistenz der Domänenklassen (JAXB)                                   |
| `api`            | `api/`           | Spring Boot REST-API; verbindet alle Java-Module und stellt Endpunkte bereit |
| Frontend         | `frontend/`      | React-UI, kommuniziert über die REST-API mit dem Backend                   |

Abhängigkeiten: `api` → `persistence`, `coreMechanics`, `coreElements`; `persistence` → `coreElements`; `coreMechanics` → `coreElements`. Das Frontend kennt keine Java-Module.

## Abhängigkeitsrichtung

Abhängigkeiten nur nach unten: `persistence` hängt von `coreElements` ab, nicht umgekehrt. Das Domänenmodell in `coreElements` hat keine Abhängigkeit zur Persistenz oder zur UI.

## Integrationspunkte

- Persistenz: XML-Dateien über `XmlRulesetRepository`; Speicherort `~/.rules-engine/data/` via `DataDirectory`
- REST-API (Port 8080): Endpunkte unter `/api/rulesets` (CRUD + Import/Export) und `/api/roll`
- CORS: Frontend (`http://localhost:5173`) ist für alle `/api/**`-Endpunkte freigegeben (`WebConfig`)

## Entscheidungen (ADRs)

- **React Router** (`react-router-dom`) für Navigation im Frontend
- **Java-Paket**: `de.heiges.rulesengine`
- **Domänenmodell bleibt annotation-frei** — JAXB-Annotationen nur in DTOs im `persistence`-Modul
- **XML-Persistenz statt Datenbank** — Domänenklassen werden als XML gespeichert/geladen (JAXB 4.x / Jakarta)
- **Spring Boot** (`spring-boot-starter-web`) als REST-Framework im `api`-Modul
- **API-DTOs getrennt vom Domänenmodell** — eigene DTO-Klassen im `api`-Modul; kein direktes Exponieren der Domänenobjekte
- **CORS-Konfiguration in eigener Klasse** — `WebConfig` statt inline in `ApiApplication`
