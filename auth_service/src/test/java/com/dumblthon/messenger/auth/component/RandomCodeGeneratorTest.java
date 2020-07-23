package com.dumblthon.messenger.auth.component;

import com.dumblthon.messenger.auth.component.impl.RandomCodeGenerator;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(JUnit4.class)
public class RandomCodeGeneratorTest {

    private static final Logger log = LoggerFactory.getLogger(RandomCodeGeneratorTest.class);

    private final RandomCodeGenerator generator = new RandomCodeGenerator();

    @Test(expected = IllegalArgumentException.class)
    public void testLengthEqualsFive() {
        generator.generate(5);
    }

    @Test
    public void testSixToEightLength() {
        for (int i = 6; i <= 8; i++) {
            String code = generator.generate(i);
            log.info("length = {}, code = {}", i, code);
            Assert.assertEquals(i, code.length());
        }
    }

}
