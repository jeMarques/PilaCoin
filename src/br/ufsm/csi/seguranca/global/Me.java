package br.ufsm.csi.seguranca.global;
import br.ufsm.csi.seguranca.util.RSAUtil;
import java.security.PublicKey;


public class Me {
    public static String myIDOrigem = "jmsilva@inf.ufsm.br";
    public static PublicKey MyPubKey() {
        try {
            return RSAUtil.getPublicKey("public_key.der");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
