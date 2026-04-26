# Neues Regelwerk erstellen

> **Hinweis:** Ersetzt den früheren Ansatz mit `showSaveFilePicker`. Anlegen erfolgt jetzt über `POST /api/rulesets/{name}`.

## Ziel

Ermöglicht dem Nutzer, über die HomeView ein neues, leeres Regelwerk anzulegen. Der Name wird per `window.prompt` abgefragt; das Regelwerk wird serverseitig erstellt und sofort zum Bearbeiten geöffnet.

## Anforderungen

- Klick auf „Neues Regelwerk" → `window.prompt` für den Namen
- `POST /api/rulesets/{name}` legt ein leeres Regelwerk an (Default: min=-10, average=0, max=10, keine Attribute, keine Skills)
- Danach `GET /api/rulesets/{name}` → `rulesetData` im Context setzen
- Navigation zu `/edit-ruleset`
- Bei leerem Namen oder Abbrechen: keine Aktion
- 409-Fehler (Name vergeben): Fehlertext auf der HomeView

## Entscheidungen

- **`POST` statt File-Dialog**: Kein Browser-API-Support-Problem, kein Speicherort-Problem.
- **Leeres Regelwerk serverseitig**: `XmlRulesetRepository.save()` wird vom API-Controller mit Default-`ValueRange` und leerem `AttributeSet` aufgerufen — konsistent mit dem Rest der Persistenzlogik.
- **`window.prompt` als minimale Eingabe**: Reicht für lokale Nutzung; kann später durch eine eigene Komponente ersetzt werden.

## Implementierung

| Artefakt | Pfad |
|---|---|
| Handler `handleNew` | `frontend/src/views/HomeView.tsx` |
| `createRuleset(name)` | `frontend/src/api.ts` |
| API-Endpoint `POST /api/rulesets/{name}` | `api/src/main/java/de/heiges/rulesengine/api/controller/RulesetController.java` |

## Rekonstruktion

```
handleNew in HomeView:
  const name = window.prompt('Name des neuen Regelwerks:')?.trim()
  if (!name) return
  await createRuleset(name)          // POST /api/rulesets/{name}
  const data = await loadRuleset(name)
  setCurrentRuleset(name)
  setRulesetData(data)
  navigate('/edit-ruleset')
```
