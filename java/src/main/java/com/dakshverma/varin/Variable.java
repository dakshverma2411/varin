package com.dakshverma.varin;

import com.dakshverma.varin.condition.Conditions;
import com.dakshverma.varin.value.VariableValue;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Variable {
    private final String name;
    private final String description;
    private final boolean required;
    private final DataType dataType;
    private final VariableValue value;

    @Builder
    public Variable(final String name,
                    final String description,
                    final boolean required,
                    final DataType dataType,
                    final VariableValue value) {
        this.name = name;
        this.description = description;
        this.required = required;
        this.dataType = dataType;
        this.value = value;
        Conditions.check(this);
    }
}
