// --- Data Types ---

export type DataType = "STRING" | "INT";

// --- Rules ---

export interface Option {
  value: string;
  displayValue?: string;
  description?: string;
}

export interface AnyRule {
  type: "ANY";
  hint?: string;
}

export interface OneOfRule {
  type: "ONE_OF";
  options: Option[];
}

export interface RangeRule {
  type: "RANGE";
  min: string;
  minInclusive: boolean;
  max: string;
  maxInclusive: boolean;
}

export type Rule = AnyRule | OneOfRule | RangeRule;

// --- Variable Value ---

export interface FixedVariableValue {
  type: "FIXED";
  value: string[];
}

export interface NonFixedVariableValue {
  type: "NON_FIXED";
  rule: Rule;
  defaultValue?: { value: string[] };
}

export type VariableValue = FixedVariableValue | NonFixedVariableValue;

// --- Variable ---

export interface Variable {
  name: string;
  description?: string;
  required: boolean;
  dataType: DataType;
  value: VariableValue;
}

// --- Component Props ---

export interface ValidationResult {
  isValid: boolean;
  error?: string;
}

export interface VarinFieldChangeEvent {
  value: string[];
  validation: ValidationResult;
}

export interface BaseComponentProps {
  className?: string;
  style?: React.CSSProperties;
}

export interface FixedDisplayProps extends BaseComponentProps {
  variable: Variable;
  labelClassName?: string;
  labelStyle?: React.CSSProperties;
  valueClassName?: string;
  valueStyle?: React.CSSProperties;
  renderLabel?: (name: string, description?: string) => React.ReactNode;
  renderValue?: (value: string[]) => React.ReactNode;
}

export interface AnyInputProps extends BaseComponentProps {
  variable: Variable;
  value: string;
  onChange: (value: string) => void;
  inputClassName?: string;
  inputStyle?: React.CSSProperties;
  renderInput?: (props: React.InputHTMLAttributes<HTMLInputElement>) => React.ReactNode;
}

export interface OneOfSelectProps extends BaseComponentProps {
  variable: Variable;
  value: string;
  onChange: (value: string) => void;
  selectClassName?: string;
  selectStyle?: React.CSSProperties;
  optionClassName?: string;
  optionStyle?: React.CSSProperties;
  renderSelect?: (
    options: Option[],
    value: string,
    onChange: (value: string) => void
  ) => React.ReactNode;
}

export interface RangeInputProps extends BaseComponentProps {
  variable: Variable;
  value: string;
  onChange: (value: string) => void;
  rangeSliderThreshold?: number;
  sliderClassName?: string;
  sliderStyle?: React.CSSProperties;
  inputClassName?: string;
  inputStyle?: React.CSSProperties;
  renderSlider?: (props: {
    min: number;
    max: number;
    step: number;
    value: number;
    onChange: (value: number) => void;
  }) => React.ReactNode;
  renderNumberInput?: (props: {
    min: number;
    max: number;
    value: number;
    onChange: (value: number) => void;
    onIncrement: () => void;
    onDecrement: () => void;
  }) => React.ReactNode;
}

export interface VarinFieldProps extends BaseComponentProps {
  variable: Variable;
  value?: string[];
  defaultValue?: string[];
  onChange?: (event: VarinFieldChangeEvent) => void;
  rangeSliderThreshold?: number;
  components?: {
    FixedDisplay?: React.ComponentType<FixedDisplayProps>;
    AnyInput?: React.ComponentType<AnyInputProps>;
    OneOfSelect?: React.ComponentType<OneOfSelectProps>;
    RangeInput?: React.ComponentType<RangeInputProps>;
  };
  // Pass-through props for sub-components
  fixedDisplayProps?: Partial<FixedDisplayProps>;
  anyInputProps?: Partial<AnyInputProps>;
  oneOfSelectProps?: Partial<OneOfSelectProps>;
  rangeInputProps?: Partial<RangeInputProps>;
}
