package br.ufsm.csi.seguranca.controllers;

import br.ufsm.csi.seguranca.crypto.RSA;
import br.ufsm.csi.seguranca.global.Community;
import br.ufsm.csi.seguranca.global.Server;
import br.ufsm.csi.seguranca.listeners.MensagemListener;
import br.ufsm.csi.seguranca.pila.model.Mensagem;
import br.ufsm.csi.seguranca.server.model.Usuario;
import br.ufsm.csi.seguranca.util.Conection;
import br.ufsm.csi.seguranca.util.File;

/**
 * Created by cpol on 03/05/2018.
 */
public class MensagemController {

    public MensagemController() {
        //register message listeners and execute functions.
        MensagemListener.addListener(this::recebeuMensagem);

    }
    public void recebeuMensagem(Mensagem Mensagem) throws Exception {
        System.out.println("Recebeu Mensagem: " + Mensagem.getTipo());
        switch (Mensagem.getTipo()) {
            case DISCOVER:
                //salvar outros usuarios
                System.out.println("Recebeu discover, outros usuarios..");
                System.out.println("ID: " + Mensagem.getIdOrigem());
                System.out.println("ENDEREÇO: " + Mensagem.getEndereco().toString());
                System.out.println("PORTA: " + Mensagem.getPorta());

                //TODO devo salvar
                Usuario user = new Usuario();
                user.setChavePublica(Mensagem.getChavePublica());
                user.setId(Mensagem.getIdOrigem());
                user.setEndereco(Mensagem.getEndereco());

                Community.addOrUpdateUser(user);

                break;
            case DISCOVER_RESP:
                if (RSA.validateSignature(Mensagem)) {
                    System.out.println("Recebeu discover RESP, salvando dados do server..");
                    System.out.println("PORT: " + Mensagem.getPorta());
                    System.out.println("ENDEREÇO: " + Mensagem.getEndereco());
                    Server.ipSync = true;
                    Server.PORT = Mensagem.getPorta();
                    Server.TCPAddress = Mensagem.getEndereco();
                } else {
                    System.out.println("Chave publica do servidor não bate.");
                }
                break;
            case PILA_TRANSF:
                System.out.println("Recebeu pila transf");
                System.out.println("ID: " + Mensagem.getIdOrigem());
                System.out.println("ENDEREÇO: " + Mensagem.getEndereco().toString());
                System.out.println("PORTA: " + Mensagem.getPorta());
                File pilaFile = new File("wallet/" + Mensagem.getPilaCoin().getNumeroMagico().toString() + ".pila");
                if (pilaFile.file==null) {
                    pilaFile = new File(Conection.serializeObject(Mensagem.getPilaCoin()));
                }
                pilaFile.onlySave();

                break;
            case ERRO:
                System.out.println("Aconteceu um erro: " + Mensagem.getErro());
                break;
        }

    }
}
