package com.dakshverma.varin.rules;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public abstract class VariableValueRule {
    private final VariableValueRuleType type;
}