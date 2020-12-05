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
    
    //Provisorio para testar o sintatico, não pode ficar assim!!!
        //Necessario fazer a abertura do arquivo dentro do sintatico
        //Ler token conforme requisitado pelo sintatico
    AnalisadorSintatico sintatico = new AnalisadorSintatico();
    
    
    public static synchronized Facade getInstance(){
        if(instancia == null){
            instancia = new Facade();
        }        
        return instancia;
    }
    
    public String getErroSintatico(){
        //return analisadorLexico.getErro();  
        return "";
    }

    public String chamaSintatico(String caminho){
        try{
            sintatico.inicioSintatico(caminho);    
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
