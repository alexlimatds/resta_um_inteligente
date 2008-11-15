/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ufrn.dca.restaum.ar;

import br.ufrn.dca.restaum.dominio.Tabuleiro;
import java.util.Map;

/**
 *
 * @author Alexandre
 */
public class Teste6Niveis {
    
    public static void main(String[] args) {
        try{
            int qtdJogos = 2000;
            //bateria de testes com quantidade inicial de casas vazias aleatória
            testar(-1, qtdJogos, false);

            //baterias de testes com quantidade inicial fixa de casas vazias
            testar(1, qtdJogos, false);
            testar(2, qtdJogos, false);
            testar(3, qtdJogos, false);
            testar(7, qtdJogos, false);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    /**
     * 
     * @param qtdInicialCasasVazias Informe um valor menor ou igual a zero para gerar um número aleatório a cada jogo.
     * @param qtdJogos
     */
    public static void testar(int qtdInicialCasasVazias, int qtdJogos, boolean mostrarTabuleiro) {
        try{
            //lê a política
            Map<String, String> politica = Util.lerPolitica("politica-6niveis.txt");
            
            //guarda a quantidade de jogos com determinado resto: índice zero -> jogos com resta um, índice um -> jogos com resta dois e assim por diante
            int[] restosDosJogos = new int[21];
            Tabuleiro tab = new Tabuleiro(6);
            int qtdCasasVazias = qtdInicialCasasVazias;
            for(int i = 0; i < qtdJogos; i++){
                if(qtdInicialCasasVazias <= 0){
                    qtdCasasVazias = (int) Math.round(1 + Math.random() * 19);//gera um número entre 1 e 19
                }
                tab.setQuantidadeInicialDeCasasVazias(qtdCasasVazias);
                tab.iniciarJogo();
                if(mostrarTabuleiro){
                    System.out.println(tab.imprimir2());
                }
                String estado = tab.getRepresentacaoBinaria();
                String acao = politica.get(estado);
                while(acao != null){
                    tab.moverPeca(acao);
                    estado = tab.getRepresentacaoBinaria();
                    acao = politica.get(estado);
                }
                int casasVaziasAoFinal = Util.contaCasasVazias(estado);
                restosDosJogos[casasVaziasAoFinal - 1]++;
                if(mostrarTabuleiro){
                    System.out.println(tab.imprimir2());
                }
            }
            System.out.println("** RESULTADOS **");
            System.out.println("Total de jogos realizados: " + qtdJogos);
            System.out.println("Quantidade inicial de casas vazias: " + qtdInicialCasasVazias);
            for(int i = 0; i < restosDosJogos.length; i++) {
                int qtd = restosDosJogos[i];
                System.out.println("Jogos com resta " + (i + 1) + ": " + qtd);
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }

}
