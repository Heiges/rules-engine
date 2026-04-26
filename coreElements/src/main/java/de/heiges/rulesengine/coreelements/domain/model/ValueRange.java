package de.heiges.rulesengine.coreelements.domain.model;

public record ValueRange(int min, int average, int max) {

    public ValueRange {
        if (min > average || average > max) {
            throw new IllegalArgumentException(
                    "ValueRange requires min ≤ average ≤ max, got min=" + min + ", average=" + average + ", max=" + max);
        }
    }
}
