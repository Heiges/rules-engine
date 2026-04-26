package de.heiges.rulesengine.coreelements.domain.model;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class AttributeSet {

    private final Map<String, AttributeGroup> groups = new LinkedHashMap<>();

    public void addGroup(AttributeGroup group) {
        if (group == null) {
            throw new IllegalArgumentException("AttributeGroup must not be null");
        }
        if (groups.containsKey(group.getName())) {
            throw new IllegalArgumentException("Group already exists: " + group.getName());
        }
        for (Attribute attribute : group.getAll()) {
            if (contains(attribute.getName())) {
                throw new IllegalArgumentException("Attribute already exists in another group: " + attribute.getName());
            }
        }
        groups.put(group.getName(), group);
    }

    public void removeGroup(String groupName) {
        if (groupName == null || groupName.isBlank()) {
            throw new IllegalArgumentException("Group name must not be blank");
        }
        if (!groups.containsKey(groupName)) {
            throw new IllegalArgumentException("Group not found: " + groupName);
        }
        groups.remove(groupName);
    }

    public Optional<AttributeGroup> findGroup(String groupName) {
        return Optional.ofNullable(groups.get(groupName));
    }

    public boolean containsGroup(String groupName) {
        return groups.containsKey(groupName);
    }

    public Collection<AttributeGroup> getGroups() {
        return Collections.unmodifiableCollection(groups.values());
    }

    public Optional<Attribute> find(String name) {
        for (AttributeGroup group : groups.values()) {
            Optional<Attribute> attr = group.find(name);
            if (attr.isPresent()) return attr;
        }
        return Optional.empty();
    }

    public boolean contains(String name) {
        return groups.values().stream().anyMatch(g -> g.contains(name));
    }

    public Collection<Attribute> getAll() {
        return groups.values().stream()
                .flatMap(g -> g.getAll().stream())
                .toList();
    }

    public int size() {
        return groups.values().stream().mapToInt(AttributeGroup::size).sum();
    }
}
