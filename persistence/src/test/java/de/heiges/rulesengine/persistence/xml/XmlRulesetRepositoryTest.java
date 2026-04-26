package de.heiges.rulesengine.persistence.xml;

import de.heiges.rulesengine.coreelements.domain.model.Attribute;
import de.heiges.rulesengine.coreelements.domain.model.AttributeSet;
import de.heiges.rulesengine.coreelements.domain.model.Skill;
import de.heiges.rulesengine.coreelements.domain.model.Value;
import de.heiges.rulesengine.coreelements.domain.model.ValueRange;
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
    private final ValueRange standardRange = new ValueRange(-10, 0, 10);

    @Test
    void speichernUndLaden_erhaltAttributeUndSkillsKorrekt(@TempDir Path tempDir) throws IOException {
        AttributeSet attributeSet = new AttributeSet();
        Attribute staerke = new Attribute("Stärke", "Körperliche Kraft", new Value(3));
        Attribute geschicklichkeit = new Attribute("Geschicklichkeit", "Feinmotorik und Reaktion", new Value(-2));
        attributeSet.add(staerke);
        attributeSet.add(geschicklichkeit);

        List<Skill> skills = List.of(
                new Skill("Klettern", geschicklichkeit, 3),
                new Skill("Schwimmen", staerke, 2)
        );

        Path file = tempDir.resolve("grundregeln.xml");
        repository.save(standardRange, attributeSet, skills, file);
        LoadedRuleset geladen = repository.load(file);

        assertEquals(2, geladen.attributeSet().size());
        assertTrue(geladen.attributeSet().contains("Stärke"));
        assertEquals("Körperliche Kraft", geladen.attributeSet().find("Stärke").orElseThrow().getDescription());
        assertEquals(3, geladen.attributeSet().find("Stärke").orElseThrow().getValue().amount());
        assertEquals(-2, geladen.attributeSet().find("Geschicklichkeit").orElseThrow().getValue().amount());

        List<Skill> skillListe = List.copyOf(geladen.skills());
        assertEquals(2, skillListe.size());
        assertEquals("Klettern", skillListe.get(0).getName());
        assertEquals("Geschicklichkeit", skillListe.get(0).getLinkedAttribute().getName());
        assertEquals(3, skillListe.get(0).getLevel());
        assertEquals("Schwimmen", skillListe.get(1).getName());
        assertEquals(2, skillListe.get(1).getLevel());
    }

    @Test
    void speichernUndLaden_erhaltValueRangeKorrekt(@TempDir Path tempDir) throws IOException {
        ValueRange range = new ValueRange(-5, 2, 15);
        Path file = tempDir.resolve("range.xml");
        repository.save(range, new AttributeSet(), List.of(), file);
        LoadedRuleset geladen = repository.load(file);

        assertEquals(-5, geladen.valueRange().min());
        assertEquals(2, geladen.valueRange().average());
        assertEquals(15, geladen.valueRange().max());
    }

    @Test
    void laden_ohneWertebereichElement_verwendetStandardwerte(@TempDir Path tempDir) throws IOException {
        String xml = """
                <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
                <ruleset>
                  <attributeSet/>
                  <skills/>
                </ruleset>
                """;
        Path file = tempDir.resolve("alt.xml");
        Files.writeString(file, xml);
        LoadedRuleset geladen = repository.load(file);

        assertEquals(-10, geladen.valueRange().min());
        assertEquals(0, geladen.valueRange().average());
        assertEquals(10, geladen.valueRange().max());
    }

    @Test
    void speichernUndLaden_leereDaten(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("leer.xml");
        repository.save(standardRange, new AttributeSet(), List.of(), file);
        LoadedRuleset geladen = repository.load(file);

        assertEquals(0, geladen.attributeSet().size());
        assertTrue(geladen.skills().isEmpty());
    }

    @Test
    void gespeicherteDateiExistiert(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("test.xml");
        repository.save(standardRange, new AttributeSet(), List.of(), file);
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
        repository.save(standardRange, new AttributeSet(), List.of(), tempDir.resolve("alpha.xml"));
        repository.save(standardRange, new AttributeSet(), List.of(), tempDir.resolve("beta.xml"));
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
