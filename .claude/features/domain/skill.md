# SkillVerb

## Ziel

Fähigkeit (Verb) einer Spielfigur, beschrieben durch einen Namen und eine Beschreibung. `SkillVerb` ist bewusst einfach gehalten: kein Level, keine Attribut-Bindung — diese Aspekte werden in späteren Schichten (Regelausprägung) ergänzt.

## Anforderungen

- Name ist unveränderlich (final) und darf nicht leer oder blank sein
- Beschreibung ist optional; `null` wird intern zu leerem String normalisiert
- Beschreibung ist nachträglich änderbar via `setDescription`
- `equals`/`hashCode` ausschließlich über den Namen (fachlicher Schlüssel)
- `toString` für Debugging: `SkillVerb{name='...', description='...'}`

## Entscheidungen

- **Umbenennung von `Skill` → `SkillVerb`**: Die Klasse repräsentiert eine Handlung/Fähigkeit (ein Verb), nicht ein messbares Attribut. Der neue Name macht die semantische Rolle explizit.
- **Kein `linkedAttribute`, kein `level`**: Diese Felder gehörten zur alten `Skill`-Klasse; sie werden jetzt nicht mehr im Domänenmodell modelliert. Eine Attribut-Verknüpfung ist regelspezifisch und gehört in eine spätere Abstraktion.
- **Name ist `final`**: wie bei `Attribute` ist der Name Identität des Objekts.
- **`description` normalisiert `null` → `""`**: konsistentes Verhalten ohne NPE-Risiko im UI.

## Implementierung

| Artefakt | Pfad |
|---|---|
| Domain-Klasse | `coreElements/src/main/java/de/heiges/rulesengine/coreelements/domain/model/SkillVerb.java` |
| Unit-Test | `coreElements/src/test/java/de/heiges/rulesengine/coreelements/domain/model/SkillVerbTest.java` |
| Persistenz-DTO | `persistence/src/main/java/de/heiges/rulesengine/persistence/xml/dto/SkillDto.java` |
| Repository-Interface | `persistence/src/main/java/de/heiges/rulesengine/persistence/repository/RulesetRepository.java` |
| Repository-Impl | `persistence/src/main/java/de/heiges/rulesengine/persistence/xml/XmlRulesetRepository.java` |
| Persistenz-Test | `persistence/src/test/java/de/heiges/rulesengine/persistence/xml/XmlRulesetRepositoryTest.java` |
| API-DTO | `api/src/main/java/de/heiges/rulesengine/api/dto/SkillApiDto.java` |
| API-Controller | `api/src/main/java/de/heiges/rulesengine/api/controller/RulesetController.java` |
| Frontend-Typ | `frontend/src/api.ts` (`SkillVerb`-Interface) |

## Rekonstruktion

```
Benenne die Domänenklasse Skill um in SkillVerb. SkillVerb hat nur Name (unveränderlich, nicht blank)
und Beschreibung (optional, null → ""). Kein linkedAttribute, kein level.
Passe alle abhängigen Schichten an: persistence (SkillDto, XmlRulesetRepository, LoadedRuleset,
RulesetRepository), api (SkillApiDto, RulesetController), frontend (api.ts Interface SkillVerb,
CharacterEditorView).
```

Kontext: Ursprüngliche `Skill`-Klasse hatte `linkedAttribute: Attribute` und `level: int`. Beide wurden entfernt. Die alten Dateien `Skill.java` und `SkillTest.java` sind nach der Migration obsolet (Löschung auf Bestätigung ausstehend).
