package Backend;

import java.util.ArrayList;

public class PilhaDados {
    
    private ArrayList<String> pilhaDados = new ArrayList();
    
    public int insereDado(String dado)
    {
        pilhaDados.add(dado);
        return pilhaDados.size();
    }
    
    public String lePilha(int posicao)
    {
        return pilhaDados.get(posicao);
    }
    
    public int excluiDado(int posicao)
    {
        pilhaDados.remove(posicao);
        return pilhaDados.size();
    }  
}
