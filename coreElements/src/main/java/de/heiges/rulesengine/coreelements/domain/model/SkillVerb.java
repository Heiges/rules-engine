package de.heiges.rulesengine.coreelements.domain.model;

import java.util.Objects;

public class SkillVerb {

    private final String name;
    private String description;

    public SkillVerb(String name, String description) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("SkillVerb name must not be blank");
        }
        this.name = name;
        this.description = description == null ? "" : description;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SkillVerb other)) return false;
        return Objects.equals(name, other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "SkillVerb{name='" + name + "', description='" + description + "'}";
    }
}
