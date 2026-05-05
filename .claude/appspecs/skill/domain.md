# Skills — Domänespezifikation

## Klassen

### `SkillVerb`

Benennt eine Fähigkeit (das „Was" eines Skills). Name ist unveränderlich.

| Feld | Typ | Veränderlich | Beschreibung |
|------|-----|-------------|-------------|
| `name` | `String` | nein | Eindeutige Kennung; darf nicht leer sein |
| `description` | `String` | ja | Freitext; `null` wird zu `""` normalisiert |

**Gleichheit:** ausschließlich über `name`.

### `SkillDomain`

Kategorisiert Skills fachlich (das „Wo" eines Skills). Struktur analog zu `SkillVerb`.

| Feld | Typ | Veränderlich | Beschreibung |
|------|-----|-------------|-------------|
| `name` | `String` | nein | Eindeutige Kennung; darf nicht leer sein |
| `description` | `String` | ja | Freitext; `null` wird zu `""` normalisiert |

**Gleichheit:** ausschließlich über `name`.

### `Skill`

Konkrete Fähigkeit eines Charakters: verknüpft einen Namen mit einem Attribut und einem Level.

| Feld | Typ | Veränderlich | Beschreibung |
|------|-----|-------------|-------------|
| `name` | `String` | nein | Eindeutige Kennung; darf nicht leer sein |
| `linkedAttribute` | `Attribute` | nein | Das zugeordnete Attribut; darf nicht `null` sein |
| `level` | `int` | ja | Ausprägungsstärke; muss `≥ 0` sein |

**Gleichheit:** ausschließlich über `name`.

## Beziehungen

`SkillVerb` und `SkillDomain` sind derzeit unabhängige Katalogeinträge ohne direkte Referenz aufeinander oder auf `Skill`. `Skill` referenziert ein `Attribute` direkt.

```
Skill ──→ Attribute
SkillVerb   (Katalog, eigenständig)
SkillDomain (Katalog, eigenständig)
```

## Dateien

| Klasse | Pfad |
|--------|------|
| `Skill` | [coreElements/…/Skill.java](../../../coreElements/src/main/java/de/heiges/rulesengine/coreelements/domain/model/Skill.java) |
| `SkillVerb` | [coreElements/…/SkillVerb.java](../../../coreElements/src/main/java/de/heiges/rulesengine/coreelements/domain/model/SkillVerb.java) |
| `SkillDomain` | [coreElements/…/SkillDomain.java](../../../coreElements/src/main/java/de/heiges/rulesengine/coreelements/domain/model/SkillDomain.java) |
