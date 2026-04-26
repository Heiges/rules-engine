# Projektstruktur

Dieses Dokument beschreibt die Modulstruktur der Rules Engine und ist Ausgangspunkt für alle Feature-Specs. Jedes Feature verweist auf die hier definierten Module.

## Module

### `coreElements/`

Domänenmodell — enthält ausschließlich fachliche Klassen ohne Framework-Abhängigkeiten.

```
coreElements/
  src/main/java/de/heiges/rulesengine/coreelements/domain/model/   ← Domain-Klassen
  src/test/java/de/heiges/rulesengine/coreelements/domain/model/   ← Unit-Tests
```

- Keine Abhängigkeit zu `persistence` oder Frontend
- Keine Annotations (JAXB, Jakarta, etc.)
- Build: `cd coreElements && mvn test`

### `coreMechanics/`

Kernmechaniken — spielmechanische Logik (Proben, Würfelwürfe, Modifikatoren etc.); hängt von `coreElements` ab.

```
coreMechanics/
  src/main/java/de/heiges/rulesengine/coremechanics/domain/model/   ← Mechanik-Klassen
  src/test/java/de/heiges/rulesengine/coremechanics/domain/model/   ← Unit-Tests
```

- Keine Annotation (JAXB, Jakarta etc.)
- Abhängigkeit: `coreMechanics` → `coreElements` (nur in diese Richtung)
- Build (nach `coreElements install`): `cd coreMechanics && mvn test`

### `persistence/`

XML-Persistenz via JAXB — hängt von `coreElements` ab.

```
persistence/
  src/main/java/de/heiges/rulesengine/persistence/
    xml/dto/            ← JAXB-annotierte DTOs (ein DTO pro Domain-Klasse)
    repository/         ← Repository-Interfaces
    xml/                ← XML-Implementierungen (XmlXxxRepository)
  src/test/java/de/heiges/rulesengine/persistence/xml/   ← Integrationstests
```

- Abhängigkeit: `persistence` → `coreElements` (nur in diese Richtung)
- JAXB-Annotationen ausschließlich in DTOs, nie in Domain-Klassen
- Build (nach `coreElements install`): `cd persistence && mvn test`

### `api/`

Spring Boot REST API — verbindet Frontend und Persistenzschicht.

```
api/
  src/main/java/de/heiges/rulesengine/api/
    ApiApplication.java          ← Startklasse + CORS-Konfiguration
    controller/
      RulesetController.java     ← REST-Endpunkte /api/rulesets
    dto/
      RulesetApiDto.java         ← JSON-Transferobjekt (records)
      ValueRangeApiDto.java
      AttributeApiDto.java
      SkillApiDto.java
  src/main/resources/
    application.properties       ← server.port=8080
```

- Abhängigkeiten: `api` → `persistence` → `coreElements`
- Starten: `cd api && mvn spring-boot:run` (Port 8080)
- Build: `cd api && mvn package`

### `frontend/`

React-UI — kommuniziert über `/api` mit dem Spring Boot Backend.

```
frontend/
  src/
    api.ts           ← Typen (RulesetData, Attribute, …) + fetch-Funktionen
    components/      ← Wiederverwendbare UI-Komponenten
    context/         ← RulesetContext (currentRuleset, rulesetData)
    views/           ← Seiten (HomeView, Detail-Views)
  dist/              ← Build-Artefakt (nach npm run build)
```

- Stack: React 19 + TypeScript + Vite
- Dev-Server: `cd frontend && npm run dev` (Port 5173)
- Vite-Proxy leitet `/api` an `http://localhost:8080` weiter
- Kein XML im Frontend; kein File System Access API

## Abhängigkeitsrichtung

```
frontend  →  /api (HTTP)  →  api  →  persistence  →  coreElements
                                      coreMechanics  →  coreElements
```

Abhängigkeiten zeigen immer nach unten / innen. Umgekehrte Abhängigkeiten sind verboten.

## Namenskonventionen

| Artefakt | Muster | Beispiel |
|---|---|---|
| Domain-Klasse | `<Name>.java` | `Attribute.java` |
| Unit-Test | `<Name>Test.java` | `AttributeTest.java` |
| Persistenz-DTO | `<Name>Dto.java` | `AttributeDto.java` |
| Repository-Interface | `<Name>Repository.java` | `AttributeRepository.java` |
| Repository-Impl | `Xml<Name>Repository.java` | `XmlAttributeRepository.java` |
| Persistenz-Test | `Xml<Name>RepositoryTest.java` | `XmlAttributeRepositoryTest.java` |

## Bestehende Features

### Domain (`domain/`)

- [coreMechanics-Modul](domain/coremechanics-modul.md) — Maven-Modul-Gerüst für Kernmechaniken (noch ohne Domänenklassen)
- [Attribute](domain/attribute.md) — benannte, messbare Eigenschaft mit Value
- [AttributeSet](domain/attribute-set.md) — geordnete Sammlung von Attributen
- [Skill](domain/skill.md) — Fähigkeit mit Attribut-Bindung und Level
- [Value](domain/value-domain-class.md) — numerischer Wert eines Regelbausteins (pos./neg.)

### Persistenz (`persistence/`)

- [XML-Persistenz](persistence/persistenz-xml.md) — Regelwerk (AttributeSet + Skills) als einzelne XML-Datei speichern/laden (JAXB)
- [Datenspeicher](persistence/datenspeicher.md) — Fester Speicherort `~/.rules-engine/data/`, Regelwerke per Dateiname identifizierbar

### API (`api/`)

- [Spring Boot REST API](api/rest-api.md) — REST-Endpunkte für Regelwerk laden/speichern/anlegen; JSON als Transportformat

### Frontend (`frontend/`)

- [Anwendungsrahmen](frontend/frontend-anwendungsrahmen.md) — Entry Point, Routing, globales Theming
- [Kacheln](frontend/frontend-kacheln.md) — Kachel-Navigation (HomeView, Tile-Komponente, DetailView)
- [Statuszeile](frontend/statusbar-aktuelles-regelwerk.md) — Statuszeile am unteren Rand mit aktuellem Regelwerk-Namen (React Context)
- [Regelwerk-Auswahl über API](frontend/regelwerk-laden-xml.md) — HomeView listet alle Regelwerke per API-Call; Klick öffnet Regelwerk direkt ohne Dateidialog
- [Neues Regelwerk erstellen](frontend/neues-regelwerk-erstellen.md) — Name per prompt → POST /api/rulesets/{name} → direkt öffnen
- [Edit-Regelwerk-Kachel](frontend/edit-ruleset-tile.md) — Kachel in HomeView navigiert zu /edit-ruleset
- [Edit-Regelwerk-View](frontend/edit-ruleset-view.md) — Bearbeitungsansicht unter /edit-ruleset mit Attributanzahl aus rulesetData
- [Attribute-View (CRUD)](frontend/attribute-view.md) — Liste, Umbenennen, Löschen, Anlegen von Attributen; persistiert via PUT /api/rulesets/{name}
- [Wertebereich](frontend/wertebereich.md) — Bearbeiten von min, average, max; persistiert via PUT /api/rulesets/{name}
