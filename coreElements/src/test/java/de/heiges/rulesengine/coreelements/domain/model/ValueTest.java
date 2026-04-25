package de.heiges.rulesengine.coreelements.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValueTest {

    @Test
    void storesPositiveAmount() {
        Value v = new Value(10);
        assertEquals(10, v.amount());
    }

    @Test
    void storesNegativeAmount() {
        Value v = new Value(-5);
        assertEquals(-5, v.amount());
    }

    @Test
    void storesZero() {
        Value v = new Value(0);
        assertEquals(0, v.amount());
    }

    @Test
    void equalityBasedOnAmount() {
        assertEquals(new Value(3), new Value(3));
        assertNotEquals(new Value(3), new Value(-3));
    }

    @Test
    void toStringContainsAmount() {
        assertTrue(new Value(7).toString().contains("7"));
    }
}
