package com.dakshverma.varin.rules;

import lombok.Getter;

import java.util.List;

@Getter
public class OneOfVariableRule extends VariableValueRule {

    private final List<Option> options;

    public OneOfVariableRule(final List<Option> options) {
        super(VariableValueRuleType.ONE_OF);
        this.options = options;
    }

}
