package org.motheen.server.io;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.*;

/**
 *
 * @author Motheen Baig
 */
public class Session {

    //
    private Cipher cipher;
    private KeyAgreement keyAgreement;
    private KeyPairGenerator keyPairGenerator;
    //
    private PrivateKey privateKey;
    private PublicKey publicKey, receivedPublicKey;
    private byte secretKey[];
    //

    public Session() {
        try {
            keyAgreement = KeyAgreement.getInstance("DH");
            cipher  = Cipher.getInstance("DES/ECB/PKCS5Padding");
            keyPairGenerator = KeyPairGenerator.getInstance("DH");
        } catch (final NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        }
    }

    public byte[] encryptData(final String message) {
        SecretKeySpec keySpec;
        try {
            keySpec = new SecretKeySpec(secretKey, "DES");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            return cipher.doFinal(message.getBytes());
        } catch (final InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void sendData(final byte encryptedMessage[], final DataOutputStream outputStream) {
        try {
            outputStream.writeInt(encryptedMessage.length);
            outputStream.write(encryptedMessage, 0, encryptedMessage.length);
            outputStream.flush();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] decryptData(final byte[] encryptedMessage) {
        SecretKeySpec keySpec;
        byte decrypted[] = null;
        try {
            keySpec = new SecretKeySpec(secretKey, "DES");
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            decrypted = cipher.doFinal(encryptedMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decrypted;
    }

    public byte[] getData(final DataInputStream inputStream) {
        int len;
        byte data[] = null;
        try {
            len = inputStream.readInt();
            if (len > -1) {
                data = new byte[len];
                inputStream.readFully(data, 0, len);
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    public void generateSecretKey() {

        try {
            final KeyAgreement keyAgreement = KeyAgreement.getInstance("DH");
            keyAgreement.init(privateKey);
            keyAgreement.doPhase(receivedPublicKey, true);
            secretKey = shortenSecretKey(keyAgreement.generateSecret());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void generateKeyPair() {
        KeyPair keyPair;
        KeyPairGenerator keyPairGenerator;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance("DH");
            keyPairGenerator.initialize(1024);
            keyPair = keyPairGenerator.generateKeyPair();
            privateKey = keyPair.getPrivate();
            publicKey  = keyPair.getPublic();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private byte[] shortenSecretKey(final byte[] longKey) {
        byte[] shortenedKey;
        try {
            shortenedKey = new byte[8];
            System.arraycopy(longKey, 0, shortenedKey, 0, shortenedKey.length);
            return shortenedKey;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public byte[] getSecretKey() {
        return secretKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setOtherPublicKey(PublicKey receivedPublicKey) {
        this.receivedPublicKey = receivedPublicKey;
    }
}

