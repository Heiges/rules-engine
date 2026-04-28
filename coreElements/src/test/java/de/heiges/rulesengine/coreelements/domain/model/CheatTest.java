package de.heiges.rulesengine.coreelements.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CheatTest {

    @Test
    void createsCheatWithNameAndDescription() {
        Cheat cheat = new Cheat("Unverwundbar", "Nimmt keinen Schaden");
        assertEquals("Unverwundbar", cheat.getName());
        assertEquals("Nimmt keinen Schaden", cheat.getDescription());
    }

    @Test
    void rejectsBlankName() {
        assertThrows(IllegalArgumentException.class, () -> new Cheat("", "beschreibung"));
    }

    @Test
    void nullDescriptionBecomesEmpty() {
        Cheat cheat = new Cheat("Unverwundbar", null);
        assertEquals("", cheat.getDescription());
    }

    @Test
    void updatesDescription() {
        Cheat cheat = new Cheat("Unverwundbar", "alt");
        cheat.setDescription("neu");
        assertEquals("neu", cheat.getDescription());
    }

    @Test
    void equalityBasedOnName() {
        Cheat c1 = new Cheat("Unverwundbar", "alt");
        Cheat c2 = new Cheat("Unverwundbar", "neu");
        assertEquals(c1, c2);
    }
}
