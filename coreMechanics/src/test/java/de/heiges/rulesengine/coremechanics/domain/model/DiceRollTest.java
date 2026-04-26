package de.heiges.rulesengine.coremechanics.domain.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class DiceRollTest {

    @Test
    void noPaschIsFailure() {
        DiceRoll roll = new DiceRoll(List.of(1, 2, 3, 4));
        assertFalse(roll.isSuccess());
        assertEquals(Optional.empty(), roll.bestPasch());
    }

    @Test
    void pairIsSuccess() {
        DiceRoll roll = new DiceRoll(List.of(3, 3, 1, 2));
        assertTrue(roll.isSuccess());
        assertEquals(new Pasch(3, 2), roll.bestPasch().orElseThrow());
    }

    @Test
    void tripletBeatsPair() {
        DiceRoll roll = new DiceRoll(List.of(5, 5, 5, 2, 2));
        Pasch best = roll.bestPasch().orElseThrow();
        assertEquals(3, best.count());
        assertEquals(5, best.faceValue());
    }

    @Test
    void higherFaceValueBreaksTie() {
        DiceRoll roll = new DiceRoll(List.of(2, 2, 5, 5));
        Pasch best = roll.bestPasch().orElseThrow();
        assertEquals(2, best.count());
        assertEquals(5, best.faceValue());
    }

    @Test
    void fullPasch() {
        DiceRoll roll = new DiceRoll(List.of(4, 4, 4, 4, 4, 4));
        Pasch best = roll.bestPasch().orElseThrow();
        assertEquals(6, best.count());
        assertEquals(4, best.faceValue());
    }

    @Test
    void valuesAreImmutable() {
        List<Integer> mutable = new ArrayList<>(List.of(1, 2));
        DiceRoll roll = new DiceRoll(mutable);
        mutable.add(3);
        assertEquals(2, roll.values().size());
    }
}
