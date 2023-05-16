package com.testing.a2z;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;

class MyFirstBddTest {

    @Test
    void shouldDivideNumbers() {
        // given
        var givenDividend = 20;
        var givenDivisor = 4;

        // when
        var result = givenDividend / givenDivisor;

        // then
        var expectedResult = 5;
        then(result).isEqualTo(expectedResult);
    }

}
