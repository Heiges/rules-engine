package de.heiges.rulesengine.coreelements.domain.model;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class AttributeGroup {

    private final String name;
    private final Map<String, Attribute> attributes = new LinkedHashMap<>();

    public AttributeGroup(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("AttributeGroup name must not be blank");
        }
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void add(Attribute attribute) {
        if (attribute == null) {
            throw new IllegalArgumentException("Attribute must not be null");
        }
        if (attributes.containsKey(attribute.getName())) {
            throw new IllegalArgumentException("Attribute already exists: " + attribute.getName());
        }
        attributes.put(attribute.getName(), attribute);
    }

    public void remove(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Attribute name must not be blank");
        }
        if (!attributes.containsKey(name)) {
            throw new IllegalArgumentException("Attribute not found: " + name);
        }
        attributes.remove(name);
    }

    public void modify(String name, String newDescription) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Attribute name must not be blank");
        }
        Attribute attribute = attributes.get(name);
        if (attribute == null) {
            throw new IllegalArgumentException("Attribute not found: " + name);
        }
        attribute.setDescription(newDescription);
    }

    public Optional<Attribute> find(String name) {
        return Optional.ofNullable(attributes.get(name));
    }

    public boolean contains(String name) {
        return attributes.containsKey(name);
    }

    public Collection<Attribute> getAll() {
        return Collections.unmodifiableCollection(attributes.values());
    }

    public int size() {
        return attributes.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AttributeGroup other)) return false;
        return Objects.equals(name, other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "AttributeGroup{name='" + name + "', size=" + attributes.size() + "}";
    }
}
