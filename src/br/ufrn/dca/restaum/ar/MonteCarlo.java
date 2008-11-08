/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ufrn.dca.restaum.ar;

import br.ufrn.dca.restaum.dominio.Tabuleiro;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Alexandre
 */
public class MonteCarlo {
    private Tabuleiro tabuleiro;
    Map<String, TabelaValorEstadoAcao> tabelaQ;

    public MonteCarlo(Tabuleiro tabuleiro) {
        this.tabuleiro = tabuleiro;
    }
    
    public Map<String, String> treinar(int qtdEpisodios){
        Map<String, String> politica = new LinkedHashMap<String, String>(); //política gerada ao final
        tabelaQ = new LinkedHashMap<String, TabelaValorEstadoAcao>(); //valores dos pares estado-ação. a chave é o estado
        Map<String, List<Double>> retornosEstadoAcao = new HashMap<String, List<Double>>(); //listas de retornos dos pares estado-ação. a chave é a junção das string de estado e ação separadas por ponto e vírgula
        
        //utilizado para indicar se um par estado-ação já ocorreu no episódio. a chave é a junção das string de estado e ação separadas por ponto e vírgula
        Map<String, Boolean> ocorrido = new HashMap<String, Boolean>();
        for(int i = 0; i < qtdEpisodios; i++){
            int maxCasasVazias = getMaxCasasVazias();
            int qtdCasasVazias = (int) Math.round(Math.random() * maxCasasVazias);//gera um número entre 0 e maxCasasVazias
            tabuleiro.setQuantidadeInicialDeCasasVazias(qtdCasasVazias);
            tabuleiro.iniciarJogo();
            List<String> acoes = tabuleiro.getMovimentosValidos2();
            ocorrido.clear(); //limpa as aparições dos pares estado-ação para o novo episódio
            while(acoes.size() > 0){
                String estado = tabuleiro.getRepresentacaoBinaria();
                String acao = null;
                if(politica.containsKey(estado)){ //escolhe a ação de acordo com a política
                    acao = politica.get(estado);
                }
                else{ //não há ação definida para o estado de acordo com a política, então usa qualquer ação
                    acao = acoes.get(0);
                }
                boolean ok = tabuleiro.moverPeca(acao);
                if(ok){//ação realizada
                    //verifica se é a primeira ocorrência deste par estado-ação no episódio
                    String key = estado + ";" + acao;
                    if(!ocorrido.containsKey(key)){
                        ocorrido.put(key, ok);
                        double valorRetorno = getRetorno(acao);
                        List<Double> listaRetornos = retornosEstadoAcao.get(key);
                        if(listaRetornos != null){//verifica se já há uma lista de retornos para o par estado-ação
                            retornosEstadoAcao.get(key).add(valorRetorno);
                        }
                        else{//não há lista de retornos para o par estado-ação, então a cria e insere o valor de retorno
                            listaRetornos = new ArrayList<Double>();
                            listaRetornos.add(valorRetorno);
                            retornosEstadoAcao.put(key, listaRetornos);
                        }
                        //calcula a média dos retornos do par estado-ação
                        double media = 0;
                        for(Double d : listaRetornos){
                            media += d;
                        }
                        media = media / listaRetornos.size();
                        //verifica se já há uma tabela de valores para o par estado-ação
                        TabelaValorEstadoAcao tabela = tabelaQ.get(estado);
                        if(tabela == null){
                            tabela = new TabelaValorEstadoAcao(estado);
                            tabelaQ.put(estado, tabela);
                        }
                        tabela.inserirValor(acao, media);
                        //atualiza a política com a ação com maior valor Q
                        politica.put(estado, tabela.getAcaoValorMaximo());
                    }
                }
                else{//ação não realizada, o que significa que há algum erro de implementação
                    System.out.println("Ação não realizada -> existe algum BUG!!");
                }
                
                acoes = tabuleiro.getMovimentosValidos2();
            }
        }
        
        return politica;
    }
    
    private int getMaxCasasVazias(){
        if(tabuleiro.getQuantidadeNiveis() < 3){
            return 1;
        }
        else{
            return tabuleiro.getCasas().size() - 2;
        }
    }
    
    /**
     * Retorna o valor de retorno de uma ação.
     * @param acao  ação que levou ao estado atual do tabuleiro.
     * @return
     */
    public double getRetorno(String acao){
        String estado = tabuleiro.getRepresentacaoBinaria();
        int qtdVazias = 0;
        for(char c : estado.toCharArray()){
            if(c == '0'){
                qtdVazias += 1;
            }
        }
        if(qtdVazias == (estado.length() - 1)){
            return qtdVazias * 100;
        }
        return qtdVazias * 2;
    }
    
    public Map<String, TabelaValorEstadoAcao> getTabelaValorEstadoAcao(){
        return tabelaQ;
    }
    
    public static void main(String[] args) {
        FileWriter fWriter = null;
        try {
            int qtdEpisodios = 300000;
            int qtdNiveis = 5;
            Tabuleiro t = new Tabuleiro(qtdNiveis);
            MonteCarlo mc = new MonteCarlo(t);
            Map<String, String> politicaGerada = mc.treinar(qtdEpisodios);
            
            //gera arquivo da política
            fWriter = new FileWriter("pol-" + qtdNiveis + "niveis-" + System.currentTimeMillis() + ".txt");
            for (String key : politicaGerada.keySet()) {
                fWriter.write(key + " " + politicaGerada.get(key) + "\n");
            }
            fWriter.write("\n");
            fWriter.write(qtdEpisodios + " episódios\n");
            fWriter.close();
            
            //gera arquivo da tabela de pares estado-ação
            Map<String, TabelaValorEstadoAcao> table = mc.getTabelaValorEstadoAcao();
            fWriter = new FileWriter("tabela-estado-acao.txt");
            for (String key : table.keySet()) {
                TabelaValorEstadoAcao ta = table.get(key);
                String acao = ta.getAcaoValorMaximo();
                fWriter.write(key + " " + ta.getValor(acao) + "\n");
            }
            fWriter.write("\n");
            fWriter.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
