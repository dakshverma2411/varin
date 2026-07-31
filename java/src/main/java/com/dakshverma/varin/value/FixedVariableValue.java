package com.dakshverma.varin.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(force = true)
public class FixedVariableValue extends VariableValue {
    private final Value value;

    @JsonCreator
    @Builder
    public FixedVariableValue(@JsonProperty("value") final Value value) {
        super(VariableValueType.FIXED);
        this.value = value;
    }
}
