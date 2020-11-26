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
public class Simbolo {
    private String lexema;
    private String tipoLexema;//nome de programa, variável, procedimento, função inteiro, função boolean.    
    private String nivelEscopo;    
    private int memoria;
    private String tipo;//inteiro, booleano.
    private boolean procedimentoCorrente;
    
    
    Simbolo(){
        lexema = "";
        tipoLexema = "";
        tipo = "";
        procedimentoCorrente = false;      
        nivelEscopo = "";    
        memoria = -1;
    }
    //Insere_tabela(token.lexema,”procedimento”,nível, rótulo)
    
    public void addInfo(String lex, String tipoLex, String nivel, int rotulo){
        lexema = lex;
        tipoLexema = tipoLex;
        nivelEscopo = nivel; 
    }
    
    public void setLexema(String lex){
        lexema = lex;
    }
    
    public void setTipoLexema(String tipoLex){
        tipoLexema = tipoLex;
    } 
    
    public void setNivelEscopo(String nivelEsc){
        nivelEscopo = nivelEsc;
    }
    
    public void setMemoria(int mem){
        memoria = mem;
    } 
    
    public void setProcedimentoCorrente(boolean procCorr){
        procedimentoCorrente = procCorr;
    }
    
    public void setTipo(String tipoVar){
        tipo = tipoVar;
    }    
      
    
    
    
    public String getLexema(){
        return  lexema;
    }
    
    public String getTipoLexema(){
        return tipoLexema;
    }
   
    public String getNivelEscopo(){
        return nivelEscopo;
    }
    
    public int getMemoria(){
        return memoria;
    }
    

    
    public boolean getProcedimentoCorrente(){
        return procedimentoCorrente;
    } 

    public String getTipo(){
        return tipo;
    }

}
