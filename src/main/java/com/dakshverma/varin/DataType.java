package com.dakshverma.varin;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Data type supported
 */
public enum DataType {
    STRING,
    INT
    ;

    public static final Set<DataType> ALL = Arrays.stream(DataType.values()).collect(Collectors.toSet());
}
