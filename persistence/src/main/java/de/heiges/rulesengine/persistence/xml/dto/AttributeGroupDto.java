package de.heiges.rulesengine.persistence.xml.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;

import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class AttributeGroupDto {

    @XmlAttribute(required = true)
    private String name;

    @XmlElement(name = "attribute")
    private List<AttributeDto> attributes = new ArrayList<>();

    public AttributeGroupDto() {}

    public AttributeGroupDto(String name) {
        this.name = name;
    }

    public String getName() { return name; }
    public List<AttributeDto> getAttributes() { return attributes; }
}
