package com.dakshverma.varin.value;

import com.dakshverma.varin.rules.VariableValueRule;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

@NoArgsConstructor(force = true)
public class NonFixedVariableValue extends VariableValue {

    @Nullable
    private final Value defaultValue;

    @Getter
    private final VariableValueRule rule;

    @JsonCreator
    @Builder
    public NonFixedVariableValue(
            @JsonProperty("defaultValue") final @Nullable Value defaultValue,
            @JsonProperty("rule") final VariableValueRule rule) {
        super(VariableValueType.NON_FIXED);
        this.defaultValue = defaultValue;
        this.rule = rule;
    }

    public Optional<Value> getDefaultValue() {
        return Optional.ofNullable(defaultValue);
    }
}
