package com.dakshverma.varin;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Data type supported
 */
public enum DataType {
    STRING {
        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitString();
        }
    },
    INT {
        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitInt();
        }
    };

    public static final Set<DataType> ALL = Arrays.stream(DataType.values()).collect(Collectors.toSet());

    public abstract <R> R accept(Visitor<R> visitor);

    public interface Visitor<R> {
        R visitString();
        R visitInt();
    }
}
