package br.ufsm.csi.seguranca;

import br.ufsm.csi.seguranca.controllers.MensagemController;
import br.ufsm.csi.seguranca.pila.model.Mensagem;
import br.ufsm.csi.seguranca.util.Conection;
import br.ufsm.csi.seguranca.util.Network;
import br.ufsm.csi.seguranca.util.RSAUtil;

import java.awt.event.ActionListener;
import java.beans.EventHandler;
import java.io.IOException;
import java.net.*;
import java.util.EventListener;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {
        // write your code here
        Network net = new Network(3333, 3333);
        MensagemController controller = new MensagemController();

        MensagemListener.addListener(mensagem -> {
            System.out.println("Recebeu Mensagem 2: " + mensagem);
        });

        // net.recebeuMensagemListener.setMensagemListener();


        // net.listenManual();
    }
}
