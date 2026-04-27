# SkillVerb-View (CRUD)

## Ziel

Tabellarische Übersicht aller `SkillVerb`- und `SkillDomain`-Einträge des geladenen Regelwerks. Zwei getrennte Tabellen auf einer Seite. Sortierung, Massenauswahl, Inline-Anlage und Navigation zu Detail-Views. Jede Änderung wird sofort persistiert.

## Anforderungen

- Route: `/tile/skills` — erreichbar über die Skills-Kachel in `/edit-ruleset`.
- Zurück-Button navigiert zu `/edit-ruleset`.
- **Tabelle 1 „Verb"**: SkillVerb-CRUD; Spalten Checkbox | Verb (sortierbar) | Beschreibung | Aktionen.
- **Tabelle 2 „Domäne"**: SkillDomain-CRUD; Spalten Checkbox | Domäne (sortierbar) | Beschreibung | Aktionen.
- Sortierung der Namensspalte: 3-Zustand (unsortiert → aufsteigend → absteigend → unsortiert), Symbol ⇅ / ▲ / ▼.
- Massenauswahl: Checkbox im Header wählt alle/keine aus; bei aktiver Auswahl erscheint ein „X löschen"-Button im Header.
- Aktionen pro Zeile: Anzeigen · Bearbeiten (navigieren zu Detail-View) · Löschen.
  - Verb: navigiert zu `/tile/skills/<index>`
  - Domäne: navigiert zu `/tile/skills/domains/<index>`
- Selektierte Zeilen werden farblich hervorgehoben (`attr-row-selected`).
- „+ Neues Verb" / „+ Neue Domäne" öffnen je ein Inline-Formular (Name + Beschreibung).
- Leere Liste: Hinweistext „Noch keine Verben vorhanden." / „Noch keine Domänen vorhanden."
- Fehlertext bei Speicher-/API-Fehler.

## Entscheidungen

- **CSS-Wiederverwendung aus `AttributeView.css`**: Alle Tabellen-, Button- und Formular-Styles sind identisch — keine separate CSS-Datei.
- **Zwei getrennte State-Blöcke + Persist-Funktionen**: `skills`/`domains` und `persistSkills()`/`persistDomains()` — beide persistieren das gesamte `rulesetData`-Objekt, schreiben aber jeweils nur ein Feld.
- **Sortierung arbeitet auf `originalIndex`**: Renderreihenfolge geändert, Array-Index bleibt stabil; CRUD-Operationen operieren immer auf `originalIndex`.
- **`persist()`-Muster wie `AttributeView`**: fileHandle → XML exportieren + schreiben; sonst `saveRuleset` via API.
- **Route-Reihenfolge in App.tsx**: `/tile/skills/domains/:index` muss VOR `/tile/skills/:index` stehen, damit „domains" nicht als Index-Parameter interpretiert wird.

## Implementierung

| Artefakt | Pfad |
|---|---|
| Listen-View (beide Tabellen) | `frontend/src/views/SkillVerbView.tsx` |
| Detail-View SkillVerb | `frontend/src/views/SkillVerbDetailView.tsx` |
| Detail-View SkillDomain | `frontend/src/views/SkillDomainDetailView.tsx` |
| CSS (wiederverwendet) | `frontend/src/views/AttributeView.css` |
| Route-Registrierung | `frontend/src/App.tsx` (`/tile/skills`, `/tile/skills/domains/:index`, `/tile/skills/:index`) |
| Skills-Kachel (Einstiegspunkt) | `frontend/src/views/EditRulesetView.tsx` |

## Rekonstruktion

```
View /tile/skills mit zwei Tabellen:
Tabelle 1 "Verb" (SkillVerb): Checkbox-Spalte, sortierbare "Verb"-Spalte, Beschreibung, Aktionen
(Anzeigen/Bearbeiten → /tile/skills/<index>, Löschen). Massenauswahl + Inline-Formular.
Tabelle 2 "Domäne" (SkillDomain): analog, Aktionen → /tile/skills/domains/<index>.
Separate State-Blöcke (skills/domains) und separate persistSkills()/persistDomains() Funktionen.
CSS aus AttributeView.css. Route /tile/skills/domains/:index vor /tile/skills/:index in App.tsx.
Detail-View SkillVerbDetailView: isNew (index="neu") oder Edit, Duplikat-Check, navigiert /tile/skills.
Detail-View SkillDomainDetailView: analog, liest rulesetData.skillDomains.
```

Kontext: `SkillVerb`/`SkillDomain` in `api.ts` je mit `name: string` und `description: string`. `RulesetData.skills` ist `SkillVerb[]`, `RulesetData.skillDomains` ist `SkillDomain[]`. EditRulesetView zeigt in der Skills-Kachel beide Zählungen: `${skillCount} Verben, ${domainCount} Domänen`.
