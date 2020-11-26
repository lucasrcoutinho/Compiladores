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
        if (!"".equals(token.getErro())){
            trataErro(token.getErro());
        }
    }
    
    private void analisadorSintatico(){
        getToken();
        if ("sprograma".equals(token.getSimbolo())){
            getToken();
            if ("sidentificador".equals(token.getSimbolo())){
               //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
               analisadorSemantico.insere_tabela(token.getLexema(), "nomedeprograma", "", -1);
               //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
               getToken();
                if ("sponto_virgula".equals(token.getSimbolo())){
                   analisaBloco();
                   //getToken();
                   //System.out.println("token.getSimbolo() " + token.getSimbolo());
                   if ("sponto".equals(token.getSimbolo())){                       
                       getToken();
                       if ("".equals(token.getSimbolo())){//Confirmar esta opcao
                           //System.out.println("Sucesso");
                           //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                           analisadorSemantico.imprimeTabelaSimbolos();
                           //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
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
        //analisadorSemantico.imprimeTabelaSimbolos();
        analisaSubRotina();//Aqui checa retorno de funcao na declaracao!
        analisaComandos();
    }
    
    private void analisaEtVariaveis(){
        if ("svar".equals(token.getSimbolo())){
            getToken();
            if ("sidentificador".equals(token.getSimbolo())){

                while("sidentificador".equals(token.getSimbolo())){
                    analisaVariaveis();
                    if ("sponto_virgula".equals(token.getSimbolo())){
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
            if ("sidentificador".equals(token.getSimbolo())){
                //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                if(analisadorSemantico.pesquisa_duplicvar_tabela(token.getLexema())){
                    trataErro("Varialvel ja declarada");
                    return;
                }
                analisadorSemantico.insere_tabela(token.getLexema(), "tipoVariavel", "", -1);
                //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                getToken();
                if ("svirgula".equals(token.getSimbolo()) || "sdoispontos".equals(token.getSimbolo())){
                    if ("svirgula".equals(token.getSimbolo())){
                        getToken();
                        if ("sdoispontos".equals(token.getSimbolo())) trataErro("Esperado identificador");
                    }
                }else{
                    trataErro("Esperado virgula ou doispontos");
                    return;
                }
            }else{
                trataErro("Esperado identificador");
                return;
            }
        }while (!"sdoispontos".equals(token.getSimbolo() /*!=*/));
        getToken();
        analisaTipo();
    }
    
    private void analisaTipo(){
        if (!"sinteiro".equals(token.getSimbolo()) && !"sbooleano".equals(token.getSimbolo())){
            trataErro("Esperado inteiro ou booleano");
        }//else getToken();
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        analisadorSemantico.coloca_tipo(token.getLexema());
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        getToken();
    }
    
    private void analisaComandos(){
        if ("sinicio".equals(token.getSimbolo())){
            getToken();
            //******************************************************************
            //flagRetornoOk=false
            //******************************************************************
            analisaComandoSimples();
            while (!"sfim".equals(token.getSimbolo())){
                //**************************************************************
                //Se flagRetornoOk == true então erro   |talvez sai
                //Encontrou codigo unreachable          |essa parte
                //**************************************************************
                if ("sponto_virgula".equals(token.getSimbolo())){
                    getToken();
                    if (!"sfim".equals(token.getSimbolo()))
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
        if ("satribuicao".equals(token.getSimbolo())){
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
        if ("sabre_parenteses".equals(token.getSimbolo())){
            getToken();
            if ("sidentificador".equals(token.getSimbolo())){
                //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                if(!analisadorSemantico.pesquisa_declvar_tabela(token.getLexema())){
                    trataErro("Variavel nao declarada");
                    return;
                }
                //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                getToken();
                if ("sfecha_parenteses".equals(token.getSimbolo())){
                    getToken();
                }else trataErro("Esperado fecha parentese"); 
            }else trataErro("Esperado identificador");              
        }else trataErro("Esperado abre parentese"); 
    }
    
    private void analisaEscreva(){
        getToken();
        if ("sabre_parenteses".equals(token.getSimbolo())){
            getToken();
            if ("sidentificador".equals(token.getSimbolo())){
                //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                if(!analisadorSemantico.pesquisa_declvar_tabela(token.getLexema())){
                    trataErro("Variavel nao declarada");
                    return;
                }
                //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                getToken();
                if ("sfecha_parenteses".equals(token.getSimbolo())){
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
        if ("sfaca".equals(token.getSimbolo())){       
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
        if ("sentao".equals(token.getSimbolo())){
            getToken();
            analisaComandoSimples();
            //******************************************************************
            //backupflagRetornoOk = flagRetornoOk
            //flagRetornoOk = false
            //******************************************************************
            if ("ssenao".equals(token.getSimbolo())){
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
        if ("sprocedimento".equals(token.getSimbolo()) || "sfuncao".equals(token.getSimbolo())){
            //Geração de codigo
        }
        while("sprocedimento".equals(token.getSimbolo()) || "sfuncao".equals(token.getSimbolo())){
            if ("sprocedimento".equals(token.getSimbolo())){
                analisaDeclaracaoProcedimento();
            }else{
                analisaDeclaracaoFuncao();
            }
            if ("sponto_virgula".equals(token.getSimbolo())){
                getToken();
            }else {
                trataErro("Esperado pontovirgula");
                return;
            }  
        }
    }
    
    private void analisaDeclaracaoProcedimento(){
        getToken();
        if ("sidentificador".equals(token.getSimbolo())){
            //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            if(analisadorSemantico.pesquisa_declproc_tabela(token.getLexema())){
                trataErro("Procedimento ja declarado");
                return;
            }else{
                analisadorSemantico.insere_tabela(token.getLexema(), "tipoProcedimento", "L", -1);
            }
            //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            
            getToken();
            if("sponto_virgula".equals(token.getSimbolo())){
                analisaBloco();
            }else trataErro("Esperado pontovirgula");  
        }else trataErro("Esparado identificado");
        
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        analisadorSemantico.desempilha();        
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    }
    
    private void analisaDeclaracaoFuncao(){//BO*********************************
        getToken();
        if ("sidentificador".equals(token.getSimbolo())){
            //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            if(analisadorSemantico.pesquisa_declvarfunc_tabela(token.getLexema())){
                trataErro("Funcao ja declarada");
                return;
            }else{
                analisadorSemantico.insere_tabela(token.getLexema(), "tipoFuncao", "L", -1);
            }
            //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            getToken();
            if ("sdoispontos".equals(token.getSimbolo())){
                getToken();
                if ("sinteiro".equals(token.getSimbolo()) || "sbooleano".equals(token.getSimbolo())){
                    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                    analisadorSemantico.coloca_tipo(token.getLexema());
                    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                    getToken();
                    if ("sponto_virgula".equals(token.getSimbolo())){
                        //******************************************************
                        //Liga verificação de retorno(verificaRetorno = true)
                        analisaBloco();
                        //Desliga a verificação de retorno(verificaRetorno = false)
                        //******************************************************
                    }
                }else trataErro("O tipo da funcao deve ser inteiro ou booleano");  
            }else trataErro("Esperado doispontos");  
        }else trataErro("Esperado identifacador");
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        analisadorSemantico.desempilha();
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    }
    
    //--------------------------------------------------------------------------
    //Aqui gera a expressao pos fixa e faz a compatibilizacao de tipos
    private int analisaExpressaoRetorno(){
        expressao.clear();        
        analisaExpressao();
        ArrayList<String> exprecaoPosFixa =  new ArrayList();
        exprecaoPosFixa = analisadorSemantico.convertePosFixa(expressao);
        //System.out.print("Expressao original: ");
        //imprimeExpressao(expressao);
        //analisadorSemantico.convertePosFixa(expressao);       
        //analisadorSemantico.compatibilizacaoTipos(exprecaoPosFixa);
        return 1; //analisadorSemantico.compatibilizacaoTipos(exprecaoPosFixa);//Retorna o tipo da expressao -1-erro 0-Bool 1-int     
    }
    //--------------------------------------------------------------------------
    
    private void analisaExpressao(){
        analisaExpressaoSimples();
        if (token.getSimbolo().equals("smenor") ||
                token.getSimbolo().equals("sdif") ||
                token.getSimbolo().equals("smenorig") ||
                token.getSimbolo().equals("smaior") ||
                token.getSimbolo().equals("smaiorig") ||
                token.getSimbolo().equals("sig")){
            //------------------------------------------------------------------
            expressao.add(new Token(token.getSimbolo(), token.getLexema(), 0));
            //------------------------------------------------------------------
            getToken();
            analisaExpressaoSimples();
        }        
        //O retorno do analisaExpressao nao pode ser aqui!
    }
    
    private void analisaExpressaoSimples(){
        if ("smais".equals(token.getSimbolo()) || "smenos".equals(token.getSimbolo())){
            //------------------------------------------------------------------
            expressao.add(new Token(token.getSimbolo(), token.getLexema(), 0));
            //------------------------------------------------------------------
            getToken();
        }
        analisaTermo();
        while ("smenos".equals(token.getSimbolo()) || 
                "smais".equals(token.getSimbolo()) ||
                "sou".equals(token.getSimbolo())){
            //------------------------------------------------------------------
            expressao.add(new Token(token.getSimbolo(), token.getLexema(), 0));
            //------------------------------------------------------------------
            getToken();
            analisaTermo();
        }
    }
    
    private void analisaTermo(){
        analisaFator();
        while("smult".equals(token.getSimbolo()) ||
                "sdiv".equals(token.getSimbolo()) ||
                "se".equals(token.getSimbolo())){
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
        if ("sidentificador".equals(token.getSimbolo())){
            //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            if(analisadorSemantico.pesquisa_tabela(token.getLexema())){
                //System.out.println("analisadorSemantico.buscaTipoFuncao(token.getLexema() " + analisadorSemantico.buscaTipoFuncao(token.getLexema()));
                if(analisadorSemantico.buscaTipoFuncao(token.getLexema()) == "booleano" ||
                   analisadorSemantico.buscaTipoFuncao(token.getLexema()) == "inteiro"){
                   analisaChamadaDeFuncao();
                }else{
                    getToken(); 
                }
                
            }else{
                trataErro("Identificador nao definido********************");
                return;
            }
            //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~            
        }else if ("snumero".equals(token.getSimbolo())){
                getToken();            
        }else if ("snao".equals(token.getSimbolo())){
            getToken();
            analisaFator();
        }else if ("sabre_parenteses".equals(token.getSimbolo())){
            getToken();
            analisaExpressao();
            if ("sfecha_parenteses".equals(token.getSimbolo())){
                //--------------------------------------------------------------
                expressao.add(new Token(token.getSimbolo(), token.getLexema(), 0));
                //--------------------------------------------------------------
                getToken();
            }else trataErro("Esperado fecha parentese");
        }else if ("verdadeiro".equals(token.getLexema()) || "falso".equals(token.getLexema())){
            getToken();
        }else trataErro("Esperado identficador, numero, parentese ou valor booleano");
    }
    
    private void analisaChamadaDeFuncao(){
        System.out.println("Entrou no analisaChamadaDeFuncao");
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
        }else if ("".equals(token.getErro())){
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

