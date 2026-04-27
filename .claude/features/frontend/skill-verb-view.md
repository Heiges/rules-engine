# SkillVerb-View (CRUD)

## Ziel

Tabellarische Übersicht aller `SkillVerb`-Einträge des geladenen Regelwerks mit Sortierung, Massenauswahl und Inline-Anlage neuer Skills. Jede Änderung wird sofort persistiert.

## Anforderungen

- Route: `/tile/skills` — erreichbar über die Skills-Kachel in `/edit-ruleset`.
- Zurück-Button navigiert zu `/edit-ruleset`.
- Tabelle mit vier Spalten: Checkbox | Name (sortierbar) | Beschreibung | Aktionen.
- Sortierung der Namensspalte: 3-Zustand (unsortiert → aufsteigend → absteigend → unsortiert), Symbol ⇅ / ▲ / ▼.
- Massenauswahl: Checkbox im Header wählt alle/keine aus; bei aktiver Auswahl erscheint ein „X löschen"-Button im Header.
- Aktionen pro Zeile: Anzeigen · Bearbeiten (beide navigieren zu `/tile/skills/<index>`) · Löschen.
- Selektierte Zeilen werden farblich hervorgehoben (`attr-row-selected`).
- „+ Neuer Skill"-Button öffnet ein Inline-Formular (Name + Beschreibung); Abbrechen oder Hinzufügen schließt es wieder.
- Jede Änderung (Hinzufügen, Einzellöschen, Massenlöschen) löst sofort `persist()` aus.
- Leere Liste: Hinweistext „Noch keine Skills vorhanden."
- Fehlertext bei Speicher-/API-Fehler.

## Entscheidungen

- **CSS-Wiederverwendung aus `AttributeView.css`**: Alle Tabellen-, Button- und Formular-Styles sind identisch — keine separate CSS-Datei für `SkillVerbView`.
- **Anzeigen = Bearbeiten (gleiche Route)**: Beide Links navigieren zu `/tile/skills/<index>`; eine separate Detail-View existiert noch nicht.
- **Sortierung arbeitet auf `originalIndex`**: Die Sortierung ändert nur die Renderreihenfolge, nicht das zugrundeliegende Array; `del()` und Checkboxen operieren immer auf dem `originalIndex`.
- **`persist()`-Muster wie `AttributeView`**: fileHandle → XML exportieren + schreiben; sonst `saveRuleset` via API.

## Implementierung

| Artefakt | Pfad |
|---|---|
| Listen-View | `frontend/src/views/SkillVerbView.tsx` |
| Detail-View (Anlage/Bearbeitung) | `frontend/src/views/SkillVerbDetailView.tsx` |
| CSS (wiederverwendet) | `frontend/src/views/AttributeView.css` |
| Route-Registrierung | `frontend/src/App.tsx` (`/tile/skills` + `/tile/skills/:index`) |
| Skills-Kachel (Einstiegspunkt) | `frontend/src/views/EditRulesetView.tsx` |

## Rekonstruktion

```
Neue View /tile/skills für SkillVerb-CRUD.
Tabelle analog zu AttributeView: Checkbox-Spalte (Massenauswahl + Alles-auswählen im Header),
sortierbare Namensspalte (3-Zustand), Beschreibungs-Spalte, Aktionen (Anzeigen/Bearbeiten/Löschen).
Massenauswahl: "X löschen"-Button im Header. Inline-Formular für neuen Skill (Name + Beschreibung).
Jede Änderung sofort persistieren (fileHandle oder saveRuleset). CSS aus AttributeView.css wiederverwenden.
```

Kontext: `SkillVerb`-Interface in `api.ts` hat `name: string` und `description: string`. `RulesetData.skills` ist `SkillVerb[]`. `persist()` wie in `AttributeView`: fileHandle → exportRuleset + createWritable; sonst saveRuleset. Routing in `App.tsx`. Die Skills-Kachel in `EditRulesetView` zeigt Skillanzahl aus `rulesetData.skills.length`.
