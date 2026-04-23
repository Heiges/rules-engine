package de.heiges.rulesengine.persistence.xml.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "skills")
@XmlAccessorType(XmlAccessType.FIELD)
public class SkillSetDto {

    @XmlElement(name = "skill")
    private List<SkillDto> skills = new ArrayList<>();

    public SkillSetDto() {}

    public List<SkillDto> getSkills() { return skills; }
}
