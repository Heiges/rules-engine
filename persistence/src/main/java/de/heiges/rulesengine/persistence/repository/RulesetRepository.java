package de.heiges.rulesengine.persistence.repository;

import de.heiges.rulesengine.coreelements.domain.model.AttributeSet;
import de.heiges.rulesengine.coreelements.domain.model.Skill;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

public interface RulesetRepository {

    void save(AttributeSet attributeSet, Collection<Skill> skills, Path file) throws IOException;

    LoadedRuleset load(Path file) throws IOException;

    List<Path> listAll(Path directory) throws IOException;
}
