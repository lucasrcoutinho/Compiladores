/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analisador.lexico.backend;

/**
 *
 * @author lucas
 */
public class Token {
    private String lexema;
    private String simbolo;
    private String erro;
    private int linha;
    

    public Token(String lexemaIn, String simboloIn, int linhaIn){
        lexema = lexemaIn;
        simbolo = simboloIn;
        linha = linhaIn;       
    }
    
    public void setLexema(String l){
        lexema = l;
    }
    
    public void setSimbolo(String s){
        simbolo = s;
    }
        
    public void setLinha(int l){
        linha = l;
    }
    
    public void setErro(String e){
        erro = e;
    }
    
    public String getLexema(){
        return lexema;
    }
    
    public String getSimbolo(){
        return simbolo;
    }
    
    public int getLinha(){
        return linha;
    }
   
    public String getErro(){
        return erro;
    }
}
