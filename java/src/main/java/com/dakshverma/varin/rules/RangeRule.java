package com.dakshverma.varin.rules;

import lombok.Getter;

@Getter
public class RangeRule extends VariableValueRule {

    private final String min;
    private final boolean minInclusive;
    private final String max;
    private final boolean maxInclusive;

    public RangeRule(
            final String min,
            final boolean minInclusive,
            final String max,
            final boolean maxInclusive) {
        super(VariableValueRuleType.RANGE);
        this.min = min;
        this.minInclusive = minInclusive;
        this.max = max;
        this.maxInclusive = maxInclusive;
    }
}
