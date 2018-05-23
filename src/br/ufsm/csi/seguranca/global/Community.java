package br.ufsm.csi.seguranca.global;

import br.ufsm.csi.seguranca.server.model.Usuario;

import java.util.ArrayList;

public class Community {
    private static ArrayList<Usuario> usuarios = new ArrayList<Usuario>();


    public static void addOrUpdateUser(Usuario user) {
        if (!usuarios.contains(user)) {
            usuarios.add(user);
        } else {
            usuarios.set(usuarios.indexOf(user), user);
        }
    }

    public static void showComunnityList() {
        for (Usuario user : usuarios) {
            System.out.println("________");
            System.out.println("ID: " + user.getId());
            System.out.println("Endereco: " + user.getEndereco());
        }
    }
}
