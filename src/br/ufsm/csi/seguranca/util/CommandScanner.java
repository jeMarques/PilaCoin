package br.ufsm.csi.seguranca.util;

import br.ufsm.csi.seguranca.global.Community;
import br.ufsm.csi.seguranca.listeners.PilaCoinListener;

import java.util.Scanner;

public class CommandScanner {
    private Thread scannerThread;

    public CommandScanner() {
        runScannerThread();
    }
    public void runScannerThread() {
        this.scannerThread = new Thread(() -> {
            System.out.println("###########################");
            System.out.println("#                         #");
            System.out.println("#        PilaCoin         #");
            System.out.println("#                         #");
            System.out.println("###########################");
            while (true) {
                try {
                    Scanner scanner = new Scanner(System.in);
                    String fullComand = scanner.nextLine();
                    System.out.println("Comando recebido..");
                    String[] commands = fullComand.split(":");
                    if (commands.length == 3) {
                        String comando = commands[0];

                        if (comando.toLowerCase().equals("t")) {
                            String quantidade = commands[1];
                            String destinatario = commands[2];
                            System.out.println("Transferindo..");
                            System.out.println("Qtde: " + quantidade);
                            System.out.println("ID: " + destinatario);

                            PilaCoinListener.InvocaTransferencia(Integer.parseInt(quantidade),destinatario);
                        }
                    } else if (commands.length == 1) {
                        String comando = commands[0];

                        if (comando.toLowerCase().equals("lu")) {
                            System.out.println("Listando usuarios da comunidade..");
                            Community.showComunnityList();
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Erro Scanner: " + e.getMessage());
                    break;
                }
            }
        });
        this.scannerThread.start();
    }
}
