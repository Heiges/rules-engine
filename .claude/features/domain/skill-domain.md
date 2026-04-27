# SkillDomain

## Ziel

Domäne (Wissensbereich) einer SkillVerb-Fähigkeit — beschrieben durch einen Namen und eine Beschreibung. `SkillDomain` gruppiert Fähigkeiten thematisch (z. B. „Kampf", „Soziales").

## Anforderungen

- Name ist unveränderlich (final) und darf nicht leer oder blank sein
- Beschreibung ist optional; `null` wird intern zu leerem String normalisiert
- Beschreibung ist nachträglich änderbar via `setDescription`
- `equals`/`hashCode` ausschließlich über den Namen (fachlicher Schlüssel)
- `toString` für Debugging: `SkillDomain{name='...', description='...'}`

## Entscheidungen

- **Struktur analog `SkillVerb`**: beide Klassen haben genau dieselbe Signatur; getrennte Klassen wegen semantisch unterschiedlicher Rollen (Verb vs. Domäne).
- **Name ist `final`**: Der Name ist Identität — nachträgliche Umbenennung erfordert Neuanlage.
- **`description` normalisiert `null` → `""`**: konsistentes Verhalten ohne NPE-Risiko im UI.

## Implementierung

| Artefakt | Pfad |
|---|---|
| Domain-Klasse | `coreElements/src/main/java/de/heiges/rulesengine/coreelements/domain/model/SkillDomain.java` |
| Unit-Test | `coreElements/src/test/java/de/heiges/rulesengine/coreelements/domain/model/SkillDomainTest.java` |
| Persistenz-DTO (Einzeln) | `persistence/src/main/java/de/heiges/rulesengine/persistence/xml/dto/SkillDomainDto.java` |
| Persistenz-DTO (Liste) | `persistence/src/main/java/de/heiges/rulesengine/persistence/xml/dto/SkillDomainSetDto.java` |
| Regelwerk-DTO (erweitert) | `persistence/src/main/java/de/heiges/rulesengine/persistence/xml/dto/RulesetDto.java` |
| LoadedRuleset (erweitert) | `persistence/src/main/java/de/heiges/rulesengine/persistence/repository/LoadedRuleset.java` |
| Repository-Interface (erweitert) | `persistence/src/main/java/de/heiges/rulesengine/persistence/repository/RulesetRepository.java` |
| Repository-Impl (erweitert) | `persistence/src/main/java/de/heiges/rulesengine/persistence/xml/XmlRulesetRepository.java` |
| Persistenz-Test (erweitert) | `persistence/src/test/java/de/heiges/rulesengine/persistence/xml/XmlRulesetRepositoryTest.java` |
| API-DTO | `api/src/main/java/de/heiges/rulesengine/api/dto/SkillDomainApiDto.java` |
| API-Controller (erweitert) | `api/src/main/java/de/heiges/rulesengine/api/controller/RulesetController.java` |
| Ruleset-DTO (erweitert) | `api/src/main/java/de/heiges/rulesengine/api/dto/RulesetApiDto.java` |
| Frontend-Typ | `frontend/src/api.ts` (`SkillDomain`-Interface + `skillDomains` in `RulesetData`) |
| Listen-View (zweite Tabelle) | `frontend/src/views/SkillVerbView.tsx` |
| Detail-View | `frontend/src/views/SkillDomainDetailView.tsx` |
| Route-Registrierung | `frontend/src/App.tsx` (`/tile/skills/domains/:index`) |
| Kachel-Beschreibung (erweitert) | `frontend/src/views/EditRulesetView.tsx` (`domainCount`) |

## Rekonstruktion

```
Baue SkillDomain komplett durch alle Schichten:
1. Domain (coreElements): SkillDomain.java analog SkillVerb (name final, description mutable, null→"",
   equals/hashCode auf name). Test: 5 Tests.
2. Persistence: SkillDomainDto (@XmlAttribute name, description), SkillDomainSetDto (@XmlRootElement),
   RulesetDto erweitern (@XmlElement skillDomains), LoadedRuleset erweitern (skillDomains-Feld),
   RulesetRepository-Interface (save mit Collection<SkillDomain>), XmlRulesetRepository anpassen.
3. API: SkillDomainApiDto (record), RulesetApiDto erweitern (skillDomains-Feld),
   RulesetController.toSkillDomains() (null-safe), alle save/export/create Calls anpassen.
4. Frontend: SkillDomain-Interface in api.ts, skillDomains in RulesetData, fromApiDto/toApiDto anpassen,
   SkillVerbView zweite Tabelle "Domäne" hinzufügen (separate State + persistDomains),
   SkillDomainDetailView erstellen (isNew/Edit, Duplikat-Check, navigate /tile/skills),
   App.tsx Route /tile/skills/domains/:index, EditRulesetView domainCount.
```

Kontext: `SkillDomain` ist semantisch unabhängig von `SkillVerb` — keine Verknüpfung zwischen beiden im Domänenmodell. `toSkillDomains()` im Controller ist null-safe für ältere JSON-Payloads ohne `skillDomains`-Feld. Route `/tile/skills/domains/:index` muss VOR `/tile/skills/:index` in App.tsx registriert werden (React Router v6 matcht sonst "domains" als Index).
