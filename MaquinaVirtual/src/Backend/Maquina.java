
package Backend;

import java.util.ArrayList;

public class Maquina {
    private int reg_i = 0;  //Registrador Programa
    private int reg_s = 0;  // Registrador Dados
    
    private PilhaDados pilhaDados = PilhaDados.getInstance();
    private PilhaPrograma pilhaPrograma = PilhaPrograma.getInstance();
    
    public void preenchePilhaPrograma(ArrayList<String> dados)
    {
        for(String linha : dados)
        {
            pilhaPrograma.insereDado(linha);
        }
        
        pilhaPrograma.insereDado(null);
        
        /*for(String linha : dados)
        {
            System.out.println(linha);
        }*/
    } 
    
    public void executaInstrucoes(boolean debug, int parada)
    {
        int contadorInstExecutadas = 0;
        double num1 = 0;
        double num2 = 0;
        String[] mn;
        int n;
        int m;
        
        String linha = pilhaPrograma.lePilha(reg_i);
        String[] instrucao = linha.split(" "); //pos 0 = instrucao, pos 1 = atriuto
        System.out.println(instrucao[0]);
        
        while(true)
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
                        reg_s++;
                        pilhaDados.insereDado(Double.parseDouble(instrucao[1]), reg_s);  //inseriando o dado contido no atributo da instrucao na posicao reg_s
                    break;
                    
                    case "LDV" : 
                        reg_s++;
                        pilhaDados.insereDado(pilhaDados.lePilha(Integer.parseInt(instrucao[1])), reg_s);  //inseriando o dado contido no atributo da instrucao na posicao reg_s
                    break;
                    
                    case "ADD" : 
                        num1 = pilhaDados.lePilha(reg_s - 1);
                        num2 = pilhaDados.lePilha(reg_s);
                        
                        pilhaDados.insereDado((num1 + num2), (reg_s - 1));
                        reg_s--;
                    break;
                    
                    case "SUB" : 
                        num1 = pilhaDados.lePilha(reg_s - 1);
                        num2 = pilhaDados.lePilha(reg_s);                        
                        pilhaDados.insereDado((num1 - num2), (reg_s - 1));
                        reg_s--;
                    break;
                    
                    case "MULT" : 
                        num1 = pilhaDados.lePilha(reg_s - 1);
                        num2 = pilhaDados.lePilha(reg_s);
                        
                        //pilhaDados.insereDado((num1 * num2), (reg_s - 1));
                        //reg_s--;
                    break;
                    
                    case "DIVI" :
                        num1 = pilhaDados.lePilha(reg_s - 1);
                        num2 = pilhaDados.lePilha(reg_s);
                        
                        pilhaDados.insereDado((num1 / num2), (reg_s - 1));
                        reg_s--;
                    break;
                    
                    case "INV" :
                        num1 = pilhaDados.lePilha(reg_s);
                        pilhaDados.insereDado((num1*(-1)), reg_s);
                    break;
                    
                    case "AND" :
                        num1 = pilhaDados.lePilha(reg_s - 1);
                        num2 = pilhaDados.lePilha(reg_s);
                        
                        if(num1 == 1 && num2 == 1)                        {
                             pilhaDados.insereDado((double)1, (reg_s - 1));
                        }
                        else{
                            pilhaDados.insereDado((double)0, (reg_s - 1));
                        }
                        //reg_s--;
                    break;
                    
                    case "OR" :
                        num1 = pilhaDados.lePilha(reg_s - 1);
                        num2 = pilhaDados.lePilha(reg_s);
                        
                        if(num1 == 1 || num2 == 1){
                            pilhaDados.insereDado((double)1, (reg_s - 1));
                        }
                        else                        {
                            pilhaDados.insereDado((double)0, (reg_s - 1));
                        }
                        reg_s--;
                    break;
                    
                    case "NEG" :
                        num1 = pilhaDados.lePilha(reg_s);
                        pilhaDados.insereDado((1 - num1), reg_s);
                    break;
                    
                    case "CME" : 
                        num1 = pilhaDados.lePilha(reg_s - 1);
                        num2 = pilhaDados.lePilha(reg_s);
                        
                        if(num1 < num2)                        {
                             pilhaDados.insereDado((double)1, (reg_s - 1));
                        }
                        else                        {
                            pilhaDados.insereDado((double)0, (reg_s - 1));
                        }
                        reg_s--;
                    break;
                    
                    case "CMA" :
                        num1 = pilhaDados.lePilha(reg_s - 1);
                       num2 = pilhaDados.lePilha(reg_s);
                        
                        if(num1 > num2)                        {
                             pilhaDados.insereDado((double)1, (reg_s - 1));
                        }
                        else                        {
                            pilhaDados.insereDado((double)0, (reg_s - 1));
                        }
                        reg_s--;
                    break;
                    
                    case "CEQ" :
                        num1 = pilhaDados.lePilha(reg_s - 1);
                        num2 = pilhaDados.lePilha(reg_s);
                        
                        if(num1 == num2){
                             pilhaDados.insereDado((double)1, (reg_s - 1));
                        }
                        else{
                            pilhaDados.insereDado((double)0, (reg_s - 1));
                        }
                        reg_s--;
                    break;
                    
                    case "CDIF" :
                        num1 = pilhaDados.lePilha(reg_s - 1);
                        num2 = pilhaDados.lePilha(reg_s);
                        
                        if(num1 != num2){
                             pilhaDados.insereDado((double)1, (reg_s - 1));
                        }
                        else{
                            pilhaDados.insereDado((double)0, (reg_s - 1));
                        }
                        reg_s--;
                    break;
                    
                    case "CMEQ" :
                        num1 = pilhaDados.lePilha(reg_s - 1);
                        num2 = pilhaDados.lePilha(reg_s);
                        
                        if(num1 <= num2){
                             pilhaDados.insereDado((double)1, (reg_s - 1));
                        }
                        else{
                            pilhaDados.insereDado((double)0, (reg_s - 1));
                        }
                        reg_s--;
                    break;
                    
                    case "CMAQ" :
                        num1 = pilhaDados.lePilha(reg_s - 1);
                        num2 = pilhaDados.lePilha(reg_s);
                        
                        if(num1 >= num2){
                             pilhaDados.insereDado((double)1, (reg_s - 1));
                        }
                        else{
                            pilhaDados.insereDado((double)0, (reg_s - 1));
                        }
                        reg_s--;
                    break;
                    
                    case "START" :
                        reg_s = -1;
                    break;
                    
                    case "HLT" :
                        debug = true;
                        contadorInstExecutadas = parada + 1; 
                    break;
                    
                    case "SRT" :
                        n = Integer.parseInt(instrucao[1]);
                        pilhaDados.insereDado(pilhaDados.lePilha(reg_s), n);
                        
                        reg_s--;
                    break;
                    
                    case "JMP" : 
                        reg_i = Integer.parseInt(instrucao[1]);
                    break;
                    
                    case "JMPF" :
                        if(pilhaDados.lePilha(reg_s) == 0){
                            reg_i = Integer.parseInt(instrucao[1]);
                        }
                        else{
                            reg_i = reg_i + 1;
                        }
                        
                        reg_s--;
                   break;
                    
                    case "NULL" :
                        //nao faz nada
                    break;
                    
                    case "RD" : System.out.println("");   //!!!!!!!!!!!!!!!!!!!Implementar
                    break;
                    
                    case "PRN" : System.out.println("");  //!!!!!!!!!!!!!!!!!!!Implementar
                    break;
                    
                    case "ALLOC" :
                        mn = instrucao[1].split(",");
                        m = Integer.parseInt(mn[0]);
                        n = Integer.parseInt(mn[1]);
                        for(int k = 0; k < 1-1; k++){
                            reg_s++;
                            pilhaDados.insereDado(pilhaDados.lePilha(m+k), pilhaDados.lePilha(reg_s).intValue());
                        }
                    break;
                    
                    case "DALLOC" :
                        mn = instrucao[1].split(",");
                        m = Integer.parseInt(mn[0]);
                        n = Integer.parseInt(mn[1]);                      
                        for(int k = n-1; k > n - 1; k--){ // De 0 ate n -1 
                            pilhaDados.insereDado(pilhaDados.lePilha(reg_s) , pilhaDados.lePilha(m+k).intValue());
                            reg_s--;
                        }
                    break;
                    
                    case "CALL" :
                        reg_s++;
                        pilhaDados.insereDado((double)(reg_i + 1), reg_s);
                        reg_i = Integer.parseInt(instrucao[1]);
                    break;
                    
                    case "RETURN" :
                        reg_i = pilhaDados.lePilha(reg_s).intValue();
                        reg_s--;
                    break;
                }
            }
            
            atualizaMainFrame();
            
            reg_i++;           
            linha = pilhaPrograma.lePilha(reg_i);
            if(linha == null)
            {
                break;
            }
            else
            {
                instrucao = linha.split(" ");
                System.out.println(instrucao[0].toString());
            }
        }
    }
    
    private void atualizaMainFrame()
    {
        Facede instanciaFacede = Facede.getInstance();
        instanciaFacede.atualizaTabelas();
    }
}
