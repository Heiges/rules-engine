package de.heiges.rulesengine.coreelements.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AttributeSetTest {

    private AttributeSet attributeSet;

    @BeforeEach
    void setUp() {
        attributeSet = new AttributeSet();
    }

    @Test
    void addsAttribute() {
        attributeSet.add(new Attribute("Stärke", ""));
        assertTrue(attributeSet.contains("Stärke"));
        assertEquals(1, attributeSet.size());
    }

    @Test
    void rejectsNullAttribute() {
        assertThrows(IllegalArgumentException.class, () -> attributeSet.add(null));
    }

    @Test
    void rejectsDuplicateAttribute() {
        attributeSet.add(new Attribute("Stärke", ""));
        assertThrows(IllegalArgumentException.class, () -> attributeSet.add(new Attribute("Stärke", "Andere")));
    }

    @Test
    void removesAttribute() {
        attributeSet.add(new Attribute("Stärke", ""));
        attributeSet.remove("Stärke");
        assertFalse(attributeSet.contains("Stärke"));
        assertEquals(0, attributeSet.size());
    }

    @Test
    void rejectsRemoveOfUnknownAttribute() {
        assertThrows(IllegalArgumentException.class, () -> attributeSet.remove("Geschicklichkeit"));
    }

    @Test
    void rejectsRemoveWithBlankName() {
        assertThrows(IllegalArgumentException.class, () -> attributeSet.remove(""));
        assertThrows(IllegalArgumentException.class, () -> attributeSet.remove("  "));
    }

    @Test
    void modifiesAttributeDescription() {
        attributeSet.add(new Attribute("Stärke", "Alt"));
        attributeSet.modify("Stärke", "Neu");
        assertEquals("Neu", attributeSet.find("Stärke").map(Attribute::getDescription).orElseThrow());
    }

    @Test
    void rejectsModifyOfUnknownAttribute() {
        assertThrows(IllegalArgumentException.class, () -> attributeSet.modify("Stärke", "Beschreibung"));
    }

    @Test
    void findsAttributeByName() {
        attributeSet.add(new Attribute("Stärke", ""));
        Optional<Attribute> result = attributeSet.find("Stärke");
        assertTrue(result.isPresent());
        assertEquals("Stärke", result.get().getName());
    }

    @Test
    void returnsEmptyForUnknownAttribute() {
        assertTrue(attributeSet.find("Unbekannt").isEmpty());
    }

    @Test
    void getAllReturnsAllAttributes() {
        attributeSet.add(new Attribute("Stärke", ""));
        attributeSet.add(new Attribute("Geschicklichkeit", ""));
        assertEquals(2, attributeSet.getAll().size());
    }

    @Test
    void getAllIsUnmodifiable() {
        attributeSet.add(new Attribute("Stärke", ""));
        assertThrows(UnsupportedOperationException.class, () -> attributeSet.getAll().clear());
    }
}
