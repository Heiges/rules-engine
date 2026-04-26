package de.heiges.rulesengine.coreelements.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AttributeGroupTest {

    private AttributeGroup group;

    @BeforeEach
    void setUp() {
        group = new AttributeGroup("Körper");
    }

    @Test
    void createsGroupWithName() {
        assertEquals("Körper", group.getName());
    }

    @Test
    void rejectsBlankName() {
        assertThrows(IllegalArgumentException.class, () -> new AttributeGroup(""));
        assertThrows(IllegalArgumentException.class, () -> new AttributeGroup("  "));
    }

    @Test
    void rejectsNullName() {
        assertThrows(IllegalArgumentException.class, () -> new AttributeGroup(null));
    }

    @Test
    void addsAttribute() {
        group.add(new Attribute("Kraft", ""));
        assertTrue(group.contains("Kraft"));
        assertEquals(1, group.size());
    }

    @Test
    void rejectsNullAttribute() {
        assertThrows(IllegalArgumentException.class, () -> group.add(null));
    }

    @Test
    void rejectsDuplicateAttribute() {
        group.add(new Attribute("Kraft", ""));
        assertThrows(IllegalArgumentException.class, () -> group.add(new Attribute("Kraft", "Andere")));
    }

    @Test
    void removesAttribute() {
        group.add(new Attribute("Kraft", ""));
        group.remove("Kraft");
        assertFalse(group.contains("Kraft"));
        assertEquals(0, group.size());
    }

    @Test
    void rejectsRemoveOfUnknownAttribute() {
        assertThrows(IllegalArgumentException.class, () -> group.remove("Ausdauer"));
    }

    @Test
    void rejectsRemoveWithBlankName() {
        assertThrows(IllegalArgumentException.class, () -> group.remove(""));
        assertThrows(IllegalArgumentException.class, () -> group.remove("  "));
    }

    @Test
    void modifiesAttributeDescription() {
        group.add(new Attribute("Kraft", "Alt"));
        group.modify("Kraft", "Neu");
        assertEquals("Neu", group.find("Kraft").orElseThrow().getDescription());
    }

    @Test
    void rejectsModifyOfUnknownAttribute() {
        assertThrows(IllegalArgumentException.class, () -> group.modify("Kraft", "Beschreibung"));
    }

    @Test
    void findsAttributeByName() {
        group.add(new Attribute("Kraft", ""));
        Optional<Attribute> result = group.find("Kraft");
        assertTrue(result.isPresent());
        assertEquals("Kraft", result.get().getName());
    }

    @Test
    void returnsEmptyForUnknownAttribute() {
        assertTrue(group.find("Unbekannt").isEmpty());
    }

    @Test
    void getAllReturnsAllAttributes() {
        group.add(new Attribute("Kraft", ""));
        group.add(new Attribute("Ausdauer", ""));
        assertEquals(2, group.getAll().size());
    }

    @Test
    void getAllIsUnmodifiable() {
        group.add(new Attribute("Kraft", ""));
        assertThrows(UnsupportedOperationException.class, () -> group.getAll().clear());
    }

    @Test
    void equalityBasedOnName() {
        AttributeGroup g1 = new AttributeGroup("Körper");
        AttributeGroup g2 = new AttributeGroup("Körper");
        assertEquals(g1, g2);
    }

    @Test
    void inequalityForDifferentNames() {
        AttributeGroup g1 = new AttributeGroup("Körper");
        AttributeGroup g2 = new AttributeGroup("Geist");
        assertNotEquals(g1, g2);
    }
}
