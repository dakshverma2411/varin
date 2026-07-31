package com.dakshverma.varin.rules;

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
        @JsonSubTypes.Type(value = AnyRule.class, name = "ANY"),
        @JsonSubTypes.Type(value = OneOfVariableRule.class, name = "ONE_OF"),
        @JsonSubTypes.Type(value = RangeRule.class, name = "RANGE")
})
public abstract class VariableValueRule {
    private final VariableValueRuleType type;
}
