# Schicht-Spec: Domain-Modell (coreElements)

## Zweck

Definiert Muster und Regeln für Domänenklassen in `coreElements`.
Das Domänenmodell ist **framework-frei** — keine Abhängigkeit zu Persistenz oder UI.

## Klassenkategorien

### Record — unveränderlicher Werttyp

Für Typen ohne Identität, die nur Daten kapseln.

```java
public record Foo(int amount) {}
```

Validierung im Compact Constructor:

```java
public record FooRange(int min, int max) {
    public FooRange {
        if (min > max) throw new IllegalArgumentException("min <= max erforderlich");
    }
}
```

- Kein Setter — unveränderlich
- `equals` / `hashCode` / `toString` automatisch durch Record

---

### Entität — Name + optionale Felder

Für Domänenobjekte mit fachlichem Namen als Schlüssel.

```java
public class Foo {
    private final String name;
    private String description;

    public Foo(String name, String description) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name darf nicht leer sein");
        this.name = name;
        this.description = description != null ? description : "";
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public void setDescription(String d) { this.description = d != null ? d : ""; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Foo f)) return false;
        return name.equals(f.name);
    }
    @Override public int hashCode() { return name.hashCode(); }
    @Override public String toString() { return "Foo{name='" + name + "', description='" + description + "'}"; }
}
```

Variante mit Wertefeld: zusätzliches Feld eines Record-Typs, null-sicher im Konstruktor und Setter normalisiert.

---

### Gruppe — benannte Sammlung eines Typs

Für eine geordnete Sammlung gleichartiger Objekte unter einem gemeinsamen Namen.

```java
public class FooGroup {
    private final String name;
    private final List<Foo> items = new ArrayList<>();

    public FooGroup(String name) { ... }
    public void add(Foo item) { items.add(item); }
    public List<Foo> getAll() { return Collections.unmodifiableList(items); }
    public String getName() { return name; }
}
```

---

### Menge — Sammlung von Gruppen ohne Duplikate

Für eine geordnete Sammlung von Gruppen; doppelte Gruppennamen sind verboten.

```java
public class FooSet {
    private final LinkedHashMap<String, FooGroup> groups = new LinkedHashMap<>();

    public void addGroup(FooGroup group) {
        if (groups.containsKey(group.getName()))
            throw new IllegalArgumentException("Gruppe bereits vorhanden: " + group.getName());
        groups.put(group.getName(), group);
    }

    public Collection<FooGroup> getGroups() { return Collections.unmodifiableCollection(groups.values()); }
}
```

## Pflichtmuster

| Muster | Regel |
|--------|-------|
| Name unveränderlich | `private final String name` — kein `setName` |
| Name-Validierung | im Konstruktor: `null` oder blank → `IllegalArgumentException` |
| Null-Normalisierung | optionale Felder nie `null` intern halten — Default im Konstruktor und Setter |
| `equals` / `hashCode` | ausschließlich über den fachlichen Schlüssel (Name) |
| `toString` | `Klassenname{name='...', feldN='...'}` — alle relevanten Felder |

## Verbote

- Keine JAXB-Annotationen (`@XmlElement`, `@XmlRootElement` etc.)
- Keine Spring-Annotationen (`@Component`, `@Service` etc.)
- Keine Wildcardimporte
- Java 21 — kein Legacy-Stil

## Ablageort

```
coreElements/src/main/java/de/heiges/rulesengine/coreelements/domain/model/<Klasse>.java
coreElements/src/test/java/de/heiges/rulesengine/coreelements/domain/model/<Klasse>Test.java
```
