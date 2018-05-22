package br.ufsm.csi.seguranca.controllers;

import br.ufsm.csi.seguranca.global.Me;
import br.ufsm.csi.seguranca.listeners.PilaCoinListener;
import br.ufsm.csi.seguranca.pila.model.PilaCoin;
import br.ufsm.csi.seguranca.util.Conection;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;

public class MinningController {
    Thread miningThread;
    boolean mining = false;
    private static final BigInteger bigInt = new BigInteger("99999998000000000000000000000000000000000000000000000000000000000000000");


    public MinningController() {
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

                try {
                    PilaCoinListener.InvocaValidacao(pila);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //validation
            }
        });
        this.miningThread.start();
    }
}
