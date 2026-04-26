package de.heiges.rulesengine.persistence.xml.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class ValueRangeDto {

    @XmlAttribute(required = true)
    private int min;

    @XmlAttribute(required = true)
    private int average;

    @XmlAttribute(required = true)
    private int max;

    public ValueRangeDto() {}

    public ValueRangeDto(int min, int average, int max) {
        this.min = min;
        this.average = average;
        this.max = max;
    }

    public int getMin() { return min; }
    public int getAverage() { return average; }
    public int getMax() { return max; }
}
