package br.ufsm.csi.seguranca.controllers;

import br.ufsm.csi.seguranca.listeners.MensagemListener;
import br.ufsm.csi.seguranca.listeners.PilaCoinListener;
import br.ufsm.csi.seguranca.pila.model.PilaCoin;

public class PilaCoinController {
    public PilaCoinController() {
        PilaCoinListener.addValidacaoListener(this::validaPilaCoin);
    }

    public void validaPilaCoin(PilaCoin pila) {

    }
}
