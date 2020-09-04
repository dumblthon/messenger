package com.dumblthon.messenger.auth.security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;

@Configuration
public class JwtConfiguration {

    @Value("${key.store.file}")
    private String keyStoreFile;

    @Value("${key.store.pass}")
    private String keyStorePass;

    @Value("${key.alias}")
    private String keyAlias;

    @Value("${key.pass}")
    private String keyPass;

    @Value("${key.id}")
    private String kid;

    @Bean
    public RSAKey rsaKey() throws IOException, NoSuchAlgorithmException, KeyStoreException,
            CertificateException, UnrecoverableKeyException, JOSEException {

        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        Path path = Paths.get("src/main/resources", keyStoreFile);
        FileInputStream pkcs = new FileInputStream(path.toFile());
        keyStore.load(pkcs, keyStorePass.toCharArray());

        // Given final block not properly padded. Such issues can arise if a bad key is used during decryption
        RSAPrivateKey privateKey = (RSAPrivateKey) keyStore.getKey(keyAlias, keyPass.toCharArray());
        X509Certificate certificate = (X509Certificate) keyStore.getCertificate(keyAlias);
        RSAKey publicKey = RSAKey.parse(certificate);

        return new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyUse(KeyUse.SIGNATURE)
                .algorithm(JWSAlgorithm.RS256)
                .keyID(kid)
                .build();
    }

}
