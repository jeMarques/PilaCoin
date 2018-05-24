package br.ufsm.csi.seguranca.util;

import br.ufsm.csi.seguranca.crypto.AES;
import br.ufsm.csi.seguranca.global.Me;
import br.ufsm.csi.seguranca.global.Server;
import br.ufsm.csi.seguranca.listeners.MensagemListener;
import br.ufsm.csi.seguranca.listeners.NetworkListener;
import br.ufsm.csi.seguranca.listeners.PilaCoinListener;
import br.ufsm.csi.seguranca.pila.model.Mensagem;
import br.ufsm.csi.seguranca.pila.model.MensagemFragmentada;
import br.ufsm.csi.seguranca.pila.model.ObjetoTroca;
import br.ufsm.csi.seguranca.pila.model.PilaCoin;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
            NetworkListener.addListener(this::sendTransfer);
        } catch (Exception e) {
            System.err.println("Connection failed. " + e.getMessage());
        }
        listenThread();
        sendDiscover();
    }

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
    public void listenThread() {
        this.listen = new Thread(() -> {
            while (true) {
                DatagramSocket sock = null;
                try {
                    byte[] buf = new byte[3500];
                    sock = new DatagramSocket(portReceive);
                    DatagramPacket packet = new DatagramPacket(buf,
                            buf.length);
                    sock.receive(packet);
                    try {
                        Mensagem mensagem = (Mensagem) Conection.deserializeObject(buf);
                        MensagemListener.RecebeuMensagem(mensagem);
                    } catch(Exception e) {
                        MensagemFragmentada mensagemFragmentada = (MensagemFragmentada) Conection.deserializeObject(buf);
                        System.out.println("[Recebeu Mensagem Fragmentada] Ultimo? " + mensagemFragmentada.isUltimo());
                        System.out.println("[Recebeu Mensagem Fragmentada] Cont? " + mensagemFragmentada.getSequencia());

                        if (!mensagemFragmentada.isUltimo()) {
                            outputStream.write( mensagemFragmentada.getFragmento() );
                        } else {
                            outputStream.write( mensagemFragmentada.getFragmento() );
                            Mensagem mensagem = (Mensagem) Conection.deserializeObject(outputStream.toByteArray());
                            MensagemListener.RecebeuMensagem(mensagem);
                            outputStream = new ByteArrayOutputStream();
                        }
                    }
                    sock.close();
                } catch (Exception e) {
                    outputStream = new ByteArrayOutputStream();
                    if (sock!=null) sock.close();
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    e.printStackTrace(pw);
                    String sStackTrace = sw.toString(); // stack trace as a string
                    System.err.println("Listen Error: " + e.getMessage());
                    System.err.println(sStackTrace);

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

    public void sendTransfer(PilaCoin pila) {
        try {
            System.out.println("Transfering trougt udp...");
            Mensagem mensagem = new Mensagem();
            mensagem.setPorta(this.portReceive);
            mensagem.setEndereco(InetAddress.getLocalHost());
            mensagem.setIdOrigem(Me.myIDOrigem);
            mensagem.setTipo(Mensagem.TipoMensagem.PILA_TRANSF);
            mensagem.setChavePublica(Me.MyPubKey());
            mensagem.setPilaCoin(pila);


            byte[] buf = Conection.serializeObject(mensagem);
            int cont = 0;
            byte[][] fragmentos = divideArray(buf,1000);
            for (byte[] frag : fragmentos) {
                MensagemFragmentada mensfrag = new MensagemFragmentada();
                mensfrag.setSequencia(cont);
                mensfrag.setFragmento(frag);
                cont++;
                mensfrag.setUltimo(fragmentos.length == cont);
                DatagramPacket packet = new DatagramPacket( Conection.serializeObject(mensfrag),  Conection.serializeObject(mensfrag).length, address, port);
                socket.send(packet);
                Thread.sleep(0);
            }

            System.out.println("UDP Send");

        } catch (Exception e) {
            System.err.println("Sending failed. " + e.getMessage());
        }

    }

    //t:1:meet@alanwgt.com
    ///t:1:Glenio

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

    public static byte[][] divideArray(byte[] source, int chunksize) {


        byte[][] ret = new byte[(int)Math.ceil(source.length / (double)chunksize)][chunksize];

        int start = 0;

        for(int i = 0; i < ret.length; i++) {
            ret[i] = Arrays.copyOfRange(source,start, start + chunksize);
            start += chunksize ;
        }

        return ret;
    }
}