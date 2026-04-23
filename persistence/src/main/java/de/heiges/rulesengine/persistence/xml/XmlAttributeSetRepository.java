package de.heiges.rulesengine.persistence.xml;

import de.heiges.rulesengine.coreelements.domain.model.Attribute;
import de.heiges.rulesengine.coreelements.domain.model.AttributeSet;
import de.heiges.rulesengine.persistence.repository.AttributeSetRepository;
import de.heiges.rulesengine.persistence.xml.dto.AttributeDto;
import de.heiges.rulesengine.persistence.xml.dto.AttributeSetDto;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;

import java.io.IOException;
import java.nio.file.Path;

public class XmlAttributeSetRepository implements AttributeSetRepository {

    @Override
    public void save(AttributeSet attributeSet, Path file) throws IOException {
        AttributeSetDto dto = toDto(attributeSet);
        try {
            JAXBContext context = JAXBContext.newInstance(AttributeSetDto.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(dto, file.toFile());
        } catch (JAXBException e) {
            throw new IOException("XML-Serialisierung fehlgeschlagen: " + file, e);
        }
    }

    @Override
    public AttributeSet load(Path file) throws IOException {
        if (!file.toFile().exists()) {
            throw new IOException("Datei nicht gefunden: " + file);
        }
        try {
            JAXBContext context = JAXBContext.newInstance(AttributeSetDto.class);
            AttributeSetDto dto = (AttributeSetDto) context.createUnmarshaller().unmarshal(file.toFile());
            return toDomain(dto);
        } catch (JAXBException e) {
            throw new IOException("XML-Deserialisierung fehlgeschlagen: " + file, e);
        }
    }

    private AttributeSetDto toDto(AttributeSet attributeSet) {
        AttributeSetDto dto = new AttributeSetDto();
        for (Attribute attribute : attributeSet.getAll()) {
            dto.getAttributes().add(new AttributeDto(attribute.getName(), attribute.getValue()));
        }
        return dto;
    }

    private AttributeSet toDomain(AttributeSetDto dto) {
        AttributeSet attributeSet = new AttributeSet();
        for (AttributeDto attributeDto : dto.getAttributes()) {
            attributeSet.add(new Attribute(attributeDto.getName(), attributeDto.getValue()));
        }
        return attributeSet;
    }
}
