package com.dakshverma.varin.evaluator;

import com.dakshverma.varin.Variable;

public interface Evaluator {
    /**
     * evaluates if value can be a valid value of given variables and returns it in correct data type
     */
    Object evaluateAndGet(final String value, final Variable variable);
    Integer evaluateAndGetInt(final String value, final Variable variable);
    String evaluateAndGetString(final String value, final Variable variable);
}
