package com.dakshverma.varin.value;

import lombok.Builder;

import java.util.List;

@lombok.Value
@Builder
public class Value {
    List<String> value;
}
