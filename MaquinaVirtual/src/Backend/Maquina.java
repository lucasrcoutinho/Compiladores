
package Backend;

import java.util.ArrayList;

public class Maquina {
    private int reg_i = 0;  //Registrador Programa
    private int reg_s = 0;  // Registrador Dados
    
    private PilhaDados pilhaDados = new PilhaDados();
    private PilhaPrograma pilhaPrograma = new PilhaPrograma();
    
    public void preenchePilhaPrograma(ArrayList<String> dados)
    {
        for(String linha : dados)
        {
            pilhaPrograma.insereDado(linha);
        }
        
        for(String linha : dados)
        {
            System.out.println(linha);
        }
    }
    
    public void executaInstrucoes(boolean debug, int parada)
    {
        int contadorInstExecutadas = 0;
        
        String linha = pilhaPrograma.lePilha(reg_i);
        String[] instrucao = linha.split(" "); //pos 0 = instrucao, pos 1 = atriuto
        
        while(instrucao != null)
        {
            contadorInstExecutadas++;
            
            if(debug && contadorInstExecutadas == parada + 1)
            {
                break;
            }
            else
            {
                switch(instrucao[0])
                {
                    case "LDC" :
                    {
                        reg_s++;
                        pilhaDados.insereDado(Double.parseDouble(instrucao[1]), reg_s);  //inseriando o dado contido no atributo da instrucao na posicao reg_s
                    }
                    break;
                    
                    case "LDV" : 
                    {
                        reg_s++;
                        pilhaDados.insereDado(pilhaDados.lePilha(Integer.parseInt(instrucao[1])), reg_s);  //inseriando o dado contido no atributo da instrucao na posicao reg_s
                    }
                    break;
                    
                    case "ADD" : 
                    {
                        double num1 = pilhaDados.lePilha(reg_s - 1);
                        double num2 = pilhaDados.lePilha(reg_s);
                        
                        pilhaDados.insereDado((num1 + num2), (reg_s - 1));
                        reg_s--;
                    }
                    break;
                    
                    case "SUB" : 
                    {
                        double num1 = pilhaDados.lePilha(reg_s - 1);
                        double num2 = pilhaDados.lePilha(reg_s);
                        
                        pilhaDados.insereDado((num1 - num2), (reg_s - 1));
                        reg_s--;
                    }
                    break;
                    
                    case "MULT" : 
                    {
                        double num1 = pilhaDados.lePilha(reg_s - 1);
                        double num2 = pilhaDados.lePilha(reg_s);
                        
                        pilhaDados.insereDado((num1 * num2), (reg_s - 1));
                        reg_s--;
                    }
                    break;
                    
                    case "DIVI" :
                    {
                        double num1 = pilhaDados.lePilha(reg_s - 1);
                        double num2 = pilhaDados.lePilha(reg_s);
                        
                        pilhaDados.insereDado((num1 / num2), (reg_s - 1));
                        reg_s--;
                    }
                    break;
                    
                    case "INV" :
                    {
                        double num = pilhaDados.lePilha(reg_s);
                        pilhaDados.insereDado((num*(-1)), reg_s);
                    }
                    break;
                    
                    case "AND" :
                    {
                        double num1 = pilhaDados.lePilha(reg_s - 1);
                        double num2 = pilhaDados.lePilha(reg_s);
                        
                        if(num1 == 1 && num2 == 1)
                        {
                             pilhaDados.insereDado((double)1, (reg_s - 1));
                        }
                        else
                        {
                            pilhaDados.insereDado((double)0, (reg_s - 1));
                        }
                        reg_s--;
                    }
                    break;
                    
                    case "OR" :
                    {
                        double num1 = pilhaDados.lePilha(reg_s - 1);
                        double num2 = pilhaDados.lePilha(reg_s);
                        
                        if(num1 == 1 || num2 == 1)
                        {
                             pilhaDados.insereDado((double)1, (reg_s - 1));
                        }
                        else
                        {
                            pilhaDados.insereDado((double)0, (reg_s - 1));
                        }
                        reg_s--;
                    }
                    break;
                    
                    case "NEG" :
                    {
                        double num = pilhaDados.lePilha(reg_s);
                        pilhaDados.insereDado((1 - num), reg_s);
                    }
                    break;
                    
                    case "CME" : 
                    {
                        double num1 = pilhaDados.lePilha(reg_s - 1);
                        double num2 = pilhaDados.lePilha(reg_s);
                        
                        if(num1 < num2)
                        {
                             pilhaDados.insereDado((double)1, (reg_s - 1));
                        }
                        else
                        {
                            pilhaDados.insereDado((double)0, (reg_s - 1));
                        }
                        reg_s--;
                    }
                    break;
                    
                    case "CMA" :
                    {
                        double num1 = pilhaDados.lePilha(reg_s - 1);
                        double num2 = pilhaDados.lePilha(reg_s);
                        
                        if(num1 > num2)
                        {
                             pilhaDados.insereDado((double)1, (reg_s - 1));
                        }
                        else
                        {
                            pilhaDados.insereDado((double)0, (reg_s - 1));
                        }
                        reg_s--;
                    }
                    break;
                    
                    case "CEQ" :
                    {
                        double num1 = pilhaDados.lePilha(reg_s - 1);
                        double num2 = pilhaDados.lePilha(reg_s);
                        
                        if(num1 == num2)
                        {
                             pilhaDados.insereDado((double)1, (reg_s - 1));
                        }
                        else
                        {
                            pilhaDados.insereDado((double)0, (reg_s - 1));
                        }
                        reg_s--;
                    }
                    break;
                    
                    case "CDIF" :
                     {
                        double num1 = pilhaDados.lePilha(reg_s - 1);
                        double num2 = pilhaDados.lePilha(reg_s);
                        
                        if(num1 != num2)
                        {
                             pilhaDados.insereDado((double)1, (reg_s - 1));
                        }
                        else
                        {
                            pilhaDados.insereDado((double)0, (reg_s - 1));
                        }
                        reg_s--;
                    }
                    break;
                    
                    case "CMEQ" :
                    {
                        double num1 = pilhaDados.lePilha(reg_s - 1);
                        double num2 = pilhaDados.lePilha(reg_s);
                        
                        if(num1 <= num2)
                        {
                             pilhaDados.insereDado((double)1, (reg_s - 1));
                        }
                        else
                        {
                            pilhaDados.insereDado((double)0, (reg_s - 1));
                        }
                        reg_s--;
                    }
                    break;
                    
                    case "CMAQ" :
                    {
                        double num1 = pilhaDados.lePilha(reg_s - 1);
                        double num2 = pilhaDados.lePilha(reg_s);
                        
                        if(num1 >= num2)
                        {
                             pilhaDados.insereDado((double)1, (reg_s - 1));
                        }
                        else
                        {
                            pilhaDados.insereDado((double)0, (reg_s - 1));
                        }
                        reg_s--;
                    }
                    break;
                    
                    case "START" :
                        reg_s = -1;
                    break;
                    
                    case "HLT" :
                    {
                        debug = true;
                        contadorInstExecutadas = parada + 1; 
                    }
                    break;
                    
                    case "SRT" :
                    {
                        int n = Integer.parseInt(instrucao[1]);
                        pilhaDados.insereDado(pilhaDados.lePilha(reg_s), n);
                        
                        reg_s--;
                    }
                    break;
                    
                    case "JMP" : 
                    {
                        reg_i = Integer.parseInt(instrucao[1]);
                    }
                    break;
                    
                    case "JMPF" :
                    {
                        if(pilhaDados.lePilha(reg_s) == 0)
                        {
                            reg_i = Integer.parseInt(instrucao[1]);
                        }
                        else
                        {
                            reg_i = reg_i + 1;
                        }
                        
                        reg_s--;
                    }
                    break;
                    
                    case "NULL" :
                    {
                        //nao faz nada
                    }
                    break;
                    
                    case "RD" : System.out.println("");   //!!!!!!!!!!!!!!!!!!!Implementar
                    break;
                    
                    case "PRN" : System.out.println("");  //!!!!!!!!!!!!!!!!!!!Implementar
                    break;
                    
                    case "ALLOC" :
                    {
                        String[] mn = instrucao[1].split(",");
                        int m = Integer.parseInt(mn[0]);
                        int n = Integer.parseInt(mn[1]);
                        
                        for(int k = 0; k < n-1; k++)
                        {
                            reg_s++;
                            pilhaDados.insereDado(pilhaDados.lePilha(m+k), pilhaDados.lePilha(reg_s).intValue());
                        }
                    }
                    break;
                    
                    case "DALLOC" :
                    {
                        String[] mn = instrucao[1].split(",");
                        int m = Integer.parseInt(mn[0]);
                        int n = Integer.parseInt(mn[1]);
                        
                        for(int k = n-1; k > n - 1; k--) // De 0 ate n -1
                        { 
                            pilhaDados.insereDado(pilhaDados.lePilha(reg_s) , pilhaDados.lePilha(m+k).intValue());
                            reg_s--;
                        }
                    }
                    break;
                    
                    case "CALL" :
                    {
                        reg_s++;
                        pilhaDados.insereDado(((int)reg_i + 1), reg_s);
                        reg_i = Integer.parseInt(instrucao[1]);
                    }
                    break;
                    
                    case "RETURN" :
                    {
                        reg_i = pilhaDados.lePilha(reg_s).intValue();
                        reg_s--;
                    }
                    break;
                }
            }
            
            linha = pilhaPrograma.lePilha(reg_i);
            instrucao = linha.split(" ");
        }
    }
}
