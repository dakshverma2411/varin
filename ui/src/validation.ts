import type { DataType, Rule, NonFixedVariableValue, Variable, ValidationResult } from "./types";

export function validate(rawValue: string, variable: Variable): ValidationResult {
  if (rawValue === "" || rawValue == null) {
    if (variable.required) {
      return { isValid: false, error: `${variable.name} is required` };
    }
    return { isValid: true };
  }

  const parseResult = parseValue(rawValue, variable.dataType);
  if (!parseResult.isValid) {
    return parseResult;
  }

  if (variable.value.type === "NON_FIXED") {
    const nonFixed = variable.value as NonFixedVariableValue;
    return validateRule(rawValue, parseResult.parsed!, nonFixed.rule);
  }

  return { isValid: true };
}

interface ParseResult {
  isValid: boolean;
  error?: string;
  parsed?: number | string;
}

function parseValue(rawValue: string, dataType: DataType): ParseResult {
  switch (dataType) {
    case "STRING":
      return { isValid: true, parsed: rawValue };
    case "INT": {
      const parsed = Number(rawValue);
      if (!Number.isInteger(parsed) || rawValue.trim() === "") {
        return { isValid: false, error: `'${rawValue}' is not a valid integer` };
      }
      return { isValid: true, parsed };
    }
  }
}

function validateRule(
  rawValue: string,
  parsedValue: number | string,
  rule: Rule
): ValidationResult {
  switch (rule.type) {
    case "ANY":
      return { isValid: true };

    case "ONE_OF": {
      const match = rule.options.some((opt) => opt.value === rawValue);
      if (!match) {
        return {
          isValid: false,
          error: `'${rawValue}' is not one of the allowed options`,
        };
      }
      return { isValid: true };
    }

    case "RANGE": {
      const value = parsedValue as number;
      const min = Number(rule.min);
      const max = Number(rule.max);
      const aboveMin = rule.minInclusive ? value >= min : value > min;
      const belowMax = rule.maxInclusive ? value <= max : value < max;

      if (!aboveMin || !belowMax) {
        const minBracket = rule.minInclusive ? "[" : "(";
        const maxBracket = rule.maxInclusive ? "]" : ")";
        return {
          isValid: false,
          error: `${value} is out of range ${minBracket}${min}, ${max}${maxBracket}`,
        };
      }
      return { isValid: true };
    }
  }
}
