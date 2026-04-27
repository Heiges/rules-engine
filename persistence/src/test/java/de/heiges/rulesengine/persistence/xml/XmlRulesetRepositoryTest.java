package de.heiges.rulesengine.persistence.xml;

import de.heiges.rulesengine.coreelements.domain.model.Attribute;
import de.heiges.rulesengine.coreelements.domain.model.AttributeGroup;
import de.heiges.rulesengine.coreelements.domain.model.AttributeSet;
import de.heiges.rulesengine.coreelements.domain.model.SkillVerb;
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
        Attribute staerke = new Attribute("Stärke", "Körperliche Kraft", new Value(3));
        Attribute geschicklichkeit = new Attribute("Geschicklichkeit", "Feinmotorik und Reaktion", new Value(-2));

        AttributeGroup koerper = new AttributeGroup("Körper");
        koerper.add(staerke);
        koerper.add(geschicklichkeit);

        AttributeSet attributeSet = new AttributeSet();
        attributeSet.addGroup(koerper);

        List<SkillVerb> skills = List.of(
                new SkillVerb("Klettern", "Vertikale Fortbewegung"),
                new SkillVerb("Schwimmen", "Fortbewegung im Wasser")
        );

        Path file = tempDir.resolve("grundregeln.xml");
        repository.save(standardRange, attributeSet, skills, file);
        LoadedRuleset geladen = repository.load(file);

        assertEquals(1, geladen.attributeSet().getGroups().size());
        assertEquals("Körper", geladen.attributeSet().getGroups().iterator().next().getName());
        assertEquals(2, geladen.attributeSet().size());
        assertTrue(geladen.attributeSet().contains("Stärke"));
        assertEquals("Körperliche Kraft", geladen.attributeSet().find("Stärke").orElseThrow().getDescription());
        assertEquals(3, geladen.attributeSet().find("Stärke").orElseThrow().getValue().amount());
        assertEquals(-2, geladen.attributeSet().find("Geschicklichkeit").orElseThrow().getValue().amount());

        List<SkillVerb> skillListe = List.copyOf(geladen.skills());
        assertEquals(2, skillListe.size());
        assertEquals("Klettern", skillListe.get(0).getName());
        assertEquals("Vertikale Fortbewegung", skillListe.get(0).getDescription());
        assertEquals("Schwimmen", skillListe.get(1).getName());
        assertEquals("Fortbewegung im Wasser", skillListe.get(1).getDescription());
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
