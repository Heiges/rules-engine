# Schicht-Spec: Lokale XML-Persistenz

## Zweck

Speichern und Laden von Domänenobjekten als XML-Dateien im lokalen Dateisystem. Eine XML-Datei pro Aggregat-Root.
Die Domänenklassen bleiben annotation-frei — alle JAXB-Annotationen liegen ausschließlich in DTOs innerhalb dieses Moduls.

## Typische Ablagestruktur

```
persistence/src/main/java/com/example/persistence/
  DataDirectory.java                        — Pfad-Hilfsmethoden (~/.app-name/data/)
  repository/
    EntityRepository.java                   — Interface
    LoadedEntity.java                       — Rückgabe-Record für load()
  xml/
    XmlEntityRepository.java                — JAXB-Implementierung
    dto/
      RootDto.java                          — XML-Wurzelelement (@XmlRootElement)
      EntitySetDto.java                     — Container-DTO
      EntityGroupDto.java                   — Gruppen-DTO (optional, bei gruppierten Listen)
      EntityDto.java                        — Scalar-DTO
      ChildEntitySetDto.java
      ChildEntityDto.java
```

## Repository-Interface

```java
public interface EntityRepository {
    void save(RootAggregate aggregate, Path file) throws IOException;
    LoadedEntity load(Path file) throws IOException;
    List<Path> listAll(Path directory) throws IOException;
}
```

`LoadedEntity` ist ein Record im `repository`-Paket — kein Domänenobjekt, nur eine Datenhülle:

```java
public record LoadedEntity(
    RootAggregate aggregate,
    Collection<ChildEntity> children
) {}
```

## DTO-Muster

### Scalar-DTO (Einzelobjekt mit Attributen)

Felder als XML-Attribute (`@XmlAttribute`). Kein `@XmlRootElement` außer am Wurzel-DTO.

```java
@XmlAccessorType(XmlAccessType.FIELD)
public class EntityDto {
    @XmlAttribute(required = true) private String name;
    @XmlAttribute                  private String description;
    @XmlAttribute                  private int value;

    public EntityDto() {}                                      // no-arg für JAXB zwingend
    public EntityDto(String name, String description, int value) { ... }
    // nur Getter — keine Setter
}
```

### Container-DTO (Liste von Scalar-DTOs)

Felder als XML-Elemente (`@XmlElement`). Interne Liste vorinitialisiert mit `new ArrayList<>()`.

```java
@XmlAccessorType(XmlAccessType.FIELD)
public class EntitySetDto {
    @XmlElement(name = "entity")
    private List<EntityDto> entities = new ArrayList<>();

    public EntitySetDto() {}
    public List<EntityDto> getEntities() { return entities; }
}
```

### Gruppen-DTO (hat Name + Liste von Scalar-DTOs)

```java
@XmlAccessorType(XmlAccessType.FIELD)
public class EntityGroupDto {
    @XmlAttribute(required = true)  private String name;
    @XmlElement(name = "entity")    private List<EntityDto> entities = new ArrayList<>();

    public EntityGroupDto() {}
    public EntityGroupDto(String name) { this.name = name; }
    public String getName() { ... }
    public List<EntityDto> getEntities() { ... }
}
```

### Wurzel-DTO

Nur einmal pro Persistenz-Modul. Enthält alle Container-DTOs als `@XmlElement`.

```java
@XmlRootElement(name = "root")
@XmlAccessorType(XmlAccessType.FIELD)
public class RootDto {
    @XmlElement(name = "settings")   private SettingsDto settings;
    @XmlElement(name = "entities")   private EntitySetDto entities = new EntitySetDto();
    @XmlElement(name = "children")   private ChildEntitySetDto children = new ChildEntitySetDto();

    public RootDto() {}
    // Getter + Setter für alle Felder
}
```

## `XmlEntityRepository` — Implementierungsregeln

- `JAXBContext.newInstance(RootDto.class)` — nur die Wurzelklasse übergeben
- `Marshaller.JAXB_FORMATTED_OUTPUT = true` — formatierte XML-Ausgabe
- `JAXBException` immer in `IOException` wrappen — kein Leak technischer Exceptions
- Datei existiert nicht beim `load()` → `IOException` mit klarer Meldung vor dem JAXB-Aufruf
- Verzeichnis existiert nicht bei `listAll()` → leere Liste zurückgeben (kein Fehler)
- Optional: `fromXml(String xml)` und `toXml(...)` für Import/Export ohne Dateizugriff

## DataDirectory

```java
DataDirectory.base()                  // → ~/.app-name/data/
DataDirectory.filePath("name")        // → ~/.app-name/data/name.xml
DataDirectory.ensureExists()          // legt Verzeichnis an falls nötig
DataDirectory.listFiles()             // alle .xml-Dateien sortiert
```

Wird vom `api`-Modul genutzt — nicht von `XmlEntityRepository` selbst.

## Beispiel XML-Struktur

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<root>
  <settings min="-10" default="0" max="10"/>
  <entities>
    <group name="CategoryA">
      <entity name="Entity1" description="Erste Entität" value="3"/>
      <entity name="Entity2" description="Zweite Entität" value="7"/>
    </group>
    <group name="CategoryB">
      <entity name="Entity3" description="Dritte Entität" value="1"/>
    </group>
  </entities>
  <children>
    <child name="Child1" description="Erstes Kindelement"/>
    <child name="Child2" description="Zweites Kindelement"/>
  </children>
</root>
```

## Checkliste: Neues Domain-Objekt einbinden

Wenn ein neues Domänenobjekt (`Foo`) persistiert werden soll:

1. `FooDto.java` — Scalar-DTO mit `@XmlAttribute`-Feldern und no-arg Konstruktor
2. `FooSetDto.java` — Container-DTO mit `@XmlElement(name="foo") List<FooDto>`
3. Wurzel-DTO — neues Feld `FooSetDto foos = new FooSetDto()` mit `@XmlElement(name="foos")`
4. Repository-Interface — `Collection<Foo>` zu `save()`-Signatur hinzufügen
5. Rückgabe-Record — neues Feld `Collection<Foo> foos`
6. `XmlEntityRepository.toDto()` — Schleife: Domäne → DTO
7. `XmlEntityRepository.toDomain()` — Schleife: DTO → Domäne

## Maven-Abhängigkeiten (pom.xml)

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

- [architecture.md](../../architecture.md) — Abhängigkeitsrichtung: persistence → domain
- [domain.md](domain.md) — Domänenklassen, die hier als DTOs gespiegelt werden
