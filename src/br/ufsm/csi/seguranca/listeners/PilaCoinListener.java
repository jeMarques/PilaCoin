package br.ufsm.csi.seguranca.listeners;

import br.ufsm.csi.seguranca.pila.model.Mensagem;
import br.ufsm.csi.seguranca.pila.model.PilaCoin;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cpol on 15/05/2018.
 */
public class PilaCoinListener {

    private static final List<ValidacaoListener> Vlisteners = new ArrayList<>();
    private static final List<TransferenciaListener> Tlisteners = new ArrayList<>();
    public static void InvocaValidacao(PilaCoin pila) {
        fireValidacaoEvent(pila);
    }
    public static void InvocaTransferencia(PilaCoin pila) {
        fireTransferenciaEvent(pila);
    }

    // método a ser chamado para 'enviar' o evento
    private static void fireValidacaoEvent(PilaCoin pila) {
        for (ValidacaoListener listener : Vlisteners) {
            listener.ValidacaoEvento(pila);  // this opcional
        }
    }
    private static void fireTransferenciaEvent(PilaCoin pila) {
        for (TransferenciaListener listener : Tlisteners) {
            listener.TransferenciaEvento(pila);  // this opcional
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

    public static void removeValidacaoListener(ValidacaoListener listener) {
        Vlisteners.remove(listener);
    }
    public static void removeTrasnsferenciaListener(TransferenciaListener listener) {
        Tlisteners.remove(listener);
    }

    public interface ValidacaoListener {
        public void ValidacaoEvento(PilaCoin pila);
    }
    public interface TransferenciaListener {
        public void TransferenciaEvento(PilaCoin pila);
    }
}

