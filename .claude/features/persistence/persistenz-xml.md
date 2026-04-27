# XML-Persistenz

## Ziel

Alle Regelwerk-Daten (Attribute und Skills) in einer einzigen XML-Datei pro Regelwerk speichern und laden, ohne das Domänenmodell mit technischen Annotationen zu belasten. Der Dateiname bestimmt den Namen des Regelwerks.

## Anforderungen

- `save(ValueRange, AttributeSet, Collection<SkillVerb>, Path)` schreibt eine XML-Datei an den vom Aufrufer übergebenen Pfad
- `load(Path)` liest die Datei ein und rekonstruiert `ValueRange`, `AttributeSet` + `Collection<SkillVerb>` als `LoadedRuleset`
- `listAll(Path directory)` gibt alle `.xml`-Dateien im Verzeichnis sortiert zurück
- Datei existiert nicht beim Laden → `IOException` mit klarer Meldung
- JAXB-Fehler werden als `IOException` weitergegeben (kein Leak von JAXBException)
- XML-Ausgabe ist formatiert (`JAXB_FORMATTED_OUTPUT = true`)
- Skills haben keine Attribut-Bindung — `SkillDto` speichert nur `name` und `description`
- Domänenklassen bleiben annotation-frei — alle JAXB-Annotationen nur in DTOs

## Entscheidungen

- **Ein File pro Regelwerk** statt getrennter Dateien für AttributeSet und Skills: kein Mehrwert durch Trennung, Dateiname = Regelwerk-Name
- **DTO-Schicht statt annotierter Domain-Klassen**: Domänenmodell hat keine Persistenz-Abhängigkeit; Konvertierung liegt vollständig im Repository
- **`LoadedRuleset` Record** als Rückgabetyp von `load()`: hält `ValueRange`, `AttributeSet` und `Collection<SkillVerb>`; kein neues Domänenobjekt, nur ein Datenhüllen-Record im Persistence-Modul
- **XML statt Datenbank**: für ein Regelwerk mit überschaubaren Datenmengen ausreichend; keine DB-Infrastruktur nötig

## Implementierung

| Artefakt | Pfad |
|---|---|
| Repository-Interface | `persistence/src/main/java/de/heiges/rulesengine/persistence/repository/RulesetRepository.java` |
| Rückgabe-Record | `persistence/src/main/java/de/heiges/rulesengine/persistence/repository/LoadedRuleset.java` |
| XML-Implementierung | `persistence/src/main/java/de/heiges/rulesengine/persistence/xml/XmlRulesetRepository.java` |
| DTO Wurzel | `persistence/src/main/java/de/heiges/rulesengine/persistence/xml/dto/RulesetDto.java` |
| DTO AttributeSet | `persistence/src/main/java/de/heiges/rulesengine/persistence/xml/dto/AttributeSetDto.java` |
| DTO Attribute | `persistence/src/main/java/de/heiges/rulesengine/persistence/xml/dto/AttributeDto.java` |
| DTO SkillSet | `persistence/src/main/java/de/heiges/rulesengine/persistence/xml/dto/SkillSetDto.java` |
| DTO Skill | `persistence/src/main/java/de/heiges/rulesengine/persistence/xml/dto/SkillDto.java` |
| Integrationstest | `persistence/src/test/java/de/heiges/rulesengine/persistence/xml/XmlRulesetRepositoryTest.java` |

**Abhängigkeiten (pom.xml):**
- `jakarta.xml.bind:jakarta.xml.bind-api:4.0.2`
- `org.glassfish.jaxb:jaxb-runtime:4.0.5` (runtime)

**XML-Struktur:**
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
</ruleset>
```

## Rekonstruktion

```
Erstelle im Modul `persistence` eine XML-Persistenzschicht, die alle Regelwerk-Daten
(ValueRange + AttributeSet + SkillVerbs) in einer einzigen Datei speichert.
Muster: RulesetDto als @XmlRootElement mit verschachteltem AttributeSetDto und SkillSetDto,
RulesetRepository-Interface mit save(ValueRange, AttributeSet, Collection<SkillVerb>, Path) /
load(Path) / listAll(Path), LoadedRuleset als Record-Rückgabetyp. Domänenklassen bleiben annotation-frei.
SkillDto hat nur name und description — keine Attribut-Referenz.
```

Kontext: JAXB 4.x / Jakarta; JAXBException in IOException wrappen; Pfad vom Aufrufer übergeben. `SkillVerb` hat kein `linkedAttribute` mehr — Auflösung gegen das AttributeSet beim Laden entfällt.
