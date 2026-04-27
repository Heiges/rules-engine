package de.heiges.rulesengine.persistence.xml.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ruleset")
@XmlAccessorType(XmlAccessType.FIELD)
public class RulesetDto {

    @XmlElement(name = "wertebereich")
    private ValueRangeDto valueRange;

    @XmlElement(name = "attributeSet")
    private AttributeSetDto attributeSet = new AttributeSetDto();

    @XmlElement(name = "skills")
    private SkillSetDto skills = new SkillSetDto();

    @XmlElement(name = "skillDomains")
    private SkillDomainSetDto skillDomains = new SkillDomainSetDto();

    public RulesetDto() {}

    public ValueRangeDto getValueRange() { return valueRange; }
    public void setValueRange(ValueRangeDto valueRange) { this.valueRange = valueRange; }
    public AttributeSetDto getAttributeSet() { return attributeSet; }
    public SkillSetDto getSkills() { return skills; }
    public SkillDomainSetDto getSkillDomains() { return skillDomains; }
}
