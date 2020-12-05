package Backend;

import Frontend.MainFrame;
import java.util.ArrayList;
import javax.swing.SwingWorker;

public class Facede {

    private static Facede instancia;

    private Maquina maquina = new Maquina();
    private PilhaDados pilhaDados = PilhaDados.getInstance();
    private PilhaPrograma pilhaPrograma = PilhaPrograma.getInstance();

    private boolean continuar = false;
    private boolean debug;
    private int parada;

    private Facede() {
    }

    public static Facede getInstance() {
        if (instancia == null) {
            instancia = new Facede();
        }
        return instancia;
    }

    public void executaProg(boolean debug, int parada) {
        this.debug = debug;
        this.parada = parada;
        MainFrame instanciaMainFrame = MainFrame.getInstance();
        //maquina.executaInstrucoes(debug, parada);
        //start();
        //new MaquinaThread().start();
    }

    public ArrayList obtemArquivo(String caminho) {
        LeArquivo dadosArquivo = new LeArquivo(caminho);
        dadosArquivo.ler();

        maquina.preenchePilhaPrograma(dadosArquivo.getDados());
        return dadosArquivo.getDados();
    }

    public ArrayList obtemPilhaDados() {
        return pilhaDados.getPilha();
    }

    public ArrayList obtemPilhaPrograma() {
        return pilhaPrograma.getPilha();
    }
    //erro
    public void atualizaTabelas() {
        MainFrame instanciaMainFrame = MainFrame.getInstance();
        instanciaMainFrame.atualizaPilhas();
    }

    public void reiniciaInstancias() {
        maquina = new Maquina();
        pilhaDados = PilhaDados.getInstance();
        pilhaPrograma = PilhaPrograma.getInstance();
    }

    public void cotinuaExecucao() {
        this.continuar = continuar;
    }

    public boolean verificaContinuacaoExe() {
        return continuar;
    }

    public void escritaDeDados(String dado) {
        MainFrame instanciaMainFrame = MainFrame.getInstance();
        instanciaMainFrame.escrita_out(dado);
    }

    public class MaquinaThread extends Thread{
        public void run()
        {
            maquina.executaInstrucoes(debug, parada);
        }
    }
    
    private void start() {
        SwingWorker worker = new SwingWorker() {
            @Override
            protected Void doInBackground() throws Exception {
                
                maquina.executaInstrucoes(debug, parada);
                return null;
            }
        };

        worker.execute();
    }
}
