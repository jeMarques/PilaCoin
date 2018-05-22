package br.ufsm.csi.seguranca;

import br.ufsm.csi.seguranca.controllers.MensagemController;
import br.ufsm.csi.seguranca.controllers.MinningController;
import br.ufsm.csi.seguranca.util.Network;

public class Main {
    public static void main(String[] args) throws Exception {
        // instancia classe de conexão.
        Network net = new Network(3333, 3000);

        //instancia controllers
        MensagemController controller = new MensagemController();

        //instancia minerador
        MinningController minningController = new MinningController();



    }
}
