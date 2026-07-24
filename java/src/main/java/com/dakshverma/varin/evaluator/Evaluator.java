package com.dakshverma.varin.evaluator;


import com.dakshverma.varin.Variable;
import com.dakshverma.varin.value.Value;

import java.util.List;

public interface Evaluator {
    List<?> evaluateAndGet(final Value input, final Variable forVariable);
}
