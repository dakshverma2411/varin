import type { AnyInputProps, AnyRule, NonFixedVariableValue } from "../types";

export function AnyInput({
  variable,
  value,
  onChange,
  className,
  style,
  inputClassName,
  inputStyle,
  renderInput,
}: AnyInputProps) {
  const nonFixed = variable.value as NonFixedVariableValue;
  const rule = nonFixed.rule as AnyRule;
  const inputType = variable.dataType === "INT" ? "number" : "text";

  const inputProps: React.InputHTMLAttributes<HTMLInputElement> = {
    type: inputType,
    value,
    placeholder: rule.hint ?? undefined,
    required: variable.required,
    className: inputClassName,
    style: inputStyle,
    onChange: (e) => onChange(e.target.value),
    "aria-label": variable.displayName ?? variable.name,
  };

  return (
    <div
      className={className}
      style={style}
      data-varin-type="any"
      data-varin-name={variable.name}
    >
      {renderInput ? renderInput(inputProps) : <input {...inputProps} />}
    </div>
  );
}
