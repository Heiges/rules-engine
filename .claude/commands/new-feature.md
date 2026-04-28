# Feature: "$ARGUMENTS"

## Ziel
Eine "$ARGUMENTS" ist ein Domänenelement mit nur einem Namen. Sie wird im Regelwerk gespeichert
und im Frontend verwaltet (Liste + Detailansicht).

---

## 1. Domain (coreElements)

**Klasse:** `"$ARGUMENTS"`  
**Paket:** `de.heiges.rulesengine.coreelements.domain.model`  
**Typ:** Klasse (kein Record, weil Felder potenziell mutierbar)

| Feld   | Typ    | final | Validierung              |
|--------|--------|-------|--------------------------|
| `name` | String | ja    | nicht null, nicht blank  |

- Gleichheit (`equals`/`hashCode`) **nur über `name`**
- `toString`: `"$ARGUMENTS"{name='...'}`
- Keine Framework-Annotationen

---

## 2. Persistence-DTO

**Klasse:** `"$ARGUMENTS"Dto`  
**Paket:** `de.heiges.rulesengine.persistence.xml.dto`  
**Annotationen:** `@XmlAccessorType(XmlAccessType.FIELD)`

| Feld   | Annotation                   | Nullsafe |
|--------|------------------------------|----------|
| `name` | `@XmlAttribute(required=true)` | nein   |

No-arg-Konstruktor + All-args-Konstruktor. Getter für alle Felder.

**Container-DTO:** `"$ARGUMENTS"SetDto`  
`@XmlAccessorType(XmlAccessType.FIELD)` + `List<"$ARGUMENTS"Dto> "$ARGUMENTS"s` mit `@XmlElement(name=""$ARGUMENTS"")`  
No-arg-Konstruktor + Konstruktor mit `List<"$ARGUMENTS"Dto>`. Getter.

**Einbettung in `RulesetDto`:**  
Neues Feld `@XmlElement(name=""$ARGUMENTS"s") private "$ARGUMENTS"SetDto "$ARGUMENTS"s;`  
Getter gibt `"$ARGUMENTS"s != null ? "$ARGUMENTS"s : new "$ARGUMENTS"SetDto(List.of())` zurück.

---

## 3. Repository-Schicht

**`LoadedRuleset`:** neues Feld `List<"$ARGUMENTS"> "$ARGUMENTS"s`; Getter `get"$ARGUMENTS"s()`

**`RulesetRepository`:** Methode `List<"$ARGUMENTS"> get"$ARGUMENTS"s(String rulesetName)`

**`XmlRulesetRepository`:** Mapping in `toLoadedRuleset()`:
```java
dto.get"$ARGUMENTS"s().get"$ARGUMENTS"s().stream()
    .map(w -> new "$ARGUMENTS"(w.getName()))
    .toList()
4. API-DTO
Klasse: "$ARGUMENTS"ApiDto

Typ: Record

Paket: de.heiges.rulesengine.api.dto

record "$ARGUMENTS"ApiDto(String name) {}

Einbettung in RulesetApiDto:

Neues Feld List<"$ARGUMENTS"ApiDto> "$ARGUMENTS"s (nie null, default List.of())

Controller-Mapping (RulesetController):

In toApiDto(): ."$ARGUMENTS"s(loaded.get"$ARGUMENTS"s().stream().map(w -> new "$ARGUMENTS"ApiDto(w.getName())).toList())

In toDomain()/Save: dto."$ARGUMENTS"s().stream().map(w -> new "$ARGUMENTS"(w.name())).toList() → in "$ARGUMENTS"SetDto

5. Frontend
TypeScript-Interface (in api.ts):


export interface "$ARGUMENTS" { name: string }
RulesetData (in api.ts): neues Feld "$ARGUMENTS"s: "$ARGUMENTS"[]

fromApiDto() (in api.ts): "$ARGUMENTS"s: data."$ARGUMENTS"s ?? []

Routen (in App.tsx):

/tile/"$ARGUMENTS"s → "$ARGUMENTS"View

/tile/"$ARGUMENTS"s/:index → "$ARGUMENTS"DetailView

"$ARGUMENTS"View (List-View):

Tabelle mit Sortierung nach Name (asc/desc/unsorted)
Massenauswahl + Löschen
Inline-Add-Formular (nur name, Pflichtfeld)
Doppelter Name → Inline-Fehlermeldung, kein Speichern
„Anzeigen" / „Bearbeiten"-Button → navigate('/tile/"$ARGUMENTS"s/:index')
Persist: fileHandle.createWritable() → sonst saveRuleset()
CSS: Bestehende Klassen aus AttributeView.css
"$ARGUMENTS"DetailView (Detail-View):

Route /tile/"$ARGUMENTS"s/neu → Neuanlage
Route /tile/"$ARGUMENTS"s/:index → Bearbeitung
Felder: name (Pflicht, readonly wenn Ansicht)
Speichern: wie List-View persist
Zurück-Button → navigate('/tile/"$ARGUMENTS"s')
Kachel in EditRulesetView:

Label: „Waffen"
Beschreibung: „Waffen verwalten"
Badge: Anzahl der Einträge (rulesetData?."$ARGUMENTS"s.length ?? 0)
Navigation: /tile/"$ARGUMENTS"s


---

## Was diese Spec leistet, was die bisherige nicht hat

| Information | Bisherige Spec | Diese Spec |
|---|---|---|
| Artefakt-Pfade | ✓ | ✓ |
| Exakte Felder + Typen | — | ✓ |
| XML-Annotationsmuster | — | ✓ |
| Repository-Mapping-Code | — | ✓ |
| Null-Safety-Regeln | — | ✓ |
| Frontend-Verhalten (Sortierung, Validation) | — | ✓ |
| Route-Pfade | — | ✓ |