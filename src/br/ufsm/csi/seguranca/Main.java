package br.ufsm.csi.seguranca;

import br.ufsm.csi.seguranca.controllers.MensagemController;
import br.ufsm.csi.seguranca.listeners.MensagemListener;
import br.ufsm.csi.seguranca.util.Network;

public class Main {




    public static void main(String[] args) throws Exception {
        // write your code here
        Network net = new Network(3333, 3000);
        MensagemController controller = new MensagemController();

        MensagemListener.addListener(mensagem -> {
            System.out.println("Recebeu Mensagem 2: " + mensagem);
        });

        // net.recebeuMensagemListener.setMensagemListener();


        // net.listenManual();
    }
}
