package Backend;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LeArquivo 
{
    public static ArrayList ler(String caminho)
    {   
        ArrayList<String> linhas = new ArrayList<>();
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
        
        return linhas;
    }
}
