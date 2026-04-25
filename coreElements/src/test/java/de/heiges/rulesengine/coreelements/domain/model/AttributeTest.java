package de.heiges.rulesengine.coreelements.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AttributeTest {

    @Test
    void createsAttributeWithNameAndDescription() {
        Attribute attr = new Attribute("Stärke", "Körperliche Kraft");
        assertEquals("Stärke", attr.getName());
        assertEquals("Körperliche Kraft", attr.getDescription());
    }

    @Test
    void allowsEmptyDescription() {
        Attribute attr = new Attribute("Stärke", "");
        assertEquals("", attr.getDescription());
    }

    @Test
    void treatsNullDescriptionAsEmpty() {
        Attribute attr = new Attribute("Stärke", null);
        assertEquals("", attr.getDescription());
    }

    @Test
    void rejectsBlankName() {
        assertThrows(IllegalArgumentException.class, () -> new Attribute("", "Beschreibung"));
        assertThrows(IllegalArgumentException.class, () -> new Attribute("  ", "Beschreibung"));
    }

    @Test
    void updatesDescription() {
        Attribute attr = new Attribute("Stärke", "Alt");
        attr.setDescription("Neu");
        assertEquals("Neu", attr.getDescription());
    }

    @Test
    void equalityBasedOnName() {
        Attribute a1 = new Attribute("Stärke", "Erste Beschreibung");
        Attribute a2 = new Attribute("Stärke", "Zweite Beschreibung");
        assertEquals(a1, a2);
    }

    @Test
    void defaultValueIsZero() {
        Attribute attr = new Attribute("Stärke", "Beschreibung");
        assertEquals(new Value(0), attr.getValue());
    }

    @Test
    void acceptsPositiveValue() {
        Attribute attr = new Attribute("Stärke", "Beschreibung", new Value(5));
        assertEquals(new Value(5), attr.getValue());
    }

    @Test
    void acceptsNegativeValue() {
        Attribute attr = new Attribute("Malus", "Nachteil", new Value(-3));
        assertEquals(new Value(-3), attr.getValue());
    }

    @Test
    void updatesValue() {
        Attribute attr = new Attribute("Stärke", "Beschreibung");
        attr.setValue(new Value(10));
        assertEquals(new Value(10), attr.getValue());
    }

    @Test
    void nullValueDefaultsToZero() {
        Attribute attr = new Attribute("Stärke", "Beschreibung", null);
        assertEquals(new Value(0), attr.getValue());
    }
}
