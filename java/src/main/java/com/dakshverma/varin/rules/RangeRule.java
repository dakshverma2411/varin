package com.dakshverma.varin.rules;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(force = true)
public class RangeRule extends VariableValueRule {

    private final String min;
    private final boolean minInclusive;
    private final String max;
    private final boolean maxInclusive;

    @JsonCreator
    public RangeRule(
            @JsonProperty("min") final String min,
            @JsonProperty("minInclusive") final boolean minInclusive,
            @JsonProperty("max") final String max,
            @JsonProperty("maxInclusive") final boolean maxInclusive) {
        super(VariableValueRuleType.RANGE);
        this.min = min;
        this.minInclusive = minInclusive;
        this.max = max;
        this.maxInclusive = maxInclusive;
    }
}
