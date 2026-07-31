package com.dakshverma.varin.rules;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

@NoArgsConstructor(force = true)
public class AnyRule extends VariableValueRule {
    private final String hint;

    @JsonCreator
    @Builder
    public AnyRule(@JsonProperty("hint") @Nullable final String hint) {
        super(VariableValueRuleType.ANY);
        this.hint = hint;
    }

    public Optional<String> getHint() {
        return Optional.ofNullable(hint);
    }
}
