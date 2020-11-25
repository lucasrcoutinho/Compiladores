/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analisador.sintatico;
import java.util.ArrayList;
import analisador.lexico.backend.AnalisadorLexico;
import analisador.lexico.backend.Token;
import analisador.semantico.AnalisadorSemantico;

/**
 *
 * @author lucas
 */

public class AnalisadorSintatico {    
    //AnalisadorLexico lexico = new AnalisadorLexico();
    Token token;
    Token novoToken;
    ArrayList <Token> listaDeTokens = new ArrayList();
    ArrayList <Token> expressao = new ArrayList<>();
    int indiceToken = 0;
    int linhaErro = -1;
    int linhaErroSintatico=-1;
    boolean erro = false;
    String mensagemErro;
    String descricaoErroLexico;
    String caminhoArquivoFonte;
    AnalisadorLexico analisadorLexico;
    AnalisadorSemantico analisadorSemantico;
    
    public void inicioSintatico(String caminho){
        erro = false;
        indiceToken = 0;
        linhaErro = -1;
        mensagemErro = "";
        analisadorLexico = new AnalisadorLexico(caminho);
        analisadorSemantico = new AnalisadorSemantico();
        
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
               //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
               //analisadorSemantico.
               //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
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
        analisaSubRotina();//Aqui checa retorno de funcao na declaracao!
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
            //******************************************************************
            //flagRetornoOk=false
            //******************************************************************
            analisaComandoSimples();
            while (token.getSimbolo() != "sfim"){
                //**************************************************************
                //Se flagRetornoOk == true então erro   |talvez sai
                //Encontrou codigo unreachable          |essa parte
                //**************************************************************
                if (token.getSimbolo() == "sponto_virgula"){
                    getToken();
                    if (token.getSimbolo() != "sfim")
                        analisaComandoSimples();                    
                } else{
                    trataErro("Esperado pontovirgula");
                    return;
                }                
            }
            //******************************************************************
            // Se flagRetornoOk == false então erro
            // Não encontrou um retorno
            //******************************************************************
            getToken();
        } else trataErro("Eperado palavra reservada inicio");
    }
    
    private void analisaComandoSimples(){
        switch(token.getSimbolo()){
            case "sidentificador":
                //**************************************************************
                //  O valor de retorno ou expressao tem que ter o mesmo tipo da funcao 
                //  se token.getLexema == (getProcCorrente) 
                //  Então analisa atribuição e 
                //  se tiver mais codigo depois = erro
                //**************************************************************
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
    
    private void analisaAtribChProcedimento(){//Momento de utilizacao (consultas na tabela)
        //Salva token anterior antes de pegar o proximo
        getToken();
        if (token.getSimbolo() == "satribuicao"){
            //Verificar se o token anterior eh variavel
            
            //******************************************************************
            //com o retorno de funcao ativado
            //Verificar se o token anterior eh funcao
            //******************************************************************
            analisaAtribuicao(/*token anterior*/);
        }else{
            //******************************************************************
            //com o retorno de funcao ativado
            //Se entrou aqui nao teve atribuicao, erro samantico.
            //******************************************************************
            //Verificar se o token anterior eh procedimento
            chamadaProcedimento(/*token anterior*/);
        } 
    }
    
    private void analisaLeia(){//So pode ler inteiros
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
        //----------------------------------------------------------------------
        analisaExpressaoRetorno();//retorno
        //----------------------------------------------------------------------
        if (token.getSimbolo() == "sfaca"){       
            getToken();
            analisaComandoSimples();
            //**************************************************************
            //flagRetornoOk=false
            //Retorno aqui nao eh garantido
            //**************************************************************
        }else trataErro("Esperado palavra reservada faca"); 
    }     
    
    private void analisaSe(){
        getToken();
        //----------------------------------------------------------------------
        analisaExpressaoRetorno();//retorno 
        //----------------------------------------------------------------------
        if (token.getSimbolo() == "sentao"){
            getToken();
            analisaComandoSimples();
            //******************************************************************
            //backupflagRetornoOk = flagRetornoOk
            //flagRetornoOk = false
            //******************************************************************
            if (token.getSimbolo() == "ssenao"){
                getToken();
                analisaComandoSimples();
            }
        }else trataErro("Esperado palavra reservada entao");
        //**********************************************************************
        //se backupflagRetornoOk ou flagRetornoOk != true entao
        //flagRetornoOk == false
        //senao flagRetornoOk == true
        //**********************************************************************
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
    
    private void analisaDeclaracaoFuncao(){//BO*********************************
        getToken();
        if (token.getSimbolo() == "sidentificador"){
            getToken();
            if (token.getSimbolo() == "sdoispontos"){
                getToken();
                if (token.getSimbolo() == "sinteiro" || token.getSimbolo() == "sbooleano"){
                    getToken();
                    if (token.getSimbolo() == "sponto_virgula"){
                        //******************************************************
                        //Liga verificação de retorno(verificaRetorno = true)
                        analisaBloco();
                        //Desliga a verificação de retorno(verificaRetorno = false)
                        //******************************************************
                    }
                }else trataErro("Esperado inteiro ou booleano");  
            }else trataErro("Esperado doispontos");  
        }else trataErro("Esperado identifacador");  
    }
    
    //----------------------------------------------------------------------
    private int analisaExpressaoRetorno(){
        expressao.clear();        
        analisaExpressao();
        ArrayList<String> exprecaoPosFixa =  new ArrayList();
        //exprecaoPosFixa = analisadorSemantico.convertePosFixa(expressao);
        System.out.print("Expressao original: ");
        imprimeExpressao(expressao);
        analisadorSemantico.convertePosFixa(expressao);       
        return 1; //analisadorSemantico.compatibilizacaoTipos(exprecaoPosFixa);//Retorna o tipo da expressao -1-erro 0-Bool 1-int     
    }
    //----------------------------------------------------------------------
    
    private void analisaExpressao(){
        analisaExpressaoSimples();
        if (token.getSimbolo() == "smaior" ||
                token.getSimbolo() == "smaiorig" ||
                token.getSimbolo() == "sig" ||
                token.getSimbolo() == "smenor" ||
                token.getSimbolo() == "smenorig" ||
                token.getSimbolo() == "sdif"){
            //------------------------------------------------------------------
            expressao.add(new Token(token.getSimbolo(), token.getLexema(), 0));
            //------------------------------------------------------------------
            getToken();
            analisaExpressaoSimples();
        }        
        //O retorno do analisaExpressao nao pode ser aqui!
    }
    
    private void analisaExpressaoSimples(){
        if (token.getSimbolo() == "smais" || token.getSimbolo() == "smenos"){
            //------------------------------------------------------------------
            expressao.add(new Token(token.getSimbolo(), token.getLexema(), 0));
            //------------------------------------------------------------------
            getToken();
        }
        analisaTermo();
        while (token.getSimbolo() == "smais" || 
                token.getSimbolo() == "smenos" ||
                token.getSimbolo() == "sou"){
            //------------------------------------------------------------------
            expressao.add(new Token(token.getSimbolo(), token.getLexema(), 0));
            //------------------------------------------------------------------
            getToken();
            analisaTermo();
        }
    }
    
    private void analisaTermo(){
        analisaFator();
        while(token.getSimbolo() == "smult" ||
                token.getSimbolo() == "sdiv" ||
                token.getSimbolo() == "se"){
            //------------------------------------------------------------------
            expressao.add(new Token(token.getSimbolo(), token.getLexema(), 0));
            //------------------------------------------------------------------
            getToken();
            analisaFator();
        }        
    }
    
    private void analisaFator(){
        //----------------------------------------------------------------------
        expressao.add(new Token(token.getSimbolo(), token.getLexema(), 0));
        //----------------------------------------------------------------------
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
                //--------------------------------------------------------------
                expressao.add(new Token(token.getSimbolo(), token.getLexema(), 0));
                //--------------------------------------------------------------
                getToken();
            }else trataErro("Esperado fecha parentese");
        }else if (token.getLexema() == "verdadeiro" || token.getLexema() == "falso"){
            getToken();
        }else trataErro("Esperado identficador, numero, parentese ou valor booleano");
    }
    
    private void analisaChamadaDeFuncao(){
        getToken();
    }
    
    private void chamadaProcedimento(){  
        //Verifcar se identificador eh do tipo procedimento
        
        
        //????getToken();
        //????Verificar se atribuição 
    }
    
    
    //b:= a*a+(c div b) exemplo de atribuição
    private void analisaAtribuicao(){
        getToken();
        analisaExpressaoRetorno();//Retorno    
    }  
    
    private void trataErro(String metodoChamouErro){
        if (!erro){
            //System.out.println("Erro na linha " + token.getLinha()+ " " + 
                //token.getLexema() + " " + token.getSimbolo() + " - " + metodoChamouErro);
            linhaErro = token.getLinha();
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
            return("Erro sintatico na linha: " + linhaErro + mensagemErro);
        }        
        return("Erro lexico na linha: " + linhaErro + " - " + token.getErro());
    }
    
    
    private void imprimeExpressao(ArrayList<Token> expressao){//Metodo temporario

        for(int i = 0; i< expressao.size(); i++){
            System.out.print(expressao.get(i).getSimbolo());
        }
        System.out.println("");
    }
}

