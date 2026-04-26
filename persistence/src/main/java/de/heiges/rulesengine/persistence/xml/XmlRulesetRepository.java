package de.heiges.rulesengine.persistence.xml;

import de.heiges.rulesengine.coreelements.domain.model.Attribute;
import de.heiges.rulesengine.coreelements.domain.model.AttributeGroup;
import de.heiges.rulesengine.coreelements.domain.model.AttributeSet;
import de.heiges.rulesengine.coreelements.domain.model.Skill;
import de.heiges.rulesengine.coreelements.domain.model.Value;
import de.heiges.rulesengine.coreelements.domain.model.ValueRange;
import de.heiges.rulesengine.persistence.repository.LoadedRuleset;
import de.heiges.rulesengine.persistence.repository.RulesetRepository;
import de.heiges.rulesengine.persistence.xml.dto.AttributeDto;
import de.heiges.rulesengine.persistence.xml.dto.AttributeGroupDto;
import de.heiges.rulesengine.persistence.xml.dto.RulesetDto;
import de.heiges.rulesengine.persistence.xml.dto.SkillDto;
import de.heiges.rulesengine.persistence.xml.dto.ValueRangeDto;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public class XmlRulesetRepository implements RulesetRepository {

    private static final ValueRange DEFAULT_VALUE_RANGE = new ValueRange(-10, 0, 10);

    @Override
    public void save(ValueRange valueRange, AttributeSet attributeSet, Collection<Skill> skills, Path file) throws IOException {
        RulesetDto dto = toDto(valueRange, attributeSet, skills);
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

    public LoadedRuleset fromXml(String xml) throws IOException {
        try {
            JAXBContext context = JAXBContext.newInstance(RulesetDto.class);
            RulesetDto dto = (RulesetDto) context.createUnmarshaller().unmarshal(new StringReader(xml));
            return toDomain(dto);
        } catch (JAXBException e) {
            throw new IOException("XML-Deserialisierung fehlgeschlagen", e);
        }
    }

    public String toXml(ValueRange valueRange, AttributeSet attributeSet, Collection<Skill> skills) throws IOException {
        RulesetDto dto = toDto(valueRange, attributeSet, skills);
        try {
            JAXBContext context = JAXBContext.newInstance(RulesetDto.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            StringWriter writer = new StringWriter();
            marshaller.marshal(dto, writer);
            return writer.toString();
        } catch (JAXBException e) {
            throw new IOException("XML-Serialisierung fehlgeschlagen", e);
        }
    }

    private RulesetDto toDto(ValueRange valueRange, AttributeSet attributeSet, Collection<Skill> skills) {
        RulesetDto dto = new RulesetDto();
        dto.setValueRange(new ValueRangeDto(valueRange.min(), valueRange.average(), valueRange.max()));
        for (AttributeGroup group : attributeSet.getGroups()) {
            AttributeGroupDto groupDto = new AttributeGroupDto(group.getName());
            for (Attribute attribute : group.getAll()) {
                groupDto.getAttributes().add(
                        new AttributeDto(attribute.getName(), attribute.getDescription(), attribute.getValue().amount()));
            }
            dto.getAttributeSet().getGroups().add(groupDto);
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
        ValueRange valueRange = dto.getValueRange() != null
                ? new ValueRange(dto.getValueRange().getMin(), dto.getValueRange().getAverage(), dto.getValueRange().getMax())
                : DEFAULT_VALUE_RANGE;

        AttributeSet attributeSet = new AttributeSet();
        for (AttributeGroupDto groupDto : dto.getAttributeSet().getGroups()) {
            AttributeGroup group = new AttributeGroup(groupDto.getName());
            for (AttributeDto attributeDto : groupDto.getAttributes()) {
                group.add(new Attribute(
                        attributeDto.getName(),
                        attributeDto.getDescription(),
                        new Value(attributeDto.getValue())));
            }
            attributeSet.addGroup(group);
        }

        List<Skill> skills = new ArrayList<>();
        for (SkillDto skillDto : dto.getSkills().getSkills()) {
            Attribute linkedAttribute = attributeSet.find(skillDto.getLinkedAttributeName())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Attribut nicht gefunden: " + skillDto.getLinkedAttributeName()));
            skills.add(new Skill(skillDto.getName(), linkedAttribute, skillDto.getLevel()));
        }

        return new LoadedRuleset(valueRange, attributeSet, skills);
    }
}
