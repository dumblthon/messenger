package com.dumblthon.messenger.auth;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

@RunWith(JUnit4.class)
public class HmacTest {

    /*@Test
    public void testDefault() throws InvalidKeyException, NoSuchAlgorithmException {
        String testKey = "fDw7MpHk5czHNuSRtLhGmAGL42CaxQB9";
        String testMessage = "51090";

        byte[] expectedHmac = ("95d8edd7a3e38260d80cef11a4254c" +
                "49231e8aa13fd07f96026fc7d7c28de1d865e5f1e5cd208394fc674e441" +
                "a162df251aad1a6ad33b1a8f115a50faa758407")
                .getBytes(StandardCharsets.UTF_8);
        String expectedBase64EncodedHmac = "OTVkOGVkZDdhM2UzODI" +
                "2MGQ4MGNlZjExYTQyNTRjNDkyMzFlOGFhMTNmZDA3Zjk2MDI2ZmM3ZDdjMjhk" +
                "ZTFkODY1ZTVmMWU1Y2QyMDgzOTRmYzY3NGU0NDFhMTYyZGYyNTFhYWQxYTZhZD" +
                "MzYjFhOGYxMTVhNTBmYWE3NTg0MDc=";
        String expected = Base64.getEncoder().encodeToString(expectedHmac);

        Assert.assertEquals(expected, new Hmac(testKey).generate(testMessage, counter));
    }*/

    @Test
    public void test() {
        byte[] generatedHmac = ("95d8edd7a3e38260d80cef11a4254c" +
                "49231e8aa13fd07f96026fc7d7c28de1d865e5f1e5cd208394fc674e441" +
                "a162df251aad1a6ad33b1a8f115a50faa75840Z")
                .getBytes(StandardCharsets.UTF_8);
        System.out.println(Arrays.toString(generatedHmac));
        int offset = generatedHmac[generatedHmac.length - 1] & 0xf;
        System.out.println(offset);
        System.out.println("***");
        System.out.println(generatedHmac[offset]);
        System.out.println(generatedHmac[offset] & 0x7f);
        System.out.println((generatedHmac[offset] & 0x7f) << 24);
        System.out.println("***");
        System.out.println(generatedHmac[offset + 1]);
        System.out.println(generatedHmac[offset + 1] & 0xff);
        System.out.println((generatedHmac[offset + 1] & 0xff) << 16);
        System.out.println("***");
        System.out.println(generatedHmac[offset + 2]);
        System.out.println(generatedHmac[offset + 2] & 0xff);
        System.out.println((generatedHmac[offset + 2] & 0xff) << 8);
        System.out.println("***");
        System.out.println(generatedHmac[offset + 3]);
        System.out.println(generatedHmac[offset + 3] & 0xff);
        System.out.println((generatedHmac[offset + 3] & 0xff));
        System.out.println("***");
        int binary = ((generatedHmac[offset] & 0x7f) << 24) |
                        ((generatedHmac[offset + 1] & 0xff) << 16) |
                        ((generatedHmac[offset + 2] & 0xff) << 8) |
                        (generatedHmac[offset + 3] & 0xff);
        System.out.println(binary);
        System.out.println("***");
        System.out.println(binary % ((int) Math.pow(10, 6)));
    }

}
