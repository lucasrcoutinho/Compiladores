/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analisador.lexico.backend;

import analisador.sintatico.AnalisadorSintatico;
import java.util.ArrayList;

/**
 *
 * @author lucas
 */
public class Facade{
    private static Facade instancia;
    //AnalisadorLexico analisadorLexico = new AnalisadorLexico();
    ProcessaArquivos processaArquivos = new ProcessaArquivos(); 

    //Ler token conforme requisitado pelo sintatico
    AnalisadorSintatico sintatico = new AnalisadorSintatico();    
    
    public static synchronized Facade getInstance(){
        if(instancia == null){
            instancia = new Facade();
        }        
        return instancia;
    }

    public String chamaSintatico(String caminho, String nomeArquivo){
        try{
            sintatico.inicioSintatico(caminho, nomeArquivo);    
        }catch(Exception e){
            return e.getMessage();
        }
        return "Compilado com sucesso!";    
    }
    
    public void salvarCodigo(String codgigoFonte, String caminho){
        processaArquivos.salvarCodigo(codgigoFonte, caminho);    
    }
    
    public ArrayList getArquivoFonte(String caminho){
        return processaArquivos.getPrograma(caminho);
    }

}
