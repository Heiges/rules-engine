package de.heiges.rulesengine.persistence.xml;

import de.heiges.rulesengine.coreelements.domain.model.Attribute;
import de.heiges.rulesengine.coreelements.domain.model.AttributeSet;
import de.heiges.rulesengine.coreelements.domain.model.Skill;
import de.heiges.rulesengine.persistence.repository.LoadedRuleset;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class XmlRulesetRepositoryTest {

    private final XmlRulesetRepository repository = new XmlRulesetRepository();

    @Test
    void speichernUndLaden_erhaltAttributeUndSkillsKorrekt(@TempDir Path tempDir) throws IOException {
        AttributeSet attributeSet = new AttributeSet();
        Attribute staerke = new Attribute("Stärke", "Körperliche Kraft");
        Attribute geschicklichkeit = new Attribute("Geschicklichkeit", "Feinmotorik und Reaktion");
        attributeSet.add(staerke);
        attributeSet.add(geschicklichkeit);

        List<Skill> skills = List.of(
                new Skill("Klettern", geschicklichkeit, 3),
                new Skill("Schwimmen", staerke, 2)
        );

        Path file = tempDir.resolve("grundregeln.xml");
        repository.save(attributeSet, skills, file);
        LoadedRuleset geladen = repository.load(file);

        assertEquals(2, geladen.attributeSet().size());
        assertTrue(geladen.attributeSet().contains("Stärke"));
        assertEquals("Körperliche Kraft", geladen.attributeSet().find("Stärke").orElseThrow().getDescription());
        assertEquals("Feinmotorik und Reaktion", geladen.attributeSet().find("Geschicklichkeit").orElseThrow().getDescription());

        List<Skill> skillListe = List.copyOf(geladen.skills());
        assertEquals(2, skillListe.size());
        assertEquals("Klettern", skillListe.get(0).getName());
        assertEquals("Geschicklichkeit", skillListe.get(0).getLinkedAttribute().getName());
        assertEquals(3, skillListe.get(0).getLevel());
        assertEquals("Schwimmen", skillListe.get(1).getName());
        assertEquals(2, skillListe.get(1).getLevel());
    }

    @Test
    void speichernUndLaden_leereDaten(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("leer.xml");
        repository.save(new AttributeSet(), List.of(), file);
        LoadedRuleset geladen = repository.load(file);

        assertEquals(0, geladen.attributeSet().size());
        assertTrue(geladen.skills().isEmpty());
    }

    @Test
    void gespeicherteDateiExistiert(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("test.xml");
        repository.save(new AttributeSet(), List.of(), file);
        assertTrue(file.toFile().exists());
    }

    @Test
    void laden_nichtExistenteDatei_wirftIOException() {
        Path nichtVorhanden = Path.of("/tmp/ruleset-existiert-nicht-abc123.xml");
        assertThrows(IOException.class, () -> repository.load(nichtVorhanden));
    }

    @Test
    void laden_unbekannterAttributName_wirftIllegalArgumentException(@TempDir Path tempDir) throws IOException {
        String xml = """
                <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
                <ruleset>
                  <attributeSet/>
                  <skills>
                    <skill name="Bogenschießen" linkedAttributeName="NichtVorhanden" level="1"/>
                  </skills>
                </ruleset>
                """;
        Path file = tempDir.resolve("broken.xml");
        Files.writeString(file, xml);
        assertThrows(IllegalArgumentException.class, () -> repository.load(file));
    }

    @Test
    void listAll_liefertNurXmlDateien(@TempDir Path tempDir) throws IOException {
        repository.save(new AttributeSet(), List.of(), tempDir.resolve("alpha.xml"));
        repository.save(new AttributeSet(), List.of(), tempDir.resolve("beta.xml"));
        Files.createFile(tempDir.resolve("ignorieren.txt"));

        List<Path> rulesets = repository.listAll(tempDir);

        assertEquals(2, rulesets.size());
        assertEquals("alpha.xml", rulesets.get(0).getFileName().toString());
        assertEquals("beta.xml", rulesets.get(1).getFileName().toString());
    }

    @Test
    void listAll_nichtExistentesVerzeichnis_liefertLeere(@TempDir Path tempDir) throws IOException {
        List<Path> rulesets = repository.listAll(tempDir.resolve("nicht-vorhanden"));
        assertTrue(rulesets.isEmpty());
    }
}
