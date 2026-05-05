# Toolstack

## Sprache & Runtime

- Java 21
- Maven als Build-Tool

## Frontend

- React 19 + TypeScript + Vite
- Verzeichnis: `frontend/`
- Dev-Server: `cd frontend && npm run dev` (Port 5173)

## Frameworks & Bibliotheken

- **Spring Boot 3.2.x** — REST-API (`api`-Modul)
- **Jakarta XML Bind (JAXB) 4.0.x** — XML-Serialisierung im `persistence`-Modul
- **JUnit Jupiter 5.10.x** — Tests in allen Java-Modulen

## Wichtige Build-Befehle

```bash
# Frontend Dev-Server starten (Port 5173)
cd frontend && npm run dev

# Frontend bauen (TypeScript-Check + Vite-Build nach frontend/dist/)
cd frontend && npm run build

# Frontend-Build lokal vorschauen (nach npm run build)
cd frontend && npm run preview

# Linting
cd frontend && npm run lint

# coreElements bauen & testen
cd coreElements && mvn test

# coreElements ins lokale Maven-Repo installieren (Voraussetzung für persistence)
cd coreElements && mvn install

# persistence bauen & testen
cd persistence && mvn test

# API starten
cd api && mvn spring-boot:run
```
