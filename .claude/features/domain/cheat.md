# Cheat

## Ziel

Cheats sind eine Regelrubrik für Sonderregeln und Ausnahmen, die Spielern oder Spielleiterinnen zur Verfügung stehen. Ein Cheat hat einen Namen und eine Beschreibung.

## Anforderungen

- Name ist unveränderlich und darf nicht leer sein.
- Beschreibung ist änderbar; `null` wird zu `""` normalisiert.
- Gleichheit zweier Cheats basiert ausschließlich auf dem Namen.

## Entscheidungen

- **Keine Framework-Annotationen** — reine Domänenklasse ohne JAXB oder Spring.
- **Gleiches Muster wie `SkillDomain`** — Name final, Beschreibung via Setter änderbar, Equality nur auf Name.

## Implementierung

| Artefakt | Pfad |
|---|---|
| Domänenklasse | `coreElements/src/main/java/de/heiges/rulesengine/coreelements/domain/model/Cheat.java` |
| Unit-Test | `coreElements/src/test/java/de/heiges/rulesengine/coreelements/domain/model/CheatTest.java` |

## Rekonstruktion

```
Neue Domänenklasse Cheat: Name (unveränderlich, nicht leer) + Beschreibung (änderbar).
Gleichheit basiert auf Name. Kein Framework-Annotations. Muster wie SkillDomain.
```

Kontext: Liegt in `coreElements/src/main/java/de/heiges/rulesengine/coreelements/domain/model/`. Keine Abhängigkeit zu Persistence oder API.
