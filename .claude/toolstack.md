# Toolstack

## Sprache & Runtime

Für die Entwicklung wird Java genutzt mit der Version 17.

## Build-Tool

Für den Build wird Maven genutzt.

## Frontend

- React 19 + TypeScript + Vite
- Verzeichnis: `frontend/`
- Dev-Server: `cd frontend && npm run dev` (Port 5173)

## Frameworks & Bibliotheken

- **Jakarta XML Bind (JAXB) 4.0.x** — XML-Serialisierung im `persistence`-Modul
- **JUnit Jupiter 5.10.x** — Tests in allen Java-Modulen

## Entwicklungsumgebung

<!-- IDE, lokale Setup-Schritte, benötigte Tools -->

## CI/CD

<!-- Pipeline, wo sie liegt, wie sie getriggert wird -->

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
```
