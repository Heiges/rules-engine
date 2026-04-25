# Attribute

## Ziel

Grundlegender Baustein des Domänenmodells. Repräsentiert eine benannte Eigenschaft einer Spielfigur (z. B. Stärke, Geschicklichkeit) mit einer optionalen Beschreibung. Dient als Referenz für Skills.

## Anforderungen

- Name ist unveränderlich (final) und darf nicht leer oder blank sein
- Beschreibung ist ein `String`, nachträglich änderbar via `setDescription`; `null` wird intern zu leerem String normalisiert
- Kein Duplikat-Schutz auf dieser Ebene — liegt in `AttributeSet`
- `equals`/`hashCode` ausschließlich über den Namen (fachlicher Schlüssel)
- `toString` für Debugging: `Attribute{name='...', description='...'}`

## Entscheidungen

- Name ist `final`: Name ist Identität, eine Umbenennung wäre ein neues Attribute
- Beschreibung ist mutabel: kann nachträglich gepflegt werden
- `value` wurde entfernt: Zahlenwerte werden in einer eigenen Domänenklasse modelliert (noch nicht implementiert)
- `null`-Beschreibung wird stillschweigend zu leerem String normalisiert, um NPE-Risiken zu vermeiden

## Implementierung

| Artefakt | Pfad |
|---|---|
| Domain-Klasse | `coreElements/src/main/java/de/heiges/rulesengine/coreelements/domain/model/Attribute.java` |
| Unit-Test | `coreElements/src/test/java/de/heiges/rulesengine/coreelements/domain/model/AttributeTest.java` |
| Persistenz-DTO | `persistence/src/main/java/de/heiges/rulesengine/persistence/xml/dto/AttributeDto.java` |

## Rekonstruktion

```
/new-core-element Attribute
```

Kontext für den Prompt: Name unveränderlich, Beschreibung als String mutabel, `null` → leer normalisiert, equals/hashCode nur über Name. Kein `value`-Feld — Zahlenwerte sind eigene Domänenklasse.
