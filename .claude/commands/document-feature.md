Dokumentiere das Feature "$ARGUMENTS" in `.claude/features/`.

Lies zunächst die relevanten Quelldateien, um die Dokumentation aus dem tatsächlichen Code abzuleiten — nicht aus Annahmen.

## Was zu tun ist

1. Feature dem richtigen Unterordner zuordnen:
   - Domänenklassen (coreElements) → `.claude/features/domain/`
   - Persistenz (XML, Repository) → `.claude/features/persistence/`
   - Frontend (React, Views, Komponenten) → `.claude/features/frontend/`
   - Mehrschichtige Features → `.claude/features/domain/` (Primärschicht)

2. `.claude/features/<unterordner>/$ARGUMENTS.md` anlegen oder aktualisieren mit diesen Pflichtabschnitten:

   - **Ziel**: warum dieses Feature existiert, welchen Mehrwert es liefert
   - **Anforderungen**: alle Invarianten, Verhaltensregeln und Randbedingungen
   - **Entscheidungen**: warum so implementiert und nicht anders (Alternativen, Trade-offs)
   - **Implementierung**: Artefakt-Tabelle + Schichten-Spec (siehe Vorlage)
   - **Rekonstruktion**: minimaler Prompt + notwendiger Kontext

3. `.claude/features/_projekt-struktur.md` — im passenden Unterabschnitt einen Link auf die neue Spec ergänzen.

## Vorlage

```markdown
# <Feature-Name>

## Ziel

<Warum existiert dieses Feature?>

## Anforderungen

- <Invariante oder Verhaltensregel>

## Entscheidungen

- <Warum so und nicht anders>

## Implementierung

### Artefakte

| Artefakt | Pfad |
|---|---|
| <Typ> | `<Pfad>` |

### Schichten-Spec

Nur Schichten dokumentieren, die das Feature berührt.

#### Domain

**Klasse:** `<Name>` — Klasse oder Record?

| Feld | Typ | final | Validierung |
|---|---|---|---|
| `name` | `String` | ja | nicht null, nicht blank |

- Gleichheit (`equals`/`hashCode`): <Felder>
- `toString`-Format: `<ClassName>{field='...'}`
- Keine Framework-Annotationen

---

#### Persistence

**Persistenz-DTO:** `<Name>Dto`

| Feld | XML-Annotation | Nullsafe/Default |
|---|---|---|
| `name` | `@XmlAttribute(required=true)` | — |

No-arg-Konstruktor + All-args-Konstruktor. Getter für alle Felder.

**Container-DTO:** `<Name>SetDto`  
Feld: `List<<Name>Dto> <plural>` mit `@XmlElement(name="<singular>")`

**Einbettung in `RulesetDto`:**  
`@XmlElement(name="<plural>") private <Name>SetDto <plural>;`  
Getter: `<plural> != null ? <plural> : new <Name>SetDto(List.of())`

**Repository-Mapping (`XmlRulesetRepository`):**
```java
dto.get<Plural>().get<Plural>().stream()
    .map(d -> new <Name>(d.getName()))
    .toList()
```

**`LoadedRuleset`:** Feld `List<<Name>> <plural>` + Getter  
**`RulesetRepository`:** Methode `List<<Name>> get<Plural>(String rulesetName)`

---

#### API

**API-DTO:** `<Name>ApiDto`  
Typ: Record — `record <Name>ApiDto(String name) {}`

**Einbettung in `RulesetApiDto`:**  
Feld `List<<Name>ApiDto> <plural>` (nie null, default `List.of()`)

**Controller-Mapping (`RulesetController`):**  
`toApiDto`: `..<plural>(loaded.get<Plural>().stream().map(x -> new <Name>ApiDto(x.getName())).toList())`  
`toDomain`: `dto.<plural>().stream().map(x -> new <Name>(x.name())).toList()` → in `<Name>SetDto`

---

#### Frontend

**TypeScript-Interface** (`api.ts`):
```ts
export interface <Name> { name: string }
```

**`RulesetData`-Feld** (`api.ts`): `<plural>: <Name>[]`

**`fromApiDto`** (`api.ts`): `<plural>: data.<plural> ?? []`

**Routen** (`App.tsx`):  
`/tile/<plural>` → `<Name>View`  
`/tile/<plural>/:index` → `<Name>DetailView`

**`<Name>View` (List-View):**
- Tabelle: sortierbar nach Name (asc/desc/unsorted)
- Massenauswahl + Löschen
- Inline-Add-Formular (<Felder>, Pflichtfelder markieren)
- Doppelter Name → Inline-Fehlermeldung, kein Speichern
- „Anzeigen" / „Bearbeiten" → `navigate('/tile/<plural>/:index')`
- Persist: `fileHandle.createWritable()` → sonst `saveRuleset()`
- CSS: Klassen aus `AttributeView.css` (`attr-table`, `attr-form`, …)

**`<Name>DetailView` (Detail-View):**
- `/tile/<plural>/neu` → Neuanlage
- `/tile/<plural>/:index` → Bearbeitung
- `name` Pflichtfeld; readonly im Ansichtsmodus
- Persist: wie List-View
- Zurück → `navigate('/tile/<plural>')`

**Kachel in `EditRulesetView`:**
- Label: `<Anzeigename>`
- Beschreibung: `<Kurzbeschreibung>`
- Badge: `rulesetData?.<plural>.length ?? 0`
- Navigation: `/tile/<plural>`

---

## Rekonstruktion

\`\`\`
<Minimaler Prompt zum Wiederherstellen>
\`\`\`

Kontext: <Was Claude wissen muss, damit der Prompt korrekt ausgeführt wird>
```

## Hinweise

- Dokumentation aus dem Code ableiten, nicht erfinden
- Nur berührte Schichten in der Schichten-Spec ausfüllen — fehlende Abschnitte weglassen
- Entscheidungen nur eintragen, wenn der Grund nicht offensichtlich ist
- Pfade exakt wie im Dateisystem (keine Platzhalter)
- `<Plural>` = Großschreibung (Methodenname), `<plural>` = Kleinschreibung (Feldname/Route)
