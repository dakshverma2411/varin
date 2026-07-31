package com.dakshverma.varin;

import com.dakshverma.varin.condition.Conditions;
import com.dakshverma.varin.value.VariableValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Nullable;

@Getter
@NoArgsConstructor(force = true)
public class Variable {
    private final String name;
    @Nullable
    private final String displayName;
    @Nullable
    private final String description;
    private final boolean required;
    private final DataType dataType;
    private final VariableValue value;

    @JsonCreator
    @Builder
    public Variable(
            @JsonProperty("name") final String name,
            @JsonProperty("displayName") @Nullable final String displayName,
            @JsonProperty("description") @Nullable final String description,
            @JsonProperty("required") final boolean required,
            @JsonProperty("dataType") final DataType dataType,
            @JsonProperty("value") final VariableValue value) {
        this.name = name;
        this.displayName = displayName;
        this.description = description;
        this.required = required;
        this.dataType = dataType;
        this.value = value;
        Conditions.check(this);
    }
}
