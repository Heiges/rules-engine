# Datenspeicher (DataDirectory)

## Ziel

Einen festen, standardisierten Speicherort für Regelwerk-XML-Dateien definieren (`~/.rules-engine/data/`), sodass Aufrufer keinen Pfad mehr kennen müssen und mehrere Regelwerke nebeneinander gespeichert werden können.

## Anforderungen

- Fester Standard-Speicherort: `~/.rules-engine/data/`
- Verzeichnis wird bei Bedarf angelegt (`ensureExists()`)
- Regelwerk-Pfad über Name auflösbar: `rulesetPath("grundregeln")` → `~/.rules-engine/data/grundregeln.xml`
- Alle vorhandenen Regelwerke auflistbar: `listRulesets()` → sortierte Liste aller `.xml`-Dateien
- Verzeichnis noch nicht vorhanden → `listRulesets()` gibt leere Liste zurück (kein Fehler)

## Entscheidungen

- **Fester Default ohne Override-Möglichkeit** — für den aktuellen Stand ausreichend; konfigurierbar machen wenn Bedarf entsteht
- **Statische Utility-Klasse** — kein Zustand, kein Lifecycle-Management nötig
- **Dateiname = Regelwerk-Name** — kein separates Metadaten-Feld; der Name ist direkt aus dem Dateinamen ableitbar (ohne `.xml`-Suffix)

## Implementierung

| Artefakt | Pfad |
|---|---|
| DataDirectory | `persistence/src/main/java/de/heiges/rulesengine/persistence/DataDirectory.java` |

## Rekonstruktion

```
Erstelle im Modul `persistence` eine statische Utility-Klasse DataDirectory,
die den festen Speicherort ~/.rules-engine/data/ kapselt.
Methoden: base(), rulesetPath(String name), ensureExists(), listRulesets().
Kein Konfigurationsparameter — fester Default.
```
