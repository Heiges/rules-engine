package de.heiges.rulesengine.coreelements.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValueRangeTest {

    @Test
    void erstelltGueltigeWertebereich() {
        ValueRange vr = new ValueRange(-10, 0, 10);
        assertEquals(-10, vr.min());
        assertEquals(0, vr.average());
        assertEquals(10, vr.max());
    }

    @Test
    void erstelltWertbereichMitGleichenWerten() {
        ValueRange vr = new ValueRange(0, 0, 0);
        assertEquals(0, vr.min());
        assertEquals(0, vr.average());
        assertEquals(0, vr.max());
    }

    @Test
    void erstelltWertbereichWoAverageMitMinGleich() {
        ValueRange vr = new ValueRange(-5, -5, 10);
        assertEquals(-5, vr.min());
        assertEquals(-5, vr.average());
    }

    @Test
    void erstelltWertbereichWoAverageMitMaxGleich() {
        ValueRange vr = new ValueRange(0, 10, 10);
        assertEquals(10, vr.average());
        assertEquals(10, vr.max());
    }

    @Test
    void wirftExceptionWennMinGroesserAlsAverage() {
        assertThrows(IllegalArgumentException.class, () -> new ValueRange(5, 0, 10));
    }

    @Test
    void wirftExceptionWennAverageGroesserAlsMax() {
        assertThrows(IllegalArgumentException.class, () -> new ValueRange(-10, 15, 10));
    }

    @Test
    void wirftExceptionWennMinGroesserAlsMax() {
        assertThrows(IllegalArgumentException.class, () -> new ValueRange(10, 10, -10));
    }

    @Test
    void gleichheitBasiertAufAllenFeldern() {
        assertEquals(new ValueRange(-10, 0, 10), new ValueRange(-10, 0, 10));
        assertNotEquals(new ValueRange(-10, 0, 10), new ValueRange(-10, 1, 10));
    }
}
