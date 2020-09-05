
package Backend;

import java.util.ArrayList;

public class Maquina {
    private int reg_i;  //Registrador Programa
    private int reg_s;  // Registrador Dados
    
    private PilhaDados pilhaDados = new PilhaDados();
    private PilhaPrograma pilhaPrograma = new PilhaPrograma();
    
    public void preenchePilhaPrograma(ArrayList<String> dados)
    {
        for(String linha : dados)
        {
            pilhaDados.insereDado(linha);
        }
    }
}
