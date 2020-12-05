package Backend;

import java.util.ArrayList;
import java.util.Scanner;

public class Maquina{

    private int reg_i = 0;  //Registrador Programa
    private int reg_s = 0;  // Registrador Dados

    private PilhaDados pilhaDados = PilhaDados.getInstance();
    private PilhaPrograma pilhaPrograma = PilhaPrograma.getInstance();

    //Facede instanciaFacede = Facede.getInstance();
    Scanner scan = new Scanner(System.in); // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!Remover Depois !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

    public void preenchePilhaPrograma(ArrayList<String> dados) {
        for (String linha : dados) {
            pilhaPrograma.insereDado(linha);
        }

        pilhaPrograma.insereDado(null);
    }

    public void executaInstrucoes(boolean debug, int parada) {
        Facede instanciaFacede = Facede.getInstance();
        double num1 = 0;
        double num2 = 0;
        String[] mn;
        int n;
        int m;

        String linha = pilhaPrograma.lePilha(reg_i);
        String[] instrucao = linha.split(" "); //pos 0 = instrucao, pos 1 = atriuto
        System.out.println(instrucao[0]);

        while (true) {

            /*while(debug && reg_i == parada)
            {
                if(instanciaFacede.verificaContinuacaoExe())
                {
                    break;
                }
            }
            /*if (debug && reg_i == parada) 
            {
                new Thread() {
                    @Override
                    public void run() {
                        while (!instanciaFacede.verificaContinuacaoExe()) {

                        }
                    }
                }.start();
            }*/

            switch (instrucao[0]) {
                case "LDC":
                    reg_s++;
                    pilhaDados.insereDado(Double.parseDouble(instrucao[1]), reg_s);  //inseriando o dado contido no atributo da instrucao na posicao reg_s

                    break;

                case "LDV":
                    reg_s++;
                    pilhaDados.insereDado(pilhaDados.lePilha(Integer.parseInt(instrucao[1])), reg_s);  //inseriando o dado contido no atributo da instrucao na posicao reg_s

                    break;

                case "ADD":
                    num1 = pilhaDados.lePilha(reg_s - 1);
                    num2 = pilhaDados.lePilha(reg_s);

                    pilhaDados.insereDado((num1 + num2), (reg_s - 1));
                    reg_s--;
                    break;

                case "SUB":
                    num1 = pilhaDados.lePilha(reg_s - 1);
                    num2 = pilhaDados.lePilha(reg_s);

                    pilhaDados.insereDado((num1 - num2), (reg_s - 1));
                    reg_s--;

                    break;

                case "MULT":
                    num1 = pilhaDados.lePilha(reg_s - 1);
                    num2 = pilhaDados.lePilha(reg_s);

                    pilhaDados.insereDado((num1 * num2), (reg_s - 1));
                    reg_s--;
                    break;

                case "DIVI":
                    num1 = pilhaDados.lePilha(reg_s - 1);
                    num2 = pilhaDados.lePilha(reg_s);

                    pilhaDados.insereDado((num1 / num2), (reg_s - 1));
                    reg_s--;
                    break;

                case "INV":
                    num1 = pilhaDados.lePilha(reg_s);
                    pilhaDados.insereDado((num1 * (-1)), reg_s);
                    break;

                case "AND":
                    num1 = pilhaDados.lePilha(reg_s - 1);
                    num2 = pilhaDados.lePilha(reg_s);

                    if (num1 == 1 && num2 == 1) {
                        pilhaDados.insereDado((double) 1, (reg_s - 1));
                    } else {
                        pilhaDados.insereDado((double) 0, (reg_s - 1));
                    }
                    reg_s--;
                    break;

                case "OR":
                    num1 = pilhaDados.lePilha(reg_s - 1);
                    num2 = pilhaDados.lePilha(reg_s);

                    if (num1 == 1 || num2 == 1) {
                        pilhaDados.insereDado((double) 1, (reg_s - 1));
                    } else {
                        pilhaDados.insereDado((double) 0, (reg_s - 1));
                    }
                    reg_s--;
                    break;

                case "NEG":
                    num1 = pilhaDados.lePilha(reg_s);
                    pilhaDados.insereDado((1 - num1), reg_s);
                    break;

                case "CME":
                    num1 = pilhaDados.lePilha(reg_s - 1);
                    num2 = pilhaDados.lePilha(reg_s);

                    if (num1 < num2) {
                        pilhaDados.insereDado((double) 1, (reg_s - 1));
                    } else {
                        pilhaDados.insereDado((double) 0, (reg_s - 1));
                    }
                    reg_s--;
                    break;

                case "CMA":
                    num1 = pilhaDados.lePilha(reg_s - 1);
                    num2 = pilhaDados.lePilha(reg_s);

                    if (num1 > num2) {
                        pilhaDados.insereDado((double) 1, (reg_s - 1));
                    } else {
                        pilhaDados.insereDado((double) 0, (reg_s - 1));
                    }

                    reg_s--;

                    break;

                case "CEQ":
                    num1 = pilhaDados.lePilha(reg_s - 1);
                    num2 = pilhaDados.lePilha(reg_s);

                    if (num1 == num2) {
                        pilhaDados.insereDado((double) 1, (reg_s - 1));
                    } else {
                        pilhaDados.insereDado((double) 0, (reg_s - 1));
                    }
                    reg_s--;
                    break;

                case "CDIF":
                    num1 = pilhaDados.lePilha(reg_s - 1);
                    num2 = pilhaDados.lePilha(reg_s);

                    if (num1 != num2) {
                        pilhaDados.insereDado((double) 1, (reg_s - 1));
                    } else {
                        pilhaDados.insereDado((double) 0, (reg_s - 1));
                    }
                    reg_s--;
                    break;

                case "CMEQ":
                    num1 = pilhaDados.lePilha(reg_s - 1);
                    num2 = pilhaDados.lePilha(reg_s);

                    if (num1 <= num2) {
                        pilhaDados.insereDado((double) 1, (reg_s - 1));
                    } else {
                        pilhaDados.insereDado((double) 0, (reg_s - 1));
                    }
                    reg_s--;
                    break;

                case "CMAQ":
                    num1 = pilhaDados.lePilha(reg_s - 1);
                    num2 = pilhaDados.lePilha(reg_s);

                    if (num1 >= num2) {
                        pilhaDados.insereDado((double) 1, (reg_s - 1));
                    } else {
                        pilhaDados.insereDado((double) 0, (reg_s - 1));
                    }
                    reg_s--;
                    break;

                case "START":
                    reg_s = -1;
                    break;

                case "HLT":
                    debug = true;
                    break;

                case "STR":
                    n = Integer.parseInt(instrucao[1]);
                    pilhaDados.insereDado(pilhaDados.lePilha(reg_s), n);
                    reg_s--;
                    break;

                case "JMP":
                    reg_i = pilhaPrograma.buscalabel(instrucao[1]);
                    break;

                case "JMPF":
                    if (pilhaDados.lePilha(reg_s) == 0) {
                        reg_i = pilhaPrograma.buscalabel(instrucao[1]);
                    } else {
                        //reg_i++;
                    }

                    reg_s--;
                    break;

                case "NULL":
                    //nao faz nada
                    break;

                case "RD":
                    reg_s++;
                    num1 = scan.nextInt();
                    pilhaDados.insereDado(num1, reg_s);
                    break;

                case "PRN":
                    num1 = pilhaDados.lePilha(reg_s);
                    instanciaFacede.escritaDeDados(Double.toString(num1));
                    System.out.println(num1);  //!!!!!!!!!!!!!!!!!!!Implementar
                    reg_s--;
                    break;

                case "ALLOC":
                    mn = instrucao[1].split(",");
                    if (mn.length == 1) {
                        System.out.printf("ALLOC m - M: %s | N: %s \n", mn[0], mn[0]);
                    } else {
                        m = Integer.parseInt(mn[0]);
                        n = Integer.parseInt(mn[1]);

                        for (int k = 0; k < n; k++) {
                            reg_s++;
                            pilhaDados.insereDado(pilhaDados.lePilha(m + k), reg_s);
                        }
                    }

                    break;

                case "DALLOC":

                    mn = instrucao[1].split(",");

                    if (mn.length == 1) {
                        System.out.printf("DALLOC m - M: %s | N: %s \n", mn[0], mn[0]);
                    } else {

                        m = Integer.parseInt(mn[0]);
                        n = Integer.parseInt(mn[1]);

                        for (int k = (n - 1); k >= 0; k--) { // De 0 ate n -1
                            pilhaDados.insereDado(pilhaDados.lePilha(reg_s), m + k);
                            if (reg_s > 0) {
                                reg_s--;
                            }
                        }
                    }
                    break;

                case "CALL":
                    reg_s++;
                    pilhaDados.insereDado((double) (reg_i + 1), reg_s);
                    reg_i = pilhaPrograma.buscalabel(instrucao[1]);
                    break;

                case "RETURN":
                    reg_i = pilhaDados.lePilha(reg_s).intValue() - 1;
                    reg_s--;
                    break;

                case "DEBUG":
                    System.out.println("==========DEBUG==========");
                    System.out.println("Var X: " + pilhaDados.lePilha(0));
                    System.out.println("Var Y: " + pilhaDados.lePilha(1));
                    System.out.println("Var Z: " + pilhaDados.lePilha(2));
                    System.out.println("==========DEBUG==========");

                default:
                    break;
            }

            atualizaMainFrame();

            reg_i++;
            linha = pilhaPrograma.lePilha(reg_i);
            if (linha == null) {
                break;
            } else {
                instrucao = linha.split(" ");
                System.out.println(instrucao[0].toString());
            }
        }
    }

    private void atualizaMainFrame() {
        Facede instanciaFacede = Facede.getInstance();
        instanciaFacede.atualizaTabelas();
    }
}
