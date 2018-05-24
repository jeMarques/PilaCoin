package br.ufsm.csi.seguranca.listeners;

import br.ufsm.csi.seguranca.pila.model.Mensagem;
import br.ufsm.csi.seguranca.pila.model.PilaCoin;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cpol on 15/05/2018.
 */
public class NetworkListener {

        private static final List<Listener> listeners = new ArrayList<>();
        public static void sendTransfer(PilaCoin pila) throws Exception {
            fireEvento(pila);
        }

        // método a ser chamado para 'enviar' o evento
        private static void fireEvento(PilaCoin pila) throws Exception {
            for (Listener listener : listeners) {
                listener.sendTransfer(pila);  // this opcional
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
            public void sendTransfer(PilaCoin pila) throws Exception;
        }
    }

