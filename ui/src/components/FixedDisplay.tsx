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
        renderLabel(variable.displayName ?? variable.name, variable.description)
      ) : (
        <span className={labelClassName} style={{ ...labelStyle, display: "inline-flex", alignItems: "center", gap: 4 }}>
          {variable.displayName ?? variable.name}
          {variable.description && (
            <span
              title={variable.description}
              style={{ cursor: "help", fontSize: "0.85em", opacity: 0.6 }}
              role="img"
              aria-label={variable.description}
            >
              ⓘ
            </span>
          )}
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
