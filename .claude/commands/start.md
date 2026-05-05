Starte die Anwendung vollständig (Backend + Frontend) im Hintergrund.

Führe diese beiden Befehle parallel aus:

1. Backend (Spring Boot API):
```bash
cd /home/hheiges/Dev/workspace-code/rules-engine/api && mvn spring-boot:run
```

2. Frontend (Vite Dev-Server):
```bash
cd /home/hheiges/Dev/workspace-code/rules-engine/frontend && npm run dev
```

Warte ca. 20 Sekunden, prüfe dann die Ausgaben beider Prozesse und melde dem Nutzer:
- ob Backend auf Port 8080 gestartet ist
- ob Frontend auf Port 5173 bereit ist
- bei Fehlern: die relevante Fehlerausgabe
