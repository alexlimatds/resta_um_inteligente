/*
 * PnlRestaUmDinamico.java
 *
 * Created on 18 de Outubro de 2008, 12:12
 */

package br.ufrn.dca.restaum.gui;

import br.ufrn.dca.restaum.ar.Util;
import br.ufrn.dca.restaum.dominio.Casa;
import br.ufrn.dca.restaum.dominio.Tabuleiro;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;

/**
 *
 * @author  Alexandre
 */
public class PnlRestaUmDinamico extends javax.swing.JPanel{
    
    private int qtdNiveis;
    private int timeStep = 500; //delay do piloto automático
    private Tabuleiro tabuleiro;
    private CasaLabel[] labels;
    private CasaLabelListener listener;
    private CasaLabel casaSelecionada;
    
    public PnlRestaUmDinamico(){
        this(5);
    }
    
    /** Creates new form PnlRestaUmDinamico */
    public PnlRestaUmDinamico(int qtdNiveis) {
        this.qtdNiveis = qtdNiveis;
        tabuleiro = new Tabuleiro(qtdNiveis);
        listener = new CasaLabelListener();
        initComponents();
        montarTabuleiro();
    }
    
    public void novoJogo(){
        getTabuleiro().iniciarJogo();
        repaint();
    }
    
    /**
     * Inicia o piloto automático. Isto significa que o computador passa a 
     * jogar por conta própria o tabuleiro atual. As jogadas são feitas com 
     * base na política gerada pelo algoritmo de aprendizagem por reforço.
     */
    public void jogarAutomaticamente(){
        try{
            Map<String, String> politica = null;
            if(qtdNiveis == 5){
                politica = Util.lerPolitica("politica-5niveis.txt");
            }
            else if(qtdNiveis == 6){
                politica = Util.lerPolitica("politica-6niveis.txt");
            }
            else{
                throw new IllegalStateException("Sem política definida para esta quantidade de níveis");
            }
            String estado = tabuleiro.getRepresentacaoBinaria();
            String acao = politica.get(estado);
            while(acao != null){
                int posEspaco = acao.indexOf(" ");
                int numCasaOrigem = Integer.parseInt( acao.substring(0, posEspaco) ) - 1;
                CasaLabel origem = labels[numCasaOrigem];
                origem.setSelecionado(true);
                paintChildren(getGraphics());
                Thread.sleep(timeStep);
                tabuleiro.moverPeca(acao);
                origem.setSelecionado(false);
                update(getGraphics());
                paintChildren(getGraphics());
                repaint();
                Thread.sleep(timeStep);
                estado = tabuleiro.getRepresentacaoBinaria();
                acao = politica.get(estado);
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    //cria e posiciona os labels que representam as casas do tabuleiro
    private void montarTabuleiro(){
        //calcula tamanho deste panel
        int w = (qtdNiveis - 1) * 116 + 60; //60 -> 30 margem direita e 30 margem esquerda
        int h = (qtdNiveis - 1) * 100 + 60;
        setSize(w, h);
        
        //cria e configura labels
        w = 52; 
        h = 52; //tamanho dos labels
        labels = new CasaLabel[tabuleiro.getCasas().size()];
        int metadeDHorizontal = 58;
        int dEntreNiveis = 100;
        int xInicio = 30 + metadeDHorizontal * (qtdNiveis - 1);
        int yInicio = 30;
        int xLabel = xInicio;
        int yLabel = yInicio;
        int i = 0;
        for(int nivel = 0; nivel < qtdNiveis; nivel++){
            for(int pos = 0; pos <= nivel; pos++){
                labels[i] = new CasaLabel(getTabuleiro().getCasa(nivel, pos));
                labels[i].setSize(w, h);
                labels[i].setLocation(xLabel, yLabel);
                labels[i].addMouseListener(listener);
                add(labels[i]);
                i++;
                xLabel += metadeDHorizontal * 2;
            }
            xInicio -= metadeDHorizontal;
            yInicio += dEntreNiveis;
            xLabel = xInicio;
            yLabel = yInicio;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        //desenha as linas que unem as casas do tabuleiro
        if(labels != null){
            Graphics2D g2 = (Graphics2D)g;
            for(CasaLabel lb : labels){
                List<Casa> vizinhos = getTabuleiro().getVizinhos(lb.getCasa());
                for(Casa casaVizinha : vizinhos){
                    CasaLabel lbVizinho = getLabel(casaVizinha);
                    //desenha linha entre o label atual e o seu vizinho
                    g.drawLine(lb.getX() + lb.getWidth() / 2, lb.getY() + lb.getHeight() / 2, 
                            lbVizinho.getX() + lbVizinho.getWidth() / 2, lbVizinho.getY() + lbVizinho.getHeight() / 2);
                }
            }
        }
    }
    
    /**
     * Retorna a instância de <code>CasaLabel</code> associada com uma determinada 
     * casa do tabuleiro.
     * @param c A casa do tabuleiro que se desej obter o Label associado.
     * @return
     */
    private CasaLabel getLabel(Casa c){
        for(CasaLabel lb : labels){
            if(lb.getCasa() == c){
                return lb;
            }
        }
        return null;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 655, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 519, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    public Tabuleiro getTabuleiro() {
        return tabuleiro;
    }

    public int getTimeStep() {
        return timeStep;
    }

    public void setTimeStep(int timeStep) {
        this.timeStep = timeStep;
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    
    class CasaLabelListener extends MouseAdapter{

        @Override
        public void mouseClicked(MouseEvent e) {
            if(e.getSource() instanceof CasaLabel){
                CasaLabel c = (CasaLabel)e.getSource();
                if(casaSelecionada != null){ //já há casa selecionada
                    getTabuleiro().moverPeca(casaSelecionada.getCasa(), c.getCasa());
                    casaSelecionada.setSelecionado(false);
                    casaSelecionada = null;
                }
                else{
                    c.setSelecionado(true);
                    casaSelecionada = c;
                }
                repaint();
            }
            /*for (Integer[] array : getTabuleiro().getMovimentosValidos()) {
                System.out.println("----");
                System.out.print("[");
                for (Integer integer : array) {
                    System.out.print(integer.intValue() + " ");
                }
                System.out.print("]");
            }*/
        }
    }
    
}
