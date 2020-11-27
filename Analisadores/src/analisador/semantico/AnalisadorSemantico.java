/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analisador.semantico;

import analisador.lexico.backend.Token;
import analisador.semantico.Simbolo;
import java.awt.List;
import java.util.ArrayList;
import java.util.Stack;

/**
 *
 * @author lucas
 */

public class AnalisadorSemantico {
    private ArrayList<Simbolo> tabelaDeSimbolos;
    //private int nivelEscopo;
    
    public AnalisadorSemantico(){    
        tabelaDeSimbolos = new ArrayList();
        //nivelEscopo = 0;
    }

    public void imprimeTabelaSimbolos(){
        for(int i = 0; i < tabelaDeSimbolos.size(); i++){
            //for(int cont = 0; cont < tabelaDeSimbolos.get(i).getNivelEscopo(); cont++){
            //    System.out.print("\t");
            //}
            System.out.print("Nome: " + tabelaDeSimbolos.get(i).getLexema());
            System.out.print("| Tipo: " + tabelaDeSimbolos.get(i).getTipoLexema());
            System.out.print("| Nivel: " + tabelaDeSimbolos.get(i).getNivelEscopo());
            if(tabelaDeSimbolos.get(i).getTipo() != ""){
                System.out.print("| Tipo: " + tabelaDeSimbolos.get(i).getTipo());
            }
            //if(tabelaDeSimbolos.get(i).getProcedimentoCorrente() == true){
            //    System.out.println(" *");
            //}else
            System.out.println("");
        }
    
    }
    
    public void insere_tabela(String lexema, String tipoLexema,String nivel, int rotulo){
        SimboloVariavel var = new SimboloVariavel();
        var.addInfo(lexema, tipoLexema, nivel, rotulo);
        tabelaDeSimbolos.add(var);
    }
    /*
    public void insere_SubRotina_tabela(String lexema, String tipoLexema,int nivel, int rotulo){
        Simbolo subRotina = new Simbolo();
        subRotina.addInfo(lexema, tipoLexema, nivel, rotulo);
        desmarcaProcCorrente();
        subRotina.setProcedimentoCorrente(true);//Marca atual como corrente
        tabelaDeSimbolos.add(subRotina);
    }
    
    
    public void coloca_tipo_funcao(String lexemaTipoFuncao){
        int i = tabelaDeSimbolos.size()-1; 
        tabelaDeSimbolos.get(i).setTipo(lexemaTipoFuncao);
    }
    */
    
    public void coloca_tipo(String lexemaTipoVar){  
        int i = tabelaDeSimbolos.size()-1;
        
        if(tabelaDeSimbolos.get(i).getNivelEscopo() == "L" &&
           tabelaDeSimbolos.get(i).getTipo() == ""){
            tabelaDeSimbolos.get(i).setTipo(lexemaTipoVar);
        }else 
        while(i > 0 && tabelaDeSimbolos.get(i).getNivelEscopo() == "" && 
                       tabelaDeSimbolos.get(i).getTipo() == ""){
            tabelaDeSimbolos.get(i).setTipo(lexemaTipoVar);
            i--;
        }          
    }
    
    /*
    private void desmarcaProcCorrente(){
        if(tabelaDeSimbolos.size() == 0) return;
        int i = tabelaDeSimbolos.size()-1;        
        while(!tabelaDeSimbolos.get(i).getProcedimentoCorrente() && i >= 0){
            i--;
        }
        tabelaDeSimbolos.get(i).setProcedimentoCorrente(false);
    }

    private void marcaProcAnterior(){    
        int i = tabelaDeSimbolos.size()-1;
        
        while(!tabelaDeSimbolos.get(i).getProcedimentoCorrente() && i > 0 &&
               tabelaDeSimbolos.get(i).getTipoLexema() != "tipoVariavel"){
            i--;
        }
        tabelaDeSimbolos.get(i).setProcedimentoCorrente(true);
    }*/
    
    //Metodo Remove (recolhe nivel) da tabela de simbolos
    //#################Problema quando desempilhar e tiver um proc ou fuc desempilhado#############################
    //#################Esse proc ou func vai ser marcado como anterior########################
    public void desempilha(){       
        int i = tabelaDeSimbolos.size()-1;
        
        while(tabelaDeSimbolos.get(i).getNivelEscopo() != "L" && i > 0){
            tabelaDeSimbolos.remove(i);
            i--;
        }
        if (tabelaDeSimbolos.get(i).getTipoLexema() != "tipoPrograma"){
            tabelaDeSimbolos.get(i).setNivelEscopo("");
        }
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
                if(tabelaDeSimbolos.get(i).getNivelEscopo() == "L"){
                    nivelAtual = false;
                }
                if(lexema.equals(tabelaDeSimbolos.get(i).getLexema())){//E encontrou duplicidade
                    return true;
                }
            }else{//Esta em outro nivel
                if(tabelaDeSimbolos.get(i).getTipoLexema() != "tipoVariavel"){//Não compara mais com variaveis
                    if(lexema == tabelaDeSimbolos.get(i).getLexema()){//E encontrou duplicidade
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
                tabelaDeSimbolos.get(i).getTipoLexema() == "tipoVariavel"){//E encontrou duplicidade
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
                (tabelaDeSimbolos.get(i).getTipoLexema() == "tipoFuncao" ||
                 tabelaDeSimbolos.get(i).getTipoLexema() == "tipoVariavel" )){
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
                tabelaDeSimbolos.get(i).getTipoLexema() == "tipoProcedimento"){
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
                tabelaDeSimbolos.get(i).getTipoLexema() == "tipoFuncao"){
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
    
    public String buscaTipoFuncao(int lexema){
        return tabelaDeSimbolos.get(lexema).getTipo();
    }

    public int compatibilizacaoTipos(ArrayList expressaoPosFixa){
        //ab+c+d*
        //abc*+
        int i = 0;
        int eOu = 7;        //e,ou              //Converter para constante
        int aritmeticos = 6;//+,-,*div          //Converter para constante
        int relacionais = 5;//>,<,>=,<=,=,!=    //Converter para constante
        int comparacao = 4; //=,!=,             //Converter para constante
        int uAritmetico = 3;//+,-               //Converter para constante
        int negacao = 2;    //nao               //Converter para constante
        int inteiro = 1;    //numero ouvariavel //Converter para constante
        int booleano = 0;   //booleano          //Converter para constante       
        
        ArrayList<Integer> expressaoTipos = new ArrayList();//Necessario ArrayList para deletar elemento
        expressaoTipos = geraTabelaDeTipos(expressaoPosFixa);
        /*
        expressaoTipos.add(1);
        expressaoTipos.add(3);
        expressaoTipos.add(1);
        expressaoTipos.add(1);
        expressaoTipos.add(6);
        expressaoTipos.add(6);
        expressaoTipos.add(1);
        expressaoTipos.add(1);
        expressaoTipos.add(6);
        expressaoTipos.add(1);
        expressaoTipos.add(6);
        expressaoTipos.add(1);
        expressaoTipos.add(6);
        expressaoTipos.add(5);
        */ 
        print(expressaoTipos); 
        //Se encontrar 3 operandos avanca uma posicao
        int op1, op2, op3;
        while(expressaoTipos.size() >= 3){
            print(expressaoTipos);   
            
            op1 = expressaoTipos.get(i);
            op2 = expressaoTipos.get(i+1);
            op3 = expressaoTipos.get(i+2);
            
            if(op1 == inteiro && op2 == uAritmetico){
                //System.out.println("Inteiro aritmetico");
                expressaoTipos.set(i+1, inteiro);
                expressaoTipos.remove(i);
                i=0;
                //continue;
            }else if(op1 == booleano && op2 == negacao){
                //System.out.println("booleano unario(Not)");
                expressaoTipos.set(i+1, booleano);
                expressaoTipos.remove(i);
                i=0;
                //continue;
            }else if(op1<2 && op2<2 && (op3<2 || op3 == 3)){
                //System.out.println("Tres operandos");
                i++;
                //continue;
            }else if(op1 == inteiro && op2 == inteiro && op3 == aritmeticos){
                //System.out.println("Inteiro aritmetico");
                expressaoTipos.set(i+2, inteiro);
                expressaoTipos.remove(i);
                expressaoTipos.remove(i);
                i=0;
                //continue;
            }else if(op1 == inteiro && op2 == inteiro && op3 == relacionais){
                //System.out.println("Inteiro relacionais");
                expressaoTipos.set(i+2, booleano);
                expressaoTipos.remove(i);
                expressaoTipos.remove(i);
                i=0;
                //continue;
            }else if(op1 == booleano && op2 == booleano && op3 == comparacao){
                //System.out.println("Boolearno comparacao");
                expressaoTipos.set(i+2, booleano);
                expressaoTipos.remove(i);
                expressaoTipos.remove(i);
                i=0;
                //continue;
            }else if(op1 == booleano && op2 == booleano && op3 == eOu){
                //System.out.println("Boolearno comparacao");
                expressaoTipos.set(i+2, booleano);
                expressaoTipos.remove(i);
                expressaoTipos.remove(i);
                i=0;
                //continue;
            }else if(op1 == inteiro && op2 == inteiro && op3 == comparacao){
                //System.out.println("Boolearno comparacao");
                expressaoTipos.set(i+2, booleano);
                expressaoTipos.remove(i);
                expressaoTipos.remove(i);
                i=0;
                //continue;
            }else return -1;
        } 
        
        if(expressaoTipos.size() == 2){
            System.out.println("Verifica unario");
            op1 = expressaoTipos.get(i);
            op2 = expressaoTipos.get(i+1);
            
            if(op1 == inteiro && op2 == uAritmetico){
                //System.out.println("Inteiro aritmetico");
                expressaoTipos.set(i+1, inteiro);
                expressaoTipos.remove(i); 
                //continue;
            }else if(op1 == booleano && op2 == negacao){
                //System.out.println("booleano unario(Not)");
                expressaoTipos.set(i+1, booleano);
                expressaoTipos.remove(i);
                //continue;
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
    
    //ab+c+d*12*+
    private ArrayList geraTabelaDeTipos(ArrayList<String> expressaoPosFixa){
        int i=0;
        int index;
        String t;
        ArrayList<Integer> tabelaDeTipos = new ArrayList();        
        
        
        
        while(i<expressaoPosFixa.size()){            
            t = expressaoPosFixa.get(i);
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
                if(index>0 && tabelaDeSimbolos.get(index).getTipo() == "inteiro"){
                    tabelaDeTipos.add(1);
                }else if(index>0 && tabelaDeSimbolos.get(index).getTipo() == "booleano"){
                    tabelaDeTipos.add(0);
                }else if ("verdadeiro".equals(t) || "falso".equals(t)){//Eh pra ter sobrado apenas numero
                    tabelaDeTipos.add(0);
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
        public OperadorPrecedecia(int p, String o){
            preced = p;
            operador = o;
        }
    }
    
    public ArrayList convertePosFixa(ArrayList<Token> expressao){
        //!!!!!!!!!!!VERIFICAR UNARIO DEPOIS DE PARENTESES!!!!!!!!!!!!!!!!!!!!!!
        ArrayList<String> expressaoPosFixa =  new ArrayList();  
        Stack<OperadorPrecedecia> pilha = new Stack();
        String lexeamaTemp;
        boolean podeOperadorUnario = true;//Proximo operador pode ser unario
        int valorPrecedencia = 0;
        
        while(expressao.size()>0){
            lexeamaTemp = expressao.get(0).getLexema();
            
            if(lexeamaTemp == "snumero" || 
               lexeamaTemp == "sidentificador" ||
               lexeamaTemp == "sbooleano"){
               expressaoPosFixa.add(expressao.get(0).getSimbolo());
               //Tem que buscar o tipo na tabela de simbolos
               expressao.remove(0);
               podeOperadorUnario = false;
               continue;
            }else if(lexeamaTemp == "sou"){
                valorPrecedencia = 1;
            }else if(lexeamaTemp == "se"){//And!
                valorPrecedencia = 2;
            }else if(lexeamaTemp == "sig" ||
                     lexeamaTemp == "sdif"){
                valorPrecedencia = 3;
            }else if (lexeamaTemp == "smaior" ||
                      lexeamaTemp == "smaiorig" ||                      
                      lexeamaTemp == "smenor" ||
                      lexeamaTemp == "smenorig"){
                valorPrecedencia = 4;
            }else if(lexeamaTemp == "smenos" ||
                     lexeamaTemp == "smais"){
                if(podeOperadorUnario){
                    valorPrecedencia = 7;//Unario
                    expressao.get(0).setSimbolo(expressao.get(0).getSimbolo() + " ");
                }else{
                    valorPrecedencia = 5;
                }
            }else if(lexeamaTemp == "smult" ||
                     lexeamaTemp == "sdiv"){
                valorPrecedencia = 6;
            }else if(lexeamaTemp == "snao" ){
                valorPrecedencia = 7;
            }else if(lexeamaTemp == "sabre_parenteses"){                
                valorPrecedencia = 8;//sempre insere na pilha
            }else if(lexeamaTemp == "sfecha_parenteses"){
                if (pilha.size() > 0){
                    while (pilha.lastElement().operador != "("){
                        if(pilha.lastElement().operador == ")"){
                            pilha.pop();
                        }else{
                            expressaoPosFixa.add(pilha.pop().operador);
                        }
                    }
                    pilha.pop();
                }else System.out.println("Erro Semantico");
                expressao.remove(0);
                continue;
            }      
           
            while(pilha.size()>0 && pilha.lastElement().preced >= valorPrecedencia
                                 && pilha.lastElement().preced != 8){
                if(pilha.lastElement().operador == ")"){
                    pilha.pop();
                }else{
                    expressaoPosFixa.add(pilha.pop().operador);
                }                
            }
            pilha.add(new OperadorPrecedecia(valorPrecedencia, expressao.get(0).getSimbolo()));
            expressao.remove(0);
            
            if(valorPrecedencia == 8 || valorPrecedencia == 4){
                podeOperadorUnario = true;
            }else{
                podeOperadorUnario = false;
            }            
        }
        
        while(expressao.size()==0 && pilha.size()>0){
            if(pilha.lastElement().operador == ")"){
                pilha.pop(); 
            }else{
                expressaoPosFixa.add(pilha.pop().operador);
            }
        }
        
        for(int i = 0; i < expressaoPosFixa.size(); i++){
            System.out.print(expressaoPosFixa.get(i));
        }
        
        System.out.println("");
        
        return expressaoPosFixa;
    }
    
    public boolean testesIf(){
            int x = 0;
        
        if (x>2){
            x=3;
        }else if(x<2){
            x=1;
        }
        
        /*
        while(x>0){
            return true;
        }
        
        if (x==0){
            return true;
        }else
            return true;
        */
        //return true;
        
        if (x>2){
            return true;
        }else if(x<=2){
            return false;
        }else if (x == x+1){
            
            if (x == 2*x){
                return true;
            }else
                return true;
            
            //return true;
        }else          
            return true;
        
        //return true;
    }
}
