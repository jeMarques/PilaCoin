package br.ufsm.csi.seguranca.crypto;
import br.ufsm.csi.seguranca.global.Me;
import br.ufsm.csi.seguranca.pila.model.Mensagem;
import br.ufsm.csi.seguranca.pila.model.ObjetoTroca;
import br.ufsm.csi.seguranca.util.Conection;
import br.ufsm.csi.seguranca.util.RSAUtil;

import javax.crypto.*;
import java.io.IOException;
import java.security.*;
import java.util.Arrays;

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
    public RSA() throws NoSuchPaddingException, NoSuchAlgorithmException {
        this.privateKey = Me.MyPrivateKey();
        this.publicKey = Me.MyPubKey();
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
    public byte[] CypherWithPrivateKey(byte[] data) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        cipher.init(Cipher.ENCRYPT_MODE, this.privateKey);
        return cipher.doFinal(data);
    }

    public static byte[] DecypherHashSignature(byte[] data) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, RSAUtil.getMasterPublicKey());
        return cipher.doFinal(data);
    }

    public static byte[] CypherWithMyPrivateKey(byte[] data) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, Me.MyPrivateKey());
        return cipher.doFinal(data);
    }


    public static boolean validateSignature(Object mensagemOrTroca) throws Exception {
        if (mensagemOrTroca instanceof Mensagem) {
            Mensagem Mensagem = (Mensagem)mensagemOrTroca;
            byte[] hash_cripto = Mensagem.getAssinatura();
            byte[] hash_decripto = RSA.DecypherHashSignature(hash_cripto);
            Mensagem.setAssinatura(null);
            byte[] hash_mensagem = Conection.hash(Conection.serializeObject(Mensagem));
            if (!Arrays.equals(hash_decripto,hash_mensagem)) {
                System.out.println("Mensagem não autentico..");
                return false;
            } else {
                return true;
            }
        } else if (mensagemOrTroca instanceof ObjetoTroca) {
            try {
                ObjetoTroca troca = (ObjetoTroca) mensagemOrTroca;
                byte[] hash_cripto = troca.getAssinatura();
                byte[] hash_decripto = RSA.DecypherHashSignature(hash_cripto);
                troca.setAssinatura(null);
                byte[] hash_mensagem = Conection.hash(Conection.serializeObject(troca));
                if (!Arrays.equals(hash_decripto, hash_mensagem)) {
                    System.out.println("Troca não autentico..");
                    return false;
                } else {
                    return true;
                }
            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }
    }
}