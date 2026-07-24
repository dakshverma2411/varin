package com.dakshverma.varin;

import com.dakshverma.varin.evaluator.Evaluator;
import com.dakshverma.varin.evaluator.EvaluatorImpl;
import com.dakshverma.varin.rules.*;
import com.dakshverma.varin.value.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class Tests {

    private static final ObjectMapper YAML_MAPPER = new ObjectMapper(new YAMLFactory());
    private static final Evaluator EVALUATOR = new EvaluatorImpl();

    private static final List<String> TEST_FILES = List.of(
            "range_non_fixed_required.yml",
            "one_of_non_fixed_required.yml",
            "any_non_fixed_optional.yml",
            "fixed_int_required.yml",
            "range_exclusive_bounds.yml",
            "one_of_int_non_fixed.yml"
    );

    record TestCase(String file, Variable variable, boolean expectedCorrect,
                    List<InputCase> inputs) {}

    record InputCase(Value value, boolean valid) {}

    static Stream<TestCase> testCases() throws IOException {
        List<TestCase> cases = new ArrayList<>();
        for (String file : TEST_FILES) {
            try (InputStream is = Tests.class.getClassLoader().getResourceAsStream(file)) {
                JsonNode root = YAML_MAPPER.readTree(is);
                cases.add(parseTestCase(file, root));
            }
        }
        return cases.stream();
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("testCases")
    void testVariableCreationAndInputs(TestCase testCase) {
        // Test variable creation
        if (testCase.expectedCorrect()) {
            assertNotNull(testCase.variable(),
                    "Variable should have been created successfully for " + testCase.file());
        }
        // If variable creation was expected to fail, the parsing step would have
        // caught it (see parseTestCase). If we reach here with a null variable,
        // inputs can't be tested.
        if (testCase.variable() == null) {
            return;
        }

        // Test each input against the evaluator
        for (int i = 0; i < testCase.inputs().size(); i++) {
            InputCase input = testCase.inputs().get(i);
            final int idx = i;
            if (input.valid()) {
                assertDoesNotThrow(
                        () -> EVALUATOR.evaluateAndGet(input.value(), testCase.variable()),
                        () -> String.format("[%s] input[%d] %s should be valid",
                                testCase.file(), idx, input.value().getValue()));
            } else {
                assertThrows(IllegalArgumentException.class,
                        () -> EVALUATOR.evaluateAndGet(input.value(), testCase.variable()),
                        () -> String.format("[%s] input[%d] %s should be invalid",
                                testCase.file(), idx, input.value().getValue()));
            }
        }
    }

    // --- YAML Parsing ---

    private static TestCase parseTestCase(String file, JsonNode root) {
        JsonNode variableNode = root.get("variable");
        boolean expectedCorrect = variableNode.get("expectedCorrect").asBoolean();
        JsonNode schema = variableNode.get("schema");

        Variable variable = null;
        try {
            variable = buildVariable(schema);
            if (!expectedCorrect) {
                fail("Expected variable creation to fail for " + file + " but it succeeded");
            }
        } catch (Exception e) {
            if (expectedCorrect) {
                fail("Expected variable creation to succeed for " + file + " but got: " + e.getMessage());
            }
        }

        List<InputCase> inputs = new ArrayList<>();
        JsonNode inputsNode = root.get("inputs");
        if (inputsNode != null) {
            for (JsonNode inputNode : inputsNode) {
                List<String> values = new ArrayList<>();
                JsonNode valueArray = inputNode.get("value");
                if (valueArray != null && valueArray.isArray()) {
                    for (JsonNode v : valueArray) {
                        values.add(v.asText());
                    }
                }
                boolean valid = inputNode.get("valid").asBoolean();
                inputs.add(new InputCase(Value.builder().value(values).build(), valid));
            }
        }

        return new TestCase(file, variable, expectedCorrect, inputs);
    }

    private static Variable buildVariable(JsonNode schema) {
        String name = schema.get("name").asText();
        String description = schema.has("description") ? schema.get("description").asText() : null;
        boolean required = schema.get("required").asBoolean();
        DataType dataType = DataType.valueOf(schema.get("dateType").asText());

        VariableValue variableValue = buildVariableValue(schema.get("value"));

        return Variable.builder()
                .name(name)
                .description(description)
                .required(required)
                .dataType(dataType)
                .value(variableValue)
                .build();
    }

    private static VariableValue buildVariableValue(JsonNode valueNode) {
        String type = valueNode.get("type").asText();
        if ("FIXED".equals(type)) {
            List<String> values = new ArrayList<>();
            for (JsonNode v : valueNode.get("value")) {
                values.add(v.asText());
            }
            return FixedVariableValue.builder()
                    .value(Value.builder().value(values).build())
                    .build();
        } else {
            // NON_FIXED
            VariableValueRule rule = buildRule(valueNode.get("rule"));
            Value defaultValue = null;
            if (valueNode.has("defaultValue")) {
                List<String> defValues = new ArrayList<>();
                for (JsonNode v : valueNode.get("defaultValue").get("value")) {
                    defValues.add(v.asText());
                }
                defaultValue = Value.builder().value(defValues).build();
            }
            return NonFixedVariableValue.builder()
                    .rule(rule)
                    .defaultValue(defaultValue)
                    .build();
        }
    }

    private static VariableValueRule buildRule(JsonNode ruleNode) {
        String type = ruleNode.get("type").asText();
        return switch (type) {
            case "ANY" -> AnyRule.builder().hint(
                    ruleNode.has("hint") ? ruleNode.get("hint").asText() : null
            ).build();
            case "ONE_OF" -> {
                List<Option> options = new ArrayList<>();
                for (JsonNode opt : ruleNode.get("options")) {
                    options.add(new Option(
                            opt.get("value").asText(),
                            opt.has("displayValue") ? opt.get("displayValue").asText() : null,
                            opt.has("description") ? opt.get("description").asText() : null
                    ));
                }
                yield new OneOfVariableRule(options);
            }
            case "RANGE" -> new RangeRule(
                    ruleNode.get("min").asText(),
                    ruleNode.get("minInclusive").asBoolean(),
                    ruleNode.get("max").asText(),
                    ruleNode.get("maxInclusive").asBoolean()
            );
            default -> throw new IllegalArgumentException("Unknown rule type: " + type);
        };
    }
}
