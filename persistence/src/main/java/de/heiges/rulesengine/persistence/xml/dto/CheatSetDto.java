package de.heiges.rulesengine.persistence.xml.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "cheats")
@XmlAccessorType(XmlAccessType.FIELD)
public class CheatSetDto {

    @XmlElement(name = "cheat")
    private List<CheatDto> cheats = new ArrayList<>();

    public CheatSetDto() {}

    public List<CheatDto> getCheats() { return cheats; }
}
