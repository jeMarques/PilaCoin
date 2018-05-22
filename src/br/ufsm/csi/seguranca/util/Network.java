package br.ufsm.csi.seguranca.util;

import br.ufsm.csi.seguranca.global.Me;
import br.ufsm.csi.seguranca.listeners.MensagemListener;
import br.ufsm.csi.seguranca.pila.model.Mensagem;

import java.net.*;

/**
 * Created by cpol on 24/04/2018.
 */
public class Network {
    private DatagramSocket socket;
    private InetAddress address;
    private int port;
    private int portReceive;

    private Thread listen;
    private Thread sendDiscover;

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
        this.listen = new Thread(() -> {
            while (true) {
                System.out.println("Thread listening..");
                try {
                    byte[] buf = new byte[1500];
                    DatagramSocket sock = new DatagramSocket(portReceive);
                    DatagramPacket packet = new DatagramPacket(buf,
                            buf.length);
                    sock.receive(packet);
                    Mensagem mensagem = (Mensagem) Conection.deserializeObject(buf);
                    MensagemListener.RecebeuMensagem(mensagem);
                    sock.close();
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        });
        this.listen.start();
    }
    public void sendDiscover() {
        this.sendDiscover = new Thread(() -> {
            while (true) {
                try {
                    System.out.println("Sending discover..");
                    Mensagem mensagem = new Mensagem();
                    mensagem.setPorta(this.portReceive);
                    mensagem.setEndereco(InetAddress.getLocalHost());
                    mensagem.setIdOrigem(Me.myIDOrigem);
                    mensagem.setTipo(Mensagem.TipoMensagem.DISCOVER);
                    mensagem.setChavePublica(Me.MyPubKey());
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
        });
        this.sendDiscover.start();

    }
}