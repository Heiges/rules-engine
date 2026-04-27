Erstelle ein neues Domain-Element im Modul coreElements mit dem Namen $ARGUMENTS —
oder passe ein bestehendes an (Umbenennung, Struktur-Änderung).

Dieser Befehl gilt für beides: Neu-Erstellung **und** Änderung eines bestehenden Elements.
Bei einer Änderung alle Schritte der Reihe nach prüfen — auch wenn einzelne Schichten
unverändert bleiben.

---

## Schicht 1 — Domain (coreElements)

**Ablage:**
- Klasse: `coreElements/src/main/java/de/heiges/rulesengine/coreelements/domain/model/$ARGUMENTS.java`
- Test:   `coreElements/src/test/java/de/heiges/rulesengine/coreelements/domain/model/$ARGUMENTSTest.java`

**Anforderungen an die Klasse:**
- Validierung im Konstruktor (kein Blank-Name, keine negativen Werte)
- `equals`/`hashCode` über fachlichen Schlüssel (Name)
- `toString` für Debugging
- Keine Framework-Annotationen (kein JAXB, kein Spring)

**Anforderungen an den Test (JUnit 5):**
- Erzeugung mit gültigen Werten
- Ablehnung ungültiger Eingaben (`assertThrows`)
- Gleichheitsprüfung

**Build-Check:**
```bash
cd coreElements && mvn test
```

---

## Schicht 2 — Persistenz (persistence)

Wenn das Element gespeichert/geladen wird, folgende Artefakte anlegen oder anpassen:

| Artefakt | Pfad |
|---|---|
| Persistenz-DTO (JAXB-annotiert) | `persistence/src/main/java/.../xml/dto/$ARGUMENTSDto.java` |
| Repository-Interface | `persistence/src/main/java/.../repository/RulesetRepository.java` (Signatur prüfen) |
| Rückgabe-Record | `persistence/src/main/java/.../repository/LoadedRuleset.java` (Typen prüfen) |
| XML-Implementierung | `persistence/src/main/java/.../xml/XmlRulesetRepository.java` |
| Integrationstest | `persistence/src/test/java/.../xml/XmlRulesetRepositoryTest.java` |

**Build-Check:**
```bash
cd coreElements && mvn install -q && cd ../persistence && mvn test
```

---

## Schicht 3 — API (api)

Wenn das Element über die REST-API transportiert wird, folgende Artefakte anlegen oder anpassen:

| Artefakt | Pfad |
|---|---|
| API-DTO (Record) | `api/src/main/java/.../dto/$ARGUMENTSApiDto.java` (Felder prüfen) |
| Controller-Mapping | `api/src/main/java/.../controller/RulesetController.java` (toApiDto + toDomain prüfen) |

**Checkliste:**
- DTO-Felder spiegeln exakt die aktuellen Domain-Felder wider (keine veralteten Felder)
- `toApiDto()` liest alle aktuellen Getter
- `toDomain()` / `toSkillVerbs()` o.ä. konstruiert das Domain-Objekt korrekt
- JSON-Beispiel in `rest-api.md` stimmt mit dem DTO-Record überein

---

## Schicht 4 — Frontend (frontend)

Wenn das Element im Frontend angezeigt oder bearbeitet wird, alle vier Punkte abarbeiten:

### 4a — Typen in api.ts

| Artefakt | Was prüfen |
|---|---|
| `frontend/src/api.ts` | Interface-Felder aktuell? Keine veralteten Felder (z.B. `linkedAttributeName`, `level`)? |
| `RulesetData` in `api.ts` | Neues Interface als Feldtyp eingetragen? |

### 4b — Listen-View (`$ARGUMENTSView.tsx`)

Neue View unter `frontend/src/views/$ARGUMENTSView.tsx` anlegen.
Muster: `AttributeView.tsx`. Pflichtbestandteile:

- Tabelle mit: Checkbox-Spalte (Massenauswahl + Alles-auswählen), sortierbare Name-Spalte (3-Zustand ⇅/▲/▼), Inhalts-Spalten, Aktionen-Spalte
- Aktionen pro Zeile: **Anzeigen** · **Bearbeiten** (beide → `/tile/$arguments/:index`) · **Löschen**
- Massenauswahl: „X löschen"-Button im Header bei aktiver Selektion
- Inline-Formular für Neu-Anlage ODER Navigation zu `/tile/$arguments/neu`
- `persist()`-Funktion: fileHandle → exportRuleset + createWritable; sonst saveRuleset
- Zurück-Button → `/edit-ruleset`
- CSS aus `AttributeView.css` wiederverwenden (kein eigenes CSS nötig)

### 4c — Detail-View (`$ARGUMENTSDetailView.tsx`)

Neue View unter `frontend/src/views/$ARGUMENTSDetailView.tsx` anlegen.
Muster: `AttributeDetailView.tsx`. Pflichtbestandteile:

- Route-Parameter `:index`; Sonderfall `"neu"` für Neu-Anlage (`isNew`-Flag)
- Formularfelder für alle editierbaren Domain-Felder (Name + weitere)
- Duplikat-Check auf Name vor dem Speichern
- `persist()` wie in Listen-View
- Nach Speichern: zurück zu `/tile/$arguments`
- Zurück-Button → `/tile/$arguments`
- CSS aus `AttributeView.css` wiederverwenden

### 4d — Kachel + Routing

| Artefakt | Was tun |
|---|---|
| `frontend/src/views/EditRulesetView.tsx` | Neue `<Tile id="$arguments" name="..." description="(${count} Einträge)" />` ergänzen; `count` aus `rulesetData.$argumentsPlural.length ?? 0` |
| `frontend/src/App.tsx` | Route `/tile/$arguments` → Listen-View; Route `/tile/$arguments/:index` → Detail-View |
| Betroffene andere Views (z.B. `CharacterEditorView`) | Anzeige auf aktuelle Felder prüfen; veraltete States/Felder entfernen |

**Build-Check:**
```bash
cd frontend && npm run build
```

---

## Schicht 5 — Datendateien (~/.rules-engine/data/)

Bestehende XML-Regelwerke können noch das alte Format enthalten.

**Checkliste:**
```bash
grep -r "linkedAttributeName\|level=" ~/.rules-engine/data/
```
- Alle gefundenen Stellen manuell auf das neue Format migrieren
- Veraltete Attribute entfernen, neue hinzufügen

---

## Abschluss — Dokumentation

Nach erfolgreichem Build-Check in allen Schichten:

1. **Feature-Spec anlegen oder aktualisieren** — je nach Modul:
   - Domain → `.claude/features/domain/$ARGUMENTS.md`
   - Persistence → `.claude/features/persistence/*.md` (ggf. `persistenz-xml.md`)
   - API → `.claude/features/api/rest-api.md` (JSON-Beispiel prüfen!)
   - Frontend → `.claude/features/frontend/*.md`

2. **`_projekt-struktur.md`** — im passenden Abschnitt `### Domain / Persistenz / API / Frontend` Link ergänzen oder aktualisieren.

3. **`design.md`** — im Abschnitt `## Domain-Modell` Eintrag für `$ARGUMENTS` ergänzen oder anpassen.

Pflichtabschnitte jeder Feature-Spec: **Ziel**, **Anforderungen**, **Entscheidungen**, **Implementierung** (Tabelle), **Rekonstruktion** (Prompt + Kontext).
