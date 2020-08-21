package Backend;
import java.util.ArrayList;

public class Facede {
    
    public ArrayList obtemArquivo(String caminho)
    {
        LeArquivo dadosArquivo = new LeArquivo(caminho);
        dadosArquivo.ler();
        return dadosArquivo.getDados();
        
    }
}
