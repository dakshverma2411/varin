import type { RangeInputProps, RangeRule, NonFixedVariableValue } from "../types";

const DEFAULT_SLIDER_THRESHOLD = 100;

export function RangeInput({
  variable,
  value,
  onChange,
  className,
  style,
  rangeSliderThreshold = DEFAULT_SLIDER_THRESHOLD,
  sliderClassName,
  sliderStyle,
  inputClassName,
  inputStyle,
  renderSlider,
  renderNumberInput,
}: RangeInputProps) {
  const nonFixed = variable.value as NonFixedVariableValue;
  const rule = nonFixed.rule as RangeRule;

  const min = Number(rule.min);
  const max = Number(rule.max);
  const numericValue = Number(value) || min;
  const span = max - min;
  const useSlider = span <= rangeSliderThreshold;

  const handleChange = (v: number) => onChange(String(v));

  const clamp = (v: number): number => {
    const effectiveMin = rule.minInclusive ? min : min + 1;
    const effectiveMax = rule.maxInclusive ? max : max - 1;
    return Math.max(effectiveMin, Math.min(effectiveMax, v));
  };

  const handleIncrement = () => handleChange(clamp(numericValue + 1));
  const handleDecrement = () => handleChange(clamp(numericValue - 1));

  if (useSlider) {
    if (renderSlider) {
      return (
        <div
          className={className}
          style={style}
          data-varin-type="range"
          data-varin-variant="slider"
          data-varin-name={variable.name}
        >
          {renderSlider({ min, max, step: 1, value: numericValue, onChange: handleChange })}
        </div>
      );
    }

    return (
      <div
        className={className}
        style={style}
        data-varin-type="range"
        data-varin-variant="slider"
        data-varin-name={variable.name}
      >
        <input
          type="range"
          className={sliderClassName}
          style={sliderStyle}
          min={min}
          max={max}
          step={1}
          value={numericValue}
          aria-label={variable.displayName ?? variable.name}
          onChange={(e) => handleChange(Number(e.target.value))}
        />
        <output>{numericValue}</output>
      </div>
    );
  }

  // Number input with increment/decrement buttons
  if (renderNumberInput) {
    return (
      <div
        className={className}
        style={style}
        data-varin-type="range"
        data-varin-variant="number"
        data-varin-name={variable.name}
      >
        {renderNumberInput({
          min,
          max,
          value: numericValue,
          onChange: handleChange,
          onIncrement: handleIncrement,
          onDecrement: handleDecrement,
        })}
      </div>
    );
  }

  return (
    <div
      className={className}
      style={style}
      data-varin-type="range"
      data-varin-variant="number"
      data-varin-name={variable.name}
    >
      <button
        type="button"
        aria-label="Decrement"
        onClick={handleDecrement}
      >
        −
      </button>
      <input
        type="number"
        className={inputClassName}
        style={inputStyle}
        min={min}
        max={max}
        value={numericValue}
        aria-label={variable.displayName ?? variable.name}
        onChange={(e) => handleChange(Number(e.target.value))}
      />
      <button
        type="button"
        aria-label="Increment"
        onClick={handleIncrement}
      >
        +
      </button>
    </div>
  );
}
