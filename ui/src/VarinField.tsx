import { useState, useCallback } from "react";
import type {
  VarinFieldProps,
  NonFixedVariableValue,
  VarinFieldChangeEvent,
} from "./types";
import { validate } from "./validation";
import { FixedDisplay } from "./components/FixedDisplay";
import { AnyInput } from "./components/AnyInput";
import { OneOfSelect } from "./components/OneOfSelect";
import { RangeInput } from "./components/RangeInput";

export function VarinField({
  variable,
  value: controlledValue,
  defaultValue,
  onChange,
  className,
  style,
  rangeSliderThreshold,
  components,
  fixedDisplayProps,
  anyInputProps,
  oneOfSelectProps,
  rangeInputProps,
}: VarinFieldProps) {
  const getInitialValue = (): string => {
    if (controlledValue != null) return controlledValue[0] ?? "";
    if (defaultValue != null) return defaultValue[0] ?? "";
    if (variable.value.type === "NON_FIXED") {
      const nf = variable.value as NonFixedVariableValue;
      return nf.defaultValue?.value[0] ?? "";
    }
    return "";
  };

  const [internalValue, setInternalValue] = useState<string>(getInitialValue);

  const currentValue = controlledValue != null ? (controlledValue[0] ?? "") : internalValue;

  const handleChange = useCallback(
    (newValue: string) => {
      if (controlledValue == null) {
        setInternalValue(newValue);
      }
      if (onChange) {
        const validation = validate(newValue, variable);
        const event: VarinFieldChangeEvent = {
          value: newValue === "" ? [] : [newValue],
          validation,
        };
        onChange(event);
      }
    },
    [controlledValue, onChange, variable]
  );

  // FIXED — read-only display
  if (variable.value.type === "FIXED") {
    const Fixed = components?.FixedDisplay ?? FixedDisplay;
    return (
      <Fixed
        variable={variable}
        className={className}
        style={style}
        {...fixedDisplayProps}
      />
    );
  }

  // NON_FIXED — dispatch by rule type
  const nonFixed = variable.value as NonFixedVariableValue;

  switch (nonFixed.rule.type) {
    case "ANY": {
      const Any = components?.AnyInput ?? AnyInput;
      return (
        <Any
          variable={variable}
          value={currentValue}
          onChange={handleChange}
          className={className}
          style={style}
          {...anyInputProps}
        />
      );
    }
    case "ONE_OF": {
      const OneOf = components?.OneOfSelect ?? OneOfSelect;
      return (
        <OneOf
          variable={variable}
          value={currentValue}
          onChange={handleChange}
          className={className}
          style={style}
          {...oneOfSelectProps}
        />
      );
    }
    case "RANGE": {
      const Range = components?.RangeInput ?? RangeInput;
      return (
        <Range
          variable={variable}
          value={currentValue}
          onChange={handleChange}
          className={className}
          style={style}
          rangeSliderThreshold={rangeSliderThreshold}
          {...rangeInputProps}
        />
      );
    }
  }
}
