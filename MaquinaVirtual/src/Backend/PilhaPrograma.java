package Backend;

import java.util.ArrayList;

public class PilhaPrograma {
    
    private ArrayList<String> pilhaPrograma = new ArrayList();
    
    private static PilhaPrograma instancia;
    
    private PilhaPrograma()
    {
    }
    
    //Singleton
    public static synchronized PilhaPrograma getInstance()
    {
        if(instancia == null)
        {
            instancia = new PilhaPrograma();
        }
        
        return instancia;
    }
    
    public int insereDado(String dado)
    {
        pilhaPrograma.add(dado);
        return pilhaPrograma.size();
    }
    
    public String lePilha(int posicao)
    {
        return pilhaPrograma.get(posicao);
    }
    
    public int excluiDado(int posicao)
    {
        pilhaPrograma.remove(posicao);
        return pilhaPrograma.size();
    }   
    
    public ArrayList<String> getPilha()
    {
        return pilhaPrograma;
    }
}
