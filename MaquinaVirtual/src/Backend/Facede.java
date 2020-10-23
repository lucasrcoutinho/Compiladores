package Backend;
import Frontend.MainFrame;
import java.util.ArrayList;

public class Facede {
    
    private static Facede instancia;
    
    private Maquina maquina = new Maquina();
    private PilhaDados pilhaDados = PilhaDados.getInstance();
    private PilhaPrograma pilhaPrograma = PilhaPrograma.getInstance();
    
    private Facede()
    {
    }
    
    public static Facede getInstance()
    {
        if(instancia == null)
        {
            instancia = new Facede();
        }
        
        return instancia;
    }
    
    public void executaProg(boolean debug, int parada)
    {
        maquina.executaInstrucoes(debug, parada);
    }
    
    public ArrayList obtemArquivo(String caminho)
    {
        LeArquivo dadosArquivo = new LeArquivo(caminho);
        dadosArquivo.ler();
        
        maquina.preenchePilhaPrograma(dadosArquivo.getDados());
        return dadosArquivo.getDados();   
    }
    
    
    public ArrayList obtemPilhaDados()
    {
         return pilhaDados.getPilha();
    }
    
    public ArrayList obtemPilhaPrograma()
    {
          return pilhaPrograma.getPilha();
    }
    
    public void atualizaTabelas()
    {
        MainFrame instanciaMainFrame = MainFrame.getInstance();
        
        instanciaMainFrame.atualizaPilhas();
    }
}
