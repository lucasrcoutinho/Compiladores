package Frontend;

import java.awt.BorderLayout;
import java.awt.Color;
import static java.awt.Frame.MAXIMIZED_BOTH;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class FramePrincipal extends JFrame{
    
    JButton jb_debug = new JButton("Debug");
    JButton jb_run = new JButton("Run");
    
    JTextArea editorCodigo = new JTextArea();
    JScrollPane editorCodigoScroll = new JScrollPane(editorCodigo);
    
    JPanel norte = new JPanel();
    JPanel oeste = new JPanel();
    JPanel sul = new JPanel();
        
    public FramePrincipal()
    {
        setTitle("Interpretador Assembly - Lucas e Adriano");
        setExtendedState(MAXIMIZED_BOTH);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        norte.add(jb_run);
        norte.add(jb_debug);
        add(BorderLayout.NORTH, norte);
        
        add(BorderLayout.CENTER, editorCodigoScroll);
        
        
    }
    
    
    
}
