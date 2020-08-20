package Backend;

import java.util.ArrayList;

public class Facede {
    
    public ArrayList obtemArquivo(String caminho)
    {
        return new LeArquivo().ler(caminho);
    }
}
