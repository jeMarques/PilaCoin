package br.ufsm.csi.seguranca.controllers;

import br.ufsm.csi.seguranca.global.Server;
import br.ufsm.csi.seguranca.listeners.MensagemListener;
import br.ufsm.csi.seguranca.pila.model.Mensagem;
/**
 * Created by cpol on 03/05/2018.
 */
public class MensagemController {

    public MensagemController() {
        //register message listeners and execute functions.
        MensagemListener.addListener(mensagem -> this.recebeuMensagem(mensagem));

    }
    public void recebeuMensagem(Mensagem Mensagem) {
        System.out.println("Recebeu Mensagem: " + Mensagem.getTipo());
        switch (Mensagem.getTipo()) {
            case DISCOVER:
                //salvar outros usuarios
                break;
            case DISCOVER_RESP:
                if (Server.ServerPublicKey().equals(Mensagem.getChavePublica())) {
                    Server.ipSync = true;
                    Server.PORT = Mensagem.getPorta();
                    Server.TCPAddress = Mensagem.getEndereco();
                } else {
                    System.out.println("Chave publica do servidor não bate.");
                }
                break;
            case PILA_TRANSF:
                break;
            case ERRO:
                System.out.println("Aconteceu um erro: " + Mensagem.getErro());
                break;
        }

    }
}
