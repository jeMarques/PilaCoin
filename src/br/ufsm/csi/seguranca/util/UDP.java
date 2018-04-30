package br.ufsm.csi.seguranca.util;

import br.ufsm.csi.seguranca.pila.model.Mensagem;

import java.beans.EventHandler;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.EventListener;

/**
 * Created by cpol on 19/04/2018.
 */
public class UDP {
    private static DatagramSocket socket = null;
    private static int port = 3333;
    private static boolean running = false;

    //    broadcast("Hello", InetAddress.getByName("255.255.255.255"));

    public static void Start() throws SocketException {
        socket = new DatagramSocket();
        socket.setBroadcast(true);
        running = true;
    }

    public static void broadcast(Mensagem broadcastMessage, InetAddress address) throws IOException {
        if (!running) Start();
        byte[] buffer = Conection.serializeObject(broadcastMessage);
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, getPort());
        socket.send(packet);
    }
    public static Mensagem receive(InetAddress address) throws IOException, ClassNotFoundException {
        if (!running) Start();
        byte[] buffer = new byte[10000];
 //       DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, getPort());
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        socket.receive(packet);

        return (Mensagem) Conection.deserializeObject(buffer);
    }

    public static int getPort() {
        return port;
    }

    public static void setPort(int port) {
        UDP.port = port;
    }

    public static void Stop() {
        socket.close();
        running = false;
    }
}
