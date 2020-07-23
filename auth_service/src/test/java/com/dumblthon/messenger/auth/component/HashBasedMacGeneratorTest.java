package com.dumblthon.messenger.auth.component;

import com.dumblthon.messenger.auth.component.impl.HashBasedMacGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RunWith(JUnit4.class)
public class HashBasedMacGeneratorTest {

    private static final Logger log = LoggerFactory.getLogger(HashBasedMacGeneratorTest.class);

    @Test
    public void test() throws NoSuchAlgorithmException, InvalidKeyException {
        HashBasedMacGenerator generator = new HashBasedMacGenerator();
        String mac = generator.generate("test", "secret");

//         https://www.freeformatter.com/hmac-generator.html
//        Assert.assertEquals("f8a4f0a209167bc192a1bffaa01ecdb09e" +
//                "06c57f96530d92ec9ccea0090d290e55071306d6b654f26ae0c8721" +
//                "f7e48a2d7130b881151f2cec8d61d941a6be88a", mac);
    }

}
