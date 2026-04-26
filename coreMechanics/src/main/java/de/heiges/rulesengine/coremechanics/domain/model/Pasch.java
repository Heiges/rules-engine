package de.heiges.rulesengine.coremechanics.domain.model;

public record Pasch(int faceValue, int count) {

    public Pasch {
        if (count < 2) throw new IllegalArgumentException("Ein Pasch braucht mindestens 2 gleiche Würfel, aber war: " + count);
        if (faceValue < 1 || faceValue > 6) throw new IllegalArgumentException("Augenzahl muss zwischen 1 und 6 liegen, aber war: " + faceValue);
    }
}
