package com.dakshverma.varin.rules;

import lombok.Builder;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class AnyRule extends VariableValueRule {
    private final String hint;

    @Builder
    public AnyRule(@Nullable final String hint) {
        super(VariableValueRuleType.ANY);
        this.hint = hint;
    }

    public Optional<String> getHint() {
        return Optional.ofNullable(hint);
    }
}
