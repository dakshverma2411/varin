package com.dakshverma.varin.rules;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(force = true)
public class OneOfVariableRule extends VariableValueRule {

    private final List<Option> options;

    @JsonCreator
    public OneOfVariableRule(@JsonProperty("options") final List<Option> options) {
        super(VariableValueRuleType.ONE_OF);
        this.options = options;
    }

}
