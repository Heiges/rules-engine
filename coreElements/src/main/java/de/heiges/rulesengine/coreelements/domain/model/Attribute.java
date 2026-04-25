package de.heiges.rulesengine.coreelements.domain.model;

import java.util.Objects;

public class Attribute {

    private final String name;
    private String description;
    private Value value;

    public Attribute(String name, String description) {
        this(name, description, new Value(0));
    }

    public Attribute(String name, String description, Value value) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Attribute name must not be blank");
        }
        this.name = name;
        this.description = description == null ? "" : description;
        this.value = value != null ? value : new Value(0);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? "" : description;
    }

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value != null ? value : new Value(0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Attribute other)) return false;
        return Objects.equals(name, other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Attribute{name='" + name + "', description='" + description + "', value=" + value + "}";
    }
}
