package com.github.why168.androidhttputils;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }
    // 0.488 --13.49%

    @Test
    public void dd() {
        System.out.println(0.488 / 1.1349);
        System.out.println(0.43 * (1 + 0.1349));

        System.out.println((0.488 - 0.43) / 0.43 * 100 + "%");

        System.out.println(new BigDecimal((0.488 - 0.43) / 0.43 * 100).floatValue());
    }
}