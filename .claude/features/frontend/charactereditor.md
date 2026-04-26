# Charactereditor

## Ziel

Erlaubt es, auf Basis eines geladenen Regelwerks einen Charakter zu erstellen: Name, Attributwerte (innerhalb des definierten Wertebereichs) und Fertigkeitslevel werden interaktiv eingestellt. Pro Attribut kann gewürfelt werden (`POST /api/roll`). Das Feature ist rein transient — kein Speichern des Charakters.

## Anforderungen

- Die Kachel „Charactereditor" erscheint in der HomeView **nur**, wenn ein Regelwerk geladen ist (`currentRuleset !== null` im RulesetContext).
- Attributwerte werden auf `[valueRange.min, valueRange.max]` geklemmt; Initialbelegung ist `valueRange.average`.
- Fertigkeitslevel haben als Untergrenze 0 (kein oberes Limit definiert); Initialbelegung ist `0`.
- Jedes Attribut wird mit Slider **und** Zahleneingabe angezeigt; beide sind synchron.
- Jede Fertigkeit zeigt ihr verknüpftes Attribut (`linkedAttributeName`) in Klammern an.
- Enthält das Regelwerk weder Attribute noch Fertigkeiten, erscheint ein Hinweistext statt der leeren Abschnitte.
- Der Zustand (Name, Attributwerte, Fertigkeitslevel) wird einmalig beim Mounten aus dem RulesetContext initialisiert und danach lokal gehalten.
- Hinter jedem Attribut gibt es einen „Würfeln"-Button; beim Klick wird `POST /api/roll` mit dem aktuellen Attributwert aufgerufen.
- Das Würfelergebnis wird inline in der Attributzeile angezeigt: alle geworfenen Würfel als kleine Kacheln, Pasch-Würfel farblich hervorgehoben, daneben „Erfolg (N× X)" oder „Misserfolg".
- Erfolg = mindestens ein Pasch (≥ 2 gleiche Würfel); Misserfolg sonst.
- Während des laufenden Requests ist der Button deaktiviert und zeigt „…".
- Das letzte Ergebnis bleibt stehen bis zum nächsten Würfelwurf für dieses Attribut.

## Entscheidungen

- **Kein Persistieren, keine API für den Charakter** — der Charakter lebt nur im Arbeitsspeicher bis zur nächsten Navigation.
- **State einmalig initialisieren** — `useState(() => ...)` mit Lazy-Initializer, damit der Zustand nicht bei jedem Re-Render des Kontexts zurückgesetzt wird.
- **`DetailView.css` als gemeinsame Basis** — Layout-Klassen `.detail-view` und `.back-button` werden projektübergreifend wiederverwendet.
- **Würfel-API-Typen in `api.ts`** — `RollResult`-Interface und `rollDice()`-Funktion liegen zentral in `api.ts`, nicht inline in der View.
- **Pro-Attribut-Ergebnisstate** — `Record<string, RollResult>` statt eines einzelnen Ergebnisses, damit mehrere Attributwürfe gleichzeitig sichtbar bleiben.

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
- Neue Kachel "Charactereditor" in HomeView — nur sichtbar, wenn ein Regelwerk geladen ist (currentRuleset !== null).
- Route /character-editor → CharacterEditorView.
- View zeigt: Namensfeld, alle Attribute aus rulesetData mit Slider + Zahleneingabe (Wertebereich min/max,
  Initialwert average) + "Würfeln"-Button pro Attribut, alle Skills mit Zahleneingabe (min 0, Initialwert 0)
  und Anzeige des linkedAttributeName.
- Würfeln: POST /api/roll { value } → { dice, success, paschValue, paschCount }.
  Erfolg wenn success=true (mindestens Pasch). Würfel als Kacheln anzeigen, Pasch-Würfel hervorgehoben.
- Kein Speichern des Charakters — nur lokaler State.
```

Kontext: `POST /api/roll` existiert im Backend (`RollController`). `RollResult`-Interface in `api.ts` hinzufügen. RulesetContext stellt `rulesetData` (Typen aus `api.ts`) bereit. Routing über React Router v6. Layout-Basis aus `DetailView.css`.
