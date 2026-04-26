package de.heiges.rulesengine.api.dto;

import java.util.List;

public record RollResultApiDto(
        List<Integer> dice,
        boolean success,
        Integer paschValue,
        Integer paschCount
) {}
