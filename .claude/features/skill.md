# Skill

## Ziel

Fähigkeit einer Spielfigur, die auf einem `Attribute` basiert. Ein Skill hat ein Level, das den Grad der Beherrschung ausdrückt, und ist fest an ein Attribut gebunden (z. B. Skill „Klettern" → Attribut „Stärke").

## Anforderungen

- Name ist unveränderlich (final) und darf nicht leer oder blank sein
- `linkedAttribute` ist Pflichtfeld, darf nicht `null` sein
- Level ist ein `int ≥ 0`, nachträglich änderbar via `setLevel`
- `equals`/`hashCode` ausschließlich über den Namen (fachlicher Schlüssel)
- `toString` für Debugging: `Skill{name='...', linkedAttribute=..., level=...}`

## Entscheidungen

- Referenz auf `Attribute`-Objekt statt nur auf den Namen: ermöglicht direkten Zugriff auf Attributwerte ohne Lookup
- Name ist `final`: wie bei `Attribute` ist der Name Identität
- Keine Obergrenze für Level: regelabhängig, gehört in die Regel-Schicht
- `linkedAttribute` ist unveränderlich: ein Skill wechselt nicht die Attribut-Grundlage

## Rekonstruktion

```
/new-core-element Skill
```

Kontext für den Prompt: Name unveränderlich, linkedAttribute Pflicht-Referenz auf Attribute-Objekt (nicht nur Name), Level int ≥ 0 und mutabel, equals/hashCode nur über Name.
