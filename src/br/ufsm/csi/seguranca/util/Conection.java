package br.ufsm.csi.seguranca.util;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by cpol on 19/04/2018.
 */
public class Conection {
    public static byte[] serializeObject(Serializable obj) throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bout);
        out.writeObject(obj);
        return bout.toByteArray();
    }
    public static Serializable deserializeObject(byte[] obj) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bin = new ByteArrayInputStream(obj);
        ObjectInputStream in = new ObjectInputStream(bin);
        return (Serializable) in.readObject();
    }

    public static byte[] hash(byte[] data) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        return messageDigest.digest(data);
    }
}
