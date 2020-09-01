package com.dumblthon.messenger.auth.component;

import com.dumblthon.messenger.auth.component.impl.SecureRandomSecretGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(JUnit4.class)
public class SecureRandomSecretGeneratorTest {

    private static final Logger log = LoggerFactory.getLogger(SecureRandomSecretGeneratorTest.class);

    @Test
    public void test() {
        SecureRandomSecretGenerator generator = new SecureRandomSecretGenerator();
        log.info("secret = {}", generator.generate(128));
    }

}
