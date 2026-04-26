package de.heiges.rulesengine.persistence.xml.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "attributeSet")
@XmlAccessorType(XmlAccessType.FIELD)
public class AttributeSetDto {

    @XmlElement(name = "group")
    private List<AttributeGroupDto> groups = new ArrayList<>();

    public AttributeSetDto() {}

    public List<AttributeGroupDto> getGroups() { return groups; }
}
