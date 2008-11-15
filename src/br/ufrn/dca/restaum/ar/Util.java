/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ufrn.dca.restaum.ar;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Alexandre
 */
public class Util {
    public static Map<String, String> lerPolitica(String fileName) throws FileNotFoundException, 
            IOException{
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        Map<String, String> politica = new HashMap<String, String>();
        String line = reader.readLine();
        while(line != null && !line.trim().equals("") ){
            int posEspaco1 = line.indexOf(" ");
            String estado = line.substring(0, posEspaco1);
            String acao = line.substring(posEspaco1 + 1);
            politica.put(estado, acao);
            line = reader.readLine();
        }
        return politica;
    }
    
    /**
     * Retorna a quantidade de casas vazias de um tabuleiro
     * @param estado Representação binária de um tabuleiro.
     * @return
     */
    public static int contaCasasVazias(String estado){
        int count = 0;
        for(char c : estado.toCharArray()){
            if(c == '1'){
                count++;
            }
        }
        return count;
    }
}
