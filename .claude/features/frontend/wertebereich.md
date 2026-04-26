# Wertebereich

## Ziel

Der Nutzer kann unter `/tile/werte` den numerischen Wertebereich eines Regelwerks festlegen: den untersten Wert (Minimum), den Durchschnittswert und den obersten Wert (Maximum). Diese drei Werte definieren die Skala, auf der alle Attribute und Skills eines Regelwerks bewertet werden. Sie werden als `<wertebereich>`-Element in der XML-Datei gespeichert.

## Anforderungen

- Route: `/tile/werte` (spezifische Route vor dem generischen `/tile/:id`, damit `DetailView` nicht greift).
- Drei bearbeitbare Ganzzahl-Felder: **Unterster Wert** (`min`), **Durchschnittswert** (`average`), **Oberster Wert** (`max`).
- Invariante: `min ≤ average ≤ max`. Bei Verletzung wird eine Fehlermeldung angezeigt und der Speichern-Button deaktiviert.
- Speichern erfolgt explizit per Button (kein Auto-Persist). Nach erfolgreichem Speichern zeigt der Button kurz „✓ Gespeichert" (1800 ms), danach wieder „Speichern".
- Kein Regelwerk geladen (`xmlContent === null`): Speichern-Button ist deaktiviert.
- Standardwerte wenn kein `<wertebereich>`-Element in der XML vorhanden: `min=-10`, `average=0`, `max=10`.
- Das `<wertebereich>`-Element wird als erstes Kind von `<ruleset>` eingefügt, falls noch nicht vorhanden.
- Zurück-Button navigiert zu `/edit-ruleset`.
- Änderungen werden in `xmlContent` (Context) und — sofern `fileHandle` vorhanden — in die Datei geschrieben.
- Die Kachel „Werte" ist die erste Kachel in der `EditRulesetView`.

## Entscheidungen

- **Expliziter Speichern-Button statt Auto-Persist**: Bei Zahlenfeldern ist Auto-Persist auf jeden Keystroke störend, da Zwischenzustände (z. B. `-` beim Eintippen von `-5`) regelmäßig ungültig sind. Ein Button macht den Speicherzeitpunkt explizit.
- **XML-Struktur als Attribut-Node**: `<wertebereich min="-10" max="10" average="0"/>` statt separater Kind-Elemente — kompakt und konsistent mit der bestehenden XML-Struktur (`<attribute name="X" value="0"/>`).
- **DOMParser + XMLSerializer**: Konsistent mit dem Muster aus `AttributeView` — der restliche XML-Inhalt (attributeSet, skills) bleibt unberührt.
- **Kein Domänenobjekt in Java**: Der Wertebereich existiert nur im Frontend-XML. Ein Java-Pendant im `coreElements`-Modul ist noch nicht vorgesehen.

## Implementierung

| Artefakt | Pfad |
|---|---|
| View | `frontend/src/views/WerteView.tsx` |
| Styles | `frontend/src/views/WerteView.css` |
| Routing (Route `/tile/werte` ergänzt) | `frontend/src/App.tsx` |
| Kachel in EditRulesetView (erste Position) | `frontend/src/views/EditRulesetView.tsx` |
| Context (unverändert, liefert `xmlContent`, `setXmlContent`, `fileHandle`) | `frontend/src/context/RulesetContext.tsx` |

## Rekonstruktion

```
Erstelle eine View unter /tile/werte für den Wertebereich eines Regelwerks:
- Drei Zahlenfelder: "Unterster Wert" (min), "Durchschnittswert" (average), "Oberster Wert" (max)
- Validierung: min ≤ average ≤ max; bei Verletzung Fehlermeldung + Speichern deaktiviert
- Expliziter Speichern-Button; nach Speichern kurz "✓ Gespeichert" anzeigen (1800 ms)
- Werte aus xmlContent lesen (<wertebereich min="-10" max="10" average="0"/>);
  Fallback-Defaults: min=-10, average=0, max=10
- Speichern: xmlContent im Context aktualisieren + via fileHandle in Datei schreiben
- <wertebereich>-Element als erstes Kind von <ruleset> einfügen falls nicht vorhanden
- Zurück-Button navigiert zu /edit-ruleset
- In EditRulesetView eine Kachel "Werte" / "Bearbeite den Wertebereich" als erste Kachel ergänzen
- Route /tile/werte vor /tile/:id in App.tsx registrieren
```

Kontext: `RulesetContext` (`frontend/src/context/RulesetContext.tsx`) stellt `xmlContent` (XML-String), `setXmlContent` und `fileHandle` bereit. XML-Wurzelstruktur: `<ruleset><wertebereich .../><attributeSet>…</attributeSet><skills>…</skills></ruleset>`. Die Route muss vor `/tile/:id` stehen, sonst greift der generische `DetailView`.
