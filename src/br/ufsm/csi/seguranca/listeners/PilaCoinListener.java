package br.ufsm.csi.seguranca.listeners;

import br.ufsm.csi.seguranca.crypto.AES;
import br.ufsm.csi.seguranca.pila.model.ObjetoTroca;
import br.ufsm.csi.seguranca.pila.model.PilaCoin;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cpol on 15/05/2018.
 */
public class PilaCoinListener {

    private static final List<ValidacaoListener> Vlisteners = new ArrayList<>();
    private static final List<TransferenciaListener> Tlisteners = new ArrayList<>();
    private static final List<ValidaObjetoTrocaListener> OTlisteners = new ArrayList<>();

    public static void InvocaValidacao(PilaCoin pila) throws Exception {
        fireValidacaoEvent(pila);
    }
    public static void InvocaTransferencia(int Quantidade, String idNovoDono) {
        fireTransferenciaEvent(Quantidade,idNovoDono);
    }
    public static void InvocaValidaObjetoTroca(ObjetoTroca troca, AES aesSession) throws Exception {
        fireValidaObjetoTrocaListener(troca, aesSession);
    }

    // método a ser chamado para 'enviar' o evento
    private static void fireValidacaoEvent(PilaCoin pila) throws Exception {
        for (ValidacaoListener listener : Vlisteners) {
            listener.ValidacaoEvento(pila);  // this opcional
        }
    }
    private static void fireTransferenciaEvent(int Quantidade, String idNovoDono) {
        for (TransferenciaListener listener : Tlisteners) {
            listener.TransferenciaEvento(Quantidade, idNovoDono);  // this opcional
        }
    }

    private static void fireValidaObjetoTrocaListener(ObjetoTroca troca, AES aesSession) throws Exception {
        for (ValidaObjetoTrocaListener listener : OTlisteners) {
            listener.ValidaObjetoTrocaEvento(troca, aesSession);  // this opcional
        }
    }

    public static void addValidacaoListener(ValidacaoListener listener) {
        if (listener != null) {
            Vlisteners.add(listener);
        }
    }
    public static void addTransferenciaListener(TransferenciaListener listener) {
        if (listener != null) {
            Tlisteners.add(listener);
        }
    }
    public static void addValidaObjetoTrocaListener(ValidaObjetoTrocaListener listener) {
        if (listener != null) {
            OTlisteners.add(listener);
        }
    }

    public static void removeValidacaoListener(ValidacaoListener listener) {
        Vlisteners.remove(listener);
    }
    public static void removeTrasnsferenciaListener(TransferenciaListener listener) {
        Tlisteners.remove(listener);
    }

    public interface ValidacaoListener {
        public void ValidacaoEvento(PilaCoin pila) throws Exception;
    }
    public interface TransferenciaListener {
        public void TransferenciaEvento(int Quantidade, String idNovoDono);
    }
    public interface ValidaObjetoTrocaListener {
        public void ValidaObjetoTrocaEvento(ObjetoTroca troca, AES aesSession) throws Exception;
    }
}

