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
    int portReceive;
    public Network(int portsend, int portreceive) throws UnknownHostException {
        try {
            socket = new DatagramSocket();
            socket.setBroadcast(true);
            address = InetAddress.getByName("255.255.255.255");
            port = portsend;
            portReceive = portreceive;
        } catch (Exception e) {
            System.err.println("Connection failed. " + e.getMessage());
        }
        listenThread();
        sendDiscover();
    }

    public void listenThread() {
        new Thread(() -> {
            while (true) {
                System.out.println("Thread listening..");
                try {
                    byte[] buf = new byte[1500];
                    DatagramSocket sock = new DatagramSocket(portReceive);
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
    public void sendDiscover() {
        new Thread(() -> {
            while (true) {
                try {
                    System.out.println("Thread listening..");
                    Mensagem mensagem = new Mensagem();
                    mensagem.setPorta(3000);
                    mensagem.setEndereco(InetAddress.getLocalHost());
                    mensagem.setIdOrigem("Jeferson_Marques");
                    mensagem.setTipo(Mensagem.TipoMensagem.DISCOVER);
                    mensagem.setChavePublica(RSAUtil.getPublicKey("public_key.der"));
                    byte[] buf = Conection.serializeObject(mensagem);
                    DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
                    socket.send(packet);
                } catch (Exception e) {
                    System.err.println("Sending failed. " + e.getMessage());
                }
                try {
                    Thread.sleep(15000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }

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