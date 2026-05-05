# REST-API — Spezifikation

## Übersicht

Spring Boot 3.2.x REST-API. Vermittelt zwischen React-Frontend (JSON) und Persistenzschicht (XML). Läuft auf Port `8080`; Frontend auf Port `5173`.

## Endpunkte

| Methode | Pfad | Beschreibung | Antwort |
|---------|------|-------------|---------|
| `GET` | `/api/rulesets` | Liste aller Regelwerk-Namen | `200 ["name1", ...]` |
| `GET` | `/api/rulesets/{name}` | Regelwerk laden | `200 RulesetApiDto` / `404` |
| `PUT` | `/api/rulesets/{name}` | Regelwerk speichern/aktualisieren | `204` |
| `POST` | `/api/rulesets/{name}` | Neues leeres Regelwerk anlegen | `201` / `409` wenn Name vergeben |
| `POST` | `/api/roll` | Würfelwurf | `200 RollResultApiDto` |

CORS: `http://localhost:5173` für alle `/api/**`-Endpunkte freigegeben.

## DTOs

### `RulesetApiDto`

```java
record RulesetApiDto(
    ValueRangeApiDto valueRange,
    List<AttributeGroupApiDto> attributeGroups,
    List<SkillApiDto> skills,
    List<SkillDomainApiDto> skillDomains,
    List<CheatApiDto> cheats
) {}
```

### `AttributeGroupApiDto`

```java
record AttributeGroupApiDto(String group, List<AttributeApiDto> attributes) {}
```

### `AttributeApiDto`

```java
record AttributeApiDto(String name, String description, int value) {}
```

### `SkillApiDto`

```java
record SkillApiDto(String name, String description) {}
```

### `SkillDomainApiDto`

```java
record SkillDomainApiDto(String name, String description) {}
```

### `CheatApiDto`

```java
record CheatApiDto(String name, String description) {}
```

### `ValueRangeApiDto`

```java
record ValueRangeApiDto(int min, int average, int max) {}
```

### `RollRequestDto` / `RollResultApiDto`

```java
record RollRequestDto(int value) {}
record RollResultApiDto(int[] dice, boolean success, Integer paschValue, Integer paschCount) {}
```

## JSON-Beispiel

```json
{
  "valueRange": { "min": -10, "average": 0, "max": 10 },
  "attributeGroups": [
    {
      "group": "Körper",
      "attributes": [
        { "name": "Stärke", "description": "Körperliche Kraft", "value": 3 }
      ]
    }
  ],
  "skills": [
    { "name": "Klettern", "description": "Vertikale Fortbewegung" }
  ],
  "skillDomains": [
    { "name": "Physisch", "description": "" }
  ],
  "cheats": [
    { "name": "Göttlicher Eingriff", "description": "Einmal pro Session" }
  ]
}
```

## Entscheidungen

- **Eigene API-DTOs** — entkoppelt JSON-Format von JAXB-Persistenz-DTOs; XML-Änderungen erfordern keine API-Änderung
- **Kein Spring Security** — lokale Entwicklungsanwendung
- **CORS in eigener Klasse** (`WebConfig`) — nicht inline im Application-Bootstrap

## Dateien

| Artefakt | Pfad |
|----------|------|
| Startklasse | [api/…/ApiApplication.java](../../../api/src/main/java/de/heiges/rulesengine/api/ApiApplication.java) |
| CORS-Konfiguration | [api/…/WebConfig.java](../../../api/src/main/java/de/heiges/rulesengine/api/WebConfig.java) |
| Controller Regelwerk | [api/…/RulesetController.java](../../../api/src/main/java/de/heiges/rulesengine/api/controller/RulesetController.java) |
| Controller Würfeln | [api/…/RollController.java](../../../api/src/main/java/de/heiges/rulesengine/api/controller/RollController.java) |
| `RulesetApiDto` | [api/…/dto/RulesetApiDto.java](../../../api/src/main/java/de/heiges/rulesengine/api/dto/RulesetApiDto.java) |
| `AttributeGroupApiDto` | [api/…/dto/AttributeGroupApiDto.java](../../../api/src/main/java/de/heiges/rulesengine/api/dto/AttributeGroupApiDto.java) |
| `AttributeApiDto` | [api/…/dto/AttributeApiDto.java](../../../api/src/main/java/de/heiges/rulesengine/api/dto/AttributeApiDto.java) |
| `SkillApiDto` | [api/…/dto/SkillApiDto.java](../../../api/src/main/java/de/heiges/rulesengine/api/dto/SkillApiDto.java) |
| `SkillDomainApiDto` | [api/…/dto/SkillDomainApiDto.java](../../../api/src/main/java/de/heiges/rulesengine/api/dto/SkillDomainApiDto.java) |
| `CheatApiDto` | [api/…/dto/CheatApiDto.java](../../../api/src/main/java/de/heiges/rulesengine/api/dto/CheatApiDto.java) |
| `ValueRangeApiDto` | [api/…/dto/ValueRangeApiDto.java](../../../api/src/main/java/de/heiges/rulesengine/api/dto/ValueRangeApiDto.java) |
| `RollRequestDto` | [api/…/dto/RollRequestDto.java](../../../api/src/main/java/de/heiges/rulesengine/api/dto/RollRequestDto.java) |
| `RollResultApiDto` | [api/…/dto/RollResultApiDto.java](../../../api/src/main/java/de/heiges/rulesengine/api/dto/RollResultApiDto.java) |
