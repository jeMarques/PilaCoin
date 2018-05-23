package br.ufsm.csi.seguranca;

import br.ufsm.csi.seguranca.controllers.MensagemController;
import br.ufsm.csi.seguranca.controllers.MinningController;
import br.ufsm.csi.seguranca.controllers.PilaCoinController;
import br.ufsm.csi.seguranca.pila.model.PilaCoin;
import br.ufsm.csi.seguranca.util.CommandScanner;
import br.ufsm.csi.seguranca.util.Network;

public class Main {
    public static void main(String[] args) throws Exception {
        // instancia classe de conexão.
        Network net = new Network(3333, 3000);
        // instancia a escuta de comandos
        CommandScanner scanner = new CommandScanner();
        //instancia controllers
        MensagemController mensagemController = new MensagemController();
        PilaCoinController pilaController = new PilaCoinController();
        MinningController minningController = new MinningController();
    }
}
