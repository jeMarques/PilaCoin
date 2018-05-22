package br.ufsm.csi.seguranca.util;

import br.ufsm.csi.seguranca.crypto.AES;
import br.ufsm.csi.seguranca.global.Me;
import br.ufsm.csi.seguranca.global.Server;
import br.ufsm.csi.seguranca.listeners.MensagemListener;
import br.ufsm.csi.seguranca.listeners.PilaCoinListener;
import br.ufsm.csi.seguranca.pila.model.Mensagem;
import br.ufsm.csi.seguranca.pila.model.ObjetoTroca;
import br.ufsm.csi.seguranca.pila.model.PilaCoin;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

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
                    break;
                }
            }
        });
        this.listen.start();
    }
    public void sendDiscover() {
        this.sendDiscover = new Thread(() -> {
            while (true) {
                try {
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

    public static  void exchangeTroca(ObjetoTroca troca, AES aesSession) throws Exception {
        Socket conexao;
        if (Server.TCPAddress==null){
            conexao = new Socket("5.189.186.225", Server.PORT);
        } else {
            conexao =  new Socket(Server.TCPAddress, Server.PORT);
        }
        ObjectOutputStream out = new ObjectOutputStream(conexao.getOutputStream());
        out.writeObject(troca);

        ObjectInputStream objIn = new ObjectInputStream(conexao.getInputStream());
        Object o = objIn.readObject();
        if (o instanceof Mensagem) {
            Mensagem retorno = (Mensagem)o;
            System.out.println("RECEBEU Mensagem TCP (Erro): " + retorno.getErro());
        } else if (o instanceof ObjetoTroca) {
            ObjetoTroca retorno = (ObjetoTroca)o;
            PilaCoinListener.InvocaValidaObjetoTroca(troca, aesSession);
        }
        conexao.close();
    }
}