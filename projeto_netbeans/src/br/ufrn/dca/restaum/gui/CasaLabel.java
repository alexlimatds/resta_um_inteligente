/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ufrn.dca.restaum.gui;

import br.ufrn.dca.restaum.dominio.Casa;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JLabel;

/**
 *
 * @author Alexandre
 */
public class CasaLabel extends JLabel{
    
    private Casa casa;
    private boolean selecionado;
    
    public CasaLabel(Casa c){
        super();
        this.casa = c;
        setBackground(Color.BLUE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        //desenha a peça
        if(getCasa().isOcupada()){
            g2.setColor(Color.RED);
        }
        else{
            g2.setColor(Color.WHITE);
        }
        g2.fillOval(1, 1, 49, 49);
        
        g2.dispose();
    }

    public Casa getCasa() {
        return casa;
    }

    public boolean isSelecionado() {
        return selecionado;
    }

    public void setSelecionado(boolean selecionado) {
        this.selecionado = selecionado;
        setOpaque(selecionado);
        repaint();
    }
    
}
