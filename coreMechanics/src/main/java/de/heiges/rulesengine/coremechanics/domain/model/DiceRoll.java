package de.heiges.rulesengine.coremechanics.domain.model;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public record DiceRoll(List<Integer> values) {

    public DiceRoll {
        values = List.copyOf(values);
    }

    public Optional<Pasch> bestPasch() {
        return values.stream()
                .collect(Collectors.groupingBy(v -> v, Collectors.counting()))
                .entrySet().stream()
                .filter(e -> e.getValue() >= 2)
                .max(Comparator.comparingLong(Map.Entry<Integer, Long>::getValue)
                        .thenComparingInt(Map.Entry::getKey))
                .map(e -> new Pasch(e.getKey(), e.getValue().intValue()));
    }

    public boolean isSuccess() {
        return bestPasch().isPresent();
    }
}
