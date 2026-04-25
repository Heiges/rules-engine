# Edit-Regelwerk-View

## Ziel

Nach dem Klick auf die „Regelwerk bearbeiten"-Kachel gelangt der Nutzer in eine dedizierte Bearbeitungsansicht. Dort sieht er Kacheln für die einzelnen Bestandteile des Regelwerks (zunächst: Attribute). Die Kachel-Beschreibung zeigt direkt, wie viele Einträge bereits vorhanden sind, sodass der Nutzer auf einen Blick den aktuellen Stand erkennt.

## Anforderungen

- Route: `/edit-ruleset` — eigene Route, nicht über den generischen `/tile/:id`-Mechanismus.
- Überschrift: `<Regelwerkname> bearbeiten` (Dateiname ohne `.xml`-Endung, case-insensitiv).
- Zurück-Button navigiert zu `/` (HomeView).
- Kachel „Attribute":
  - Name: `Attribute`
  - Beschreibung: `Attribute bearbeiten (<n> Attribute)` — `n` ist die tatsächliche Anzahl der `<attribute>`-Elemente im geladenen XML.
  - Navigiert per Standard-Tile-Verhalten zu `/tile/attributes`.
- Attributanzahl wird clientseitig aus dem im Context gespeicherten XML ermittelt (`DOMParser`, Selektor `attributeSet > attribute`).
- Wenn `xmlContent` im Context `null` ist, wird `0` angezeigt (kein Fehler).

## Entscheidungen

- **`xmlContent` in RulesetContext statt lokales Parsen**: Das XML wird beim Laden/Erstellen einmalig im Context gespeichert und steht allen Views zur Verfügung. Dadurch kein erneutes Lesen der Datei in der View.
- **`DOMParser` statt Regex**: Robusteres Parsen; liefert `0` bei invalider XML ohne Ausnahme.
- **CSS-Wiederverwendung**: `EditRulesetView` importiert `DetailView.css` (`.back-button`) und `HomeView.css` (`.tile-grid`) — keine separate CSS-Datei nötig.
- **Explizites `onClick` statt Default-Tile-Navigation in HomeView**: Die Edit-Kachel in `HomeView` bekommt `onClick={() => navigate('/edit-ruleset')}`, weil der Default-Mechanismus zu `/tile/edit-ruleset` navigieren würde.
- **`handleFileChange` auf `async` umgestellt**: Notwendig, um `file.text()` awaiten und `setXmlContent` befüllen zu können.

## Implementierung

| Artefakt | Pfad |
|---|---|
| View (neu) | `frontend/src/views/EditRulesetView.tsx` |
| Context (erweitert: `xmlContent`, `setXmlContent`) | `frontend/src/context/RulesetContext.tsx` |
| HomeView (erweitert: XML lesen, navigate zu `/edit-ruleset`) | `frontend/src/views/HomeView.tsx` |
| Routing (Route `/edit-ruleset` ergänzt) | `frontend/src/App.tsx` |
| CSS (unverändert, wiederverwendet) | `frontend/src/views/DetailView.css` |
| CSS (unverändert, wiederverwendet) | `frontend/src/views/HomeView.css` |

## Rekonstruktion

```
Wenn man auf die Kachel "Regelwerk bearbeiten" klickt, soll man in eine neue View /edit-ruleset
wechseln. Diese View zeigt eine Kachel:
- Name: "Attribute"
- Beschreibung: "Attribute bearbeiten (<n> Attribute)"
  - n = Anzahl der <attribute>-Elemente in attributeSet des geladenen XML
Die View hat einen Zurück-Button zur Startseite und eine Überschrift "<Regelwerkname> bearbeiten".
```

Kontext: `RulesetContext` (`frontend/src/context/RulesetContext.tsx`) speichert `currentRuleset` (Dateiname) und `fileHandle`. Das XML wird beim Laden (`handleFileChange` via `file.text()`) und beim Erstellen (`EMPTY_RULESET_XML`) in `xmlContent` im Context abgelegt. Die `Tile`-Komponente navigiert per Default zu `/tile/<id>`. Routing in `App.tsx` via React Router.
