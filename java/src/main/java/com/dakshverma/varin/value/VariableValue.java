package com.dakshverma.varin.value;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(force = true)
@Getter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = FixedVariableValue.class, name = "FIXED"),
        @JsonSubTypes.Type(value = NonFixedVariableValue.class, name = "NON_FIXED")
})
public abstract class VariableValue {
    private final VariableValueType type;
}
