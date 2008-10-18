/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ufrn.dca.restaum.dominio;

/**
 *
 * @author Alexandre
 */
public class Casa {
    
    private Peca peca;
    private int nivel;
    private int posicao;
    //private JLabel ouvinte;

    public Casa(int nivel, int posicao) {
        this.nivel = nivel;
        this.posicao = posicao;
    }

    public boolean isOcupada() {
        if(getPeca() != null){
            return true;
        }
        return false;
    }

    public int getNivel() {
        return nivel;
    }

    public int getPosicao() {
        return posicao;
    }

    public Peca getPeca() {
        return peca;
    }

    public void definirPeca(Peca peca) {
        if(this.peca != null){
            throw new IllegalArgumentException("A casa já possui uma peça.");
        }
        this.peca = peca;
        peca.setCasa(this);
    }
    
    public void removerPeca(){
        if(peca != null){
            peca.setCasa(null);
            this.peca = null;
        }
    }

    /*public OuvinteCasa getOuvinte() {
        return ouvinte;
    }

    public void setOuvinte(OuvinteCasa ouvinte) {
        this.ouvinte = ouvinte;
        if(peca != null){
            ouvinte.pecaInserida();
        }
        else{
            ouvinte.pecaRemovida();
        }
    }*/
}