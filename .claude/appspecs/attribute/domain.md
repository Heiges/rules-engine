# Attribute — Domänespezifikation

## Klassen

### `Attribute`

Einzelnes Attribut mit unveränderlichem Namen, änderbarer Beschreibung und einem Wert.

| Feld | Typ | Veränderlich | Beschreibung |
|------|-----|-------------|-------------|
| `name` | `String` | nein | Eindeutige Kennung; darf nicht leer sein |
| `description` | `String` | ja | Freitext; `null` wird zu `""` normalisiert |
| `value` | `Value` | ja | Aktueller Zahlenwert; `null` wird zu `Value(0)` normalisiert |

**Gleichheit:** ausschließlich über `name` (equals/hashCode).

### `AttributeGroup`

Geordnete, benannte Sammlung von Attributen. Name ist unveränderlich.

| Feld | Typ | Beschreibung |
|------|-----|-------------|
| `name` | `String` | Unveränderlicher Gruppenname; darf nicht leer sein |
| `attributes` | `LinkedHashMap<String, Attribute>` | Einfügereihenfolge bleibt erhalten |

**Invariante:** kein doppelter Attributname innerhalb der Gruppe.

| Methode | Beschreibung |
|---------|-------------|
| `add(Attribute)` | Fügt Attribut hinzu; wirft bei Duplikat |
| `remove(String name)` | Entfernt nach Name; wirft wenn nicht vorhanden |
| `modify(String name, String newDescription)` | Ändert Beschreibung; wirft wenn nicht vorhanden |
| `find(String name)` | Gibt `Optional<Attribute>` zurück |
| `contains(String name)` | Prüft Existenz |
| `getAll()` | Unmodifizierbare Collection in Einfügereihenfolge |

**Gleichheit:** ausschließlich über `name`.

### `AttributeSet`

Container für mehrere `AttributeGroup`-Instanzen. Stellt sicher, dass kein Attributname gruppenübergreifend doppelt vorkommt.

| Feld | Typ | Beschreibung |
|------|-----|-------------|
| `groups` | `LinkedHashMap<String, AttributeGroup>` | Einfügereihenfolge bleibt erhalten |

**Invariante:** kein doppelter Gruppenname; kein Attributname darf in mehr als einer Gruppe vorkommen.

| Methode | Beschreibung |
|---------|-------------|
| `addGroup(AttributeGroup)` | Fügt Gruppe hinzu; prüft Duplikate auf Gruppen- und Attributebene |
| `removeGroup(String groupName)` | Entfernt Gruppe; wirft wenn nicht vorhanden |
| `findGroup(String groupName)` | Gibt `Optional<AttributeGroup>` zurück |
| `containsGroup(String groupName)` | Prüft Existenz der Gruppe |
| `getGroups()` | Unmodifizierbare Collection aller Gruppen |
| `find(String name)` | Sucht Attribut gruppenübergreifend; gibt `Optional<Attribute>` zurück |
| `contains(String name)` | Prüft gruppenübergreifend |
| `getAll()` | Alle Attribute aller Gruppen als flache Liste |
| `size()` | Gesamtanzahl aller Attribute |

## Beziehungen

```
AttributeSet
  └── 0..* AttributeGroup
              └── 0..* Attribute
                          └── Value
```

## Dateien

| Klasse | Pfad |
|--------|------|
| `Attribute` | [coreElements/…/Attribute.java](../../../coreElements/src/main/java/de/heiges/rulesengine/coreelements/domain/model/Attribute.java) |
| `AttributeGroup` | [coreElements/…/AttributeGroup.java](../../../coreElements/src/main/java/de/heiges/rulesengine/coreelements/domain/model/AttributeGroup.java) |
| `AttributeSet` | [coreElements/…/AttributeSet.java](../../../coreElements/src/main/java/de/heiges/rulesengine/coreelements/domain/model/AttributeSet.java) |
