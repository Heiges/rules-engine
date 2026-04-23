Erstelle eine neue Regel mit dem Namen $ARGUMENTS.

Beachte dabei:
- Architekturvorgaben aus @architecture.md
- Designrichtlinien aus @design.md
- Toolstack aus @toolstack.md

## Ablageorte

**Domain-Klasse** (Kernbaustein):
`coreElements/src/main/java/de/heiges/rulesengine/coreelements/domain/model/<Name>.java`

**Zugehöriger Test:**
`coreElements/src/test/java/de/heiges/rulesengine/coreelements/domain/model/<Name>Test.java`

## Schritte

1. Domain-Klasse im korrekten Paket `de.heiges.rulesengine.coreelements.domain.model` anlegen
2. Test-Klasse anlegen (JUnit 5, min. Validierungen + Gleichheitsprüfung)
3. Tests ausführen: `mvn test` im Verzeichnis `coreElements/`
4. Kurz erklären, was erstellt wurde und warum so
