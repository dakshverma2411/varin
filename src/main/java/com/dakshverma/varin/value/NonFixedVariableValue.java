package com.dakshverma.varin.value;

import com.dakshverma.varin.rules.VariableValueRules;
import lombok.Getter;

import java.util.Optional;

public class NonFixedVariableValue extends VariableValue {

    /**
     * nullable - default value
     */
    private final UnitValue defaultValue;

    @Getter
    private final VariableValueRules rules;

    public NonFixedVariableValue(
            final UnitValue defaultValue,
            final VariableValueRules rules) {
        super(VariableValueType.NON_FIXED);
        this.defaultValue = defaultValue;
        this.rules = rules;
    }

    public Optional<UnitValue> getDefaultValue() {
        return Optional.ofNullable(defaultValue);
    }
}
