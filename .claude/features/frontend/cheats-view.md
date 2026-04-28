# Cheats-View (CRUD)

## Ziel

Die Cheats-View ermöglicht das Anlegen, Bearbeiten und Löschen von Cheats im Rahmen der Referenzregeln. Sie ist über die Kachel „Cheats" in der `EditRulesetView` erreichbar.

## Anforderungen

- Kachel „Cheats" in `EditRulesetView` zeigt die Anzahl der vorhandenen Cheats.
- `CheatView` zeigt alle Cheats in einer Tabelle mit Sortierung, Massenauswahl und Löschen.
- Inline-Formular zum schnellen Hinzufügen (Name + Beschreibung) ohne Seitenwechsel.
- „Anzeigen"- und „Bearbeiten"-Button navigieren zu `CheatDetailView` mit dem Index als URL-Parameter.
- `CheatDetailView` unterstützt Anlage (`/neu`) und Bearbeitung eines bestehenden Cheats.
- Doppelter Name wird beim Speichern abgelehnt (Fehlermeldung inline).
- Jede Änderung persistiert sofort: via `fileHandle.createWritable()` oder `PUT /api/rulesets/{name}`.

## Entscheidungen

- **Eigene List-View** (`CheatView`) statt Einbettung in eine andere View — Cheats sind ein eigenständiger Baustein, nicht an Skills gebunden.
- **Gleiche CSS-Klassen** (`attr-table`, `attr-form`, …) aus `AttributeView.css` — kein eigenes Stylesheet nötig.
- **Route `/tile/cheats`** und `/tile/cheats/:index` — folgt dem bestehenden Muster für Referenzregeln-Kacheln.
- **`cheats`-Feld null-safe** in `fromApiDto` (`?? []`) — Abwärtskompatibilität mit bestehenden XML-Dateien ohne `<cheats>`-Element.

## Implementierung

| Artefakt | Pfad |
|---|---|
| Domänenklasse | `coreElements/src/main/java/de/heiges/rulesengine/coreelements/domain/model/Cheat.java` |
| Domänen-Test | `coreElements/src/test/java/de/heiges/rulesengine/coreelements/domain/model/CheatTest.java` |
| Persistenz-DTO | `persistence/src/main/java/de/heiges/rulesengine/persistence/xml/dto/CheatDto.java` |
| Persistenz-Set-DTO | `persistence/src/main/java/de/heiges/rulesengine/persistence/xml/dto/CheatSetDto.java` |
| RulesetDto (erweitert) | `persistence/src/main/java/de/heiges/rulesengine/persistence/xml/dto/RulesetDto.java` |
| LoadedRuleset (erweitert) | `persistence/src/main/java/de/heiges/rulesengine/persistence/repository/LoadedRuleset.java` |
| RulesetRepository (erweitert) | `persistence/src/main/java/de/heiges/rulesengine/persistence/repository/RulesetRepository.java` |
| XmlRulesetRepository (erweitert) | `persistence/src/main/java/de/heiges/rulesengine/persistence/xml/XmlRulesetRepository.java` |
| API-DTO | `api/src/main/java/de/heiges/rulesengine/api/dto/CheatApiDto.java` |
| RulesetApiDto (erweitert) | `api/src/main/java/de/heiges/rulesengine/api/dto/RulesetApiDto.java` |
| RulesetController (erweitert) | `api/src/main/java/de/heiges/rulesengine/api/controller/RulesetController.java` |
| Frontend-Typ | `frontend/src/api.ts` (`Cheat`, `cheats` in `RulesetData`) |
| List-View | `frontend/src/views/CheatView.tsx` |
| Detail-View | `frontend/src/views/CheatDetailView.tsx` |
| EditRulesetView (Kachel) | `frontend/src/views/EditRulesetView.tsx` |
| Routing | `frontend/src/App.tsx` |

## Rekonstruktion

```
Neues Feature Cheats vollständig von Domain bis Frontend:
- Domänenklasse Cheat (Name + Beschreibung) in coreElements
- Persistence: CheatDto, CheatSetDto, RulesetDto/<cheats>, LoadedRuleset, RulesetRepository, XmlRulesetRepository
- API: CheatApiDto, RulesetApiDto ergänzt, RulesetController (toApiDto, toCheats, save, create, exportToXml)
- Frontend: Cheat-Interface in api.ts, CheatView (Liste + Inline-Add), CheatDetailView (Anlage/Bearbeitung),
  Kachel in EditRulesetView, Routen /tile/cheats und /tile/cheats/:index in App.tsx
```

Kontext: Muster wie `SkillDomain` auf allen Schichten. `cheats`-Feld in `fromApiDto` null-safe für Abwärtskompatibilität mit alten XMLs. Styling über bestehende `AttributeView.css`-Klassen.
