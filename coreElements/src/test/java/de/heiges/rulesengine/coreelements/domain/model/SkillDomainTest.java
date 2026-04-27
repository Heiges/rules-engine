package de.heiges.rulesengine.coreelements.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SkillDomainTest {

    @Test
    void createsSkillDomainWithNameAndDescription() {
        SkillDomain domain = new SkillDomain("Kampf", "Kampffertigkeiten");
        assertEquals("Kampf", domain.getName());
        assertEquals("Kampffertigkeiten", domain.getDescription());
    }

    @Test
    void rejectsBlankName() {
        assertThrows(IllegalArgumentException.class, () -> new SkillDomain("", "beschreibung"));
    }

    @Test
    void nullDescriptionBecomesEmpty() {
        SkillDomain domain = new SkillDomain("Kampf", null);
        assertEquals("", domain.getDescription());
    }

    @Test
    void updatesDescription() {
        SkillDomain domain = new SkillDomain("Kampf", "alt");
        domain.setDescription("neu");
        assertEquals("neu", domain.getDescription());
    }

    @Test
    void equalityBasedOnName() {
        SkillDomain d1 = new SkillDomain("Kampf", "alt");
        SkillDomain d2 = new SkillDomain("Kampf", "neu");
        assertEquals(d1, d2);
    }
}
