package de.heiges.rulesengine.coreelements.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SkillTest {

    private final Attribute staerke = new Attribute("Stärke", 10);

    @Test
    void createsSkillWithNameAttributeAndLevel() {
        Skill skill = new Skill("Schwertkampf", staerke, 3);
        assertEquals("Schwertkampf", skill.getName());
        assertEquals(staerke, skill.getLinkedAttribute());
        assertEquals(3, skill.getLevel());
    }

    @Test
    void rejectsBlankName() {
        assertThrows(IllegalArgumentException.class, () -> new Skill("", staerke, 1));
    }

    @Test
    void rejectsNullAttribute() {
        assertThrows(NullPointerException.class, () -> new Skill("Schwertkampf", null, 1));
    }

    @Test
    void rejectsNegativeLevel() {
        assertThrows(IllegalArgumentException.class, () -> new Skill("Schwertkampf", staerke, -1));
    }

    @Test
    void updatesLevel() {
        Skill skill = new Skill("Schwertkampf", staerke, 3);
        skill.setLevel(5);
        assertEquals(5, skill.getLevel());
    }

    @Test
    void equalityBasedOnName() {
        Skill s1 = new Skill("Schwertkampf", staerke, 1);
        Skill s2 = new Skill("Schwertkampf", staerke, 5);
        assertEquals(s1, s2);
    }
}
