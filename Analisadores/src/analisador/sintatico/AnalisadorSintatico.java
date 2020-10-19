/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analisador.sintatico;
import java.util.ArrayList;
import analisador.lexico.backend.AnalisadorLexico;
import analisador.lexico.backend.Token;

/**
 *
 * @author lucas
 */
public class AnalisadorSintatico {    
    //AnalisadorLexico lexico = new AnalisadorLexico();
    Token token;     
    ArrayList <Token> listaDeTokens = new ArrayList();
    int indiceToken = 0;
    int linhaErroLexico=-1;
    int linhaErroSintatico=-1;
    boolean erro = false;
    String mensagemErro;
    String descricaoErroLexico;
    String caminhoArquivoFonte;
    AnalisadorLexico analisadorLexico;
    
    public void inicioSintatico(String caminho){
        erro = false;
        indiceToken = 0;
        linhaErroSintatico = -1;
        mensagemErro = "";
        analisadorLexico = new AnalisadorLexico(caminho);
        analisadorSintatico();
    }

    private void getToken(){
        token = analisadorLexico.getToken();
        if (token.getErro() != ""){
            trataErro(token.getErro());
        }
    }
    
    private void analisadorSintatico(){
        getToken();
        if (token.getSimbolo() == "sprograma"){
            getToken();
            if (token.getSimbolo() == "sidentificador"){
               getToken();
                if (token.getSimbolo() == "sponto_virgula"){
                   analisaBloco();
                   //getToken();
                   //System.out.println("token.getSimbolo() " + token.getSimbolo());
                   if (token.getSimbolo() == "sponto"){                       
                       getToken();
                       if (token.getSimbolo() == ""){//Confirmar esta opcao
                           //System.out.println("Sucesso");                           
                       }else trataErro("Caracteres apos fim do programa");  
                   }else {
                       //indiceToken--;
                       trataErro("Esperado ponto");                       
                   }                            
                }else trataErro("Esperado pontovirgula");  
            }else trataErro("Esperado indentificador"); 
        }else trataErro("Esperada palvra reservada programa"); 
    }
    
    private void analisaBloco(){
        getToken();
        analisaEtVariaveis();
        analisaSubRotina();
        analisaComandos();
    }
    
    private void analisaEtVariaveis(){
        if (token.getSimbolo() == "svar"){
            getToken();
            if (token.getSimbolo() == "sidentificador"){
                while(token.getSimbolo() == "sidentificador"){
                    analisaVariaveis();
                    if (token.getSimbolo() == "sponto_virgula"){
                        getToken();                        
                    }else{
                        trataErro("Esperado pontovirgula"); 
                        return;
                    }  
                }
            }else trataErro("Esperado indentificador");  
        }
    }
    
    private void analisaVariaveis(){
        do{
            if (token.getSimbolo() == "sidentificador"){
                getToken();
                if (token.getSimbolo() == "svirgula" || token.getSimbolo() == "sdoispontos"){
                    if (token.getSimbolo() == "svirgula"){
                        getToken();
                        if (token.getSimbolo() == "sdoispontos") trataErro("Esperado identificador");
                    }
                }else{
                    trataErro("Esperado virgula ou doispontos");
                    return;
                }
            }else{
                trataErro("Esperado identificador");
                return;
            };
        }while (token.getSimbolo() /*!=*/!= "sdoispontos");
        getToken();
        analisaTipo();
    }
    
    private void analisaTipo(){
        if (token.getSimbolo() != "sinteiro" && token.getSimbolo() != "sbooleano"){
            trataErro("Esperado inteiro ou booleano");
        }//else getToken();
        getToken();
    }
    
    private void analisaComandos(){
        if (token.getSimbolo() == "sinicio"){
            getToken();
            analisaComandoSimples();
            while (token.getSimbolo() != "sfim"){
                if (token.getSimbolo() == "sponto_virgula"){
                    getToken();
                    if (token.getSimbolo() != "sfim")
                        analisaComandoSimples();                    
                } else{
                    trataErro("Esperado pontovirgula");
                    return;
                }                
            }
            getToken();
        }
        else trataErro("Eperado palavra reservada inicio");
    }
    
    private void analisaComandoSimples(){
        switch(token.getSimbolo()){
            case "sidentificador":
                analisaAtribChProcedimento();
                break;
            case "sse":
                analisaSe();
                break;
            case "senquanto":
                analisaEnquanto();
                break;
            case "sleia":
                analisaLeia();
                break;
            case "sescreva":
                analisaEscreva();
                break;
            default:
                analisaComandos(); 
        }
    }
    
    private void analisaAtribChProcedimento(){
        getToken();
        if (token.getSimbolo() == "satribuicao"){
            analisaAtribuicao();
        }else chamadaProcedimento();
    }
    
    private void analisaLeia(){
        getToken();
        if (token.getSimbolo() == "sabre_parenteses"){
            getToken();
            if (token.getSimbolo() == "sidentificador"){
                getToken();
                if (token.getSimbolo() == "sfecha_parenteses"){
                    getToken();
                }else trataErro("Esperado fecha parentese"); 
            }else trataErro("Esperado identificador");              
        }else trataErro("Esperado abre parentese"); 
    }
    
    private void analisaEscreva(){
        getToken();
        if (token.getSimbolo() == "sabre_parenteses"){
            getToken();
            if (token.getSimbolo() == "sidentificador"){
                getToken();
                if (token.getSimbolo() == "sfecha_parenteses"){
                    getToken();
                }else trataErro("Esperado fecha parentese"); 
            }else trataErro("Esperado identificador");  
        }else trataErro("Esperado abre parentese"); 
    }
    
    private void analisaEnquanto(){
        getToken();
        analisaExpressao();
        if (token.getSimbolo() == "sfaca"){       
            getToken();
            analisaComandoSimples();
        }else trataErro("Esperado palavra reservada faca"); 
    }     
    
    private void analisaSe(){
        getToken();
        analisaExpressao();
        if (token.getSimbolo() == "sentao"){
            getToken();
            analisaComandoSimples();
            if (token.getSimbolo() == "ssenao"){
                getToken();
                analisaComandoSimples();
            }
        }else trataErro("Esperado palavra reservada entao"); 
    }
    
    private void analisaSubRotina(){
        int flag = 0;
        if (token.getSimbolo() == "sprocedimento" || token.getSimbolo() == "sfuncao"){
        
        }
        while(token.getSimbolo() == "sprocedimento" || token.getSimbolo() == "sfuncao"){
            if (token.getSimbolo() == "sprocedimento"){
                analisaDeclaracaoProcedimento();
            }else{
                analisaDeclaracaoFuncao();
            }
            if (token.getSimbolo() == "sponto_virgula"){
                getToken();
            }else {
                trataErro("Esperado pontovirgula");
                return;
            }  
        }
    }
    
    private void analisaDeclaracaoProcedimento(){
        getToken();
        if (token.getSimbolo() == "sidentificador"){
            getToken();
            if(token.getSimbolo() == "sponto_virgula"){
                analisaBloco();
            }else trataErro("Esperado pontovirgula");  
        }else trataErro("Esparado identificado");
    }
    
    private void analisaDeclaracaoFuncao(){
        getToken();
        if (token.getSimbolo() == "sidentificador"){
            getToken();
            if (token.getSimbolo() == "sdoispontos"){
                getToken();
                if (token.getSimbolo() == "sinteiro" || token.getSimbolo() == "sbooleano"){
                    getToken();
                    if (token.getSimbolo() == "sponto_virgula"){
                        analisaBloco();
                    }
                }else trataErro("Esperado inteiro ou booleano");  
            }else trataErro("Esperado doispontos");  
        }else trataErro("Esperado identifacador");  
    }
    
    private void analisaExpressao(){
        analisaExpressaoSimples();
        if (token.getSimbolo() == "smaior" ||
                token.getSimbolo() == "smaiorig" ||
                token.getSimbolo() == "sig" ||
                token.getSimbolo() == "smenor" ||
                token.getSimbolo() == "smenorig" ||
                token.getSimbolo() == "sdif"){   
            getToken();
            analisaExpressaoSimples();
        }
    }
    
    private void analisaExpressaoSimples(){
        if (token.getSimbolo() == "smais" || token.getSimbolo() == "smenos"){
            getToken();
        }
        analisaTermo();
        while (token.getSimbolo() == "smais" || 
                token.getSimbolo() == "smenos" ||
                token.getSimbolo() == "sou"){
            getToken();
            analisaTermo();
        }
    }
    
    private void analisaTermo(){
        analisaFator();
        while(token.getSimbolo() == "smult" ||
                token.getSimbolo() == "sdiv" ||
                token.getSimbolo() == "se"){
            getToken();
            analisaFator();
        }        
    }
    
    private void analisaFator(){
        if (token.getSimbolo() == "sidentificador"){
            analisaChamadaDeFuncao();
        }else if (token.getSimbolo() == "snumero"){
                getToken();            
        }else if (token.getSimbolo() == "snao"){
            getToken();
            analisaFator();
        }else if (token.getSimbolo() == "sabre_parenteses"){
            getToken();
            analisaExpressao();
            if (token.getSimbolo() == "sfecha_parenteses"){
                getToken();
            }else trataErro("Esperado fecha parentese");
        }else if (token.getLexema() == "verdadeiro" || token.getLexema() == "falso"){
            getToken();
        }else trataErro("Esperado verdadeiro ou falso");
    }
    
    private void analisaChamadaDeFuncao(){
        getToken();
    }
    
    private void chamadaProcedimento(){
        //getToken();
    }
    
    private void analisaAtribuicao(){
        getToken();
        analisaExpressao();
    }  
    
    private void trataErro(String metodoChamouErro){
        if (!erro){
            System.out.println("Erro na linha " + token.getLinha()+ " " + 
                token.getLexema() + " " + token.getSimbolo() + " - " + metodoChamouErro);
            linhaErroSintatico = token.getLinha();
            mensagemErro = " - " + metodoChamouErro;
            //indiceToken = listaDeTokens.size();
            erro = true;
            //token = listaDeTokens.get(listaDeTokens.size()-1);        
        }       
    }
    
    public String getErro(){
        if (erro == false){
            return("Sucesso");
        }else if (token.getErro() == ""){
            return("Erro sintatico na linha: " + token.getLinha() + mensagemErro);
        }        
        return("Erro lexico na linha: " + token.getLinha() + " " + token.getErro());
    }
}

