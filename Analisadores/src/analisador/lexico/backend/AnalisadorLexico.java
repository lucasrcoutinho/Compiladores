/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analisador.lexico.backend;

/**
 *
 * @author lucas
 */
public class AnalisadorLexico { 
    private static final int zero = 48;
    private static final int nove = 57;
    private static final int letra_a = 97;
    private static final int letra_z = 122;
    private static final int letra_A = 65;
    private static final int letra_Z = 90;
    
    int numeroDaLinha;    
    char caractere;
    int caracterCodASCII;
    Token token;
    
    LeArquivo leArquivo;
    String caminhoArquivoFonte;

 
    public AnalisadorLexico(String caminhoArquivoF){
        numeroDaLinha = 1; 
        caminhoArquivoFonte = caminhoArquivoF;
        leArquivo = new LeArquivo();
        token = new Token("", "", 0);
        lerChar();
    }
    
    public Token getToken(){
        token.setErro("");
        token.setLexema("");
        token.setSimbolo("");
        
        while(token.getLexema() == "" && token.getErro() == ""){

            if(caractere == '{'){
                trataComentarioChave();

            }else if(caractere == '/'){
                trataComentarioBarra();

            }else if(caractere == ' '){
                trataEspaco();

            }else if(caractere == '\n'){
                lerChar();//Deixa proximo caractere lido
                numeroDaLinha++;                

            }else if(caractere == '\b' || caractere == '\t' || 
                     caractere == '\f' || caractere == '\r'){
                lerChar();//Deixa proximo caractere lido

            }else if( (caracterCodASCII >= zero) && (caracterCodASCII <= nove) && 
                (caractere != 0) ){
                trataDigito();

            }else if(
                        (
                            (caracterCodASCII >= letra_a && caracterCodASCII <= letra_z)
                            ||
                            (caracterCodASCII >= letra_A)&&(caracterCodASCII <= letra_Z)
                        )
                        &&
                        (caractere != 0)
                    ){
                trataIdentificadorPalavra();

            }else if(caractere == ':'){
                trataAtribuicao();

            }else if(caractere == '*' || caractere == '+' || caractere == '-' ){
                trataOperadorAritmetico();

            }else if(caractere == '>' || caractere == '<' || caractere == '='){
                trataOperadorRelacional();

            }
            else if(caractere == ';' || caractere == ',' || caractere == '(' ||
                    caractere == ')' || caractere == '.'){
                trataPontuacao();

            }else if(caractere == '!'){
                trataDif();
            }else{
                trataErro("Caractere nao reconhecido");
            }        
        }
        System.out.println("Token: " + token.getSimbolo());
        
        if (caractere == 0){//Fim do arquivo
                token.setErro("");
                token.setLexema("");
                token.setSimbolo("");
                token.setLinha(numeroDaLinha);
                System.out.println("Fim do arquivo");
                return token;
            }
        return token;
    }  
    
    private void trataComentarioChave(){
        lerChar();        
        if (caractere == '\n'){
            numeroDaLinha++;
        }
        while(caractere != 0){
            if (caractere == '\n'){
                numeroDaLinha++;
            }
            if (caractere == '}'){
                lerChar();
                return;                
            }
            lerChar(); 
        }
        trataErro("Esperado caracter }");
    }    
    
    private void trataComentarioBarra(){
        lerChar();
        if (caractere == '*'){
            while(caractere != 0){
                if (caractere == '\n'){
                    numeroDaLinha++;
                }
                lerChar();
                if (caractere == '*'){
                    lerChar();
                    if (caractere == '/'){
                        lerChar();
                        return;
                    }
                }
            }
            trataErro("Esperado caracteres */");
        }else{
            trataErro("Esperado caracter *");
        }        
    }
    
    private void trataEspaco(){
         while(caractere == ' ' && caractere != 0){
            lerChar();//Deixa proximo caractere lido
        }
    }
    
    private void trataDigito(){
        String numero = "";
        numero += caractere;
        
        lerChar();
        
        while((caracterCodASCII >= zero && caracterCodASCII <= nove) && 
                (caractere != 0)){
            numero += caractere; 
            lerChar();//Deixa proximo caractere lido
        }
        salvaToken("snumero", numero, numeroDaLinha);   
    }
    
    private void trataIdentificadorPalavra(){
        String palavra = "";
        palavra += caractere;        
        lerChar();
        
        while(
                (
                    (caracterCodASCII >= letra_a && caracterCodASCII <= letra_z)
                    ||
                    (caracterCodASCII >= letra_A)&&(caracterCodASCII <= letra_Z)
                    ||
                    (caracterCodASCII >= zero && caracterCodASCII <= nove)
                    ||
                     caractere == '_'
                )
                &&
                (caractere != 0)
              ){
            palavra += caractere; 
            lerChar();//Deixa proximo caractere lido
        }
       
        switch(palavra){     
            case "programa":            
                salvaToken("sprograma", "programa", numeroDaLinha);            
                break;            
            case "se":
                salvaToken("sse", "se", numeroDaLinha);
                break;            
            case "entao":
                salvaToken("sentao", "entao", numeroDaLinha);
                break;
            case "senao":
                salvaToken("ssenao", "senao", numeroDaLinha);
                break;            
            case "enquanto":
                salvaToken("senquanto", "enquanto", numeroDaLinha);
                break;            
            case "faca":
                salvaToken("sfaca", "faca", numeroDaLinha);
                break;
            case "inicio":
                salvaToken("sinicio", "inicio", numeroDaLinha);
                break;
            case "fim":
                salvaToken("sfim", "fim", numeroDaLinha);
                break;
            case "escreva":
                salvaToken("sescreva", "escreva", numeroDaLinha);
                break;
            case "leia":
                salvaToken("sleia", "leia", numeroDaLinha);
                break;
            case "var":
                salvaToken("svar", "var", numeroDaLinha);
                break;
            case "inteiro":
                salvaToken("sinteiro", "inteiro", numeroDaLinha);
                break;
            case "booleano":
                salvaToken("sbooleano", "booleano", numeroDaLinha);
                break;
            case "verdadeiro":
                salvaToken("sverdadeiro", "verdadeiro", numeroDaLinha);
                break;
            case "falso":
                salvaToken("sfalso", "falso", numeroDaLinha);
                break;
            case "procedimento":
                salvaToken("sprocedimento", "procedimento", numeroDaLinha);
                break;
            case "funcao":
                salvaToken("sfuncao", "funcao", numeroDaLinha);
                break;
            case "div":
                salvaToken("sdiv", "div", numeroDaLinha);
                break;
            case "e":
                salvaToken("se", "e", numeroDaLinha);
                break;
            case "ou":
                salvaToken("sou", "ou", numeroDaLinha);
                break;
            case "nao":
                salvaToken("snao", "nao", numeroDaLinha);
                break;
            default:
                //salvaToken("sidentificador", "identificador", numeroDaLinha);
                salvaToken("sidentificador", palavra, numeroDaLinha);
                break;
        }
    }  
   
    private void trataAtribuicao(){
        lerChar();
        if(caractere == '='){
            salvaToken("satribuicao", ":=", numeroDaLinha);
            lerChar();
        }else{
            salvaToken("sdoispontos", ":", numeroDaLinha);
        }        
    }
    
    private void trataOperadorRelacional(){
        char tempChar = caractere;
        lerChar();
        switch(tempChar){
            case '<':
                if(caractere == '='){
                    salvaToken("smenorig", "<=", numeroDaLinha);
                    lerChar();
                }
                else{
                    salvaToken("smenor", "<", numeroDaLinha);
                }
                break;
            case '>':
                if(caractere == '='){
                    salvaToken("smaiorig", ">=", numeroDaLinha);
                    lerChar();
                }
                else{
                    salvaToken("smaior", ">", numeroDaLinha);
                }
                break;
            case'=':
                salvaToken("sig", "=", numeroDaLinha);
                break;

        }

    }
    
    private void trataOperadorAritmetico(){
        switch (caractere) {
            case '+':
                salvaToken("smais", "+", numeroDaLinha);
                break;
            case '-':
                salvaToken("smenos", "-", numeroDaLinha);
                break;
            case '*': 
                salvaToken("smult", "*", numeroDaLinha);
                break;
        }
    lerChar(); 
    }  
    
    private void trataPontuacao(){
        switch (caractere) {
            case ';':
                salvaToken("sponto_virgula", ";", numeroDaLinha);
                break;
            case ',':
                salvaToken("svirgula", ",", numeroDaLinha);
                break;
            case '(': 
                salvaToken("sabre_parenteses", "(", numeroDaLinha);
                break;
            case ')': 
                salvaToken("sfecha_parenteses", ")", numeroDaLinha);
                break;
            case '.': 
                salvaToken("sponto", ".", numeroDaLinha);
                break;
        }
    lerChar();   
    }
    
    private void trataDif(){
        lerChar();
        if(caractere == '='){
            salvaToken("sdif", "!=", numeroDaLinha);
            lerChar();
        }else{
            trataErro("Esperado caracter =");
        }
    }
    
    private void trataErro(String descrErro){
        token.setLexema("");
        token.setLinha(numeroDaLinha);
        token.setSimbolo("");
        token.setErro(descrErro);
    }
    
    private void salvaToken(String simbolo, String lexema, int linha){
        token.setLexema(lexema);
        token.setLinha(linha);
        token.setSimbolo(simbolo);
        token.setErro("");
    }
    
    private void lerChar(){
        int codASCII;
        codASCII = leArquivo.leChar(caminhoArquivoFonte);
        if (codASCII == -1){
            caractere = (char) 0;//Caractere nulo, representa fim do arquivo
            caracterCodASCII = 0;
        }else{
            caractere = (char) codASCII;
            caracterCodASCII = codASCII;            
        }
    } 
}