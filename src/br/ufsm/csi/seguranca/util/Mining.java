package br.ufsm.csi.seguranca.util;

import br.ufsm.csi.seguranca.global.Me;
import br.ufsm.csi.seguranca.pila.model.PilaCoin;

import java.io.IOException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;

public class Mining {
    Thread miningThread;
    boolean mining = false;
    private static final BigInteger bigInt = new BigInteger("99999998000000000000000000000000000000000000000000000000000000000000000");


    public Mining() {
        Start();
    }

    public void Start() {
        this.mining = true;
        StartMiningThread();
    }
    public void Stop() {
        this.miningThread.stop();
    }

    public void StartMiningThread() {
        this.miningThread = new Thread(() -> {
            while (this.mining) {
                //create new pila and set data
                PilaCoin pila = new PilaCoin();
                pila.setChaveCriador(Me.MyPubKey());
                pila.setIdCriador(Me.myIDOrigem);
                pila.setDataCriacao(new Date());

                BigInteger insideBigInt = null;

                //actual mining
                do {
                    //generate and set magic number
                    SecureRandom sr = new SecureRandom();
                    Long magicNumber = sr.nextLong();
                    pila.setNumeroMagico(magicNumber);

                    try {
                        insideBigInt = new  BigInteger(
                                1,
                                Conection.hash(Conection.serializeObject(pila))
                        );
                    } catch (NoSuchAlgorithmException | IOException e) {
                        e.printStackTrace();
                    }
                } while (insideBigInt.compareTo(bigInt) > 0);

                System.out.println("Achou Pila?");
                //validation
            }
        });
        this.miningThread.start();
    }
}
