import type { FixedDisplayProps, FixedVariableValue } from "../types";

export function FixedDisplay({
  variable,
  className,
  style,
  labelClassName,
  labelStyle,
  valueClassName,
  valueStyle,
  renderLabel,
  renderValue,
}: FixedDisplayProps) {
  const fixedValue = variable.value as FixedVariableValue;
  const values = fixedValue.value;

  return (
    <div
      className={className}
      style={style}
      data-varin-type="fixed"
      data-varin-name={variable.name}
    >
      {renderLabel ? (
        renderLabel(variable.name, variable.description)
      ) : (
        <span className={labelClassName} style={labelStyle}>
          {variable.description ?? variable.name}
        </span>
      )}
      {renderValue ? (
        renderValue(values)
      ) : (
        <span className={valueClassName} style={valueStyle}>
          {values.join(", ")}
        </span>
      )}
    </div>
  );
}
