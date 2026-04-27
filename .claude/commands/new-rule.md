Erstelle eine neue Regel mit dem Namen $ARGUMENTS.

Beachte dabei:
- Architekturvorgaben aus @architecture.md
- Designrichtlinien aus @design.md
- Toolstack aus @toolstack.md

Dieser Befehl gilt für beides: Neu-Erstellung **und** Änderung einer bestehenden Regel.
Bei Änderungen alle Schichten der Reihe nach prüfen.

---

## Schicht 1 — Domain (coreElements)

**Ablage:**
- Klasse: `coreElements/src/main/java/de/heiges/rulesengine/coreelements/domain/model/$ARGUMENTS.java`
- Test:   `coreElements/src/test/java/de/heiges/rulesengine/coreelements/domain/model/$ARGUMENTSTest.java`

**Schritte:**
1. Domain-Klasse im Paket `de.heiges.rulesengine.coreelements.domain.model` anlegen
2. Test anlegen (JUnit 5, min. Validierungen + Gleichheitsprüfung)

```bash
cd coreElements && mvn test
```

---

## Schicht 2 — Persistenz (persistence)

Wenn die Regel gespeichert wird, DTO und Repository prüfen/anlegen:
- `persistence/.../xml/dto/$ARGUMENTSDto.java`
- `persistence/.../repository/RulesetRepository.java` (Signatur)
- `persistence/.../repository/LoadedRuleset.java` (Typen)
- `persistence/.../xml/XmlRulesetRepository.java`
- `persistence/.../xml/XmlRulesetRepositoryTest.java`

```bash
cd coreElements && mvn install -q && cd ../persistence && mvn test
```

---

## Schicht 3 — API (api)

DTO und Controller-Mapping auf Aktualität prüfen:
- `api/.../dto/$ARGUMENTSApiDto.java` — Felder stimmen mit Domain überein?
- `api/.../controller/RulesetController.java` — toApiDto / toDomain korrekt?
- JSON-Beispiel in `rest-api.md` aktuell?

---

## Schicht 4 — Frontend (frontend)

- `frontend/src/api.ts` — Interface mit aktuellen Feldern; in `RulesetData` eingetragen?
- **Listen-View** (`$ARGUMENTSView.tsx`): Tabelle mit Checkbox, sortierbarer Name-Spalte, Aktionen (Anzeigen/Bearbeiten → `/tile/$arguments/:index`, Löschen), Massenauswahl, `persist()`; CSS aus `AttributeView.css`
- **Detail-View** (`$ARGUMENTSDetailView.tsx`): Route-Parameter `:index`, Sonderfall `"neu"`, Formularfelder, Duplikat-Check, `persist()` + Zurück-Navigation; CSS aus `AttributeView.css`
- **Kachel** in `EditRulesetView.tsx` mit Anzahl aus `rulesetData`
- **Routen** in `App.tsx`: `/tile/$arguments` + `/tile/$arguments/:index`
- Betroffene andere Views auf veraltete Felder prüfen

```bash
cd frontend && npm run build
```

---

## Schicht 5 — Datendateien

Bestehende XML-Regelwerke auf veraltete Felder prüfen:
```bash
grep -r "linkedAttributeName\|level=" ~/.rules-engine/data/
```
Gefundene Stellen manuell auf das neue Format migrieren.

---

## Abschluss — Dokumentation

1. Feature-Spec in `.claude/features/domain/$ARGUMENTS.md` anlegen
2. `_projekt-struktur.md` im Abschnitt `### Domain` aktualisieren
3. `design.md` im Abschnitt `## Domain-Modell` ergänzen
