package com.dakshverma.varin.evaluator;


import com.dakshverma.varin.DataType;
import com.dakshverma.varin.Variable;
import com.dakshverma.varin.rules.OneOfVariableRule;
import com.dakshverma.varin.rules.Option;
import com.dakshverma.varin.rules.RangeRule;
import com.dakshverma.varin.rules.VariableValueRule;
import com.dakshverma.varin.rules.VariableValueRuleType;
import com.dakshverma.varin.value.NonFixedVariableValue;
import com.dakshverma.varin.value.Value;
import com.dakshverma.varin.value.VariableValueType;
import com.google.common.base.Preconditions;

import java.util.List;

public class EvaluatorImpl implements Evaluator {

    @Override
    public List<?> evaluateAndGet(final Value input, final Variable forVariable) {
        if (input.getValue() == null || input.getValue().isEmpty()) {
            Preconditions.checkArgument(!forVariable.isRequired(),
                    "value is required for variable '%s'", forVariable.getName());
            return List.of();
        }

        final String rawValue = input.getValue().get(0);
        final Object parsedValue = parse(rawValue, forVariable.getDataType());

        if (forVariable.getValue().getType() == VariableValueType.NON_FIXED) {
            final VariableValueRule rule = ((NonFixedVariableValue) forVariable.getValue()).getRule();
            validateAgainstRule(rawValue, parsedValue, rule, forVariable);
        }

        return List.of(parsedValue);
    }

    private Object parse(final String rawValue, final DataType dataType) {
        return dataType.accept(new DataType.Visitor<>() {
            @Override
            public Object visitString() {
                return rawValue;
            }

            @Override
            public Object visitInt() {
                try {
                    return Integer.parseInt(rawValue);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException(
                            String.format("'%s' is not a valid integer", rawValue), e);
                }
            }
        });
    }

    private void validateAgainstRule(final String rawValue,
                                     final Object parsedValue,
                                     final VariableValueRule rule,
                                     final Variable forVariable) {
        rule.getType().accept(new VariableValueRuleType.Visitor<Void>() {
            @Override
            public Void visitAny() {
                return null;
            }

            @Override
            public Void visitOneOf() {
                final OneOfVariableRule oneOfRule = (OneOfVariableRule) rule;
                final boolean matches = oneOfRule.getOptions().stream()
                        .map(Option::getValue)
                        .anyMatch(rawValue::equals);
                Preconditions.checkArgument(matches,
                        "value '%s' is not one of the allowed options for variable '%s'",
                        rawValue, forVariable.getName());
                return null;
            }

            @Override
            public Void visitRange() {
                final RangeRule rangeRule = (RangeRule) rule;
                final int value = (Integer) parsedValue;
                final int min = Integer.parseInt(rangeRule.getMin());
                final int max = Integer.parseInt(rangeRule.getMax());

                final boolean aboveMin = rangeRule.isMinInclusive() ? value >= min : value > min;
                final boolean belowMax = rangeRule.isMaxInclusive() ? value <= max : value < max;

                Preconditions.checkArgument(aboveMin && belowMax,
                        "value %d is out of range %s%d, %d%s for variable '%s'",
                        value,
                        rangeRule.isMinInclusive() ? "[" : "(",
                        min, max,
                        rangeRule.isMaxInclusive() ? "]" : ")",
                        forVariable.getName());
                return null;
            }
        });
    }
}
