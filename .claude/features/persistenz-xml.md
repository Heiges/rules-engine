# XML-Persistenz

## Ziel

Domänenklassen (AttributeSet, Skills) dauerhaft als XML-Dateien speichern und laden, ohne das Domänenmodell mit technischen Annotationen zu belasten.

## Anforderungen

- `save(domain, Path)` schreibt eine XML-Datei an den vom Aufrufer übergebenen Pfad
- `load(Path)` liest die Datei ein und rekonstruiert das Domänenobjekt
- Datei existiert nicht beim Laden → `IOException` mit klarer Meldung
- JAXB-Fehler werden als `IOException` weitergegeben (kein Leak von JAXBException)
- XML-Ausgabe ist formatiert (`JAXB_FORMATTED_OUTPUT = true`)
- Skills beim Laden: Das zugehörige `AttributeSet` wird als Parameter übergeben; fehlendes Attribut → `IllegalArgumentException`
- Domänenklassen bleiben annotation-frei — alle JAXB-Annotationen nur in DTOs

## Entscheidungen

- **DTO-Schicht statt annotierter Domain-Klassen**: Domänenmodell hat keine Persistenz-Abhängigkeit; Konvertierung liegt vollständig im Repository
- **Pfad vom Aufrufer**: kein fester Speicherort — die aufrufende Schicht entscheidet, wo die Datei liegt
- **XML statt Datenbank**: für ein Regelwerk mit überschaubaren Datenmengen ausreichend; keine DB-Infrastruktur nötig
- **Skills brauchen AttributeSet beim Laden**: Skills referenzieren Attribute per Name; das Repository löst die Referenz beim Laden auf, anstatt Attribute doppelt zu speichern

## Implementierung

| Artefakt | Pfad |
|---|---|
| Repository-Interface AttributeSet | `persistence/src/main/java/de/heiges/rulesengine/persistence/repository/AttributeSetRepository.java` |
| Repository-Interface Skill | `persistence/src/main/java/de/heiges/rulesengine/persistence/repository/SkillRepository.java` |
| XML-Implementierung AttributeSet | `persistence/src/main/java/de/heiges/rulesengine/persistence/xml/XmlAttributeSetRepository.java` |
| XML-Implementierung Skill | `persistence/src/main/java/de/heiges/rulesengine/persistence/xml/XmlSkillRepository.java` |
| DTO AttributeSet | `persistence/src/main/java/de/heiges/rulesengine/persistence/xml/dto/AttributeSetDto.java` |
| DTO Attribute | `persistence/src/main/java/de/heiges/rulesengine/persistence/xml/dto/AttributeDto.java` |
| DTO SkillSet | `persistence/src/main/java/de/heiges/rulesengine/persistence/xml/dto/SkillSetDto.java` |
| DTO Skill | `persistence/src/main/java/de/heiges/rulesengine/persistence/xml/dto/SkillDto.java` |
| Integrationstest AttributeSet | `persistence/src/test/java/de/heiges/rulesengine/persistence/xml/XmlAttributeSetRepositoryTest.java` |
| Integrationstest Skill | `persistence/src/test/java/de/heiges/rulesengine/persistence/xml/XmlSkillRepositoryTest.java` |

**Abhängigkeiten (pom.xml):**
- `jakarta.xml.bind:jakarta.xml.bind-api:4.0.2`
- `org.glassfish.jaxb:jaxb-runtime:4.0.5` (runtime)

## Rekonstruktion

```
Erstelle im Modul `persistence` eine XML-Persistenzschicht für <DomainKlasse>.
Muster: DTO mit JAXB-Annotationen (@XmlRootElement, @XmlElement), Repository-Interface mit save(domain, Path) / load(Path), XML-Implementierung mit JAXBContext. Domänenklasse bleibt annotation-frei.
```

Kontext: JAXB 4.x / Jakarta; JAXBException in IOException wrappen; Pfad vom Aufrufer übergeben; Skills beim Laden mit AttributeSet-Parameter für Attribut-Auflösung per Name.
