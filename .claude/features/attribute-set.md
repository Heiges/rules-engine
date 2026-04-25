# AttributeSet

## Ziel

Geordnete Sammlung von `Attribute`-Objekten. Verhindert Duplikate und bietet CRUD-Operationen auf den enthaltenen Attributen. Typischerweise einem Charakter oder einer Regelgruppe zugeordnet.

## Anforderungen

- Keine zwei Attribute mit gleichem Namen erlaubt (Duplikat-Check bei `add`)
- Reihenfolge der Einfügung bleibt erhalten (`LinkedHashMap` intern)
- Operationen: `add`, `remove`, `modify` (Wert ändern), `find` (Optional), `contains`, `getAll`, `size`
- `getAll` gibt eine unveränderliche Collection zurück (kein direkter Zugriff auf die interne Map)
- Kein eigener fachlicher Schlüssel — kein `equals`/`hashCode` implementiert

## Entscheidungen

- Kein Name auf dem Set selbst: Benennung ist Aufgabe der nutzenden Schicht (z. B. ein Charakter)
- `modify` delegiert an `Attribute.setValue`, um Validierungslogik nicht zu duplizieren
- `find` gibt `Optional` zurück statt `null`
- Keine eigene Persistenz-Logik — liegt im `persistence`-Modul

## Rekonstruktion

```
/new-core-element AttributeSet
```

Kontext für den Prompt: geordnete Sammlung, kein Duplikat-Name, CRUD-Operationen, getAll unveränderlich, kein eigenes equals/hashCode.
