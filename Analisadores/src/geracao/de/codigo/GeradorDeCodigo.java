/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geracao.de.codigo; 

import analisador.lexico.backend.ProcessaArquivos;

/**
 *
 * @author lucas
 */
public class GeradorDeCodigo{
    String codigoGerado;
    ProcessaArquivos salva;

    public GeradorDeCodigo(){
        codigoGerado = "";
        salva = new ProcessaArquivos("Gravacao");
    }

    public void codigo(String comando){
        codigoGerado += comando + "\n";
    }
    
    public void salvaCodigo(String nomeArquivo){
        try{
            salva.salvarCodigo(codigoGerado, "C:/Users/lucas/Downloads/Obj-" + nomeArquivo);
        }catch(Exception e ){
            System.out.println("Erro ao gerar nome do codigo objeto");
        }

    }    
}
