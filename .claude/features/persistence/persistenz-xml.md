# XML-Persistenz

## Ziel

Alle Regelwerk-Daten (Attribute und Skills) in einer einzigen XML-Datei pro Regelwerk speichern und laden, ohne das Domänenmodell mit technischen Annotationen zu belasten. Der Dateiname bestimmt den Namen des Regelwerks.

## Anforderungen

- `save(AttributeSet, Collection<Skill>, Path)` schreibt eine XML-Datei an den vom Aufrufer übergebenen Pfad
- `load(Path)` liest die Datei ein und rekonstruiert `AttributeSet` + `Collection<Skill>` als `LoadedRuleset`
- `listAll(Path directory)` gibt alle `.xml`-Dateien im Verzeichnis sortiert zurück
- Datei existiert nicht beim Laden → `IOException` mit klarer Meldung
- JAXB-Fehler werden als `IOException` weitergegeben (kein Leak von JAXBException)
- XML-Ausgabe ist formatiert (`JAXB_FORMATTED_OUTPUT = true`)
- Skills beim Laden: Attribut-Referenz wird per Name aus dem im gleichen File gespeicherten AttributeSet aufgelöst; fehlendes Attribut → `IllegalArgumentException`
- Domänenklassen bleiben annotation-frei — alle JAXB-Annotationen nur in DTOs

## Entscheidungen

- **Ein File pro Regelwerk** statt getrennter Dateien für AttributeSet und Skills: kein Mehrwert durch Trennung, Dateiname = Regelwerk-Name
- **DTO-Schicht statt annotierter Domain-Klassen**: Domänenmodell hat keine Persistenz-Abhängigkeit; Konvertierung liegt vollständig im Repository
- **`LoadedRuleset` Record** als Rückgabetyp von `load()`: kein neues Domänenobjekt, nur ein Datenhüllen-Record im Persistence-Modul
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
  <attributeSet>
    <attribute name="Stärke" value="10"/>
  </attributeSet>
  <skills>
    <skill name="Klettern" linkedAttributeName="Stärke" level="3"/>
  </skills>
</ruleset>
```

## Rekonstruktion

```
Erstelle im Modul `persistence` eine XML-Persistenzschicht, die alle Regelwerk-Daten
(AttributeSet + Skills) in einer einzigen Datei speichert.
Muster: RulesetDto als @XmlRootElement mit verschachteltem AttributeSetDto und SkillSetDto,
RulesetRepository-Interface mit save(AttributeSet, Collection<Skill>, Path) / load(Path) / listAll(Path),
LoadedRuleset als Record-Rückgabetyp. Domänenklassen bleiben annotation-frei.
```

Kontext: JAXB 4.x / Jakarta; JAXBException in IOException wrappen; Pfad vom Aufrufer übergeben; Skills werden beim Laden gegen das mitgespeicherte AttributeSet per Name aufgelöst.
