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
    void addsGroup() {
        AttributeGroup group = new AttributeGroup("Körper");
        group.add(new Attribute("Stärke", ""));
        attributeSet.addGroup(group);
        assertTrue(attributeSet.containsGroup("Körper"));
        assertEquals(1, attributeSet.getGroups().size());
    }

    @Test
    void rejectsNullGroup() {
        assertThrows(IllegalArgumentException.class, () -> attributeSet.addGroup(null));
    }

    @Test
    void rejectsDuplicateGroupName() {
        attributeSet.addGroup(new AttributeGroup("Körper"));
        assertThrows(IllegalArgumentException.class, () -> attributeSet.addGroup(new AttributeGroup("Körper")));
    }

    @Test
    void rejectsAttributeNameCollisionAcrossGroups() {
        AttributeGroup koerper = new AttributeGroup("Körper");
        koerper.add(new Attribute("Stärke", ""));
        attributeSet.addGroup(koerper);

        AttributeGroup geist = new AttributeGroup("Geist");
        geist.add(new Attribute("Stärke", "Kollidierender Name"));
        assertThrows(IllegalArgumentException.class, () -> attributeSet.addGroup(geist));
    }

    @Test
    void removesGroup() {
        attributeSet.addGroup(new AttributeGroup("Körper"));
        attributeSet.removeGroup("Körper");
        assertFalse(attributeSet.containsGroup("Körper"));
        assertEquals(0, attributeSet.getGroups().size());
    }

    @Test
    void rejectsRemoveOfUnknownGroup() {
        assertThrows(IllegalArgumentException.class, () -> attributeSet.removeGroup("Geist"));
    }

    @Test
    void rejectsRemoveWithBlankGroupName() {
        assertThrows(IllegalArgumentException.class, () -> attributeSet.removeGroup(""));
        assertThrows(IllegalArgumentException.class, () -> attributeSet.removeGroup("  "));
    }

    @Test
    void findsGroupByName() {
        attributeSet.addGroup(new AttributeGroup("Körper"));
        Optional<AttributeGroup> result = attributeSet.findGroup("Körper");
        assertTrue(result.isPresent());
        assertEquals("Körper", result.get().getName());
    }

    @Test
    void returnsEmptyForUnknownGroup() {
        assertTrue(attributeSet.findGroup("Unbekannt").isEmpty());
    }

    @Test
    void getGroupsIsUnmodifiable() {
        attributeSet.addGroup(new AttributeGroup("Körper"));
        assertThrows(UnsupportedOperationException.class, () -> attributeSet.getGroups().clear());
    }

    @Test
    void findsAttributeCrossGroup() {
        AttributeGroup koerper = new AttributeGroup("Körper");
        koerper.add(new Attribute("Stärke", ""));
        attributeSet.addGroup(koerper);

        Optional<Attribute> result = attributeSet.find("Stärke");
        assertTrue(result.isPresent());
        assertEquals("Stärke", result.get().getName());
    }

    @Test
    void returnsEmptyForUnknownAttribute() {
        assertTrue(attributeSet.find("Unbekannt").isEmpty());
    }

    @Test
    void containsAttributeCrossGroup() {
        AttributeGroup koerper = new AttributeGroup("Körper");
        koerper.add(new Attribute("Stärke", ""));
        attributeSet.addGroup(koerper);

        assertTrue(attributeSet.contains("Stärke"));
        assertFalse(attributeSet.contains("Ausdauer"));
    }

    @Test
    void getAllReturnsAllAttributesAcrossGroups() {
        AttributeGroup koerper = new AttributeGroup("Körper");
        koerper.add(new Attribute("Stärke", ""));
        koerper.add(new Attribute("Ausdauer", ""));
        attributeSet.addGroup(koerper);

        AttributeGroup geist = new AttributeGroup("Geist");
        geist.add(new Attribute("Intelligenz", ""));
        attributeSet.addGroup(geist);

        assertEquals(3, attributeSet.getAll().size());
    }

    @Test
    void sizeReturnsTotalAttributeCount() {
        AttributeGroup koerper = new AttributeGroup("Körper");
        koerper.add(new Attribute("Stärke", ""));
        koerper.add(new Attribute("Ausdauer", ""));
        attributeSet.addGroup(koerper);

        AttributeGroup geist = new AttributeGroup("Geist");
        geist.add(new Attribute("Intelligenz", ""));
        attributeSet.addGroup(geist);

        assertEquals(3, attributeSet.size());
    }

    @Test
    void sizeIsZeroForEmptySet() {
        assertEquals(0, attributeSet.size());
    }
}
