# Charactereditor

## Ziel

Erlaubt es, auf Basis eines geladenen Regelwerks einen Charakter zu erstellen: Name und Attributwerte (innerhalb des definierten Wertebereichs) werden interaktiv eingestellt. Pro Attribut kann gewürfelt werden (`POST /api/roll`). Das Feature ist rein transient — kein Speichern des Charakters.

## Anforderungen

- Die Kachel „Charactereditor" erscheint in der HomeView **nur**, wenn ein Regelwerk geladen ist (`currentRuleset !== null` im RulesetContext).
- Attributwerte werden auf `[valueRange.min, valueRange.max]` geklemmt; Initialbelegung ist `valueRange.average`.
- **Gruppierte Attribute** (gleicher `groupName`, nicht `'Allgemein'`) erscheinen im Editor als **ein einziger Eintrag** mit dem Gruppennamen — die einzelnen Attribute der Gruppe werden zusammengefasst.
- **Ungroupierte Attribute** (kein `groupName` oder `groupName === 'Allgemein'`) erscheinen weiterhin einzeln unter ihrem Attributnamen.
- Jeder Eintrag (Gruppe oder Einzelattribut) wird mit Slider **und** Zahleneingabe angezeigt; beide sind synchron.
- Skills werden mit Name und Beschreibung (falls vorhanden) angezeigt — rein informativ, kein Level-Input.
- Enthält das Regelwerk weder (effektive) Attribute noch Skills, erscheint ein Hinweistext statt der leeren Abschnitte.
- Der Zustand (Name, Attributwerte) wird einmalig beim Mounten aus dem RulesetContext initialisiert und danach lokal gehalten.
- Hinter jedem Eintrag gibt es einen „Würfeln"-Button; beim Klick wird `POST /api/roll` mit dem aktuellen Wert aufgerufen.
- Das Würfelergebnis wird inline in der Zeile angezeigt: alle geworfenen Würfel als kleine Kacheln, Pasch-Würfel farblich hervorgehoben, daneben „Erfolg (N× X)" oder „Misserfolg".
- Erfolg = mindestens ein Pasch (≥ 2 gleiche Würfel); Misserfolg sonst.
- Während des laufenden Requests ist der Button deaktiviert und zeigt „…".
- Das letzte Ergebnis bleibt stehen bis zum nächsten Würfelwurf für diesen Eintrag.

## Entscheidungen

- **Kein Level-Input für Skills**: `SkillVerb` hat kein `level`-Feld mehr; der Charactereditor zeigt Skills daher nur informativ an.
- **Kein `linkedAttributeName`**: `SkillVerb` hat keine Attribut-Bindung; die frühere Klammern-Anzeige `(Attributname)` entfällt.
- **Kein Persistieren, keine API für den Charakter** — der Charakter lebt nur im Arbeitsspeicher bis zur nächsten Navigation.
- **State einmalig initialisieren** — `useState(() => ...)` mit Lazy-Initializer, damit der Zustand nicht bei jedem Re-Render des Kontexts zurückgesetzt wird.
- **`DetailView.css` als gemeinsame Basis** — Layout-Klassen `.detail-view` und `.back-button` werden projektübergreifend wiederverwendet.
- **Würfel-API-Typen in `api.ts`** — `RollResult`-Interface und `rollDice()`-Funktion liegen zentral in `api.ts`, nicht inline in der View.
- **Pro-Eintrag-Ergebnisstate** — `Record<string, RollResult>` mit dem effektiven Key (Gruppenname oder Attributname) als Schlüssel.
- **`buildEffectiveAttrs`-Hilfsfunktion** — berechnet die deduplizierte Liste der anzuzeigenden Einträge aus dem `attributes`-Array; Gruppen werden anhand von `groupName` (≠ `'Allgemein'`) erkannt und nur einmalig eingetragen. Das `isGroup`-Flag ist für spätere visuelle Differenzierung vorgehalten.

## Implementierung

| Artefakt | Pfad |
|---|---|
| View-Komponente | `frontend/src/views/CharacterEditorView.tsx` |
| View-Styles | `frontend/src/views/CharacterEditorView.css` |
| API-Funktion + Typ | `frontend/src/api.ts` (`rollDice`, `RollResult`) |
| Tile-Eintrag (HomeView) | `frontend/src/views/HomeView.tsx` |
| Route-Registrierung | `frontend/src/App.tsx` |

## Rekonstruktion

```
Neues Feature: Charactereditor-View im Frontend.
- Neue Kachel "Charactereditor" in HomeView — nur sichtbar, wenn ein Regelwerk geladen ist.
- Route /character-editor → CharacterEditorView.
- View zeigt: Namensfeld, alle Attribute aus rulesetData mit Slider + Zahleneingabe (Wertebereich
  min/max, Initialwert average) + "Würfeln"-Button pro Attribut, alle Skills (SkillVerb) mit Name
  und Beschreibung — rein informativ.
- Würfeln: POST /api/roll { value } → { dice, success, paschValue, paschCount }.
  Erfolg wenn success=true. Würfel als Kacheln anzeigen, Pasch-Würfel hervorgehoben.
- Kein Speichern des Charakters — nur lokaler State.
```

Kontext: `POST /api/roll` existiert im Backend (`RollController`). `RollResult`-Interface in `api.ts`. RulesetContext stellt `rulesetData` (Typen aus `api.ts`) bereit. `SkillVerb` hat nur `name` und `description`, kein `level` und kein `linkedAttributeName`. Routing über React Router v6. Layout-Basis aus `DetailView.css`.

### Änderung: Gruppierte Attribute im Editor

```
Charactereditor anpassen: Gruppierte Attribute (groupName ≠ 'Allgemein') sollen im Editor
als ein einziger Eintrag mit dem Gruppennamen erscheinen, nicht als einzelne Attribute.
Ungroupierte Attribute bleiben einzeln.
```

Kontext: Jedes `Attribute` in `rulesetData.attributes` hat ein optionales `groupName`-Feld (befüllt durch `fromApiDto` in `api.ts`). `groupName === 'Allgemein'` gilt als ungroupiert. Hilfsfunktion `buildEffectiveAttrs` in `CharacterEditorView.tsx`.
