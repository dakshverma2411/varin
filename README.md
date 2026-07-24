# Varin

A variable configuration and validation library. Define typed variables with rules (any value, one-of, range) and validate inputs against them.

## Structure

```
varin/
├── java/       # Java library — core types, rules, evaluator
└── ui/         # React component library — headless form fields
```

## Java Library

### Requirements

- Java 17+
- Maven

### Build

```bash
cd java
mvn clean install
```

### Core Concepts

**Variable** — a named, typed configuration parameter:

```java
Variable age = Variable.builder()
    .name("age")
    .description("User age")
    .required(true)
    .dataType(DataType.INT)
    .value(NonFixedVariableValue.builder()
        .rule(new RangeRule("18", true, "60", true))
        .defaultValue(Value.builder().value(List.of("25")).build())
        .build())
    .build();
```

**DataType** — `STRING` or `INT`. Both use the visitor pattern for type-safe dispatch.

**VariableValue** — either `FIXED` (constant, non-editable) or `NON_FIXED` (user-provided, governed by a rule).

**Rules** determine what values are acceptable for `NON_FIXED` variables:

| Rule | Description | Supported Types |
|------|-------------|-----------------|
| `AnyRule` | Any value is valid | STRING, INT |
| `OneOfVariableRule` | Value must match one of the given options | STRING, INT |
| `RangeRule` | Value must fall within min/max bounds (inclusive/exclusive configurable) | INT |

**Evaluator** — parses raw string input, validates against the variable's data type and rule, returns typed results:

```java
Evaluator evaluator = new EvaluatorImpl();
List<?> result = evaluator.evaluateAndGet(
    Value.builder().value(List.of("25")).build(),
    age
);
// result = [25] (List<Integer>)
```

### Testing

Tests are driven by YAML files in `java/src/test/resources/`. Each file defines a variable schema and a list of inputs with expected validity:

```yaml
variable:
  expectedCorrect: true
  schema:
    name: age
    dateType: INT
    required: true
    value:
      type: NON_FIXED
      rule:
        type: RANGE
        min: "18"
        minInclusive: true
        max: "60"
        maxInclusive: true

inputs:
  - value: ["25"]
    valid: true
  - value: ["17"]
    valid: false
```

```bash
cd java
mvn test
```

## React UI Library

A headless (unstyled) React component library that renders form fields from `Variable` JSON.

### Requirements

- Node.js 18+
- React 18 or 19

### Build

```bash
cd ui
npm install
npm run build
```

### Dev Server

```bash
cd ui
npm run dev
```

### Usage

```tsx
import { VarinField } from "@varin/ui";

const variable = {
  name: "environment",
  description: "Deployment Environment",
  required: true,
  dataType: "STRING",
  value: {
    type: "NON_FIXED",
    rule: {
      type: "ONE_OF",
      options: [
        { value: "dev", displayValue: "Development" },
        { value: "prod", displayValue: "Production" },
      ],
    },
    defaultValue: { value: ["dev"] },
  },
};

<VarinField
  variable={variable}
  onChange={({ value, validation }) => {
    console.log(value, validation.isValid, validation.error);
  }}
/>
```

### Component Mapping

| Variable Config | Component | Default UI |
|----------------|-----------|------------|
| `FIXED` | `FixedDisplay` | Read-only text |
| `ANY` rule | `AnyInput` | `<input>` with hint placeholder |
| `ONE_OF` rule | `OneOfSelect` | `<select>` dropdown |
| `RANGE` (span <= 100) | `RangeInput` | Slider |
| `RANGE` (span > 100) | `RangeInput` | Number input with +/- buttons |

### Customization

All components are headless. Customize via:

- **`className` / `style`** on every sub-element
- **`render*` props** for full markup control
- **`components` prop** on `<VarinField>` to replace any sub-component entirely
- **`rangeSliderThreshold`** prop to control slider vs number-input cutoff (default: 100)
- **`data-varin-*`** HTML attributes for CSS targeting

```tsx
<VarinField
  variable={variable}
  onChange={handleChange}
  rangeSliderThreshold={50}
  components={{ OneOfSelect: MyCustomDropdown }}
  anyInputProps={{ inputClassName: "my-input-class" }}
/>
```

## License

MIT
