# XML-Persistenz — Spezifikation

## Übersicht

Eine XML-Datei pro Regelwerk unter `~/.rules-engine/data/`. Dateiname (ohne `.xml`) = Regelwerk-Name. Domänenklassen bleiben annotation-frei — alle JAXB-Annotationen ausschließlich in DTOs.

## Repository-Interface

```java
interface RulesetRepository {
    void save(String name, RulesetApiDto dto) throws IOException;
    LoadedRuleset load(Path path) throws IOException;
    List<String> listRulesets(Path directory) throws IOException;
}
```

`LoadedRuleset` ist ein Record im Persistence-Modul:

```java
record LoadedRuleset(
    ValueRange valueRange,
    AttributeSet attributeSet,
    Collection<SkillVerb> skills,
    Collection<SkillDomain> skillDomains,
    Collection<Cheat> cheats
) {}
```

## XML-Struktur

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<ruleset>
  <wertebereich min="-10" average="0" max="10"/>
  <attributeSet>
    <group name="Körper">
      <attribute name="Stärke" description="Körperliche Kraft" value="3"/>
    </group>
  </attributeSet>
  <skills>
    <skill name="Klettern" description="Vertikale Fortbewegung"/>
  </skills>
  <skillDomains>
    <skillDomain name="Physisch" description=""/>
  </skillDomains>
  <cheats>
    <cheat name="Göttlicher Eingriff" description="Einmal pro Session"/>
  </cheats>
</ruleset>
```

## DTOs

| DTO-Klasse | XML-Element | Inhalt |
|------------|-------------|--------|
| `RulesetDto` | `<ruleset>` | Wurzelelement; enthält alle Unter-DTOs |
| `ValueRangeDto` | `<wertebereich>` | `@XmlAttribute`: `min`, `average`, `max` |
| `AttributeSetDto` | `<attributeSet>` | Liste von `AttributeGroupDto` |
| `AttributeGroupDto` | `<group>` | `@XmlAttribute name`; Liste von `AttributeDto` |
| `AttributeDto` | `<attribute>` | `@XmlAttribute`: `name`, `description`, `value` |
| `SkillSetDto` | `<skills>` | Liste von `SkillDto` |
| `SkillDto` | `<skill>` | `@XmlAttribute`: `name`, `description` |
| `SkillDomainSetDto` | `<skillDomains>` | Liste von `SkillDomainDto` |
| `SkillDomainDto` | `<skillDomain>` | `@XmlAttribute`: `name`, `description` |
| `CheatSetDto` | `<cheats>` | Liste von `CheatDto` |
| `CheatDto` | `<cheat>` | `@XmlAttribute`: `name`, `description` |

Alle DTOs: `@XmlAccessorType(XmlAccessType.FIELD)`, No-arg-Konstruktor.

## Entscheidungen

- **Ein File pro Regelwerk** — Dateiname = Regelwerk-Name; keine Trennung nach Entitätstyp
- **DTO-Schicht statt annotierter Domain-Klassen** — Domänenmodell hat keine Persistenz-Abhängigkeit
- **`LoadedRuleset` Record** — Datenhüllen-Record im Persistence-Modul; kein neues Domänenobjekt
- **XML statt Datenbank** — für überschaubare Datenmengen ausreichend; keine DB-Infrastruktur nötig
- **JAXB-Fehler als `IOException`** — kein Leak von `JAXBException` nach außen

## Dateien

| Artefakt | Pfad |
|----------|------|
| Repository-Interface | [persistence/…/RulesetRepository.java](../../../persistence/src/main/java/de/heiges/rulesengine/persistence/repository/RulesetRepository.java) |
| `LoadedRuleset` | [persistence/…/LoadedRuleset.java](../../../persistence/src/main/java/de/heiges/rulesengine/persistence/repository/LoadedRuleset.java) |
| `XmlRulesetRepository` | [persistence/…/XmlRulesetRepository.java](../../../persistence/src/main/java/de/heiges/rulesengine/persistence/xml/XmlRulesetRepository.java) |
| `DataDirectory` | [persistence/…/DataDirectory.java](../../../persistence/src/main/java/de/heiges/rulesengine/persistence/DataDirectory.java) |
| `RulesetDto` | [persistence/…/dto/RulesetDto.java](../../../persistence/src/main/java/de/heiges/rulesengine/persistence/xml/dto/RulesetDto.java) |
| `AttributeSetDto` | [persistence/…/dto/AttributeSetDto.java](../../../persistence/src/main/java/de/heiges/rulesengine/persistence/xml/dto/AttributeSetDto.java) |
| `AttributeGroupDto` | [persistence/…/dto/AttributeGroupDto.java](../../../persistence/src/main/java/de/heiges/rulesengine/persistence/xml/dto/AttributeGroupDto.java) |
| `AttributeDto` | [persistence/…/dto/AttributeDto.java](../../../persistence/src/main/java/de/heiges/rulesengine/persistence/xml/dto/AttributeDto.java) |
| `SkillSetDto` | [persistence/…/dto/SkillSetDto.java](../../../persistence/src/main/java/de/heiges/rulesengine/persistence/xml/dto/SkillSetDto.java) |
| `SkillDto` | [persistence/…/dto/SkillDto.java](../../../persistence/src/main/java/de/heiges/rulesengine/persistence/xml/dto/SkillDto.java) |
| `SkillDomainSetDto` | [persistence/…/dto/SkillDomainSetDto.java](../../../persistence/src/main/java/de/heiges/rulesengine/persistence/xml/dto/SkillDomainSetDto.java) |
| `SkillDomainDto` | [persistence/…/dto/SkillDomainDto.java](../../../persistence/src/main/java/de/heiges/rulesengine/persistence/xml/dto/SkillDomainDto.java) |
| `CheatSetDto` | [persistence/…/dto/CheatSetDto.java](../../../persistence/src/main/java/de/heiges/rulesengine/persistence/xml/dto/CheatSetDto.java) |
| `CheatDto` | [persistence/…/dto/CheatDto.java](../../../persistence/src/main/java/de/heiges/rulesengine/persistence/xml/dto/CheatDto.java) |
| Integrationstest | [persistence/…/XmlRulesetRepositoryTest.java](../../../persistence/src/test/java/de/heiges/rulesengine/persistence/xml/XmlRulesetRepositoryTest.java) |
