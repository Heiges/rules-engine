package de.heiges.rulesengine.coreelements.domain.model;

public record Value(int amount) {

    @Override
    public String toString() {
        return "Value{amount=" + amount + "}";
    }
}
