package de.heiges.rulesengine.persistence.repository;

import de.heiges.rulesengine.coreelements.domain.model.AttributeSet;
import de.heiges.rulesengine.coreelements.domain.model.SkillDomain;
import de.heiges.rulesengine.coreelements.domain.model.SkillVerb;
import de.heiges.rulesengine.coreelements.domain.model.ValueRange;

import java.util.Collection;

public record LoadedRuleset(
        ValueRange valueRange,
        AttributeSet attributeSet,
        Collection<SkillVerb> skills,
        Collection<SkillDomain> skillDomains
) {}
