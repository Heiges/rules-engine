# Cheats — Domänespezifikation

## Klassen

### `Cheat`

Regelausnahme oder Sonderregel mit unveränderlichem Namen und änderbarer Beschreibung.

| Feld | Typ | Veränderlich | Beschreibung |
|------|-----|-------------|-------------|
| `name` | `String` | nein | Eindeutige Kennung; darf nicht leer sein |
| `description` | `String` | ja | Freitext; `null` wird zu `""` normalisiert |

**Gleichheit:** ausschließlich über `name`.

## Beziehungen

`Cheat` ist eigenständig und hat keine Abhängigkeiten zu anderen Domänenklassen.

## Dateien

| Klasse | Pfad |
|--------|------|
| `Cheat` | [coreElements/…/Cheat.java](../../../coreElements/src/main/java/de/heiges/rulesengine/coreelements/domain/model/Cheat.java) |
