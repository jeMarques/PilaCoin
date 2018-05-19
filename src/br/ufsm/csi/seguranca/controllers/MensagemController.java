package br.ufsm.csi.seguranca.controllers;

import br.ufsm.csi.seguranca.listeners.MensagemListener;
import br.ufsm.csi.seguranca.pila.model.Mensagem;
/**
 * Created by cpol on 03/05/2018.
 */
public class MensagemController {

    public MensagemController() {
        MensagemListener.addListener(mensagem -> this.recebeuMensagem(mensagem));
    }
    public void recebeuMensagem(Mensagem Mensagem) {
        System.out.println("Recebendo Mensagem 1: " + Mensagem);
    }
}
