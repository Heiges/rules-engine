package de.heiges.rulesengine.persistence.xml.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class CheatDto {

    @XmlAttribute(required = true)
    private String name;

    @XmlAttribute
    private String description;

    public CheatDto() {}

    public CheatDto(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() { return name; }
    public String getDescription() { return description != null ? description : ""; }
}
