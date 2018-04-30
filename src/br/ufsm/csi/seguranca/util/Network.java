package br.ufsm.csi.seguranca.util;

import br.ufsm.csi.seguranca.pila.model.Mensagem;

import java.net.*;

/**
 * Created by cpol on 24/04/2018.
 */
public class Network {
    DatagramSocket socket;
    InetAddress address;
    int port;
    public Network() throws UnknownHostException {
        try {
            socket = new DatagramSocket();
            socket.setBroadcast(true);
            address = InetAddress.getByName("255.255.255.255");
            port = 3333;
        } catch (Exception e) {
            System.err.println("Connection failed. " + e.getMessage());
        }
        listenThread();
    }

    public void listenThread() {
        new Thread(() -> {
            while (true) {
                System.out.println("Thread listening..");
                try {
                    byte[] buf = new byte[1500];
                    DatagramSocket sock = new DatagramSocket(3333);
                    DatagramPacket packet = new DatagramPacket(buf,
                            buf.length);
                    sock.receive(packet);
                    Mensagem mensagem = (Mensagem) Conection.deserializeObject(buf);
                    System.out.println("Received: " + mensagem.getTipo());
                    sock.close();
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
//    public void listenManual() {
//            System.out.println("tentando ouvir broadcast");
//            try {
//                byte[] buf = new byte[1500];
//                DatagramPacket packet = new DatagramPacket(buf,
//                        buf.length);
//                socket.receive(packet);
//                Mensagem mensagem = (Mensagem) Conection.deserializeObject(buf);
//                System.out.println("Received: " + mensagem.getErro());
//            } catch (Exception e) {
//                System.err.println(e.getMessage());
//            }
//
//    }

    public void sendMessage(byte[] message) {
        byte[] buf = message;
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
        try {
            socket.send(packet);
        } catch (Exception e) {
            System.err.println("Sending failed. " + e.getMessage());
        }
    }
}