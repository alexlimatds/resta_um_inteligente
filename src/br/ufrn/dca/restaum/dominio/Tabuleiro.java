/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufrn.dca.restaum.dominio;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Representa um tabuleiro do jogo resta um.
 * 
 * @author Alexandre
 */
public class Tabuleiro {

    private List<Casa> casas = new ArrayList<Casa>();
    private int quantidadeNiveis;
    private int qtdIncialCasasVazias; //quantidade incial de casas vazias

    /**
     * Cria um novo tabuleiro juntamente com suas casas e peças.
     * @param qtdNiveis A quantidade de níveis (linhas) do tabuleiro.
     */
    public Tabuleiro(int qtdNiveis) {
        if (qtdNiveis <= 0) {
            throw new IllegalArgumentException("Quantidade de níveis deve ser positiva.");
        }
        this.quantidadeNiveis = qtdNiveis;
        this.qtdIncialCasasVazias = 1;
        //cria as casas sem peças
        for (int nivel = 0; nivel < quantidadeNiveis; nivel++) {
            for (int posicao = 0; posicao <= nivel; posicao++) {
                Casa c = new Casa(nivel, posicao);
                casas.add(c);
            }
        }
        iniciarJogo();
    }

    public void setQuantidadeInicialDeCasasVazias(int qtd) {
        this.qtdIncialCasasVazias = qtd;
    }

    /**
     * Inicia um novo jogo, ou seja, preenche as casas com peças de forma aleatória.
     */
    public void iniciarJogo() {
        for (Casa c : casas) {
            c.removerPeca();
            Peca p = new Peca();
            c.definirPeca(p);
            p.setCasa(c);
        }
        //remove peças aleatoriamente de acordo com a quantidade incial de casas vazias
        int i = 0;
        while (i < getQtdIncialCasasVazias()) {
            int n = (int) Math.round(Math.random() * (quantidadeNiveis - 1)); //gera um número entre 0 e (quantidadeNiveis - 1)
            int pos = (int) Math.round(Math.random() * (n));//gera um número entre 0 e n
            if (getCasa(n, pos).isOcupada()) {
                getCasa(n, pos).removerPeca();
                i++;
            }
        }
    }

    /**
     * Retorna uma string representando a situação atual do tabuleiro. Cada linha da 
     * sring representa um nível do tabuleiro, enquanto cada letra representa uma casa. 
     * A letra P indica que a casa está ocupada com uma peça, enquanto que a letra 
     * V inidica que a casa está vazia.
     * @return
     */
    public String imprimir() {
        StringBuilder strB = new StringBuilder();
        int nivelAtual = 0;
        for (Casa c : casas) {
            if (c.getNivel() != nivelAtual) {
                strB.append("\n");
                nivelAtual = c.getNivel();
            }
            if (c.isOcupada()) {
                strB.append("P ");
            } else {
                strB.append("V ");
            }
        }
        return strB.toString();
    }

    /**
     * Retorna uma string representando a situação atual do tabuleiro. Cada linha da 
     * sring representa um nível do tabuleiro, enquanto cada letra representa uma casa. 
     * A letra P indica que a casa está ocupada com uma peça, enquanto que a letra 
     * V inidica que a casa está vazia.
     * @return
     */
    public String imprimir2() {
        StringBuilder strB = new StringBuilder();
        int espacos = quantidadeNiveis - 1;
        for (int nivelAtual = 0; nivelAtual < quantidadeNiveis; nivelAtual++) {
            for (int i = 0; i < espacos; i++) {
                strB.append(" ");
            }
            espacos--;
            for (int i = 0; i <= nivelAtual; i++) {
                Casa c = getCasa(nivelAtual, i);
                if (c.isOcupada()) {
                    strB.append("P ");
                } else {
                    strB.append("V ");
                }
            }
            strB.append("\n");
        }
        return strB.toString();
    }

    public List<Casa> getCasas() {
        return casas;
    }

    /**
     * Transfere uma peça de uma casa para a outra. A transferência somente será 
     * bem-sucedida se a casa de origem possuir uma peça, a casa de destino 
     * estiver vazia e o movimento seja válido. Um movimento válido siginifica que 
     * a casa de destino é vizinha, de mesma posição, de uma casa vizinha à origem. 
     * Exemplo: destino é vizinha leste do vizinho leste de origem.
     * @param nivelOrigem    O nível da casa de origem da peça.
     * @param posOrigem      A posição da casa de origem da peça.
     * @param nivelDestino   O nível da casa de destino da peça.
     * @param posDestino     A posição da casa de destino da peça.
     * @return  <code>true</code> caso o movimento seja bem-sucedido e 
     * <code>false</code> em caso contrário.
     */
    public boolean moverPeca(int nivelOrigem, int posOrigem, int nivelDestino,
            int posDestino) {
        Casa origem = getCasa(nivelOrigem, posOrigem);
        Casa destino = getCasa(nivelDestino, posDestino);
        return moverPeca(origem, destino);
    }

    /**
     * Transfere uma peça de uma casa para a outra. A transferência somente será 
     * bem-sucedida se a casa de origem possuir uma peça, a casa de destino 
     * estiver vazia e o movimento seja válido. Um movimento válido siginifica que 
     * a casa de destino é vizinha, de mesma posição, de uma casa vizinha à origem. 
     * Exemplo: destino é vizinha leste do vizinho leste de origem.
     * @param origem    A casa de origem da peça.
     * @param destino   A casa de destino da peça.
     * @return  <code>true</code> caso o movimento seja bem-sucedido e 
     * <code>false</code> em caso contrário.
     */
    public boolean moverPeca(Casa origem, Casa destino) {
        //TODO utilizar método getMovimentosValidos
        if (!origem.isOcupada() || destino.isOcupada()) {
            return false;
        }
        //verifica se destino é vizinho nordeste do vizinho nordeste de origem
        Casa intermediaria = getVizinhoNordeste(origem);
        if (getVizinhoNordeste(intermediaria) == destino && intermediaria.isOcupada()) {
            Peca movida = origem.getPeca();
            origem.removerPeca();
            destino.definirPeca(movida);
            intermediaria.removerPeca(); //peça comida
            return true;
        }
        //idem acima para a posição noroeste
        intermediaria = getVizinhoNoroeste(origem);
        if (getVizinhoNoroeste(intermediaria) == destino && intermediaria.isOcupada()) {
            Peca movida = origem.getPeca();
            origem.removerPeca();
            destino.definirPeca(movida);
            intermediaria.removerPeca(); //peça comida
            return true;
        }
        //idem acima para a posição leste
        intermediaria = getVizinhoLeste(origem);
        if (getVizinhoLeste(intermediaria) == destino && intermediaria.isOcupada()) {
            Peca movida = origem.getPeca();
            origem.removerPeca();
            destino.definirPeca(movida);
            intermediaria.removerPeca(); //peça comida
            return true;
        }
        //idem acima para a posição oeste
        intermediaria = getVizinhoOeste(origem);
        if (getVizinhoOeste(intermediaria) == destino && intermediaria.isOcupada()) {
            Peca movida = origem.getPeca();
            origem.removerPeca();
            destino.definirPeca(movida);
            intermediaria.removerPeca(); //peça comida
            return true;
        }
        //idem acima para a posição sudeste
        intermediaria = getVizinhoSudeste(origem);
        if (getVizinhoSudeste(intermediaria) == destino && intermediaria.isOcupada()) {
            Peca movida = origem.getPeca();
            origem.removerPeca();
            destino.definirPeca(movida);
            intermediaria.removerPeca(); //peça comida
            return true;
        }
        //idem acima para a posição sudoeste
        intermediaria = getVizinhoSudoeste(origem);
        if (getVizinhoSudoeste(intermediaria) == destino && intermediaria.isOcupada()) {
            Peca movida = origem.getPeca();
            origem.removerPeca();
            destino.definirPeca(movida);
            intermediaria.removerPeca(); //peça comida
            return true;
        }
        
        return false;
    }
    
    /**
     * Retorna uma lista com todas as jogadas válidas para a configuração atual do tabuleiro.
     * @return  Uma lista de arrays. Cada array possui 4 elementos distribuídos da seguinte 
     * forma (índice - significado): 0 - nível da origem; 1 - posição da origem no nível; 
     * 2 - nível do destino; 3 - posição do destino no nível;
     */
    public List<Integer[]> getMovimentosValidos() {
        List<Integer[]> movimentos = new ArrayList<Integer[]>();
        for (Casa origem : casas) {
            if (origem.isOcupada()) {
                //LESTE
                Casa vizinho = getVizinhoLeste(origem);
                Casa vizinhoDoVizinho = getVizinhoLeste(vizinho); //vizinhoDoVizinho é o destino
                tratarMovimento(origem, vizinho, vizinhoDoVizinho, movimentos);
                //NORDESTE
                vizinho = getVizinhoNordeste(origem);
                vizinhoDoVizinho = getVizinhoNordeste(vizinho);
                tratarMovimento(origem, vizinho, vizinhoDoVizinho, movimentos);
                //NOROESTE
                vizinho = getVizinhoNoroeste(origem);
                vizinhoDoVizinho = getVizinhoNoroeste(vizinho);
                tratarMovimento(origem, vizinho, vizinhoDoVizinho, movimentos);
                //OESTE
                vizinho = getVizinhoOeste(origem);
                vizinhoDoVizinho = getVizinhoOeste(vizinho);
                tratarMovimento(origem, vizinho, vizinhoDoVizinho, movimentos);
                //SUDESTE
                vizinho = getVizinhoSudeste(origem);
                vizinhoDoVizinho = getVizinhoSudeste(vizinho);
                tratarMovimento(origem, vizinho, vizinhoDoVizinho, movimentos);
                //SUDOESTE
                vizinho = getVizinhoSudoeste(origem);
                vizinhoDoVizinho = getVizinhoSudoeste(vizinho);
                tratarMovimento(origem, vizinho, vizinhoDoVizinho, movimentos);
            }
        }
        return movimentos;
    }
    
    private void tratarMovimento(Casa origem, Casa vizinho, Casa vizinhoDoVizinho, 
            List<Integer[]> movs){
        if(vizinhoDoVizinho != null && vizinho != null && 
                vizinho.isOcupada() && !vizinhoDoVizinho.isOcupada()){
            movs.add(new Integer[]{origem.getNivel(), origem.getPosicao(), 
            vizinhoDoVizinho.getNivel(), vizinhoDoVizinho.getPosicao()});
        }
    }

    /**
     * Indica se duas casas são vizinhas.
     * @param a
     * @param b
     * @return  <code>true</code> caso as casas sejam vizinhas e <code>false</code> 
     *          em caso contrário.
     */
    public boolean isVizinhos(Casa a, Casa b) {
        for (Casa c : getVizinhos(a)) {
            if (c.equals(b)) {
                return true;
            }
        }
        return false;
    }

    public List<Casa> getVizinhos(Casa c) {
        List<Casa> vizs = new ArrayList<Casa>();
        Casa v = getVizinhoLeste(c);
        if (v != null) {
            vizs.add(v);
        }
        v = getVizinhoNordeste(c);
        if (v != null) {
            vizs.add(v);
        }
        v = getVizinhoNoroeste(c);
        if (v != null) {
            vizs.add(v);
        }
        v = getVizinhoOeste(c);
        if (v != null) {
            vizs.add(v);
        }
        v = getVizinhoSudeste(c);
        if (v != null) {
            vizs.add(v);
        }
        v = getVizinhoSudoeste(c);
        if (v != null) {
            vizs.add(v);
        }
        return vizs;
    }

    public Casa getVizinhoNordeste(Casa c) {
        if (c == null) {
            return null;
        }
        int nivel = c.getNivel();
        int pos = c.getPosicao();
        return getCasa(nivel - 1, pos);
    }

    public Casa getVizinhoNoroeste(Casa c) {
        if (c == null) {
            return null;
        }
        int nivel = c.getNivel();
        int pos = c.getPosicao();
        return getCasa(nivel - 1, pos - 1);
    }

    public Casa getVizinhoLeste(Casa c) {
        if (c == null) {
            return null;
        }
        int nivel = c.getNivel();
        int pos = c.getPosicao();
        return getCasa(nivel, pos + 1);
    }

    public Casa getVizinhoOeste(Casa c) {
        if (c == null) {
            return null;
        }
        int nivel = c.getNivel();
        int pos = c.getPosicao();
        return getCasa(nivel, pos - 1);
    }

    public Casa getVizinhoSudeste(Casa c) {
        if (c == null) {
            return null;
        }
        int nivel = c.getNivel();
        int pos = c.getPosicao();
        return getCasa(nivel + 1, pos + 1);
    }

    public Casa getVizinhoSudoeste(Casa c) {
        if (c == null) {
            return null;
        }
        int nivel = c.getNivel();
        int pos = c.getPosicao();
        return getCasa(nivel + 1, pos);
    }

    /**
     * Retorna uma casa do tabuleiro.
     * @param nivel O nível no tabuleiro da casa desejada.
     * @param pos   A posição da casa no nível.
     * @return A casa no nível e posição inidicados ou <code>null</code> caso seja 
     * informada um nível ou posição inexistente no tabuleiro.
     */
    public Casa getCasa(int nivel, int pos) {
        for (Casa c : casas) {
            if (c.getNivel() == nivel && c.getPosicao() == pos) {
                return c;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        Tabuleiro tab = new Tabuleiro(5);
        Scanner scan = new Scanner(System.in);
        System.out.println(tab.imprimir2());
        while (true) {
            System.out.print("Peça a mover (linha): ");
            int linhaP = scan.nextInt();
            System.out.print("\nPeça a mover (coluna): ");
            int colP = scan.nextInt();

            System.out.print("\nLocal de destino (linha): ");
            int linhaD = scan.nextInt();
            System.out.print("\nLocal de destino (coluna): ");
            int colD = scan.nextInt();
            boolean m = tab.moverPeca(linhaP, colP, linhaD, colD);
            System.out.println("\nMovimento: " + m);
            System.out.println(tab.imprimir2());
        }
    }

    public int getQtdIncialCasasVazias() {
        return qtdIncialCasasVazias;
    }
}
