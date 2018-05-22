package br.ufsm.csi.seguranca.controllers;

import br.ufsm.csi.seguranca.crypto.AES;
import br.ufsm.csi.seguranca.crypto.RSA;
import br.ufsm.csi.seguranca.global.Me;
import br.ufsm.csi.seguranca.global.Server;
import br.ufsm.csi.seguranca.listeners.PilaCoinListener;
import br.ufsm.csi.seguranca.pila.model.ObjetoTroca;
import br.ufsm.csi.seguranca.pila.model.PilaCoin;
import br.ufsm.csi.seguranca.util.Conection;
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
    }

    public void validaPilaCoin(PilaCoin pila) throws NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, IllegalBlockSizeException, IOException, ClassNotFoundException {

        AES sessionAes = new AES(256);
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

        Network.sendTroca(troca, sessionAes);
    }
}