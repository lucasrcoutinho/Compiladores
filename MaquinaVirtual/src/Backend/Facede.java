package Backend;
import java.util.ArrayList;

public class Facede {
    
    private Maquina maquina = new Maquina();
    
    public ArrayList obtemArquivo(String caminho)
    {
        LeArquivo dadosArquivo = new LeArquivo(caminho);
        dadosArquivo.ler();
        
        maquina.preenchePilhaPrograma(dadosArquivo.getDados());
        return dadosArquivo.getDados();   
    }
}
