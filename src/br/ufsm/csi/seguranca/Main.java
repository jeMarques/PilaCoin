package br.ufsm.csi.seguranca;

import br.ufsm.csi.seguranca.pila.model.Mensagem;
import br.ufsm.csi.seguranca.util.Conection;
import br.ufsm.csi.seguranca.util.Network;
import br.ufsm.csi.seguranca.util.RSAUtil;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {
        // write your code here
        Mensagem mensagem = new Mensagem();
        mensagem.setPorta(3333);
        mensagem.setEndereco(InetAddress.getLocalHost());
        mensagem.setIdOrigem("Jeferson_Marques");
        mensagem.setTipo(Mensagem.TipoMensagem.DISCOVER);
        mensagem.setChavePublica(RSAUtil.getPublicKey("public_key.der"));
        System.out.println("Sending message..");

        Network net = new Network();
        net.sendMessage(Conection.serializeObject(mensagem));
        System.out.println("Sending message..");
        Thread.sleep(5000);
        net.sendMessage(Conection.serializeObject(mensagem));
        System.out.println("Sending message..");
        Thread.sleep(5000);
        net.sendMessage(Conection.serializeObject(mensagem));
        System.out.println("Sending message..");
        // net.listenManual();
    }
}
