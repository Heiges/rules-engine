package de.heiges.rulesengine.persistence.xml.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class SkillDto {

    @XmlAttribute(required = true)
    private String name;

    @XmlAttribute(required = true)
    private String linkedAttributeName;

    @XmlAttribute(required = true)
    private int level;

    public SkillDto() {}

    public SkillDto(String name, String linkedAttributeName, int level) {
        this.name = name;
        this.linkedAttributeName = linkedAttributeName;
        this.level = level;
    }

    public String getName() { return name; }
    public String getLinkedAttributeName() { return linkedAttributeName; }
    public int getLevel() { return level; }
}
