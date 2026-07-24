package com.dakshverma.varin.value;

import lombok.Builder;
import lombok.Getter;

@Getter
public class FixedVariableValue extends VariableValue {
    private final UnitValue value;

    @Builder
    public FixedVariableValue(final UnitValue value) {
        super(VariableValueType.FIXED);
        this.value = value;
    }
}
