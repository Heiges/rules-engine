package de.heiges.rulesengine.persistence.xml.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "skillDomains")
@XmlAccessorType(XmlAccessType.FIELD)
public class SkillDomainSetDto {

    @XmlElement(name = "skillDomain")
    private List<SkillDomainDto> skillDomains = new ArrayList<>();

    public SkillDomainSetDto() {}

    public List<SkillDomainDto> getSkillDomains() { return skillDomains; }
}
