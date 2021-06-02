package com.squad.tdd.utils

import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat

infix fun Any?.shouldBeEqualTo(value: Any?) {
    assertThat(this, equalTo(value))
}