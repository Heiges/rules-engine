# Architekturvorgaben

## Architekturstil

<!-- z.B. Hexagonale Architektur, Clean Architecture, Schichtenmodell -->

## Modulstruktur

| Modul            | Verzeichnis      | Beschreibung                                              |
|------------------|------------------|-----------------------------------------------------------|
| `coreElements`   | `coreElements/`  | Domänenmodell (Attribute, AttributeSet, Skill)            |
| `coreMechanics`  | `coreMechanics/` | Kernmechaniken (Würfelwürfe, Proben, Modifikatoren etc.)  |
| `persistence`    | `persistence/`   | XML-Persistenz der Domänenklassen (JAXB)                  |
| Frontend         | `frontend/`      | React-UI, kommuniziert künftig über eine API mit dem Backend |

Abhängigkeiten: `persistence` → `coreElements`; `coreMechanics` → `coreElements`. Das Frontend kennt keine Java-Module.

## Abhängigkeitsrichtung

Abhängigkeiten nur nach unten: `persistence` hängt von `coreElements` ab, nicht umgekehrt. Das Domänenmodell in `coreElements` hat keine Abhängigkeit zur Persistenz oder zur UI.

## Integrationspunkte

- Persistenz: XML-Dateien über `XmlAttributeSetRepository` und `XmlSkillRepository`
- Speicherort der XML-Dateien: wird vom Aufrufer per `Path` übergeben (noch nicht standardisiert)

## Entscheidungen (ADRs)

- **React Router** (`react-router-dom`) für Navigation im Frontend
- **Java-Paket**: `de.heiges.rulesengine`
- **Domänenmodell bleibt annotation-frei** — JAXB-Annotationen nur in DTOs im `persistence`-Modul
- **XML-Persistenz statt Datenbank** — Domänenklassen werden als XML gespeichert/geladen (JAXB 4.x / Jakarta)
