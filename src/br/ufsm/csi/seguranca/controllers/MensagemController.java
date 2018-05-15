package br.ufsm.csi.seguranca.controllers;

import br.ufsm.csi.seguranca.MensagemListener;

/**
 * Created by cpol on 03/05/2018.
 */
public class MensagemController {

    public MensagemController() {
        MensagemListener.addListener(mensagem -> this.recebeuMensagem(mensagem));
    }
    public void recebeuMensagem(String Mensagem) {
        System.out.println("Recebendo Mensagem 1: " + Mensagem);
    }
}
