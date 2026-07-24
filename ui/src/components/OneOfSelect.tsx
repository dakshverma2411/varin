import type { OneOfSelectProps, OneOfRule, NonFixedVariableValue } from "../types";

export function OneOfSelect({
  variable,
  value,
  onChange,
  className,
  style,
  selectClassName,
  selectStyle,
  optionClassName,
  optionStyle,
  renderSelect,
}: OneOfSelectProps) {
  const nonFixed = variable.value as NonFixedVariableValue;
  const rule = nonFixed.rule as OneOfRule;

  if (renderSelect) {
    return (
      <div
        className={className}
        style={style}
        data-varin-type="one-of"
        data-varin-name={variable.name}
      >
        {renderSelect(rule.options, value, onChange)}
      </div>
    );
  }

  return (
    <div
      className={className}
      style={style}
      data-varin-type="one-of"
      data-varin-name={variable.name}
    >
      <select
        className={selectClassName}
        style={selectStyle}
        value={value}
        required={variable.required}
        aria-label={variable.description ?? variable.name}
        onChange={(e) => onChange(e.target.value)}
      >
        {!variable.required && <option value="">— Select —</option>}
        {rule.options.map((opt) => (
          <option
            key={opt.value}
            value={opt.value}
            className={optionClassName}
            style={optionStyle}
            title={opt.description}
          >
            {opt.displayValue ?? opt.value}
          </option>
        ))}
      </select>
    </div>
  );
}
