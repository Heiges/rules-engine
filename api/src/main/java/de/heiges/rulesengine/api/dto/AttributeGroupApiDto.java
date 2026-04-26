package de.heiges.rulesengine.api.dto;

import java.util.List;

public record AttributeGroupApiDto(String group, List<AttributeApiDto> attributes) {}
