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
public class SimboloVariavel extends Simbolo{
    private String tipo;//inteiro, booleano.
    
    
    SimboloVariavel(){
        tipo = "";
    }
    
    public void setTipoVariavel(String tipoVar){
        tipo = tipoVar;
    }

    public String getTipoVariavel(){
        return tipo;
    }    
}
