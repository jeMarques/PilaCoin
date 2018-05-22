package br.ufsm.csi.seguranca.util;

import javax.swing.*;
import java.io.*;

public class File implements Serializable {
    public byte[] file = null;
    private String path = null;
    public String nome = "";
    public File(String path) throws IOException {
        this.path = path;
        java.io.File arquivo = new java.io.File(this.path);
        if (arquivo.exists()) {
            FileInputStream fin = new FileInputStream(arquivo);
            byte[] barquivo = new byte[(int) fin.getChannel().size()];
            fin.read(barquivo);
            this.file = barquivo;
        }

    }
    public File() throws IOException {
        JFileChooser chooserArquivo = new JFileChooser();
        int escolha = chooserArquivo.showOpenDialog(new JFrame());
        if (escolha == JFileChooser.APPROVE_OPTION) {
            this.path = chooserArquivo.getSelectedFile().getAbsolutePath();
            this.nome = chooserArquivo.getSelectedFile().getName();
            java.io.File arquivo = new java.io.File(this.path);
            FileInputStream fin = new FileInputStream(arquivo);
            byte[] barquivo = new byte[(int) fin.getChannel().size()];
            fin.read(barquivo);
            this.file = barquivo;
        }
    }
    public File(byte[] arquivo_array) throws IOException {
        this.file = arquivo_array;
    }


    public void savetoPath(String path) throws IOException {
        java.io.File saida = new java.io.File(path);
        OutputStream fout = new FileOutputStream(saida);
        fout.write(file);
        fout.close();
    }
    public void onlySave() throws IOException {
        java.io.File saida = new java.io.File(this.path);
        OutputStream fout = new FileOutputStream(saida);
        fout.write(file);
        fout.close();
    }
}
