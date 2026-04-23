package de.heiges.rulesengine.persistence.xml;

import de.heiges.rulesengine.coreelements.domain.model.Attribute;
import de.heiges.rulesengine.coreelements.domain.model.AttributeSet;
import de.heiges.rulesengine.coreelements.domain.model.Skill;
import de.heiges.rulesengine.persistence.repository.SkillRepository;
import de.heiges.rulesengine.persistence.xml.dto.SkillDto;
import de.heiges.rulesengine.persistence.xml.dto.SkillSetDto;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class XmlSkillRepository implements SkillRepository {

    @Override
    public void save(Collection<Skill> skills, Path file) throws IOException {
        SkillSetDto dto = toDto(skills);
        try {
            JAXBContext context = JAXBContext.newInstance(SkillSetDto.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(dto, file.toFile());
        } catch (JAXBException e) {
            throw new IOException("XML-Serialisierung fehlgeschlagen: " + file, e);
        }
    }

    @Override
    public Collection<Skill> load(Path file, AttributeSet attributeSet) throws IOException {
        if (!file.toFile().exists()) {
            throw new IOException("Datei nicht gefunden: " + file);
        }
        try {
            JAXBContext context = JAXBContext.newInstance(SkillSetDto.class);
            SkillSetDto dto = (SkillSetDto) context.createUnmarshaller().unmarshal(file.toFile());
            return toDomain(dto, attributeSet);
        } catch (JAXBException e) {
            throw new IOException("XML-Deserialisierung fehlgeschlagen: " + file, e);
        }
    }

    private SkillSetDto toDto(Collection<Skill> skills) {
        SkillSetDto dto = new SkillSetDto();
        for (Skill skill : skills) {
            dto.getSkills().add(new SkillDto(
                    skill.getName(),
                    skill.getLinkedAttribute().getName(),
                    skill.getLevel()
            ));
        }
        return dto;
    }

    private List<Skill> toDomain(SkillSetDto dto, AttributeSet attributeSet) {
        List<Skill> skills = new ArrayList<>();
        for (SkillDto skillDto : dto.getSkills()) {
            Attribute linkedAttribute = attributeSet.find(skillDto.getLinkedAttributeName())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Attribut nicht gefunden: " + skillDto.getLinkedAttributeName()));
            skills.add(new Skill(skillDto.getName(), linkedAttribute, skillDto.getLevel()));
        }
        return skills;
    }
}
