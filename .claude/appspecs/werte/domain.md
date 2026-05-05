# Werte — Domänespezifikation

## Klassen

### `Value`

Kapselt einen einzelnen ganzzahligen Betrag. Unveränderliches Record.

| Feld | Typ | Beschreibung |
|------|-----|-------------|
| `amount` | `int` | Der Zahlenwert |

Keine Validierung — jeder `int` ist erlaubt.

### `ValueRange`

Definiert einen Wertebereich mit Unter-, Durch- und Obergrenze. Unveränderliches Record.

| Feld | Typ | Beschreibung |
|------|-----|-------------|
| `min` | `int` | Unterster erlaubter Wert |
| `average` | `int` | Durchschnittswert |
| `max` | `int` | Oberster erlaubter Wert |

**Invariante:** `min ≤ average ≤ max` — wird im Compact Constructor geprüft; Verletzung wirft `IllegalArgumentException`.

## Beziehungen

`Value` und `ValueRange` sind voneinander unabhängig. `Value` wird von `Attribute` genutzt; `ValueRange` beschreibt den gültigen Rahmen für alle Attributwerte eines Regelwerks.

## Dateien

| Klasse | Pfad |
|--------|------|
| `Value` | [coreElements/…/Value.java](../../../coreElements/src/main/java/de/heiges/rulesengine/coreelements/domain/model/Value.java) |
| `ValueRange` | [coreElements/…/ValueRange.java](../../../coreElements/src/main/java/de/heiges/rulesengine/coreelements/domain/model/ValueRange.java) |
