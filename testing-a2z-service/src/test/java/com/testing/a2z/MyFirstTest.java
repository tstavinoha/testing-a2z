package com.testing.a2z;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class MyFirstTest {

    @Test
    void dividesNumbers() {
        assertThat(20 / 4).isEqualTo(5);
    }

}
