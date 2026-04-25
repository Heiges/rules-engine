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

### `frontend/`

React-UI — kennt keine Java-Module, kommuniziert künftig über eine API.

```
frontend/
  src/
    components/   ← Wiederverwendbare UI-Komponenten
    views/        ← Seiten (HomeView, Detail-Views)
  dist/           ← Build-Artefakt (nach npm run build)
```

- Stack: React 19 + TypeScript + Vite
- Dev-Server: `cd frontend && npm run dev` (Port 5173)
- Keine direkte Abhängigkeit zu Java-Modulen

## Abhängigkeitsrichtung

```
frontend  (keine Java-Abhängigkeit)
    ↕  (künftig über API)
persistence  →  coreElements
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

- [Attribute](domain/attribute.md) — benannte, messbare Eigenschaft
- [AttributeSet](domain/attribute-set.md) — geordnete Sammlung von Attributen
- [Skill](domain/skill.md) — Fähigkeit mit Attribut-Bindung und Level

### Persistenz (`persistence/`)

- [XML-Persistenz](persistence/persistenz-xml.md) — Regelwerk (AttributeSet + Skills) als einzelne XML-Datei speichern/laden (JAXB)
- [Datenspeicher](persistence/datenspeicher.md) — Fester Speicherort `~/.rules-engine/data/`, Regelwerke per Dateiname identifizierbar

### Frontend (`frontend/`)

- [Anwendungsrahmen](frontend/frontend-anwendungsrahmen.md) — Entry Point, Routing, globales Theming
- [Kacheln](frontend/frontend-kacheln.md) — Kachel-Navigation (HomeView, Tile-Komponente, DetailView)
- [Statuszeile](frontend/statusbar-aktuelles-regelwerk.md) — Statuszeile am unteren Rand mit aktuellem Regelwerk-Namen (React Context)
- [Regelwerk laden (XML-Dateiauswahl)](frontend/regelwerk-laden-xml.md) — nativer Dateidialog über Kachel, setzt Dateinamen in Statuszeile
- [Edit-Regelwerk-Kachel](frontend/edit-ruleset-tile.md) — dritte Kachel auf HomeView, nur sichtbar wenn Regelwerk geladen, navigiert zu /tile/edit-ruleset
- [Neues Regelwerk erstellen](frontend/neues-regelwerk-erstellen.md) — nativer Speichern-Dialog, legt leere XML-Datei an und setzt sie als aktives Regelwerk
