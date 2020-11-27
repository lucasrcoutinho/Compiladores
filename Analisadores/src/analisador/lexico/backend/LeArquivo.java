/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analisador.lexico.backend;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 *
 * @author lucas
 */
public class LeArquivo {
    private ArrayList<String> linhas = new ArrayList<>();
    private static int indice;

    LeArquivo(){
        indice = 0;
    }
    ////////////////////////////////////////////////////////////////////////////
    LeArquivo(String path) throws IOException{
        Writer arquivo = new BufferedWriter(new FileWriter(path, true));
        arquivo.append("\n ");
        arquivo.close();
        indice = 0;
    }
    ////////////////////////////////////////////////////////////////////////////
    public ArrayList getPrograma(String caminho){          
        try{
            FileReader arq = new FileReader(caminho);
            BufferedReader lerArq = new BufferedReader(arq);
            linhas.clear();
            String linha;
            do{
                linha = lerArq.readLine();
                linhas.add(linha);
            }while (linha != null); 
            
            arq.close();
        }
        catch(IOException e){
            System.err.printf("Erro na leitura do arquivo: %s \n", e.getMessage());
        }
        return linhas;
    }  
    
    public String readUsingFiles(String fileName) {
        try {
            return new String(Files.readAllBytes(Paths.get(fileName)));
	} catch (IOException e) {
            e.printStackTrace();
            return null;
	}
    }

    public int leChar(String caminho){
        File arq = new File(caminho);
        int c = 0;
        try (RandomAccessFile  in = new RandomAccessFile (arq, "r")) {
             in.seek(indice);
             c = in.read();
             //System.out.print((char)c);  // this method will do whatever you want
             indice++;
        }
        catch(IOException e){
            System.err.printf("Erro na leitura do arquivo: %s \n", e.getMessage());
        }
        return c;
    }    
}
