package com.dakshverma.varin.value;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public abstract class VariableValue {
    private final VariableValueType type;
}
