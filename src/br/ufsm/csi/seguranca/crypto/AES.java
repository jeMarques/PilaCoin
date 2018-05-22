package br.ufsm.csi.seguranca.crypto;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class AES {
    public byte[] bytekey = null;

    private SecretKey aesKey = null;

    private SecretKeySpec aesKeySpec = null;

    private Cipher cipher = Cipher.getInstance("AES");

    private KeyGenerator keyGen = KeyGenerator.getInstance("AES");

    public AES(int keysize) throws NoSuchAlgorithmException, NoSuchPaddingException {
        keyGen.init(keysize);
        this.aesKey = keyGen.generateKey();
        this.bytekey = aesKey.getEncoded();
    }

    public byte[] CipherByte(byte[] file) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        cipher.init(Cipher.ENCRYPT_MODE, this.aesKey);
        return cipher.doFinal(file);
    }

    public byte[] DecipherByte(byte[] file, byte[] AesByteKey) throws InvalidKeyException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException {
        this.aesKeySpec = new SecretKeySpec(AesByteKey, "AES");
        cipher.init(Cipher.DECRYPT_MODE, this.aesKeySpec);
        return cipher.doFinal(file);
    }
}
