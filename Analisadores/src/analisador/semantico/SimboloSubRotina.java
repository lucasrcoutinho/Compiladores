/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analisador.semantico;

/**
 *
 * @author lucas
 */
public class SimboloSubRotina{
    private boolean procedimentoCorrente;
    
    public SimboloSubRotina(){
        procedimentoCorrente = false;
    }
    
    public void setProcedimentoCorrente(boolean procCorr){
        procedimentoCorrente = procCorr;
    }
    
    public boolean getProcedimentoCorrente(){
        return procedimentoCorrente;
    } 
}
