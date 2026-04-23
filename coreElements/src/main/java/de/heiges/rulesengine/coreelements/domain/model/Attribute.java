package de.heiges.rulesengine.coreelements.domain.model;

import java.util.Objects;

public class Attribute {

    private final String name;
    private int value;

    public Attribute(String name, int value) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Attribute name must not be blank");
        }
        if (value < 0) {
            throw new IllegalArgumentException("Attribute value must not be negative");
        }
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("Attribute value must not be negative");
        }
        this.value = value;
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
        return "Attribute{name='" + name + "', value=" + value + "}";
    }
}
