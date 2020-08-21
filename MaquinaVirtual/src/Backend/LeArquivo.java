package Backend;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LeArquivo 
{
    private ArrayList<String> linhas = new ArrayList<>();
    private String caminho;
    
    public LeArquivo(String caminho)
    {
        this.caminho = caminho;
    }
   
    public void ler()
    {          
        try
        {
            FileReader arq = new FileReader(caminho);
            BufferedReader lerArq = new BufferedReader(arq);
            
            String linha;
            do{
                linha = lerArq.readLine();
                linhas.add(linha);
            }while (linha != null);
            
            arq.close();
        }
        catch(IOException e)
        {
            System.err.printf("Erro na leitura do arquivo: %s \n", e.getMessage());
        }
    }
    
    public ArrayList getDados()
    {
        return linhas;
    }
}
