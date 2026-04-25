# Value

## Ziel

Repräsentiert den numerischen Wert eines Regelbausteins als eigenständige Domänenklasse. Ermöglicht positive und negative Werte, sodass Attribute sowohl Boni als auch Mali abbilden können. `Skill` hat vorerst keinen Wert.

## Anforderungen

- `amount` ist ein `int` ohne Einschränkung — positive, negative und Null-Werte sind erlaubt
- `Value` ist unveränderlich (immutable); ein neuer Wert ersetzt das bestehende Objekt
- Gleichheit wird strukturell über `amount` bestimmt (keine Identitätslogik)
- `Attribute` trägt genau einen `Value`; der Standardwert ist `Value(0)` wenn kein expliziter Wert übergeben wird
- `null` als Argument für `Value` in `Attribute` wird stillschweigend zu `Value(0)` normalisiert
- `Skill` hat keinen `Value` (bewusste Designentscheidung, kann sich künftig ändern)

## Entscheidungen

- **Java Record statt Klasse**: `Value` ist ein reines Wertobjekt ohne Identität — Record liefert `equals`, `hashCode` und Accessor gratis und macht die Absicht explizit
- **Kein Constraint auf Vorzeichen**: Regelbaustein-Werte können Boni (+) und Mali (−) sein; eine Einschränkung auf ≥ 0 würde fachliche Anforderungen ausschließen
- **Null-Normalisierung in `Attribute`**: Verhindert NullPointerExceptions ohne den Aufrufer zu zwingen, immer einen Wert zu liefern; konsistent mit der Behandlung von null-Beschreibungen
- **Skill bleibt ohne Wert**: Skills haben ein Level, aber keinen eigenen Zahlenwert — dieser käme ggf. aus dem verknüpften Attribut

## Implementierung

| Artefakt | Pfad |
|---|---|
| Domain-Klasse (Record) | `coreElements/src/main/java/de/heiges/rulesengine/coreelements/domain/model/Value.java` |
| Unit-Test | `coreElements/src/test/java/de/heiges/rulesengine/coreelements/domain/model/ValueTest.java` |
| Geändert: Domain-Klasse | `coreElements/src/main/java/de/heiges/rulesengine/coreelements/domain/model/Attribute.java` |
| Geändert: Unit-Test | `coreElements/src/test/java/de/heiges/rulesengine/coreelements/domain/model/AttributeTest.java` |

## Rekonstruktion

```
Neues Feature: Eine neue Domänenklasse namens Value. Value dient dazu den Wert eines
Regelbausteins zu speichern. Werte können positiv oder negativ sein.
Attribute haben einen Wert, Skills erstmal nicht.
```

Kontext: `Value` als Java Record mit einem `int amount` (keine Vorzeichenbeschränkung). `Attribute` erhält ein `Value`-Feld, Standardwert `Value(0)`. Bestehender 2-Arg-Konstruktor bleibt kompatibel (delegiert auf 3-Arg). Null-Argument → `Value(0)`. `Skill` bleibt unverändert. Tests in `ValueTest` und neue Tests in `AttributeTest` (defaultValueIsZero, acceptsPositiveValue, acceptsNegativeValue, updatesValue, nullValueDefaultsToZero).
