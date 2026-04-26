package de.heiges.rulesengine.api.dto;

import java.util.List;

public record RulesetApiDto(
        ValueRangeApiDto valueRange,
        List<AttributeGroupApiDto> attributeGroups,
        List<SkillApiDto> skills
) {}
