/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analisador.semantico;

import analisador.lexico.backend.Token;
import java.util.ArrayList;
import java.util.Stack;

/**
 *
 * @author lucas
 */

public class AnalisadorSemantico {
    private final ArrayList<Simbolo> tabelaDeSimbolos;
    
    public AnalisadorSemantico(){    
        tabelaDeSimbolos = new ArrayList();
    }

    public void imprimeTabelaSimbolos(){
        for(int i = 0; i < tabelaDeSimbolos.size(); i++){
            System.out.print("Nome: " + tabelaDeSimbolos.get(i).getLexema());
            System.out.print("| Tipo: " + tabelaDeSimbolos.get(i).getTipoLexema());
            System.out.print("| Nivel: " + tabelaDeSimbolos.get(i).getNivelEscopo());
            if(!"".equals(tabelaDeSimbolos.get(i).getTipo())){
                System.out.print("| Tipo: " + tabelaDeSimbolos.get(i).getTipo());
            }
            System.out.println("");
        }
    
    }
    
    public void insere_tabela(String lexema, String tipoLexema,String nivel, int rotulo){
        SimboloVariavel var = new SimboloVariavel();
        var.addInfo(lexema, tipoLexema, nivel, rotulo);
        tabelaDeSimbolos.add(var);
    }

    public void coloca_tipo(String lexemaTipoVar){  
        int i = tabelaDeSimbolos.size()-1;
        
        if("L".equals(tabelaDeSimbolos.get(i).getNivelEscopo()) &&
                "".equals(tabelaDeSimbolos.get(i).getTipo())){
            tabelaDeSimbolos.get(i).setTipo(lexemaTipoVar);
        }else 
        while(i > 0 && "".equals(tabelaDeSimbolos.get(i).getNivelEscopo()) && 
                "".equals(tabelaDeSimbolos.get(i).getTipo())){
            tabelaDeSimbolos.get(i).setTipo(lexemaTipoVar);
            i--;
        }          
    }

    public void desempilha(){       
        int i = tabelaDeSimbolos.size()-1;
        
        while(!"L".equals(tabelaDeSimbolos.get(i).getNivelEscopo()) && i > 0){
            tabelaDeSimbolos.remove(i);
            i--;
        }
        if (!"tipoPrograma".equals(tabelaDeSimbolos.get(i).getTipoLexema())){
            tabelaDeSimbolos.get(i).setNivelEscopo("");
        }
    }
    
    public String getProcCorrente(){
        int i = tabelaDeSimbolos.size()-1;
        
        while(!"L".equals(tabelaDeSimbolos.get(i).getNivelEscopo()) && i > 0){
             i--;
             if(i == 0){
                 return "";
             }
        }
        return tabelaDeSimbolos.get(i).getLexema();
    }
    
    public boolean pesquisa_duplicvar_tabela(String lexema){//ok
        //pesquisa duplicidade de variavel no escopoNivel atual
        //pesquisa se nome da variavel ja foi usada por procedimento, funcao ou programa em qq escopoNivel
        //Caso encontre duplicidade eh considerado um erro semantico(dois identificadores iguais)        
        
        //Se declaracao de procedimento ou funcao, pesquisa em toda a tabela
        //Nao pode haver nenhum tipo de duplicidade   
        int i = tabelaDeSimbolos.size()-1;
        boolean nivelAtual = true;
        
        while(i >= 0){      
            if (nivelAtual == true){//Se estiver no mesmo nivel
                if("L".equals(tabelaDeSimbolos.get(i).getNivelEscopo())){
                    nivelAtual = false;
                }
                if(lexema.equals(tabelaDeSimbolos.get(i).getLexema())){//E encontrou duplicidade
                    return true;
                }
            }else{//Esta em outro nivel
                if(!"tipoVariavel".equals(tabelaDeSimbolos.get(i).getTipoLexema())){//Não compara mais com variaveis
                    if(lexema.equals(tabelaDeSimbolos.get(i).getLexema())){//E encontrou duplicidade
                        return true;
                    }
                }
            }
            i--;
        } 
        return false;
    }
    
    public boolean pesquisa_declvar_tabela(String lexema){
        int i = tabelaDeSimbolos.size()-1;
        while(i >= 0){
            if ((lexema.equals(tabelaDeSimbolos.get(i).getLexema())) &&
                    "tipoVariavel".equals(tabelaDeSimbolos.get(i).getTipoLexema())){//E encontrou duplicidade
                return true;
            }
            i--;
        } 
        return false;
    }
    
    public boolean pesquisa_declvarfunc_tabela(String lexema){
        int i = tabelaDeSimbolos.size()-1;
        while(i >= 0){
            if (lexema.equals(tabelaDeSimbolos.get(i).getLexema()) &&
                ("tipoFuncao".equals(tabelaDeSimbolos.get(i).getTipoLexema()) ||
                    "tipoVariavel".equals(tabelaDeSimbolos.get(i).getTipoLexema()) )){
                return true;
            }
            i--;
        } 
        return false;
    }
    
    public boolean pesquisa_declproc_tabela(String lexema){//Verifica duplicidade de procedimento
        int i = tabelaDeSimbolos.size()-1;
        while(i >= 0){
            if (lexema.equals(tabelaDeSimbolos.get(i).getLexema()) &&
                    "tipoProcedimento".equals(tabelaDeSimbolos.get(i).getTipoLexema())){
                return true;
            }
            i--;
        } 
        return false;
    }
    
    public boolean pesquisa_declfunc_tabela(String lexema){//Verifica duplicidade de funcao
        int i = tabelaDeSimbolos.size()-1;
        while(i >= 0){
            if (lexema.equals(tabelaDeSimbolos.get(i).getLexema()) &&
                    "tipoFuncao".equals(tabelaDeSimbolos.get(i).getTipoLexema())){
                return true;
            }
            i--;
        } 
        return false;
    }
    
    public int pesquisa_tabela(String lexema){
        int i = tabelaDeSimbolos.size()-1;
        while(i >= 0){
            if (lexema.equals(tabelaDeSimbolos.get(i).getLexema())){
                return i;
            }
            i--;
        } 
        return -1;
    }
    
    public String buscaTipo(int indice){
        return tabelaDeSimbolos.get(indice).getTipo();
    }
    
    public int buscaMemoriaRotulo(int indice){
        return tabelaDeSimbolos.get(indice).getMemoriaRotulo();
    }

    public int compatibilizacaoTipos(ArrayList expressaoPosFixa){
        int i = 0;
        int eOu = 7;        //e,ou              //Converter para constante
        int aritmeticos = 6;//+,-,*div          //Converter para constante
        int relacionais = 5;//>,<,>=,<=,=,!=    //Converter para constante
        int comparacao = 4; //=,!=,             //Converter para constante
        int uAritmetico = 3;//+,-               //Converter para constante
        int negacao = 2;    //nao               //Converter para constante
        int inteiro = 1;    //numero ouvariavel //Converter para constante
        int booleano = 0;   //booleano          //Converter para constante       
        
        ArrayList<Integer> expressaoTipos;//Necessario ArrayList para deletar elemento
        expressaoTipos = geraTabelaDeTipos(expressaoPosFixa);

        //print(expressaoTipos);
        
        //Se encontrar 3 operandos avanca uma posicao
        int op1, op2, op3;
        while(expressaoTipos.size() >= 3){
            //print(expressaoTipos);   
            
            op1 = expressaoTipos.get(i);
            op2 = expressaoTipos.get(i+1);
            op3 = expressaoTipos.get(i+2);
            
            if(op1 == inteiro && op2 == uAritmetico){
                expressaoTipos.set(i+1, inteiro);
                expressaoTipos.remove(i);
                i=0;
            }else if(op1 == booleano && op2 == negacao){
                expressaoTipos.set(i+1, booleano);
                expressaoTipos.remove(i);
                i=0;
            }else if(op1<2 && op2<2 && op3 <= 3){
                //Se op1 e op2 != de numeros e op3 diferente de numero e unarios
                //Avanca indice das comparacoes
                //System.out.println("Tres operandos");
                i++;
            }else if(op1 == inteiro && op2 == inteiro && op3 == aritmeticos){
                expressaoTipos.set(i+2, inteiro);
                expressaoTipos.remove(i);
                expressaoTipos.remove(i);
                i=0;
            }else if(op1 == inteiro && op2 == inteiro && op3 == relacionais){
                expressaoTipos.set(i+2, booleano);
                expressaoTipos.remove(i);
                expressaoTipos.remove(i);
                i=0;
            }else if(op1 == booleano && op2 == booleano && op3 == comparacao){
                expressaoTipos.set(i+2, booleano);
                expressaoTipos.remove(i);
                expressaoTipos.remove(i);
                i=0;
            }else if(op1 == booleano && op2 == booleano && op3 == eOu){
                expressaoTipos.set(i+2, booleano);
                expressaoTipos.remove(i);
                expressaoTipos.remove(i);
                i=0;
            }else if(op1 == inteiro && op2 == inteiro && op3 == comparacao){
                expressaoTipos.set(i+2, booleano);
                expressaoTipos.remove(i);
                expressaoTipos.remove(i);
                i=0;
            }else return -1;
        } 
        
        if(expressaoTipos.size() == 2){
            op1 = expressaoTipos.get(i);
            op2 = expressaoTipos.get(i+1);
            
            if(op1 == inteiro && op2 == uAritmetico){
                expressaoTipos.set(i+1, inteiro);
                expressaoTipos.remove(i); 
            }else if(op1 == booleano && op2 == negacao){
                expressaoTipos.set(i+1, booleano);
                expressaoTipos.remove(i);
            }
        }
        
        if(expressaoTipos.size() == 1){
            if (expressaoTipos.get(0) == 1){
                return 1;
            }
            if (expressaoTipos.get(0) == 0){
                return 0;
            }
        }
        return -1;
    }
    
    private ArrayList geraTabelaDeTipos(ArrayList<Token> expressaoPosFixa){
        int i=0;
        int index;
        String t;
        ArrayList<Integer> tabelaDeTipos = new ArrayList(); 
        
        while(i<expressaoPosFixa.size()){            
            t = expressaoPosFixa.get(i).getLexema();
            if("e".equals(t)|| "ou".equals(t)){
                tabelaDeTipos.add(7);
            }else if("+".equals(t) || "-".equals(t) || "*".equals(t) || "div".equals(t)){
                tabelaDeTipos.add(6);
            }else if(">".equals(t) || "<".equals(t) || ">=".equals(t) || "<=".equals(t)){
                tabelaDeTipos.add(5);
            }else if("=".equals(t) || "!=".equals(t)){
                tabelaDeTipos.add(4);
            }else if("+ ".equals(t) || "- ".equals(t)){//Unarios
                tabelaDeTipos.add(3);
            }else if("nao".equals(t)){
                tabelaDeTipos.add(2);
            }else{

                index = pesquisa_tabela(t);                
                if(index>0 && "inteiro".equals(tabelaDeSimbolos.get(index).getTipo())){
                    tabelaDeTipos.add(1);
                }else if(index>0 && "booleano".equals(tabelaDeSimbolos.get(index).getTipo())){
                    tabelaDeTipos.add(0);
                }else if ("verdadeiro".equals(t) || "falso".equals(t)){//Eh pra ter sobrado apenas numero
                    tabelaDeTipos.add(0);
                }else if("".equals(t)){////////////////////////////////////////
                }else{//Eh pra ter sobrado so numero
                    tabelaDeTipos.add(1);
                }          
            }            
            i++;          
        }
        return tabelaDeTipos;
    }  
    private void print(ArrayList expressaoTipos){
            for(int j = 0; j < expressaoTipos.size(); j++){
                System.out.print(expressaoTipos.get(j) + " ");
            }
        System.out.println("");
    }    
    private class OperadorPrecedecia{
        public int preced;
        public String operador;
        public String codigoVM;
        public OperadorPrecedecia(int p, String o, String cod){
            preced = p;
            operador = o;
            codigoVM = cod;
        }
    } 
    
    
    public ArrayList convertePosFixa(ArrayList<Token> expressao){
        //!!!!!!!!!!!VERIFICAR UNARIO DEPOIS DE PARENTESES!!!!!!!!!!!!!!!!!!!!!!
        ArrayList<Token> expressaoPosFixa =  new ArrayList();  
        Stack<OperadorPrecedecia> pilha = new Stack();
        String lexeamaTemp, codigoVM = "";
        boolean podeOperadorUnario = true;//Proximo operador pode ser unario
        int valorPrecedencia = 0;
        int indice;

        while(expressao.size()>0){
            lexeamaTemp = expressao.get(0).getSimbolo();
            
            if("snumero".equals(lexeamaTemp) || 
                    "sidentificador".equals(lexeamaTemp) ||
                    "sverdadeiro".equals(lexeamaTemp) ||
                    "sfalso".equals(lexeamaTemp) ){
                
                if ("sidentificador".equals(lexeamaTemp)){
                    indice = pesquisa_tabela(expressao.get(0).getLexema());
                    
                    if(pesquisa_declfunc_tabela(expressao.get(0).getLexema())){
                        expressaoPosFixa.add(new Token(expressao.get(0).getLexema(),
                        "CALL L", tabelaDeSimbolos.get(indice).getMemoriaRotulo()));
                        ////////////////////////////////////////////////////////
                        expressaoPosFixa.add(new Token("","LDV 0", -1));
                        ////////////////////////////////////////////////////////
                        
                    }else if(!pesquisa_declproc_tabela(expressao.get(0).getLexema())){
                        expressaoPosFixa.add(new Token(expressao.get(0).getLexema(),
                        "LDV ", tabelaDeSimbolos.get(indice).getMemoriaRotulo()));
                    }else{
                        expressaoPosFixa.clear();//Se econtrou proc na expressoa = erro
                        return expressaoPosFixa;
                    }                
                }else if("sverdadeiro".equals(lexeamaTemp)){
                    expressaoPosFixa.add(new Token(expressao.get(0).getLexema(),
                    "LDC ", 1));
                }else if("sfalso".equals(lexeamaTemp)){
                    expressaoPosFixa.add(new Token(expressao.get(0).getLexema(),
                    "LDC ", 0));
                }else if("snumero".equals(lexeamaTemp)){
                    expressaoPosFixa.add(new Token(expressao.get(0).getLexema(), 
                    "LDC ", Integer.parseInt(expressao.get(0).getLexema()))); 
                }       
                expressao.remove(0);
                podeOperadorUnario = false;
                continue;
               
            }else if("sou".equals(lexeamaTemp)){
                valorPrecedencia = 1;
                codigoVM = "OR";
            }else if("se".equals(lexeamaTemp)){//And!
                valorPrecedencia = 2;
                codigoVM = "AND";
            }else if("sig".equals(lexeamaTemp)){
                valorPrecedencia = 3;
                codigoVM = "CEQ";
            }else if("sdif".equals(lexeamaTemp)){
                valorPrecedencia = 3;
                codigoVM = "CDIF";
            }else if("smaior".equals(lexeamaTemp)){
                valorPrecedencia = 4;
                codigoVM = "CMA";
            }else if("smaiorig".equals(lexeamaTemp)){
                valorPrecedencia = 4;
                codigoVM = "CMAQ";
            }else if("smenor".equals(lexeamaTemp)){
                valorPrecedencia = 4;
                codigoVM = "CME";
            }else if("smenorig".equals(lexeamaTemp)){
                valorPrecedencia = 4;        
                codigoVM = "CMEQ";
                
                
            }else if("smenos".equals(lexeamaTemp) ||
                    "smais".equals(lexeamaTemp)){
                if(podeOperadorUnario){
                    if("smenos".equals(lexeamaTemp)){
                        codigoVM = "INV";
                    }else{
                    expressao.remove(0);
                    continue;// codigoVM = "";//Mais(+) unario 
                    }                    
                    expressao.get(0).setLexema(expressao.get(0).getLexema() + " ");
                    valorPrecedencia = 7;//Unario
                }else{
                    if("smenos".equals(lexeamaTemp)){
                        codigoVM = "SUB";
                    }else{
                        codigoVM = "ADD";
                    }
                    valorPrecedencia = 5;
                }
            }else if("smult".equals(lexeamaTemp)){
                valorPrecedencia = 6;
                codigoVM = "MULT";
            }else if("sdiv".equals(lexeamaTemp)){
                valorPrecedencia = 6;
                codigoVM = "DIV";
            }else if("snao".equals(lexeamaTemp) ){
                valorPrecedencia = 7;
                codigoVM = "NEG";
            }else if("sabre_parenteses".equals(lexeamaTemp)){                
                valorPrecedencia = 8;//sempre insere na pilha
            }else if("sfecha_parenteses".equals(lexeamaTemp)){
                if (pilha.size() > 0){
                    while (!"(".equals(pilha.lastElement().operador)){
                        if(")".equals(pilha.lastElement().operador)){
                            pilha.pop();
                        }else{
                            expressaoPosFixa.add(new Token(pilha.lastElement().operador,pilha.lastElement().codigoVM,-1));
                            pilha.pop();
                        }
                    }
                    pilha.pop();
                }else System.out.println("Erro Semantico");
                expressao.remove(0);
                continue;
            }      
           
            while(pilha.size()>0 && pilha.lastElement().preced >= valorPrecedencia
                                 && pilha.lastElement().preced != 8){
                if(")".equals(pilha.lastElement().operador)){
                    pilha.pop();
                }else{
                    expressaoPosFixa.add(new Token(pilha.lastElement().operador,pilha.lastElement().codigoVM,-1));
                    pilha.pop();
                }                
            }
            pilha.add(new OperadorPrecedecia(valorPrecedencia, expressao.get(0).getLexema(), codigoVM));
            expressao.remove(0);
            
            if(valorPrecedencia == 8 || valorPrecedencia == 4){
                podeOperadorUnario = true;
            }else{
                podeOperadorUnario = false;
            }            
        }
        
        while(expressao.isEmpty() && pilha.size()>0){
            if(")".equals(pilha.lastElement().operador)){
                pilha.pop(); 
            }else{
                expressaoPosFixa.add(new Token(pilha.lastElement().operador,pilha.lastElement().codigoVM,-1));
                pilha.pop();
            }
        }
        
        /*
        for(int i = 0; i < expressaoPosFixa.size(); i++){
            System.out.print(expressaoPosFixa.get(i).getLexema());
        }        
        System.out.println("");
        */
        
        return expressaoPosFixa;
    }
    
    public int testesIf(){
            int x = 0;
        
        if (x>2){
            if(x>3){
                return 1;
            }else{
                return 3;
            }
        }
            return 3;

    }
}
