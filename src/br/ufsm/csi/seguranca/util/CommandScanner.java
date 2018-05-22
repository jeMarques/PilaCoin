package br.ufsm.csi.seguranca.util;

import br.ufsm.csi.seguranca.listeners.MensagemListener;
import br.ufsm.csi.seguranca.pila.model.Mensagem;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Scanner;

public class CommandScanner {
    private Thread scannerThread;

    public CommandScanner() {
        runScannerThread();
    }
    public void runScannerThread() {
        this.scannerThread = new Thread(() -> {
            while (true) {
                try {
                    System.out.println("###########################");
                    System.out.println("#                         #");
                    System.out.println("#        PilaCoin         #");
                    System.out.println("#                         #");
                    System.out.println("###########################");
                    Scanner scanner = new Scanner(System.in);
                    String comando = scanner.nextLine();

                } catch (Exception e) {
                    System.err.println(e.getMessage());
                    break;
                }
            }
        });
        this.scannerThread.start();
    }
}
