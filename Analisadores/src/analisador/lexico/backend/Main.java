/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analisador.lexico.backend;

import analisador.lexico.frontend.MainFrame;
import analisador.lexico.frontend.MainFrame2;
import analisador.semantico.TestaSemantico_SEPARADO;
import geracao.de.codigo.GeradorDeCodigo;
import javax.swing.text.BadLocationException;

/**
 *
 * @author lucas
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws BadLocationException {
        new MainFrame2();
        //Facade instaciaFacade = new Facade();
        //new MainFrame().setVisible(true);
        //TestaSemantico_SEPARADO s = new TestaSemantico_SEPARADO();
        //s.iniciaTeste();//Inicia teste da tabela de simbolos
        //s.chamaCompatibiliza();//Inicia Teste de compatibilizacao
        //s.iniciaTeste();
        //GeradorDeCodigo gera = new GeradorDeCodigo();
        //gera.salvaCodigo();
        
    }
    
}