package de.heiges.rulesengine.coreelements.domain.model;

import java.util.Objects;

public class Skill {

    private final String name;
    private final Attribute linkedAttribute;
    private int level;

    public Skill(String name, Attribute linkedAttribute, int level) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Skill name must not be blank");
        }
        Objects.requireNonNull(linkedAttribute, "Linked attribute must not be null");
        if (level < 0) {
            throw new IllegalArgumentException("Skill level must not be negative");
        }
        this.name = name;
        this.linkedAttribute = linkedAttribute;
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public Attribute getLinkedAttribute() {
        return linkedAttribute;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        if (level < 0) {
            throw new IllegalArgumentException("Skill level must not be negative");
        }
        this.level = level;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Skill other)) return false;
        return Objects.equals(name, other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Skill{name='" + name + "', linkedAttribute=" + linkedAttribute.getName() + ", level=" + level + "}";
    }
}
