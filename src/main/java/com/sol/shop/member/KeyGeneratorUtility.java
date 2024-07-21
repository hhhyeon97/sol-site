package com.sol.shop.member;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;

public class KeyGeneratorUtility {

    public static void main(String[] args) throws NoSuchAlgorithmException {
        KeyPair keyPair = generateRSAKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

        String encodedPrivateKey = Base64.getEncoder().encodeToString(privateKey.getEncoded());
        String encodedPublicKey = Base64.getEncoder().encodeToString(publicKey.getEncoded());

        System.out.println("Private Key: " + encodedPrivateKey);
        System.out.println("Public Key: " + encodedPublicKey);
    }

    public static KeyPair generateRSAKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance("RSA");
        keyGenerator.initialize(2048);
        return keyGenerator.generateKeyPair();
    }
}