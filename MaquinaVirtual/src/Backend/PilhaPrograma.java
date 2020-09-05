package Backend;

import java.util.ArrayList;

public class PilhaPrograma {
    
    private ArrayList<String> pilhaPrograma = new ArrayList();
    
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
}
