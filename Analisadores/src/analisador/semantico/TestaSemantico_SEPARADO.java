/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analisador.semantico;

import java.util.ArrayList;

/**
 *
 * @author lucas
 */
public class TestaSemantico_SEPARADO {    
    /*
    –Token. Lexema = lexema do token
    –tipo do lexema = nome de programa, variável, procedimento, função inteiro, função boolean.
    –Nível: L: marca ou novo galho
    –Rótulo: Para geração de código
    */
    AnalisadorSemantico s = new AnalisadorSemantico();
    public void chamaCompatibiliza(){
        ArrayList<String> expressaoPosFixa = new ArrayList();
               
        System.out.println(s.compatibilizacaoTipos(expressaoPosFixa));
    }
    
    public void iniciaTeste(){

        s.insere_tabela("Teste1", "tipoPrograma", "L", 0);        
        s.insere_tabela("Varia1", "tipoVariavel", "", 0);
        s.insere_tabela("Varia2", "tipoVariavel", "", 0);
        s.insere_tabela("Varia3", "tipoVariavel", "", 0);
        s.coloca_tipo("inteiro");//Confirmar se está verificando o idenficador de tipo
        s.insere_tabela("Varia4", "tipoVariavel", "", 0);
        s.insere_tabela("Varia5", "tipoVariavel", "", 0);
        s.coloca_tipo("booleano");
        s.desempilha();
        //s.imprimeTabelaSimbolos();
        
        s.insere_tabela("Teste2", "tipoFuncao", "L", 0);
        s.coloca_tipo("booleano");
        s.insere_tabela("Varia6", "tipoVariavel", "", 0);
        s.insere_tabela("Varia7", "tipoVariavel", "", 0);
        if (s.pesquisa_duplicvar_tabela("Teste2"))
            System.out.println("*****Variavel Duplicada********");
        s.coloca_tipo("booleano");
        s.desempilha();
        //s.imprimeTabelaSimbolos();
        s.insere_tabela("Teste3", "tipoProcedim", "L", 0);
        s.desempilha();
        s.imprimeTabelaSimbolos();
    }
}
