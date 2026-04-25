Erstelle ein neues Domain-Element im Modul coreElements mit dem Namen $ARGUMENTS.

## Paket

`de.heiges.rulesengine.coreelements.domain.model`

## Ablageorte

- Klasse: `coreElements/src/main/java/de/heiges/rulesengine/coreelements/domain/model/$ARGUMENTS.java`
- Test:   `coreElements/src/test/java/de/heiges/rulesengine/coreelements/domain/model/$ARGUMENTSTest.java`

## Anforderungen an die Klasse

- Validierung im Konstruktor (kein Blank-Name, keine negativen Werte)
- `equals`/`hashCode` über fachlichen Schlüssel (Name)
- `toString` für Debugging

## Anforderungen an den Test (JUnit 5)

- Erzeugung mit gültigen Werten
- Ablehnung ungültiger Eingaben (assertThrows)
- Gleichheitsprüfung

## Persistenz

Nach der Erstellung des Domain-Elements auch DTO und Repository im `persistence`-Modul anlegen:

- `persistence/src/main/java/de/heiges/rulesengine/persistence/xml/dto/$ARGUMENTSDto.java` (JAXB-annotiert)
- `persistence/src/main/java/de/heiges/rulesengine/persistence/repository/$ARGUMENTSRepository.java` (Interface)
- `persistence/src/main/java/de/heiges/rulesengine/persistence/xml/Xml$ARGUMENTSRepository.java` (Implementierung)
- `persistence/src/test/java/de/heiges/rulesengine/persistence/xml/Xml$ARGUMENTSRepositoryTest.java`

## Abschluss

Tests in beiden Modulen ausführen:
```bash
cd coreElements && mvn test
cd ../persistence && mvn test
```

## Dokumentation

Nach erfolgreichem Test-Lauf die Datei `.claude/design.md` aktualisieren:

- Im Abschnitt `## Domain-Modell` einen Eintrag für `$ARGUMENTS` hinzufügen mit:
  - fachlichem Schlüssel (Feld, das `equals`/`hashCode` bestimmt)
  - zentralen Invarianten (z. B. „Name nicht leer", „Wert ≥ 0")
  - Beziehungen zu anderen Domain-Elementen (falls vorhanden)

Das hält die Architekturdokumentation synchron mit dem Code.
