# Charactereditor

## Ziel

Erlaubt es, auf Basis eines geladenen Regelwerks einen Charakter zu erstellen: Name, Attributwerte (innerhalb des definierten Wertebereichs) und Fertigkeitslevel werden interaktiv eingestellt. Das Feature ist rein transient — kein Speichern, keine API-Anbindung.

## Anforderungen

- Die Kachel „Charactereditor" erscheint in der HomeView **nur**, wenn ein Regelwerk geladen ist (`currentRuleset !== null` im RulesetContext).
- Attributwerte werden auf `[valueRange.min, valueRange.max]` geklemmt; Initialbelegung ist `valueRange.average`.
- Fertigkeitslevel haben als Untergrenze 0 (kein oberes Limit definiert); Initialbelegung ist `0`.
- Jedes Attribut wird mit Slider **und** Zahleneingabe angezeigt; beide sind synchron.
- Jede Fertigkeit zeigt ihr verknüpftes Attribut (`linkedAttributeName`) in Klammern an.
- Enthält das Regelwerk weder Attribute noch Fertigkeiten, erscheint ein Hinweistext statt der leeren Abschnitte.
- Der Zustand (Name, Attributwerte, Fertigkeitslevel) wird einmalig beim Mounten aus dem RulesetContext initialisiert und danach lokal gehalten.

## Entscheidungen

- **Kein Persistieren, keine API** — bewusst aus dem Scope gestrichen; der Charakter lebt nur im Arbeitsspeicher bis zur nächsten Navigation.
- **State einmalig initialisieren** — `useState(() => ...)` mit Lazy-Initializer, damit der Zustand nicht bei jedem Re-Render des Kontexts zurückgesetzt wird.
- **`DetailView.css` als gemeinsame Basis** — Layout-Klassen `.detail-view` und `.back-button` werden projektübergreifend aus `DetailView.css` wiederverwendet; eigene Styles ergänzen nur die Charactereditor-spezifischen Elemente.

## Implementierung

| Artefakt | Pfad |
|---|---|
| View-Komponente | `frontend/src/views/CharacterEditorView.tsx` |
| View-Styles | `frontend/src/views/CharacterEditorView.css` |
| Tile-Eintrag (HomeView) | `frontend/src/views/HomeView.tsx` |
| Route-Registrierung | `frontend/src/App.tsx` |

## Rekonstruktion

```
Neues Feature: Charactereditor-View im Frontend.
- Neue Kachel "Charactereditor" in HomeView — nur sichtbar, wenn ein Regelwerk geladen ist (currentRuleset !== null).
- Route /character-editor → CharacterEditorView.
- View zeigt: Namensfeld, alle Attribute aus rulesetData mit Slider + Zahleneingabe (Wertebereich min/max, Initialwert average), alle Skills mit Zahleneingabe (min 0, Initialwert 0) und Anzeige des linkedAttributeName.
- Kein Speichern, keine API — nur lokaler State.
```

Kontext: RulesetContext stellt `currentRuleset`, `rulesetData` (Typen `Attribute`, `Skill`, `ValueRange` aus `api.ts`) und `rulesetData.valueRange` bereit. Routing über React Router v6 in `App.tsx`. Layout-Basis aus `DetailView.css` (`.detail-view`, `.back-button`).
