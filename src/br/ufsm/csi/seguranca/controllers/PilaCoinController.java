package br.ufsm.csi.seguranca.controllers;

import br.ufsm.csi.seguranca.crypto.AES;
import br.ufsm.csi.seguranca.crypto.RSA;
import br.ufsm.csi.seguranca.global.Me;
import br.ufsm.csi.seguranca.global.Server;
import br.ufsm.csi.seguranca.listeners.PilaCoinListener;
import br.ufsm.csi.seguranca.pila.model.ObjetoTroca;
import br.ufsm.csi.seguranca.pila.model.PilaCoin;
import br.ufsm.csi.seguranca.util.Conection;
import br.ufsm.csi.seguranca.util.File;
import br.ufsm.csi.seguranca.util.Network;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class PilaCoinController {
    public PilaCoinController() {
        PilaCoinListener.addValidacaoListener(this::validaPilaCoin);
        PilaCoinListener.addValidaObjetoTrocaListener(this::checaPilaRecebido);
    }

    public void validaPilaCoin(PilaCoin pila) throws Exception {

        AES sessionAes = new AES(128);
        byte[] aes_key_criptografed = RSA.CipherKey(sessionAes.bytekey, Server.ServerPublicKey());
        byte[] pila_coin_serialized = Conection.serializeObject(pila);
        byte[] pila_coin_cypher = sessionAes.CipherByte(pila_coin_serialized);
        ObjetoTroca troca = new ObjetoTroca();

        troca.setChavePublica(Me.MyPubKey());
        troca.setChaveSessao(aes_key_criptografed);
        troca.setObjetoSerializadoCriptografado(pila_coin_cypher);

        RSA RsaKeys = new RSA();
        byte[] signature = RsaKeys.CypherWithPrivateKey(Conection.hash(pila_coin_serialized));
        troca.setAssinatura(signature);

        Network.exchangeTroca(troca, sessionAes);
    }

    public void checaPilaRecebido(ObjetoTroca troca, AES aesSession) throws Exception {
            PilaCoin pila = (PilaCoin)Conection.deserializeObject(aesSession.DecipherByte(troca.getObjetoSerializadoCriptografado()));
            System.out.println("Infos do pila recebido");
            System.out.println("getIdCriador:" + pila.getIdCriador().toString());
            System.out.println("getDataCriacao:" + pila.getDataCriacao().toString());
            System.out.println("getNumeroMagico:" + pila.getNumeroMagico().toString());
            System.out.println("getId:" + pila.getId());

            File file = new File(Conection.serializeObject(pila));
            file.savetoPath("wallet/" + pila.getNumeroMagico() + ".pila");
            System.out.println("Pila salvo.. ");
    }

    public static void TransferePila(int Quantidade, String idNovoDono) throws IOException, ClassNotFoundException {
        PilaCoin pilaForTransfer = File.getPila();
        if (pilaForTransfer!=null) {

        }
    }
}
