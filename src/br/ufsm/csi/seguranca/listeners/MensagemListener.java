package br.ufsm.csi.seguranca.listeners;

import br.ufsm.csi.seguranca.pila.model.Mensagem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cpol on 15/05/2018.
 */
public class MensagemListener {

        private static final List<Listener> listeners = new ArrayList<>();
        public static void RecebeuMensagem(Mensagem Mensagem) throws Exception {
            fireEvento(Mensagem);
        }

        // método a ser chamado para 'enviar' o evento
        private static void fireEvento(Mensagem Mensagem) throws Exception {
            for (Listener listener : listeners) {
                listener.evento(Mensagem);  // this opcional
            }
        }

        public static void addListener(Listener listener) {
            if (listener != null) {
                listeners.add(listener);
            }
        }

        public static void removeListenerListener(Listener listener) {
            listeners.remove(listener);
        }

        public interface Listener {

            // use um nome mais 'interessante' para o método e para oS dadoS
            public void evento(Mensagem mensagem) throws Exception;
        }
    }

