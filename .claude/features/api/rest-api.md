# Spring Boot REST API

## Ziel

Stellt eine REST API bereit, über die das React-Frontend mit der Java-Persistenzschicht kommuniziert. Das Frontend kennt kein XML mehr und hält keine Datei-Handles; alle Lade- und Speicheroperationen laufen über JSON-Requests an `http://localhost:8080/api`.

## Anforderungen

- `GET  /api/rulesets` — Liste aller Regelwerk-Namen (`~/.rules-engine/data/*.xml`)
- `GET  /api/rulesets/{name}` — Regelwerk laden; Antwort als JSON (`RulesetApiDto`)
- `PUT  /api/rulesets/{name}` — Regelwerk speichern/aktualisieren; Body: JSON (`RulesetApiDto`); Antwort: 204 No Content
- `POST /api/rulesets/{name}` — Neues leeres Regelwerk anlegen; Antwort: 201 Created; 409 wenn Name bereits vergeben
- CORS: erlaubt Anfragen von `http://localhost:5173` (Vite Dev-Server)
- Fehler werden als HTTP-Statuscodes zurückgegeben (404, 400, 409, 500)

## JSON-Datenformat

```json
{
  "valueRange": { "min": -10, "average": 0, "max": 10 },
  "attributes": [
    { "name": "Stärke", "description": "", "value": 0 }
  ],
  "skills": [
    { "name": "Klettern", "linkedAttributeName": "Stärke", "level": 0 }
  ]
}
```

## Entscheidungen

- **Spring Boot 3.2.x** als Framework — Auto-Konfiguration, Jackson für JSON, kein Boilerplate nötig.
- **`coreElements` und `persistence` als Maven-Abhängigkeiten** — die bestehenden Module bleiben unverändert; der API-Layer übersetzt nur zwischen JSON und Domänenobjekten.
- **Eigene API-DTOs (`api.dto.*`)** — entkoppelt das JSON-Format von den JAXB-Persistenz-DTOs. Änderungen am XML-Format erfordern keine Anpassung der API-Schnittstelle.
- **`DataDirectory` aus `persistence`** — Speicherort `~/.rules-engine/data/` bleibt unverändert; der Controller greift direkt darauf zu.
- **Vite-Proxy statt produktiver CORS-Konfiguration** — im Dev-Server leitet `/api` an `localhost:8080` weiter; CORS-Header werden nur für den direkten Zugriff ohne Proxy benötigt.
- **Kein Spring Security** — lokale Entwicklungsanwendung; keine Authentifizierung vorgesehen.

## Implementierung

| Artefakt | Pfad |
|---|---|
| Maven-Modul | `api/pom.xml` |
| Startklasse + CORS-Bean | `api/src/main/java/de/heiges/rulesengine/api/ApiApplication.java` |
| REST-Controller | `api/src/main/java/de/heiges/rulesengine/api/controller/RulesetController.java` |
| API-DTO Regelwerk | `api/src/main/java/de/heiges/rulesengine/api/dto/RulesetApiDto.java` |
| API-DTO Wertebereich | `api/src/main/java/de/heiges/rulesengine/api/dto/ValueRangeApiDto.java` |
| API-DTO Attribut | `api/src/main/java/de/heiges/rulesengine/api/dto/AttributeApiDto.java` |
| API-DTO Skill | `api/src/main/java/de/heiges/rulesengine/api/dto/SkillApiDto.java` |
| Konfiguration | `api/src/main/resources/application.properties` |

## Rekonstruktion

```
Erstelle ein neues Maven-Modul "api" mit Spring Boot 3.2.x (spring-boot-starter-web).
Abhängigkeiten: core-elements 0.1.0-SNAPSHOT, persistence 0.1.0-SNAPSHOT, jaxb-runtime 4.0.5.

RulesetController (@RestController, @RequestMapping("/api/rulesets")):
- GET /api/rulesets → DataDirectory.listRulesets() → Namen ohne .xml-Extension
- GET /{name}       → repository.load(DataDirectory.rulesetPath(name)) → toApiDto()
- PUT /{name}       → toDomain(dto) → repository.save(...); 204 No Content
- POST /{name}      → prüfe ob Datei existiert (409); repository.save(leeres Regelwerk); 201

ApiApplication: CORS-Bean (WebMvcConfigurer) erlaubt localhost:5173 auf /api/**.
JAXB-Runtime muss explizit als Dependency deklariert werden (Spring Boot managed es nicht).
```

## Starten

```bash
# Voraussetzung: coreElements und persistence sind ins lokale Maven-Repo installiert
cd coreElements && mvn install -q
cd persistence  && mvn install -q

# API starten
cd api && mvn spring-boot:run
# oder
java -jar api/target/api-0.1.0-SNAPSHOT.jar
```
