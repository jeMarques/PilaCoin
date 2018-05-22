package br.ufsm.csi.seguranca.global;

import br.ufsm.csi.seguranca.util.RSAUtil;

import java.net.InetAddress;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;

public class Server {
    public static boolean ipSync = false;
    public static InetAddress TCPAddress;
    public static int PORT;
    public static PublicKey ServerPublicKey() {
        try {
            return RSAUtil.getPublicKey("master_public_key.der");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
