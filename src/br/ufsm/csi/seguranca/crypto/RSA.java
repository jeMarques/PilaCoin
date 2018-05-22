package br.ufsm.csi.seguranca.crypto;
import javax.crypto.*;
import java.io.IOException;
import java.security.*;

/**
 * Created by cpol on 29/03/2018.
 */
public class RSA {
    private Key publicKey = null;
    private Key privateKey = null;

    private Cipher cipher = Cipher.getInstance("RSA");
    private KeyPair kp = null;
    private KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");

    public RSA(int keysize) throws NoSuchAlgorithmException, NoSuchPaddingException {
        keyGen.initialize(keysize);
        this.kp = keyGen.generateKeyPair();
        this.publicKey = this.kp.getPublic();
        this.privateKey = this.kp.getPrivate();
    }
    public void setPublicKey(Key publicKey) {
        this.publicKey = publicKey;
    }
    public Key getPublicKey() {
        return this.publicKey;
    }
    public void setPrivateKey(Key privateKey) {
        this.privateKey = privateKey;
    }
    public Key getPrivateKey() {
        return this.privateKey;
    }



    public static byte[] CipherKey(byte[] aes_key, Key friend_public_key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, friend_public_key);
        return cipher.doFinal(aes_key);
    }
    public byte[] DecipherKey(byte[] aes_key_criptographed) throws InvalidKeyException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException {
        cipher.init(Cipher.DECRYPT_MODE, this.privateKey);
        return cipher.doFinal(aes_key_criptographed);
    }
}