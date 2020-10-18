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
public class Facade {
    
    //AnalisadorLexico analisadorLexico = new AnalisadorLexico();
    LeArquivo leArquivo = new LeArquivo(); 
    
    //Provisorio para testar o sintatico, não pode ficar assim!!!
        //Necessario fazer a abertura do arquivo dentro do sintatico
        //Ler token conforme requisitado pelo sintatico
    AnalisadorSintatico sintatico = new AnalisadorSintatico();     

    
    public String getErroSintatico()
    {
        //return analisadorLexico.getErro();  
        return sintatico.getErro();
    }

    
    //Provisorio para testar o sintatico, não pode ficar assim!!!
    public void chamaSintatico(String caminho){
        sintatico.inicioSintatico(caminho);
        
    }
    
    public ArrayList getArquivoFonte(String caminho){
        return leArquivo.getPrograma(caminho);
    }   
}
