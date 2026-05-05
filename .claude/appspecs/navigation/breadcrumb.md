# Breadcrumb — Applikationsspezifikation

Konkrete Ausprägung der [Breadcrumb-Blueprint](../../blueprints/components/breadcrumb.md) für diese Anwendung.

## Label-Map (statische Segmente)

| Route-Pfad | Label |
|------------|-------|
| `/` | Rules Engine |
| `/home` | Spielleiter |
| `/player` | Spieler |
| `/edit-ruleset` | `{currentRuleset}` — aus `RulesetContext` |
| `/create-world` | Spielwelt |
| `/tile/werte` | Werte |
| `/tile/attributes` | Attribute |
| `/world/attributes` | Attribute |
| `/tile/skills` | Skills |
| `/tile/skills/domains` | Domänen |
| `/tile/cheats` | Cheats |
| `/character-editor` | Charactereditor |

## Dynamische Auflösung (`:index`-Segmente)

Der Resolver erhält das numerische Segment und den bisherigen Pfad und schlägt den Namen im `RulesetContext` nach:

| Übergeordnetes Segment | Quelle in `rulesetData` |
|------------------------|-------------------------|
| `attributes` | `rulesetData.attributes[index].name` |
| `skills` | `rulesetData.skills[index].name` |
| `domains` | `rulesetData.skillDomains[index].name` |
| `cheats` | `rulesetData.cheats[index].name` |

Ist der Index außerhalb des gültigen Bereichs oder `rulesetData` null, wird `#` angezeigt.

## Sichtbarkeit

Auf `/` (Rollenwahl) wird kein Breadcrumb angezeigt.

## Referenzen

- [tile-navigation.md](tile-navigation.md) — Routen und Navigationshierarchie
- [Breadcrumb-Blueprint](../../blueprints/components/breadcrumb.md) — generisches Muster
