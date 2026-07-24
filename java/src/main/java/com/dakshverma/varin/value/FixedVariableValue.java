package com.dakshverma.varin.value;

import lombok.Builder;
import lombok.Getter;

@Getter
public class FixedVariableValue extends VariableValue {
    private final Value value;

    @Builder
    public FixedVariableValue(final Value value) {
        super(VariableValueType.FIXED);
        this.value = value;
    }
}
