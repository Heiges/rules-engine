package de.heiges.rulesengine.persistence.repository;

import de.heiges.rulesengine.coreelements.domain.model.AttributeSet;

import java.io.IOException;
import java.nio.file.Path;

public interface AttributeSetRepository {

    void save(AttributeSet attributeSet, Path file) throws IOException;

    AttributeSet load(Path file) throws IOException;
}
