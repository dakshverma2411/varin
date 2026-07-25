package com.dakshverma.varin;

import com.dakshverma.varin.condition.Conditions;
import com.dakshverma.varin.value.VariableValue;
import lombok.Builder;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

@Getter
public class Variable {
    private final String name;
    @Nullable
    private final String displayName;
    @Nullable
    private final String description;
    private final boolean required;
    private final DataType dataType;
    private final VariableValue value;

    @Builder
    public Variable(final String name,
                    @Nullable final String displayName,
                    @Nullable final String description,
                    final boolean required,
                    final DataType dataType,
                    final VariableValue value) {
        this.name = name;
        this.displayName = displayName;
        this.description = description;
        this.required = required;
        this.dataType = dataType;
        this.value = value;
        Conditions.check(this);
    }
}
