package de.heiges.rulesengine.coreelements.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SkillVerbTest {

    @Test
    void createsSkillVerbWithNameAndDescription() {
        SkillVerb skill = new SkillVerb("Schwertkampf", "Kampf mit Schwertern");
        assertEquals("Schwertkampf", skill.getName());
        assertEquals("Kampf mit Schwertern", skill.getDescription());
    }

    @Test
    void rejectsBlankName() {
        assertThrows(IllegalArgumentException.class, () -> new SkillVerb("", "beschreibung"));
    }

    @Test
    void nullDescriptionBecomesEmpty() {
        SkillVerb skill = new SkillVerb("Schwertkampf", null);
        assertEquals("", skill.getDescription());
    }

    @Test
    void updatesDescription() {
        SkillVerb skill = new SkillVerb("Schwertkampf", "alt");
        skill.setDescription("neu");
        assertEquals("neu", skill.getDescription());
    }

    @Test
    void equalityBasedOnName() {
        SkillVerb s1 = new SkillVerb("Schwertkampf", "alt");
        SkillVerb s2 = new SkillVerb("Schwertkampf", "neu");
        assertEquals(s1, s2);
    }
}
