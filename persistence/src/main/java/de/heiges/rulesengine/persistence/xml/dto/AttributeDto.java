package de.heiges.rulesengine.persistence.xml.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class AttributeDto {

    @XmlAttribute(required = true)
    private String name;

    @XmlAttribute
    private String description;

    @XmlAttribute
    private int value;

    public AttributeDto() {}

    public AttributeDto(String name, String description, int value) {
        this.name = name;
        this.description = description;
        this.value = value;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getValue() { return value; }
}
