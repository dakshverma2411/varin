package com.dakshverma.varin.value;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor(force = true)
public class Value {
    private final List<String> value;

    @JsonCreator
    public Value(@JsonProperty("value") final List<String> value) {
        this.value = value;
    }
}
