package com.dakshverma.varin.rules;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor(force = true)
@ToString
@EqualsAndHashCode
public class Option {
    private final String value;
    private final String displayValue;
    private final String description;

    @JsonCreator
    public Option(
            @JsonProperty("value") final String value,
            @JsonProperty("displayValue") final String displayValue,
            @JsonProperty("description") final String description) {
        this.value = value;
        this.displayValue = displayValue;
        this.description = description;
    }
}
