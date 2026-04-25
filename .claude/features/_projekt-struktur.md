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

### coreElements / persistence

- [Attribute](attribute.md) — benannte, messbare Eigenschaft
- [AttributeSet](attribute-set.md) — geordnete Sammlung von Attributen
- [Skill](skill.md) — Fähigkeit mit Attribut-Bindung und Level
- [XML-Persistenz](persistenz-xml.md) — Speichern und Laden von Domänenklassen als XML (JAXB)

### Frontend

- [Anwendungsrahmen](frontend-anwendungsrahmen.md) — Entry Point, Routing, globales Theming
- [Kacheln](frontend-kacheln.md) — Kachel-Navigation (HomeView, Tile-Komponente, DetailView)
