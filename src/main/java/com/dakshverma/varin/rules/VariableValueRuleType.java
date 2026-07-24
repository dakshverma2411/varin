package com.dakshverma.varin.rules;

import com.dakshverma.varin.DataType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@AllArgsConstructor
@Getter
public enum VariableValueRuleType {
    ANY(DataType.ALL),
    ONE_OF(DataType.ALL),
    RANGE(Set.of(DataType.INT));

    private final Set<DataType> supportedDataTypes;

}
