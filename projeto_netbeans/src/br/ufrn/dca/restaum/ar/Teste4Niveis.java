/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ufrn.dca.restaum.ar;

import br.ufrn.dca.restaum.dominio.Tabuleiro;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Alexandre
 */
public class Teste4Niveis {
    
    public static void main(String[] args) {
        try{
            //lê a política
            BufferedReader reader = new BufferedReader(new FileReader("politica-4niveis.txt"));
            Map<String, String> politica = new HashMap<String, String>();
            String line = reader.readLine();
            while(line != null && !line.trim().equals("") ){
                int posEspaco1 = line.indexOf(" ");
                String estado = line.substring(0, posEspaco1);
                String acao = line.substring(posEspaco1 + 1);
                politica.put(estado, acao);
                line = reader.readLine();
            }
            
            Tabuleiro tab = new Tabuleiro(4);
            System.out.println(tab.imprimir2());
            String estado = tab.getRepresentacaoBinaria();
            String acao = politica.get(estado);
            while(acao != null){
                tab.moverPeca(acao);
                System.out.println("JOGADA: " + acao);
                System.out.println(tab.imprimir2());
                estado = tab.getRepresentacaoBinaria();
                acao = politica.get(estado);
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }

}
