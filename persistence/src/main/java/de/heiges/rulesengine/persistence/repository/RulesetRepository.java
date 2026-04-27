package de.heiges.rulesengine.persistence.repository;

import de.heiges.rulesengine.coreelements.domain.model.AttributeSet;
import de.heiges.rulesengine.coreelements.domain.model.SkillDomain;
import de.heiges.rulesengine.coreelements.domain.model.SkillVerb;
import de.heiges.rulesengine.coreelements.domain.model.ValueRange;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

public interface RulesetRepository {

    void save(ValueRange valueRange, AttributeSet attributeSet, Collection<SkillVerb> skills, Collection<SkillDomain> skillDomains, Path file) throws IOException;

    LoadedRuleset load(Path file) throws IOException;

    List<Path> listAll(Path directory) throws IOException;
}
