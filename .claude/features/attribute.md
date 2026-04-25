# Attribute

## Ziel

Grundlegender Baustein des Domänenmodells. Repräsentiert eine benannte, messbare Eigenschaft einer Spielfigur (z. B. Stärke, Geschicklichkeit). Dient als Referenz für Skills.

## Anforderungen

- Name ist unveränderlich (final) und darf nicht leer oder blank sein
- Wert ist ein `int ≥ 0`, nachträglich änderbar via `setValue`
- Kein Duplikat-Schutz auf dieser Ebene — liegt in `AttributeSet`
- `equals`/`hashCode` ausschließlich über den Namen (fachlicher Schlüssel)
- `toString` für Debugging: `Attribute{name='...', value=...}`

## Entscheidungen

- Name ist `final`: Name ist Identität, eine Umbenennung wäre ein neues Attribute
- Wert ist mutabel: Spielwerte ändern sich im Spielverlauf
- Keine Obergrenze für den Wert: Regelwerke variieren, Grenzwerte gehören in die Regel-Schicht

## Rekonstruktion

```
/new-core-element Attribute
```

Kontext für den Prompt: Name unveränderlich, Wert int ≥ 0 und mutabel, equals/hashCode nur über Name.
