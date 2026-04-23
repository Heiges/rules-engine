package de.heiges.rulesengine.persistence.xml;

import de.heiges.rulesengine.coreelements.domain.model.Attribute;
import de.heiges.rulesengine.coreelements.domain.model.AttributeSet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class XmlAttributeSetRepositoryTest {

    private final XmlAttributeSetRepository repository = new XmlAttributeSetRepository();

    @Test
    void speichernUndLaden_erhaltAttributeSetKorrekt(@TempDir Path tempDir) throws IOException {
        AttributeSet original = new AttributeSet();
        original.add(new Attribute("Stärke", 10));
        original.add(new Attribute("Geschicklichkeit", 8));
        original.add(new Attribute("Intelligenz", 12));

        Path file = tempDir.resolve("attributes.xml");
        repository.save(original, file);
        AttributeSet geladen = repository.load(file);

        assertEquals(3, geladen.size());
        assertTrue(geladen.contains("Stärke"));
        assertTrue(geladen.contains("Geschicklichkeit"));
        assertTrue(geladen.contains("Intelligenz"));
        assertEquals(10, geladen.find("Stärke").orElseThrow().getValue());
        assertEquals(8, geladen.find("Geschicklichkeit").orElseThrow().getValue());
        assertEquals(12, geladen.find("Intelligenz").orElseThrow().getValue());
    }

    @Test
    void speichernUndLaden_leereAttributeSet(@TempDir Path tempDir) throws IOException {
        AttributeSet original = new AttributeSet();
        Path file = tempDir.resolve("empty.xml");

        repository.save(original, file);
        AttributeSet geladen = repository.load(file);

        assertEquals(0, geladen.size());
    }

    @Test
    void gespeicherteDateiExistiert(@TempDir Path tempDir) throws IOException {
        AttributeSet attributeSet = new AttributeSet();
        attributeSet.add(new Attribute("Mut", 5));
        Path file = tempDir.resolve("attributes.xml");

        repository.save(attributeSet, file);

        assertTrue(file.toFile().exists());
    }

    @Test
    void laden_nichtExistenteDatei_wirftIOException() {
        Path nichtVorhanden = Path.of("/tmp/existiert-nicht-abc123.xml");
        assertThrows(IOException.class, () -> repository.load(nichtVorhanden));
    }

    @Test
    void reihenfolgeBleibtErhalten(@TempDir Path tempDir) throws IOException {
        AttributeSet original = new AttributeSet();
        original.add(new Attribute("Erster", 1));
        original.add(new Attribute("Zweiter", 2));
        original.add(new Attribute("Dritter", 3));

        Path file = tempDir.resolve("attributes.xml");
        repository.save(original, file);
        AttributeSet geladen = repository.load(file);

        Collection<Attribute> attribute = geladen.getAll();
        String[] namen = attribute.stream().map(Attribute::getName).toArray(String[]::new);
        assertArrayEquals(new String[]{"Erster", "Zweiter", "Dritter"}, namen);
    }
}
