# AttributeGroup – Gruppierung von Attributen

## Ziel

Attribute eines Regelwerks können thematisch zusammengefasst werden (z. B. „Körper" für Kraft und Ausdauer, „Geist" für Intelligenz und Wahrnehmung). `AttributeGroup` ist der Named Container dafür; `AttributeSet` hält nun eine geordnete Folge solcher Gruppen statt einer flachen Attributliste.

## Anforderungen

- Der Gruppenname ist unveränderlich und darf nicht leer oder blank sein.
- Innerhalb einer Gruppe ist jeder Attributname eindeutig.
- Über alle Gruppen eines `AttributeSet` hinweg sind Attributnamen global eindeutig — ein Attributname darf nicht in zwei verschiedenen Gruppen vorkommen.
- Gruppenname innerhalb eines `AttributeSet` ist eindeutig; zwei Gruppen gleichen Namens sind nicht erlaubt.
- `AttributeSet` bietet weiterhin gruppenübergreifende Operationen: `find(String)`, `contains(String)`, `getAll()`, `size()` — Abwärtskompatibilität für Skills, die Attribute per Name referenzieren.
- `getAll()` und `getGroups()` liefern unmodifizierbare Collections.
- Gleichheit von `AttributeGroup` basiert ausschließlich auf dem Namen.

## Entscheidungen

- **Gruppen sind Pflicht, keine optionale Ebene** — jedes Attribut muss einer Gruppe angehören. Eine flache Fallback-Gruppe würde die fachliche Struktur verwischen und die API komplizierter machen.
- **Globale Attributnamen-Eindeutigkeit** — Skills referenzieren Attribute nur per Name, ohne Gruppenangabe. Würden zwei Gruppen denselben Attributnamen erlauben, wäre die Auflösung beim Skill-Lookup mehrdeutig.
- **`AttributeSet.modify()` entfernt** — nach der Umstellung können Aufrufer direkt `find(name).ifPresent(a -> a.setDescription(...))` nutzen oder `findGroup(groupName).ifPresent(g -> g.modify(...))`. Die Methode war nur in Tests verwendet und hätte keine neue Invariante abgedeckt.

## Implementierung

| Artefakt | Pfad |
|---|---|
| Domain-Klasse `AttributeGroup` | `coreElements/src/main/java/de/heiges/rulesengine/coreelements/domain/model/AttributeGroup.java` |
| Domain-Klasse `AttributeSet` (angepasst) | `coreElements/src/main/java/de/heiges/rulesengine/coreelements/domain/model/AttributeSet.java` |
| Unit-Test `AttributeGroupTest` | `coreElements/src/test/java/de/heiges/rulesengine/coreelements/domain/model/AttributeGroupTest.java` |
| Unit-Test `AttributeSetTest` (aktualisiert) | `coreElements/src/test/java/de/heiges/rulesengine/coreelements/domain/model/AttributeSetTest.java` |
| Persistence-DTO `AttributeGroupDto` | `persistence/src/main/java/de/heiges/rulesengine/persistence/xml/dto/AttributeGroupDto.java` |
| Persistence-DTO `AttributeSetDto` (angepasst) | `persistence/src/main/java/de/heiges/rulesengine/persistence/xml/dto/AttributeSetDto.java` |
| Repository `XmlRulesetRepository` (angepasst) | `persistence/src/main/java/de/heiges/rulesengine/persistence/xml/XmlRulesetRepository.java` |
| Persistence-Test `XmlRulesetRepositoryTest` (aktualisiert) | `persistence/src/test/java/de/heiges/rulesengine/persistence/xml/XmlRulesetRepositoryTest.java` |
| API-DTO `AttributeGroupApiDto` | `api/src/main/java/de/heiges/rulesengine/api/dto/AttributeGroupApiDto.java` |
| API-DTO `RulesetApiDto` (angepasst) | `api/src/main/java/de/heiges/rulesengine/api/dto/RulesetApiDto.java` |
| REST-Controller `RulesetController` (angepasst) | `api/src/main/java/de/heiges/rulesengine/api/controller/RulesetController.java` |
| Frontend `api.ts` (angepasst) | `frontend/src/api.ts` — `Attribute` hat `groupName?: string`; `fromApiDto` befüllt es; `toApiDto` gruppiert nach `groupName` |
| Frontend `AttributeView.tsx` (angepasst) | `frontend/src/views/AttributeView.tsx` — Spalte „Gruppe" in der Attributtabelle |
| Frontend `AttributeView.css` (angepasst) | `frontend/src/views/AttributeView.css` — `.attr-col-group` |

### XML-Format (nach der Änderung)

```xml
<attributeSet>
  <group name="Körper">
    <attribute name="Kraft" description="Körperliche Stärke" value="3"/>
    <attribute name="Ausdauer" description="Durchhaltevermögen" value="2"/>
  </group>
  <group name="Geist">
    <attribute name="Intelligenz" description="Kognitive Fähigkeiten" value="1"/>
  </group>
</attributeSet>
```

### API-Format (nach der Änderung)

```json
{
  "attributeGroups": [
    {
      "group": "Körper",
      "attributes": [
        { "name": "Kraft", "description": "Körperliche Stärke", "value": 3 }
      ]
    }
  ]
}
```

## Rekonstruktion

```
Attribute sollen gruppiert werden können. Beispielsweise soll Kraft, Ausdauer etc. zum Attribut Körper gruppiert werden können. Passe die Domänenklassen entsprechend an.
```

Kontext: `coreElements`, `persistence` und `api` müssen konsistent angepasst werden. `AttributeGroup` ist eine neue Domänenklasse (kein JAXB, kein Spring). `AttributeSet` hält Gruppen statt flache Attribute; `find()`, `contains()`, `getAll()` und `size()` bleiben als gruppenübergreifende Operationen erhalten (Skills referenzieren Attribute per Name). Im Persistence-Layer kommt `AttributeGroupDto` (JAXB) hinzu; `AttributeSetDto` hält `List<AttributeGroupDto>`. Im API-Layer ersetzt `List<AttributeGroupApiDto>` das bisherige `List<AttributeApiDto>` im `RulesetApiDto`. Das Frontend muss separat angepasst werden.
