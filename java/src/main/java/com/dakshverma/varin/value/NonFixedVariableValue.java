package com.dakshverma.varin.value;

import com.dakshverma.varin.rules.VariableValueRule;
import lombok.Builder;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class NonFixedVariableValue extends VariableValue {

    /**
     * nullable - default value
     */
    @Nullable
    private final Value defaultValue;

    @Getter
    private final VariableValueRule rule;

    @Builder
    public NonFixedVariableValue(
            final @Nullable Value defaultValue,
            final VariableValueRule rule) {
        super(VariableValueType.NON_FIXED);
        this.defaultValue = defaultValue;
        this.rule = rule;
    }

    public Optional<Value> getDefaultValue() {
        return Optional.ofNullable(defaultValue);
    }
}
