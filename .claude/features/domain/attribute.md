# Attribute

## Ziel

Grundlegender Baustein des Domänenmodells. Repräsentiert eine benannte Eigenschaft einer Spielfigur (z. B. Stärke, Geschicklichkeit) mit einer optionalen Beschreibung. Dient als Referenz für Skills.

## Anforderungen

- Name ist unveränderlich (final) und darf nicht leer oder blank sein
- Beschreibung ist ein `String`, nachträglich änderbar via `setDescription`; `null` wird intern zu leerem String normalisiert
- Trägt genau einen `Value`; Standardwert ist `Value(0)`, `null` wird stillschweigend normalisiert
- `Value` ist änderbar via `setValue(Value)`
- Kein Duplikat-Schutz auf dieser Ebene — liegt in `AttributeSet`
- `equals`/`hashCode` ausschließlich über den Namen (fachlicher Schlüssel)
- `toString` für Debugging: `Attribute{name='...', description='...', value=Value{amount=...}}`

## Entscheidungen

- Name ist `final`: Name ist Identität, eine Umbenennung wäre ein neues Attribute
- Beschreibung ist mutabel: kann nachträglich gepflegt werden
- `Value` ist eine eigene Domänenklasse (Record) — kein primitives `int` im Attribute, um den Wertebereich (pos./neg.) fachlich auszudrücken
- `null`-Beschreibung und `null`-Value werden stillschweigend normalisiert, um NPE-Risiken zu vermeiden

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

Kontext für den Prompt: Name unveränderlich, Beschreibung als String mutabel, `null` → leer normalisiert, equals/hashCode nur über Name. `Value`-Feld (Record) mit Standardwert `Value(0)`, null-sicher normalisiert. Siehe auch: [Value](value-domain-class.md).
