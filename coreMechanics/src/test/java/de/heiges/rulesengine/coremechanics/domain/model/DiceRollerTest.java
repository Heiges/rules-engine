package de.heiges.rulesengine.coremechanics.domain.model;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class DiceRollerTest {

    private final DiceRoller roller = new DiceRoller(new Random(42));

    @Test
    void rollsMinimumTwoDiceForValueOne() {
        DiceRoll roll = roller.roll(1);
        assertEquals(2, roll.values().size());
    }

    @Test
    void rollsMinimumTwoDiceForValueZero() {
        DiceRoll roll = roller.roll(0);
        assertEquals(2, roll.values().size());
    }

    @Test
    void rollsExactDiceCountForHighValue() {
        DiceRoll roll = roller.roll(5);
        assertEquals(5, roll.values().size());
    }

    @Test
    void diceValuesAreInRange() {
        DiceRoll roll = roller.roll(6);
        for (int v : roll.values()) {
            assertTrue(v >= 1 && v <= 6, "Würfelwert außerhalb 1–6: " + v);
        }
    }
}
