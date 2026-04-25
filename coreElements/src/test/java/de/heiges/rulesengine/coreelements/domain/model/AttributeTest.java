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
}
