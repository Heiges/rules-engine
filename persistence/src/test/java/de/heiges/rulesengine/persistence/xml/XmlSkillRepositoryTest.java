package de.heiges.rulesengine.persistence.xml;

import de.heiges.rulesengine.coreelements.domain.model.Attribute;
import de.heiges.rulesengine.coreelements.domain.model.AttributeSet;
import de.heiges.rulesengine.coreelements.domain.model.Skill;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class XmlSkillRepositoryTest {

    private final XmlSkillRepository repository = new XmlSkillRepository();
    private AttributeSet attributeSet;
    private Attribute staerke;
    private Attribute geschicklichkeit;

    @BeforeEach
    void setUp() {
        staerke = new Attribute("Stärke", 10);
        geschicklichkeit = new Attribute("Geschicklichkeit", 8);
        attributeSet = new AttributeSet();
        attributeSet.add(staerke);
        attributeSet.add(geschicklichkeit);
    }

    @Test
    void speichernUndLaden_erhaltSkillsKorrekt(@TempDir Path tempDir) throws IOException {
        List<Skill> original = List.of(
                new Skill("Klettern", geschicklichkeit, 3),
                new Skill("Schwimmen", staerke, 2)
        );

        Path file = tempDir.resolve("skills.xml");
        repository.save(original, file);
        Collection<Skill> geladen = repository.load(file, attributeSet);

        assertEquals(2, geladen.size());
        List<Skill> skillListe = List.copyOf(geladen);
        assertEquals("Klettern", skillListe.get(0).getName());
        assertEquals("Geschicklichkeit", skillListe.get(0).getLinkedAttribute().getName());
        assertEquals(3, skillListe.get(0).getLevel());
        assertEquals("Schwimmen", skillListe.get(1).getName());
        assertEquals("Stärke", skillListe.get(1).getLinkedAttribute().getName());
        assertEquals(2, skillListe.get(1).getLevel());
    }

    @Test
    void speichernUndLaden_leereSkillListe(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("skills.xml");
        repository.save(List.of(), file);
        Collection<Skill> geladen = repository.load(file, attributeSet);

        assertTrue(geladen.isEmpty());
    }

    @Test
    void laden_unbekannterAttributName_wirftIllegalArgumentException(@TempDir Path tempDir) throws IOException {
        List<Skill> skills = List.of(new Skill("Bogenschießen", staerke, 1));
        Path file = tempDir.resolve("skills.xml");
        repository.save(skills, file);

        AttributeSet leererAttributeSet = new AttributeSet();
        assertThrows(IllegalArgumentException.class, () -> repository.load(file, leererAttributeSet));
    }

    @Test
    void laden_nichtExistenteDatei_wirftIOException() {
        Path nichtVorhanden = Path.of("/tmp/skills-existiert-nicht-abc123.xml");
        assertThrows(IOException.class, () -> repository.load(nichtVorhanden, attributeSet));
    }
}
