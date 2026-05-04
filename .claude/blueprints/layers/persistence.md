# Schicht-Spec: Persistenz (persistence-Modul)

## Zweck

Speichern und Laden aller Regelwerk-Daten als XML. Eine XML-Datei pro Regelwerk.
Die Domänenklassen bleiben annotation-frei — alle JAXB-Annotationen liegen ausschließlich in DTOs innerhalb dieses Moduls.

## Ablagestruktur

```
persistence/src/main/java/de/heiges/rulesengine/persistence/
  DataDirectory.java                        — Pfad-Hilfsmethoden (~/.rules-engine/data/)
  repository/
    RulesetRepository.java                  — Interface
    LoadedRuleset.java                      — Rückgabe-Record für load()
  xml/
    XmlRulesetRepository.java               — JAXB-Implementierung
    dto/
      RulesetDto.java                       — XML-Wurzelelement (@XmlRootElement)
      ValueRangeDto.java
      AttributeSetDto.java
      AttributeGroupDto.java
      AttributeDto.java
      SkillSetDto.java
      SkillDto.java
      SkillDomainSetDto.java
      SkillDomainDto.java
      CheatSetDto.java
      CheatDto.java
```

## Repository-Interface

```java
public interface RulesetRepository {
    void save(ValueRange, AttributeSet, Collection<SkillVerb>,
              Collection<SkillDomain>, Collection<Cheat>, Path file) throws IOException;
    LoadedRuleset load(Path file) throws IOException;
    List<Path> listAll(Path directory) throws IOException;
}
```

`LoadedRuleset` ist ein Record im `repository`-Paket — kein Domänenobjekt, nur eine Datenhülle:

```java
public record LoadedRuleset(
    ValueRange valueRange,
    AttributeSet attributeSet,
    Collection<SkillVerb> skills,
    Collection<SkillDomain> skillDomains,
    Collection<Cheat> cheats
) {}
```

## DTO-Muster

### Scalar-DTO (Einzelobjekt mit Attributen)

Felder als XML-Attribute (`@XmlAttribute`). Kein `@XmlRootElement` außer am Wurzel-DTO.

```java
@XmlAccessorType(XmlAccessType.FIELD)
public class AttributeDto {
    @XmlAttribute(required = true) private String name;
    @XmlAttribute                  private String description;
    @XmlAttribute                  private int value;

    public AttributeDto() {}                                     // no-arg für JAXB zwingend
    public AttributeDto(String name, String description, int value) { ... }
    // nur Getter — keine Setter
}
```

### Container-DTO (Liste von Scalar-DTOs)

Felder als XML-Elemente (`@XmlElement`). Interne Liste vorinitialisiert mit `new ArrayList<>()`.

```java
@XmlAccessorType(XmlAccessType.FIELD)
public class AttributeSetDto {
    @XmlElement(name = "group")
    private List<AttributeGroupDto> groups = new ArrayList<>();

    public AttributeSetDto() {}
    public List<AttributeGroupDto> getGroups() { return groups; }
}
```

### Gruppen-DTO (hat Name + Liste von Scalar-DTOs)

```java
@XmlAccessorType(XmlAccessType.FIELD)
public class AttributeGroupDto {
    @XmlAttribute(required = true)  private String name;
    @XmlElement(name = "attribute") private List<AttributeDto> attributes = new ArrayList<>();

    public AttributeGroupDto() {}
    public AttributeGroupDto(String name) { this.name = name; }
    public String getName() { ... }
    public List<AttributeDto> getAttributes() { ... }
}
```

### Wurzel-DTO

Nur einmal im Projekt. Enthält alle Container-DTOs als `@XmlElement`.

```java
@XmlRootElement(name = "ruleset")
@XmlAccessorType(XmlAccessType.FIELD)
public class RulesetDto {
    @XmlElement(name = "wertebereich") private ValueRangeDto valueRange;
    @XmlElement(name = "attributeSet") private AttributeSetDto attributeSet = new AttributeSetDto();
    @XmlElement(name = "skills")       private SkillSetDto skills = new SkillSetDto();
    @XmlElement(name = "skillDomains") private SkillDomainSetDto skillDomains = new SkillDomainSetDto();
    @XmlElement(name = "cheats")       private CheatSetDto cheats = new CheatSetDto();

    public RulesetDto() {}
    // Getter + Setter für alle Felder
}
```

## `XmlRulesetRepository` — Implementierungsregeln

- `JAXBContext.newInstance(RulesetDto.class)` — nur die Wurzelklasse übergeben
- `Marshaller.JAXB_FORMATTED_OUTPUT = true` — formatierte XML-Ausgabe
- `JAXBException` immer in `IOException` wrappen — kein Leak technischer Exceptions
- Datei existiert nicht beim `load()` → `IOException` mit klarer Meldung vor dem JAXB-Aufruf
- Verzeichnis existiert nicht bei `listAll()` → leere Liste zurückgeben (kein Fehler)
- Zusätzlich: `fromXml(String xml)` und `toXml(...)` für den API-Import/Export (ohne Dateizugriff)

## DataDirectory

```java
DataDirectory.base()                  // → ~/.rules-engine/data/
DataDirectory.rulesetPath("name")     // → ~/.rules-engine/data/name.xml
DataDirectory.ensureExists()          // legt Verzeichnis an falls nötig
DataDirectory.listRulesets()          // alle .xml-Dateien sortiert
```

Wird vom `api`-Modul genutzt — nicht von `XmlRulesetRepository` selbst.

## XML-Struktur (aktuell)

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
    <skillDomain name="Kampf" description="Kampffertigkeiten"/>
  </skillDomains>
  <cheats>
    <cheat name="Unsichtbarkeit" description="Macht den Charakter unsichtbar"/>
  </cheats>
</ruleset>
```

## Checkliste: Neues Domain-Objekt einbinden

Wenn ein neues Domänenobjekt (z.B. `Foo`) persistiert werden soll:

1. `FooDto.java` — Scalar-DTO mit `@XmlAttribute`-Feldern und no-arg Konstruktor
2. `FooSetDto.java` — Container-DTO mit `@XmlElement(name="foo") List<FooDto>`
3. `RulesetDto` — neues Feld `FooSetDto foos = new FooSetDto()` mit `@XmlElement(name="foos")`
4. `RulesetRepository` — `Collection<Foo>` zu `save()`-Signatur hinzufügen
5. `LoadedRuleset` — neues Record-Feld `Collection<Foo> foos`
6. `XmlRulesetRepository.toDto()` — Schleife: Domäne → DTO
7. `XmlRulesetRepository.toDomain()` — Schleife: DTO → Domäne

## Maven-Abhängigkeiten (persistence/pom.xml)

```xml
<dependency>
    <groupId>jakarta.xml.bind</groupId>
    <artifactId>jakarta.xml.bind-api</artifactId>
    <version>4.0.2</version>
</dependency>
<dependency>
    <groupId>org.glassfish.jaxb</groupId>
    <artifactId>jaxb-runtime</artifactId>
    <version>4.0.5</version>
    <scope>runtime</scope>
</dependency>
```

## Referenzen

- [architecture.md](../../architecture.md) — Abhängigkeitsrichtung: persistence → coreElements
- [domain.md](domain.md) — Domänenklassen, die hier als DTOs gespiegelt werden
