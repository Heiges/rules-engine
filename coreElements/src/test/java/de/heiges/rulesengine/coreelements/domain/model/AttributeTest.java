package de.heiges.rulesengine.coreelements.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AttributeTest {

    @Test
    void createsAttributeWithNameAndValue() {
        Attribute attr = new Attribute("Stärke", 10);
        assertEquals("Stärke", attr.getName());
        assertEquals(10, attr.getValue());
    }

    @Test
    void rejectsBlankName() {
        assertThrows(IllegalArgumentException.class, () -> new Attribute("", 5));
        assertThrows(IllegalArgumentException.class, () -> new Attribute("  ", 5));
    }

    @Test
    void rejectsNegativeValue() {
        assertThrows(IllegalArgumentException.class, () -> new Attribute("Stärke", -1));
    }

    @Test
    void updatesValue() {
        Attribute attr = new Attribute("Stärke", 10);
        attr.setValue(15);
        assertEquals(15, attr.getValue());
    }

    @Test
    void rejectsNegativeValueOnUpdate() {
        Attribute attr = new Attribute("Stärke", 10);
        assertThrows(IllegalArgumentException.class, () -> attr.setValue(-1));
    }

    @Test
    void equalityBasedOnName() {
        Attribute a1 = new Attribute("Stärke", 10);
        Attribute a2 = new Attribute("Stärke", 20);
        assertEquals(a1, a2);
    }
}
