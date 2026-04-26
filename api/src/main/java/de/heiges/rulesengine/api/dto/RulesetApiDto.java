package de.heiges.rulesengine.api.dto;

import java.util.List;

public record RulesetApiDto(
        ValueRangeApiDto valueRange,
        List<AttributeApiDto> attributes,
        List<SkillApiDto> skills
) {}
