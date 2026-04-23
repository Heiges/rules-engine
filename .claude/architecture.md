# Architekturvorgaben

## Architekturstil

<!-- z.B. Hexagonale Architektur, Clean Architecture, Schichtenmodell -->

## Modulstruktur

<!-- Welche Module/Pakete gibt es, wie hängen sie zusammen -->
Oberfläche/UI
API über die die Oberfläche/UI mit dem Backend kommuniziert
Backend mit der gesamten Logik
Datebank 

## Abhängigkeitsrichtung

<!-- z.B. Domain kennt keine Infrastruktur, welche Schichten dürfen wen importieren -->
Abhängigkeiten entsprechend des Schichtensystem nur nach unten. Beispiel : Das Backend hat eine Abhängigkeit zur Datenbank und keine Abhängigkeit zur Oberfläche/UI.

## Integrationspunkte

<!-- Externe Systeme, APIs, Datenbanken -->

## Entscheidungen (ADRs)

- **React Router** (`react-router-dom`) für Navigation im Frontend
- **Java-Paket**: `de.heiges.rulesengine`
- **Maven-Struktur**: `src/main/java/de/heiges/rulesengine/`
