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
import java.io.IOException;

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
    ArrayList <String> codigoGeradoTemp = new ArrayList<>();
    int indiceToken = 0;
    int linhaErro = -1;
    int linhaErroSintatico=-1;
    int rotulo = 1;
    String mensagemErro;
    String descricaoErroLexico;
    String caminhoArquivoFonte;
    AnalisadorLexico analisadorLexico;
    AnalisadorSemantico analisadorSemantico;
    boolean flagRetornoOk = false;
    boolean flagVerificaRetorno = false;
    boolean erro = false;
    
    public void inicioSintatico(String caminho) throws IOException{
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
        //++++++++++++++++++++++++++++
        //rotulo = 0
        //++++++++++++++++++++++++++++
        getToken();
        if ("sprograma".equals(token.getSimbolo())){
            getToken();
            if ("sidentificador".equals(token.getSimbolo())){
                //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                analisadorSemantico.insere_tabela(token.getLexema(), "nomedeprograma", "", -1);
                //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                //++++++++++++++
                //Gera START
                System.out.println("START");
                //++++++++++++++
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
                           //analisadorSemantico.imprimeTabelaSimbolos();
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
        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        //Gera label NULL, se nao for programa
        //System.out.println("L" + rotulo +" NULL");
        //rotulo++;
        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        analisaEtVariaveis();
        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        //Gera ALLOC 0,quantidade de var
        System.out.println("ALLOC");
        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        //analisadorSemantico.imprimeTabelaSimbolos();
        analisaSubRotina();//Aqui checa retorno de funcao na declaracao!
        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        //gera rotulo L NULL
        //System.out.println("L" +rotulo+ " NULL");
        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        analisaComandos();
        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        //Gera DALLOC 0,quantidade de var
        System.out.println("DALLOC");
        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
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
                    trataErro("Variavel ja declarada");
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
            return;
        }
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        analisadorSemantico.coloca_tipo(token.getLexema());
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        getToken();
    }
    
    private void analisaComandos(){
        if ("sinicio".equals(token.getSimbolo())){
            getToken();
            //******************************************************************
            if(flagVerificaRetorno){
                flagRetornoOk=false;
            }            
            //******************************************************************
            analisaComandoSimples();
            while (!"sfim".equals(token.getSimbolo())){
                //**************************************************************
                if(flagVerificaRetorno){
                    if (flagRetornoOk){
                        trataErro("Codigo unreachable apos retorno");
                        return;
                    }
                }
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
            if(flagVerificaRetorno){
                if(!flagRetornoOk){
                    trataErro("Retorno nao encontrado");
                }
            } 
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
                if(token.getLexema().equals(analisadorSemantico.getProcCorrente())){
                    flagRetornoOk = true;
                }
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
        Token bkpToken = new Token(token.getLexema(), token.getSimbolo(), 0); 
        getToken();        
        if ("satribuicao".equals(token.getSimbolo())){
            //******************************************************************
            //Pode ir pra dentro do analisa atribuição 
            //Verificar se o token anterior eh variavel
            if(!analisadorSemantico.pesquisa_declvar_tabela(bkpToken.getLexema())){
                if(!flagVerificaRetorno){
                    trataErro("Tentativa de atribuicao a nao variavel");
                }else if(!analisadorSemantico.pesquisa_declfunc_tabela(bkpToken.getLexema())){
                        trataErro("Funcao nao declarada");
                        return;
                }
            }//******************************************************************
            /*else{
            //******************************************************************
            //com o retorno de funcao ativado
            //Verificar se o token anterior eh funcao 
                if(flagVerificaRetorno){
                    if(!analisadorSemantico.pesquisa_declvar_tabela(bkpToken.getLexema())){
                        trataErro("Tentativa de atribuicao a nao variavel");
                    }else if(!analisadorSemantico.pesquisa_declfunc_tabela(bkpToken.getLexema())){
                        trataErro("Funcao nao declarada");
                        return;
                    }
                }
            }*/
            //******************************************************************
            analisaAtribuicao(bkpToken);
        }else{
            //******************************************************************
            //com o retorno de funcao ativado
            //Se entrou aqui nao teve atribuicao, erro samantico.
            //?Pode ir pra dentro de chamadaProcedimento???
            if(analisadorSemantico.pesquisa_declfunc_tabela(bkpToken.getLexema())){
                trataErro("Funcao usada sem considerar o valor de retorno");                
            }
            //******************************************************************
            //Verificar se o token anterior eh procedimento
            if(!analisadorSemantico.pesquisa_declproc_tabela(bkpToken.getLexema())){
                trataErro("Procedimento nao declarado");
                return;
            }
            
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
                //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
                //gera RD
                System.out.println("RD");
                //gera STR
                System.out.println("STR");
                //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
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
                //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
                //gera LDV
                System.out.println("LDV");
                //gera PRN
                System.out.println("PRN");
                //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
                getToken();
                if ("sfecha_parenteses".equals(token.getSimbolo())){
                    getToken();
                }else trataErro("Esperado fecha parentese"); 
            }else trataErro("Esperado identificador");  
        }else trataErro("Esperado abre parentese"); 
    }
    
    private void analisaEnquanto(){
        int retorno;
        getToken();
        //----------------------------------------------------------------------
        
        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        //Gera Lx NULL
        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++        
        retorno = analisaExpressaoRetorno(); 
        if(retorno == -1){
            token.setLinha(token.getLinha()-1);
            trataErro("Expressao com tipos difentes");
        }else if(retorno != 0){
            token.setLinha(token.getLinha()-1);
            trataErro("Expressao nao booleana");
        }
        //----------------------------------------------------------------------
        
        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        //Gera JUMPF Ly
        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++        
        if ("sfaca".equals(token.getSimbolo())){       
            getToken();
            analisaComandoSimples();            
            //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
            //gera JMP Lx
            //Gera Ly NULL
            //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
            
            //******************************************************************
            //Retorno aqui nao eh garantido
            if(flagVerificaRetorno){
                flagRetornoOk = false;
            }    
            //******************************************************************
        }else trataErro("Esperado palavra reservada faca"); 
    }     
    
    private void analisaSe(){
        int retorno;
        boolean backupflagRetornoOk = false;
        getToken();
        //----------------------------------------------------------------------
        //analisaExpressaoRetorno();//retorno 
        retorno = analisaExpressaoRetorno();        
        if(retorno == -1){
            token.setLinha(token.getLinha()-1);
            trataErro("Expressao com tipos difentes");
            return;
        }else if(retorno != 0){
            token.setLinha(token.getLinha()-1);
            trataErro("Expressao nao booleana");
            return;
        }
        //----------------------------------------------------------------------
      
        if ("sentao".equals(token.getSimbolo())){            
            //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
            //Gera comparacao(Topo da pilha)
            //Gera JMPF L1
            System.out.println("JMPF L");
            //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++            
            getToken();
            analisaComandoSimples();            
            //******************************************************************
            if(flagVerificaRetorno){
                backupflagRetornoOk = flagRetornoOk;
                flagRetornoOk = false;
            }            
            //******************************************************************            
            if ("ssenao".equals(token.getSimbolo())){                
            //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
            //Gera JMP L2
                System.out.println("JMP L");
            //Gera label L1
                System.out.println("L NULL");
            //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++            
                getToken();
                analisaComandoSimples();                
            //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
            //Gera label L2
            System.out.println("L NULL");
            }else{           
            //Gera label L1
            System.out.println("L NULL");
            //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++             
            }
        }else trataErro("Esperado palavra reservada entao");        
        //**********************************************************************
        //se backupflagRetornoOk ou flagRetornoOk != true entao
        //senao flagRetornoOk = true
        if(flagVerificaRetorno){
            if(!backupflagRetornoOk || !flagRetornoOk){
                flagRetornoOk = false;
            }else{
                flagRetornoOk = true;
            }
        }        
        //**********************************************************************
    }
    
    private void analisaSubRotina(){
        int flag = 0;
        int auxrot=0;
        if ("sprocedimento".equals(token.getSimbolo()) || "sfuncao".equals(token.getSimbolo())){
            //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
            auxrot= rotulo;
            //GERA(´ ´,JMP,rotulo,´ ´) {Salta sub-rotinas}
            System.out.println("JMP L" + rotulo);    
            rotulo++;
            flag = 1;
            //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
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
        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        //se flag = 1 entao Gera(auxrot,NULL,´ ´,´ ´) {início do principal}
        if(flag == 1) System.out.println("L" + auxrot + "NULL");
        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    }
    
    private void analisaDeclaracaoProcedimento(){
        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        //gera label NULL para chamada de funcao/proc
        System.out.println("L" +rotulo+ " NULL");
        rotulo++;
        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
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
        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        //gera label NULL para chamada de funcao/proc
        System.out.println("L" +rotulo+ " NULL");
        rotulo++;
        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
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
                        flagVerificaRetorno = true;
                        analisaBloco();
                        flagVerificaRetorno = false;
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
        analisaExpressao();//Gera a expressao original
        ArrayList<String> exprecaoPosFixa =  new ArrayList();
        //System.out.println("Expressao original: ");
        //imprimeExpressao(expressao);
        exprecaoPosFixa = analisadorSemantico.convertePosFixa(expressao);

        //analisadorSemantico.convertePosFixa(expressao);
        /*
        if(analisadorSemantico.compatibilizacaoTipos(exprecaoPosFixa)<0){
            System.out.println("Tipos diferentes na expressao"); 
        }else{
            System.out.println("Expressao OK");
        }
        */
        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        //Gera expressao
        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        return analisadorSemantico.compatibilizacaoTipos(exprecaoPosFixa);//Retorna o tipo da expressao -1-erro 0-Bool 1-int     
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
        //?O retorno do analisaExpressao nao pode ser aqui por causa da "recursao"
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
        int indice;
        //----------------------------------------------------------------------
        expressao.add(new Token(token.getSimbolo(), token.getLexema(), 0));
        //----------------------------------------------------------------------
        if ("sidentificador".equals(token.getSimbolo())){
            //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            indice = analisadorSemantico.pesquisa_tabela(token.getLexema());
            if(indice >= 0){
                //System.out.println("analisadorSemantico.buscaTipoFuncao(token.getLexema() " + analisadorSemantico.buscaTipoFuncao(token.getLexema()));
                if("inteiro".equals(analisadorSemantico.buscaTipoFuncao(indice)) ||
                   "booleano".equals(analisadorSemantico.buscaTipoFuncao(indice))){
                   analisaChamadaDeFuncao();
                }else{
                    getToken(); 
                }
                
            }else{
                trataErro("Identificador nao definido");
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
        //System.out.println("Entrou no analisaChamadaDeFuncao");
        getToken();
    }
    
    private void chamadaProcedimento(){
        //verificar se é variavel
        //se for
        //  Verifcar se identificador eh do tipo procedimento
        //      se nao for "Erro, variavel nao eh procedimento"
        //senao erro "variavel nao declarada"
        
        //Ou procedimento nao declarado e ja era
        //????getToken();
    }
    
    
    //b:= a*a+(c div b) exemplo de atribuição
    private void analisaAtribuicao(Token tokenAnterior){
        getToken();
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        int indice;
        String tipo;
        int compararTipoRetorno = -1;
        int retorno;        
        indice = analisadorSemantico.pesquisa_tabela(tokenAnterior.getLexema());
        if(indice >= 0){
            tipo = analisadorSemantico.buscaTipoFuncao(indice);
            if("inteiro".equals(tipo)){
                compararTipoRetorno = 1;
            }else if("booleano".equals(tipo)){
                compararTipoRetorno = 0;
            }else{
                trataErro("Variavel de atribuicao nao eh int ou bool");
            }
        }else{
            trataErro("Variavel nao declrada");
        }
        
        retorno = analisaExpressaoRetorno();
        
        if(retorno == -1){
            //token.setLinha(token.getLinha()-1);
            trataErro("Expressao com tipos difentes");
        }else if(retorno != compararTipoRetorno){
            trataErro("Atribuicao com tipos difentes");
        }
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
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
            return("Erro na linha: " + linhaErro + mensagemErro);
        }        
        return("Erro na linha: " + linhaErro + " - " + token.getErro());
    }
    
    
    private void imprimeExpressao(ArrayList<Token> expressao){//Metodo temporario
        for(int i = 0; i< expressao.size(); i++){
            System.out.print(expressao.get(i).getSimbolo());
        }
        System.out.println("");
    }
}

