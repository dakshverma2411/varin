package com.dakshverma.varin.condition;

import com.dakshverma.varin.DataType;
import com.dakshverma.varin.Variable;
import com.dakshverma.varin.evaluator.Evaluator;
import com.dakshverma.varin.evaluator.EvaluatorImpl;
import com.dakshverma.varin.rules.OneOfVariableRule;
import com.dakshverma.varin.rules.Option;
import com.dakshverma.varin.rules.RangeRule;
import com.dakshverma.varin.rules.VariableValueRule;
import com.dakshverma.varin.rules.VariableValueRuleType;
import com.dakshverma.varin.value.FixedVariableValue;
import com.dakshverma.varin.value.NonFixedVariableValue;
import com.dakshverma.varin.value.Value;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.dakshverma.varin.rules.VariableValueRuleType.ANY;
import static com.dakshverma.varin.rules.VariableValueRuleType.ONE_OF;
import static com.dakshverma.varin.rules.VariableValueRuleType.RANGE;

public class Conditions {

    private static final Evaluator EVALUATOR = new EvaluatorImpl();

    /**
     * checks if variable is configured correctly
     */
    public static void check(final Variable variable) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(variable.getName()), "name of variable should not be null");
        Preconditions.checkArgument(variable.getDataType() != null, "dataType should not be null");
        Preconditions.checkArgument(variable.getValue() != null, "value should not be null");
        checkValue(variable);
    }

    private static void checkValue(final Variable variable) {
        Preconditions.checkArgument(variable.getValue().getType() != null, "variable value should not be null");
        Void ignore = switch (variable.getValue().getType()) {
            case FIXED -> {
                checkFixedValue(variable);
                yield null;
            }
            case NON_FIXED -> {
                checkNonFixedValue(variable);
                yield null;
            }
        };
    }

    private static void checkFixedValue(final Variable variable) {
        FixedVariableValue fixedVariableValue = (FixedVariableValue) variable.getValue();
        EVALUATOR.evaluateAndGet(fixedVariableValue.getValue(), variable);
    }

    private static void checkNonFixedValue(final Variable variable) {
        NonFixedVariableValue nonFixedVariableValue = (NonFixedVariableValue) variable.getValue();
        VariableValueRule rule = nonFixedVariableValue.getRule();
        Preconditions.checkArgument(rule != null, "rule should not be null");
        checkRule(variable, rule);
        Optional<Value> defaultValue = nonFixedVariableValue.getDefaultValue();
        defaultValue.ifPresent(value -> EVALUATOR.evaluateAndGet(value, variable));
    }

    private static void checkRule(final Variable variable, final VariableValueRule rule) {
        rule.getType().accept(new VariableValueRuleType.Visitor<Void>() {
            @Override
            public Void visitAny() {
                Preconditions.checkArgument(ANY.getSupportedDataTypes().contains(variable.getDataType()));
                // do nothing
                return null;
            }

            @Override
            public Void visitOneOf() {
                Preconditions.checkArgument(ONE_OF.getSupportedDataTypes().contains(variable.getDataType()));
                OneOfVariableRule oneOfVariableRule = (OneOfVariableRule) rule;
                Preconditions.checkArgument(oneOfVariableRule.getOptions() != null && !oneOfVariableRule.getOptions().isEmpty(), "options should not be null or empty");
                variable.getDataType().accept(new DataType.Visitor<Void>() {
                    @Override
                    public Void visitString() {
                        oneOfVariableRule.getOptions().forEach(option ->
                            Preconditions.checkArgument(!Strings.isNullOrEmpty(option.getValue()), "option value should not be null or empty")
                        );
                        return null;
                    }

                    @Override
                    public Void visitInt() {
                        oneOfVariableRule.getOptions().forEach(option -> {
                            try {
                                Integer.parseInt(option.getValue());
                            } catch (Exception e) {
                                throw new IllegalArgumentException("option value should be parsable to a valid integer");
                            }
                        });
                        return null;
                    }
                });
                List<String> values = oneOfVariableRule.getOptions().stream()
                        .map(Option::getValue)
                        .toList();
                Preconditions.checkArgument(values.size() == Set.of(values.toArray()).size(), "option values should not contain duplicates");
                return null;
            }

            @Override
            public Void visitRange() {
                Preconditions.checkArgument(RANGE.getSupportedDataTypes().contains(variable.getDataType()), "RANGE type not supported with datatype INT");
                RangeRule rangeRule = (RangeRule) rule;
                variable.getDataType().accept(
                        new DataType.Visitor<Void>() {
                            @Override
                            public Void visitString() {
                                throw new IllegalArgumentException("STRING not supported with RANGE rule");
                            }

                            @Override
                            public Void visitInt() {
                                int min;
                                int max;
                                try {
                                    min = Integer.parseInt(rangeRule.getMin());
                                    max = Integer.parseInt(rangeRule.getMax());
                                } catch (Exception e) {
                                    throw new IllegalArgumentException("min and max should be parsable to valid integer");
                                }
                                Preconditions.checkArgument(min < max, "min should be less than max");
                                return null;
                            }
                        }
                );
                return null;
            }
        });
    }


}
