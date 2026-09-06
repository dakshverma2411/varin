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

### Install from GitHub Packages

Published as `io.github.dakshverma2411:varin` on GitHub Packages (releases are immutable; `*-SNAPSHOT` versions track `develop` and are overwritten).

> GitHub Packages requires authentication even for public packages — use a PAT with the `read:packages` scope.

```xml
<!-- pom.xml -->
<dependency>
    <groupId>io.github.dakshverma2411</groupId>
    <artifactId>varin</artifactId>
    <version>1.0.0</version>
</dependency>
```

```xml
<!-- ~/.m2/settings.xml -->
<settings>
  <servers>
    <server>
      <id>github</id>
      <username>YOUR_GITHUB_USERNAME</username>
      <password>YOUR_PAT</password>
    </server>
  </servers>
</settings>
```

```xml
<!-- pom.xml -->
<repositories>
  <repository>
    <id>github</id>
    <url>https://maven.pkg.github.com/dakshverma2411/varin</url>
    <snapshots><enabled>true</enabled></snapshots>
  </repository>
</repositories>
```

Gradle (Kotlin DSL):

```kotlin
repositories {
    maven {
        url = uri("https://maven.pkg.github.com/dakshverma2411/varin")
        credentials {
            username = providers.gradleProperty("gpr.user").get()
            password = providers.gradleProperty("gpr.key").get()
        }
    }
}
dependencies {
    implementation("io.github.dakshverma2411:varin:1.0.0")
}
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

### Install from GitHub Packages

Published as `@dakshverma2411/ui` on GitHub Packages. Releases are on the `latest` tag; `develop` snapshots on the `snapshot` tag; per-PR builds on `pr-<N>` tags.

> GitHub Packages requires authentication even for public packages — use a PAT with the `read:packages` scope.

```ini
# .npmrc (project root)
@dakshverma2411:registry=https://npm.pkg.github.com
//npm.pkg.github.com/:_authToken=YOUR_PAT
```

```bash
npm install @dakshverma2411/ui           # latest release
npm install @dakshverma2411/ui@snapshot  # latest develop snapshot
```

### Usage

```tsx
import { VarinField } from "@dakshverma2411/ui";
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

## Development

### Branching

- `develop` is the default and only long-lived branch — all work lands here via PR.
- Merging requires the `java-build` and `ui-build` checks to pass; only the repository owner can merge.
- Direct pushes and force pushes to `develop` are blocked.

### CI/CD

| Workflow | Trigger | What it does |
|---|---|---|
| `CI` | PR → `develop` | Builds and tests both modules; publishes snapshots: Maven `-SNAPSHOT` (overwritable) and npm `0.0.0-pr<N>.<sha>` (dist-tag `pr<N>`) |
| `Snapshot` | push → `develop` | Publishes Maven `-SNAPSHOT` (overwritten each push) and npm `<version>-snapshot.<sha>` (dist-tag `snapshot`) |
| `Release` | manual (Actions → Release → Run workflow) | Publishes immutable releases from `develop`, tags `vX.Y.Z`, creates a GitHub Release, bumps `develop` to the next patch version via an auto-merged PR |

### Cutting a release

1. Go to **Actions → Release → Run workflow** on `develop`.
2. Enter the version (`X.Y.Z`, e.g. `1.0.0`) and run.
3. The workflow validates the input, publishes `io.github.dakshverma2411:varin:X.Y.Z` (with sources/javadoc) and `@dakshverma2411/ui@X.Y.Z`, tags `vX.Y.Z`, creates a GitHub Release, and bumps `develop` to `X.Y.(Z+1)`.

Release versions are immutable — re-running with an existing version fails by design (the tag exists and GitHub Packages rejects duplicates). Snapshots may be overwritten.

## License

MIT
