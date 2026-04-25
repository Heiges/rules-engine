package de.heiges.rulesengine.persistence.repository;

import de.heiges.rulesengine.coreelements.domain.model.AttributeSet;
import de.heiges.rulesengine.coreelements.domain.model.Skill;

import java.util.Collection;

public record LoadedRuleset(AttributeSet attributeSet, Collection<Skill> skills) {}
