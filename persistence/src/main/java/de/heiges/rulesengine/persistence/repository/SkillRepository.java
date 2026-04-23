package de.heiges.rulesengine.persistence.repository;

import de.heiges.rulesengine.coreelements.domain.model.AttributeSet;
import de.heiges.rulesengine.coreelements.domain.model.Skill;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;

public interface SkillRepository {

    void save(Collection<Skill> skills, Path file) throws IOException;

    Collection<Skill> load(Path file, AttributeSet attributeSet) throws IOException;
}
