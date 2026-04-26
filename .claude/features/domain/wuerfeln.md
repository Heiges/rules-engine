# Würfeln

## Ziel

Erste Kernmechanik des Regelwerks: Ein Charakter würfelt auf Basis eines Wertes (z. B. Attribut-Level) und erzielt einen Erfolg, wenn gleiche Augenzahlen erscheinen (Pasch). Je größer der Pasch, desto besser der Erfolg.

## Anforderungen

- Es werden so viele sechsseitige Würfel geworfen wie der übergebene `value`, mindestens aber 2.
- Erfolg erfordert mindestens einen Pasch (≥ 2 gleiche Würfel).
- Der beste Pasch ist der mit der höchsten Anzahl gleicher Würfel; bei Gleichstand gewinnt die höhere Augenzahl.
- `Pasch` ist nur konstruierbar mit `count ≥ 2` und `faceValue` zwischen 1 und 6.
- `DiceRoll.values()` ist unveränderlich (defensive Kopie bei Konstruktion).

## Entscheidungen

- **`DiceRoller` nimmt `Random` per Konstruktor** — ermöglicht deterministische Tests mit `new Random(seed)`, ohne Produktionscode zu verändern.
- **`bestPasch()` statt separater `Pasch`-Liste** — die API braucht nur das Ergebnis, nicht alle Gruppen; vereinfacht Aufruf und JSON-Serialisierung.
- **Tiebreak: höhere Augenzahl gewinnt** — spielthematisch sinnvoll (höhere Zahl = stärkeres Ergebnis), keine willkürliche Sortierung.
- **Domänenklassen in `coreMechanics`, nicht in `coreElements`** — Mechaniken sind von reinen Datenbausteinen getrennt; `coreElements` bleibt annotation- und logikfrei.
- **`POST /api/roll` als eigener Controller** — Würfeln ist keine Regelwerk-Operation; gehört nicht in `RulesetController`.
- **Keine Persistenz** — Würfelergebnisse sind rein transient; `coreMechanics` hat keine Abhängigkeit auf das `persistence`-Modul, und `RollController` speichert nichts.

## Implementierung

| Artefakt | Pfad |
|---|---|
| Domain-Record | `coreMechanics/src/main/java/de/heiges/rulesengine/coremechanics/domain/model/DiceRoll.java` |
| Domain-Record | `coreMechanics/src/main/java/de/heiges/rulesengine/coremechanics/domain/model/Pasch.java` |
| Domain-Service | `coreMechanics/src/main/java/de/heiges/rulesengine/coremechanics/domain/model/DiceRoller.java` |
| Unit-Test | `coreMechanics/src/test/java/de/heiges/rulesengine/coremechanics/domain/model/DiceRollTest.java` |
| Unit-Test | `coreMechanics/src/test/java/de/heiges/rulesengine/coremechanics/domain/model/DiceRollerTest.java` |
| API-Controller | `api/src/main/java/de/heiges/rulesengine/api/controller/RollController.java` |
| Request-DTO | `api/src/main/java/de/heiges/rulesengine/api/dto/RollRequestDto.java` |
| Response-DTO | `api/src/main/java/de/heiges/rulesengine/api/dto/RollResultApiDto.java` |
| Maven POM (Modul) | `coreMechanics/pom.xml` |
| Maven POM (API-Abhängigkeit) | `api/pom.xml` |

### API-Endpunkt

```
POST /api/roll
Content-Type: application/json

{ "value": 3 }

→ { "dice": [3,3,5], "success": true, "paschValue": 3, "paschCount": 2 }
→ { "dice": [1,2,3,4], "success": false, "paschValue": null, "paschCount": null }
```

## Rekonstruktion

```
Baue die Würfelmechanik in coreMechanics:
- DiceRoll (record): List<Integer> values, bestPasch() → Optional<Pasch>, isSuccess()
- Pasch (record): int faceValue, int count (≥2, faceValue 1–6)
- DiceRoller: roll(int value) → DiceRoll, würfelt max(value,2) W6, nimmt Random per Konstruktor
- Tests: DiceRollTest (kein Pasch, Pärchen, Drilling schlägt Pärchen, Tiebreak, Vollpasch, Immutabilität)
         DiceRollerTest (Minimum 2 Würfel, exakte Anzahl, Wertebereich 1–6)

Erweitere die API:
- POST /api/roll mit RollRequestDto(int value) → RollResultApiDto(dice, success, paschValue, paschCount)
- RollController delegiert an DiceRoller(new Random())
- api/pom.xml: Abhängigkeit auf core-mechanics 0.1.0-SNAPSHOT

Build-Reihenfolge: coreElements install → coreMechanics install → api compile
```

Kontext: `coreMechanics/pom.xml` existiert bereits (artifactId `core-mechanics`, hängt von `core-elements` ab). Architektur in `.claude/architecture.md`: Domänenklassen annotation-frei, `coreMechanics` → `coreElements`.
