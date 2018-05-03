package br.ufsm.csi.seguranca.interfaces;


import java.util.EventListener;

/**
 * Created by cpol on 03/05/2018.
 */
public class MensagemHandler {

    // add a private listener variable
    private MensagemListener mListener = null;

    // provide a way for another class to set the listener
    public void setMensagemListener(MensagemListener listener) {
        this.mListener = listener;
    }


    // interface from Step 1
    public interface MensagemListener {
        public void recebeuMensagem(String title);
    }
}