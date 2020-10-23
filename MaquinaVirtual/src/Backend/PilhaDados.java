package Backend;

import java.util.ArrayList;

public class PilhaDados {
    
    private ArrayList<Double> pilhaDados = new ArrayList();
    
    private static PilhaDados instancia;
    
    private PilhaDados()
    {   
    }
    
    //Singleton
    public static synchronized PilhaDados getInstance()
    {
        if(instancia == null)
        {
            instancia = new PilhaDados();
        }
        
        return instancia;
    }
    
    public int insereDado(Double dado, int posicao)
    {
        if(pilhaDados.size() <= posicao) //Verificando se a posicao ja existe, caso sim, sobrescreve.
        {
            pilhaDados.set(posicao, dado);
            return pilhaDados.size();
        }
        else
        {
            pilhaDados.add(posicao, dado);
            return pilhaDados.size();
        }  
    }
    
    public Double lePilha(int posicao)
    {
        return pilhaDados.get(posicao);
    }
    
    public int excluiDado(int posicao)
    {
        pilhaDados.remove(posicao);
        return pilhaDados.size();
    } 
    
    public ArrayList<Double> getPilha()
    {
        return pilhaDados;
    }
}
