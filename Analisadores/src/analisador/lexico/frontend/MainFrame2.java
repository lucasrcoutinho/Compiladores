package analisador.lexico.frontend;

import analisador.lexico.backend.Facade;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import static java.lang.Integer.parseInt;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Element;

public class MainFrame2 extends JFrame implements ActionListener{
    private static JTextArea textArea;
    private static JTextArea lines;
    private static JTextField statusCompilacao;
    private JScrollPane jsp;
    Panel panelBotoes = new Panel();
    JButton abrir = new JButton("Abrir");
    JButton salvar = new JButton("Salvar");
    JButton salvarComo = new JButton("Salvar Como");
    JButton compilar = new JButton("Compilar");
    
    private static MainFrame2 instancia;
    Facade facade = Facade.getInstance();
    String caminhoArquivo = "";
    
    private static ArrayList<String> codigoFonte = new ArrayList<>();
    
    
    public static synchronized MainFrame2 getInstance() throws BadLocationException{
        if (instancia == null){
            instancia = new MainFrame2();
        }
        return instancia;
    }
   
    public MainFrame2() throws BadLocationException {
        setTitle("LineNumberTextArea Test");
        jsp = new JScrollPane();
        textArea = new JTextArea();
        statusCompilacao = new JTextField();
        lines = new JTextArea("01     ");
        lines.setBackground(Color.LIGHT_GRAY);
        lines.setEditable(false);     
        jsp.setPreferredSize(new Dimension(700, 500));      

        abrir.addActionListener(this);
        salvar.addActionListener(this);
        compilar.addActionListener(this);
        salvarComo.addActionListener(this);
      
        //Code to implement line numbers inside the JTextArea
        textArea.getDocument().addDocumentListener(new DocumentListener() {
        public String getText() {
            int caretPosition = textArea.getDocument().getLength();
            Element root = textArea.getDocument().getDefaultRootElement();
            String text = "01     " + System.getProperty("line.separator");
                for(int i = 2; i < root.getElementIndex(caretPosition) + 2; i++) {
                    if(i<10)text += "0"+i +"      " + System.getProperty("line.separator");
                    else
                    text += i +"      " + System.getProperty("line.separator");
                }
                if(textArea.getDocument().getLength()>0){
                    salvarComo.setEnabled(true); 
                }else{
                    salvarComo.setEnabled(false);
                }               
            return text;
        }
            @Override
            public void changedUpdate(DocumentEvent de) {
                lines.setText(getText());
            }
            @Override
            public void insertUpdate(DocumentEvent de) {
                lines.setText(getText());
             }
             @Override
             public void removeUpdate(DocumentEvent de) {
                lines.setText(getText());
             }            
        });

        jsp.getViewport().add(textArea);
        jsp.setRowHeaderView(lines);
        setSize(800, 600);
      
        panelBotoes.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 0));
        panelBotoes.setBounds(0, 50, 1360, 30);
        statusCompilacao.setPreferredSize(new Dimension(250, 27));
        statusCompilacao.setEditable(false);
        
        panelBotoes.add(jsp);
        panelBotoes.add(abrir);
        panelBotoes.add(salvar);
        panelBotoes.add(salvarComo);
        panelBotoes.add(compilar);
        panelBotoes.add(statusCompilacao);
      
        add(panelBotoes);     
      
      
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        
        salvarComo.setEnabled(false);
        compilar.setEnabled(false);    
        salvar.setEnabled(false);

    }

   
    public void actionPerformed(ActionEvent evento) {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("."));
        
        if(evento.getSource() == abrir){
            chooser.showOpenDialog(this);
            try{
                caminhoArquivo = chooser.getSelectedFile().getAbsolutePath();
            }catch(java.lang.NullPointerException e){
                JOptionPane.showMessageDialog(null, "Aquivo nao selecionado \n "
                +"Erro caminho: "+e.getMessage(),"Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }           
            carregarCodigoFonte();
            salvar.setEnabled(true);
            compilar.setEnabled(true);
        }
                
        if(evento.getSource() == salvar){
            salvarArquivo();
        }
        
        if(evento.getSource() == salvarComo){
            chooser.showSaveDialog(this);
            try{
                caminhoArquivo = chooser.getSelectedFile().getAbsolutePath();
            }catch(java.lang.NullPointerException e){
                JOptionPane.showMessageDialog(null, "Aquivo nao selecionado \n "
                +"Erro caminho: "+e.getMessage(),"Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            caminhoArquivo = chooser.getSelectedFile().getAbsolutePath();
            salvarArquivo();
            salvar.setEnabled(true);
            compilar.setEnabled(true);
        }
        
        if(evento.getSource() == compilar){
            salvarArquivo();
            try {
                compilar();
            } catch (BadLocationException ex) {
                Logger.getLogger(MainFrame2.class.getName()).log(Level.SEVERE, null, ex);
            }         
        } 

        
		
    }

    private void salvarArquivo(){    
        facade.salvarCodigo(textArea.getText(), caminhoArquivo);
    }
    
    private void carregarCodigoFonte(){
        int indice = 0;
        codigoFonte.clear();
        textArea.setText("");
        getPrograma(caminhoArquivo);
            while(indice < codigoFonte.size()-1){
                textArea.append(codigoFonte.get(indice)+"\n");
                indice++;
            }
    }
    
    public static ArrayList getPrograma(String caminho){          
        try{
            FileReader arq = new FileReader(caminho);
            BufferedReader lerArq = new BufferedReader(arq);
            codigoFonte.clear();
            String linha;
            do{
                linha = lerArq.readLine();
                codigoFonte.add(linha);
            }while (linha != null); 
            
            arq.close();
        }
        catch(IOException e){
            System.err.printf("Erro na leitura do arquivo: %s \n", e.getMessage());
        }
        return codigoFonte;
    }
    
    private void compilar() throws BadLocationException{
        int linhaErro;
        String retornoCompilacao;
        retornoCompilacao = facade.chamaSintatico(caminhoArquivo);
                
        if("Compilado com sucesso!".equals(retornoCompilacao)){
            statusCompilacao.setText(retornoCompilacao);
            JOptionPane.showMessageDialog(null, retornoCompilacao);
        }else{
            statusCompilacao.setText("Erro na linha: "+retornoCompilacao);
            linhaErro = parseInt(retornoCompilacao.split(" - ")[0])-1;
            JOptionPane.showMessageDialog(null, "Erro na linha: "+retornoCompilacao, 
                    "Erro", JOptionPane.ERROR_MESSAGE);                    
            
            DefaultHighlighter.DefaultHighlightPainter painter = 
                    new DefaultHighlighter.DefaultHighlightPainter(Color.RED);
            
            lines.getHighlighter().addHighlight(lines.getLineStartOffset(linhaErro), 
                    lines.getLineEndOffset(linhaErro), painter);
        }
    }
}