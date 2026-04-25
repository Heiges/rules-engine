package de.heiges.rulesengine.persistence.xml;

import de.heiges.rulesengine.coreelements.domain.model.Attribute;
import de.heiges.rulesengine.coreelements.domain.model.AttributeSet;
import de.heiges.rulesengine.coreelements.domain.model.Skill;
import de.heiges.rulesengine.persistence.repository.LoadedRuleset;
import de.heiges.rulesengine.persistence.repository.RulesetRepository;
import de.heiges.rulesengine.persistence.xml.dto.AttributeDto;
import de.heiges.rulesengine.persistence.xml.dto.RulesetDto;
import de.heiges.rulesengine.persistence.xml.dto.SkillDto;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public class XmlRulesetRepository implements RulesetRepository {

    @Override
    public void save(AttributeSet attributeSet, Collection<Skill> skills, Path file) throws IOException {
        RulesetDto dto = toDto(attributeSet, skills);
        try {
            JAXBContext context = JAXBContext.newInstance(RulesetDto.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(dto, file.toFile());
        } catch (JAXBException e) {
            throw new IOException("XML-Serialisierung fehlgeschlagen: " + file, e);
        }
    }

    @Override
    public LoadedRuleset load(Path file) throws IOException {
        if (!file.toFile().exists()) {
            throw new IOException("Datei nicht gefunden: " + file);
        }
        try {
            JAXBContext context = JAXBContext.newInstance(RulesetDto.class);
            RulesetDto dto = (RulesetDto) context.createUnmarshaller().unmarshal(file.toFile());
            return toDomain(dto);
        } catch (JAXBException e) {
            throw new IOException("XML-Deserialisierung fehlgeschlagen: " + file, e);
        }
    }

    @Override
    public List<Path> listAll(Path directory) throws IOException {
        if (!Files.exists(directory)) {
            return List.of();
        }
        try (Stream<Path> stream = Files.list(directory)) {
            return stream
                    .filter(p -> p.getFileName().toString().endsWith(".xml"))
                    .sorted()
                    .toList();
        }
    }

    private RulesetDto toDto(AttributeSet attributeSet, Collection<Skill> skills) {
        RulesetDto dto = new RulesetDto();
        for (Attribute attribute : attributeSet.getAll()) {
            dto.getAttributeSet().getAttributes().add(new AttributeDto(attribute.getName(), attribute.getDescription()));
        }
        for (Skill skill : skills) {
            dto.getSkills().getSkills().add(new SkillDto(
                    skill.getName(),
                    skill.getLinkedAttribute().getName(),
                    skill.getLevel()
            ));
        }
        return dto;
    }

    private LoadedRuleset toDomain(RulesetDto dto) {
        AttributeSet attributeSet = new AttributeSet();
        for (AttributeDto attributeDto : dto.getAttributeSet().getAttributes()) {
            attributeSet.add(new Attribute(attributeDto.getName(), attributeDto.getDescription()));
        }

        List<Skill> skills = new ArrayList<>();
        for (SkillDto skillDto : dto.getSkills().getSkills()) {
            Attribute linkedAttribute = attributeSet.find(skillDto.getLinkedAttributeName())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Attribut nicht gefunden: " + skillDto.getLinkedAttributeName()));
            skills.add(new Skill(skillDto.getName(), linkedAttribute, skillDto.getLevel()));
        }

        return new LoadedRuleset(attributeSet, skills);
    }
}
