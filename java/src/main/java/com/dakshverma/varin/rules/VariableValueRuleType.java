package com.dakshverma.varin.rules;

import com.dakshverma.varin.DataType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@AllArgsConstructor
@Getter
public enum VariableValueRuleType {
    ANY(DataType.ALL) {
        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitAny();
        }
    },
    ONE_OF(DataType.ALL) {
        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitOneOf();
        }
    },
    RANGE(Set.of(DataType.INT)) {
        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitRange();
        }
    };

    private final Set<DataType> supportedDataTypes;

    public abstract <R> R accept(Visitor<R> visitor);

    public interface Visitor<R> {
        R visitAny();

        R visitOneOf();

        R visitRange();
    }

}
