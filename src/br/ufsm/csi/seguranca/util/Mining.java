package br.ufsm.csi.seguranca.util;

import br.ufsm.csi.seguranca.global.Me;
import br.ufsm.csi.seguranca.pila.model.PilaCoin;

public class Mining {
    Thread miningThread;
    boolean mining = false;
    public Mining() {

    }

    public void Start() {
        this.mining = true;
        StartMiningThread();
    }
    public void Stop() {

    }

    public void StartMiningThread() {
        this.miningThread = new Thread(() -> {
            while (true) {
                PilaCoin pila = new PilaCoin();
                pila.setChaveCriador(Me.MyPubKey());

            }
        });
    }
}
