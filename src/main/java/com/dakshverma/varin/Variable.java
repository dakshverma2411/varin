package com.dakshverma.varin;

import com.dakshverma.varin.value.VariableValue;
import lombok.Value;

@Value
public class Variable {
    String name;
    String description;
    boolean required;
    DataType dataType;
    VariableValue value;
}
