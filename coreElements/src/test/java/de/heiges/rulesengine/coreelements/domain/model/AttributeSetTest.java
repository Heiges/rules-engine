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
        attributeSet.add(new Attribute("Stärke", 10));
        assertTrue(attributeSet.contains("Stärke"));
        assertEquals(1, attributeSet.size());
    }

    @Test
    void rejectsNullAttribute() {
        assertThrows(IllegalArgumentException.class, () -> attributeSet.add(null));
    }

    @Test
    void rejectsDuplicateAttribute() {
        attributeSet.add(new Attribute("Stärke", 10));
        assertThrows(IllegalArgumentException.class, () -> attributeSet.add(new Attribute("Stärke", 5)));
    }

    @Test
    void removesAttribute() {
        attributeSet.add(new Attribute("Stärke", 10));
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
    void modifiesAttributeValue() {
        attributeSet.add(new Attribute("Stärke", 10));
        attributeSet.modify("Stärke", 15);
        assertEquals(15, attributeSet.find("Stärke").map(Attribute::getValue).orElseThrow());
    }

    @Test
    void rejectsModifyOfUnknownAttribute() {
        assertThrows(IllegalArgumentException.class, () -> attributeSet.modify("Stärke", 10));
    }

    @Test
    void rejectsModifyWithNegativeValue() {
        attributeSet.add(new Attribute("Stärke", 10));
        assertThrows(IllegalArgumentException.class, () -> attributeSet.modify("Stärke", -1));
    }

    @Test
    void findsAttributeByName() {
        attributeSet.add(new Attribute("Stärke", 10));
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
        attributeSet.add(new Attribute("Stärke", 10));
        attributeSet.add(new Attribute("Geschicklichkeit", 8));
        assertEquals(2, attributeSet.getAll().size());
    }

    @Test
    void getAllIsUnmodifiable() {
        attributeSet.add(new Attribute("Stärke", 10));
        assertThrows(UnsupportedOperationException.class, () -> attributeSet.getAll().clear());
    }
}
