import java.io.IOException;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Scanner;

public class Jogo {

    int id;
    String simulacao = "";

    String[][] tabuleiro = new String[9][9];
    int coluna;
    TratamentoMensagens mensagem = new TratamentoMensagens();
    Servidor server;
    Cliente client;
    Socket socket;
    String msg = "";
    String tipo = "";
    String nomeAdversario;
    GereJogo gereJogo = new GereJogo();
    private LocalDateTime tempoJogo;
    private LocalDateTime tempoJogada;
    private RunTime runtime = new RunTime();

    public Jogo() {
        resetJogo();
    }

    public void resetJogo() {
        for (int l = 1; l < 9; l++) {
            for (int c = 1; c < 9; c++) {
                tabuleiro[c][l] = " ";
            }
        }
    }

    public void singleplayer(String aNome) {
        tempoJogo = LocalDateTime.now();
        if (acabouJogo() == 0 || acabouJogo() == 1 || acabouJogo() == 2) {
            resetJogo();
        }
        printTabuleiro();
        simulacao = ",";
        while (true) {
            while (true) {
                coluna = inputInt("Introduza a coluna que deseja colocar a peça(1 a 8)\nPara desistir introduza 0\nColuna: ");
                if (coluna == 0) {
                    System.out.println("Jogo terminado a meio...");
                    return;
                }
                if (colocaPeca(coluna, "x")) {
                    simulacao = simulacao + coluna + ",";
                    printTabuleiro();
                    if (acabouJogo() == 0) {
                        System.out.println("Empate!");
                        gereJogo.addJogo(simulacao);
                        runtime.calcularTempoExecucao(tempoJogo);
                        Main.gereJogador.atualizarJogador(aNome,1,0, runtime.minutos(tempoJogo));
                        return;
                    } else if (acabouJogo() == 1) {
                        System.out.println("Ganhou!");
                        gereJogo.addJogo(simulacao);
                        runtime.calcularTempoExecucao(tempoJogo);
                        Main.gereJogador.atualizarJogador(aNome,1,1,runtime.minutos(tempoJogo));
                        return;
                    } else if (acabouJogo() == 2) {
                        System.out.println("Perdeu!");
                        gereJogo.addJogo(simulacao);
                        runtime.calcularTempoExecucao(tempoJogo);
                        Main.gereJogador.atualizarJogador(aNome,1,0,runtime.minutos(tempoJogo));
                        return;
                    }
                    break;
                } else {
                    System.out.println("Não pode colocar peça nesse local");
                }
            }
            while (!colocaPeca(coluna = (int) (Math.random() * (9 - 1)) + 1, "o")) {

            }
            simulacao = simulacao + coluna + ",";
            printTabuleiro();
            if (acabouJogo() == 0) {
                System.out.println("Empate!");
                gereJogo.addJogo(simulacao);
                runtime.calcularTempoExecucao(tempoJogo);
                Main.gereJogador.atualizarJogador(aNome,1,0,runtime.minutos(tempoJogo));
                return;
            } else if (acabouJogo() == 1) {
                System.out.println("Ganhou!");
                gereJogo.addJogo(simulacao);
                runtime.calcularTempoExecucao(tempoJogo);
                Main.gereJogador.atualizarJogador(aNome,1,1,runtime.minutos(tempoJogo));
                return;
            } else if (acabouJogo() == 2) {
                System.out.println("Perdeu!");
                gereJogo.addJogo(simulacao);
                runtime.calcularTempoExecucao(tempoJogo);
                Main.gereJogador.atualizarJogador(aNome,1,0,runtime.minutos(tempoJogo));
                return;
            }
        }
    }

    public void multiplayerHost(int aPort, String aNome) {
        resetJogo();
        server = new Servidor(aPort);
        socket = server.obtemLigacaoSocket();
        client = new Cliente(socket);

        try {
            while (!tipo.equals("hello")) {
                msg = "<" + aNome + "> <hello>;";
                client.escreveMensagemParaSocket(msg);

                msg = client.lerMensagemDoSocket();
                tipo = mensagem.retornaTipoMensagem(msg);
            }

            System.out.println("Conectado ao Cliente\nA sua peça: x");

            int firstPlayer = 0 + (int) (Math.random() * ((1 - 0) + 1));
            msg = "<" + aNome + "> <start> <" + firstPlayer + ">;";
            client.escreveMensagemParaSocket(msg);

            if (firstPlayer == 0) {
                msg = client.lerMensagemDoSocket();//joga client
                tipo = mensagem.retornaTipoMensagem(msg);

                if (tipo.equals("withdraw")) {
                    System.out.println("Adversário desistiu!");
                    msg = "<" + aNome + "> <withdraw> <ack>;";
                    client.escreveMensagemParaSocket(msg);
                } else {
                    coluna = Integer.parseInt(mensagem.getValor(msg));
                    if (colocaPeca(coluna, "o")) {
                        msg = "<" + aNome + "> <result> <valid>;";
                        client.escreveMensagemParaSocket(msg);
                        printTabuleiro();
                    } else {
                        msg = "<" + aNome + "> <result> <notvalid>;";
                        client.escreveMensagemParaSocket(msg);
                    }
                }
            } else {
                msg = client.lerMensagemDoSocket();//joga servidor
                if (mensagem.getValor(msg).equals("ok")) {
                    System.out.println("Client disse ok");
                }
                printTabuleiro();
                coluna = inputInt("Introduza a coluna que deseja colocar a peça(1 a 8)\nPara desistir introduza 0\nColuna: ");
                if (coluna == 0) {
                    msg = "<" + aNome + "> <withdraw>;";
                    client.escreveMensagemParaSocket(msg);
                } else {
                    colocaPeca(coluna, "x");
                    msg = "<" + aNome + "> <move> <" + coluna + ">;";
                    client.escreveMensagemParaSocket(msg);
                }
            }

            while (true) {
                msg = client.lerMensagemDoSocket();
                tipo = mensagem.retornaTipoMensagem(msg);

                if (tipo.equals("bye")) {
                    try {
                        socket.close();
                        break;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else if (tipo.equals("move")) {

                    coluna = Integer.parseInt(mensagem.getValor(msg));
                    if (colocaPeca(coluna, "o")) {
                        msg = "<" + aNome + "> <result> <valid>;";
                        client.escreveMensagemParaSocket(msg);
                    } else {
                        msg = "<" + aNome + "> <result> <notvalid>;";
                        client.escreveMensagemParaSocket(msg);
                    }

                } else if (tipo.equals("result")) {

                    //status check
                    if (mensagem.getValor(msg).equals("valid")) {
                        printTabuleiro();
                        if (acabouJogo() == 0) {
                            System.out.println("Empatou!");
                            msg = "<" + aNome + "> <status> <draw>;";
                            client.escreveMensagemParaSocket(msg);
                        } else if (acabouJogo() == 1) {
                            System.out.println("Ganhou!");
                            msg = "<" + aNome + "> <status> <win>;";
                            client.escreveMensagemParaSocket(msg);
                        } else if (acabouJogo() == 2) {
                            System.out.println("Perdeu!");
                            msg = "<" + aNome + "> <status> <lose>;";
                            client.escreveMensagemParaSocket(msg);
                        } else {
                            msg = "<" + aNome + "> <result> <ack>;";
                            client.escreveMensagemParaSocket(msg);
                        }

                    } else if (mensagem.getValor(msg).equals("notvalid")) {
                        coluna = inputInt("Introduza novamente a coluna que deseja colocar a peça(1 a 8)\nPara desistir introduza 0\nColuna: ");
                        if (coluna == 0) {
                            msg = "<" + aNome + "> <withdraw>;";
                            client.escreveMensagemParaSocket(msg);
                        } else {
                            colocaPeca(coluna, "x");
                            msg = "<" + aNome + "> <move> <" + coluna + ">;";
                            client.escreveMensagemParaSocket(msg);
                        }
                    } else if (mensagem.getValor(msg).equals("ack")) {
                        printTabuleiro();

                        //status check
                        if (acabouJogo() == 0) {
                            System.out.println("Empatou!");
                            msg = "<" + aNome + "> <status> <draw>;";
                            client.escreveMensagemParaSocket(msg);
                        } else if (acabouJogo() == 1) {
                            System.out.println("Ganhou!");
                            msg = "<" + aNome + "> <status> <win>;";
                            client.escreveMensagemParaSocket(msg);
                        } else if (acabouJogo() == 2) {
                            System.out.println("Perdeu!");
                            msg = "<" + aNome + "> <status> <lose>;";
                            client.escreveMensagemParaSocket(msg);
                        } else {
                            coluna = inputInt("Introduza a coluna que deseja colocar a peça(1 a 8)\nPara desistir introduza 0\nColuna: ");
                            if (coluna == 0) {
                                msg = "<" + aNome + "> <withdraw>;";
                                client.escreveMensagemParaSocket(msg);
                            } else {
                                colocaPeca(coluna, "x");
                                msg = "<" + aNome + "> <move> <" + coluna + ">;";
                                client.escreveMensagemParaSocket(msg);
                            }
                        }

                    }

                } else if (tipo.equals("new")) {

                    if (mensagem.getValor(msg).equals("y")) {
                        resetJogo();
                        System.out.println("Nova jogada");
                        firstPlayer = 0 + (int) (Math.random() * ((1 - 0) + 1));
                        msg = "<" + aNome + "> <start> <" + firstPlayer + ">;";
                        client.escreveMensagemParaSocket(msg);

                        if (firstPlayer == 0) {
                            msg = client.lerMensagemDoSocket();//joga client
                            tipo = mensagem.retornaTipoMensagem(msg);

                            if (tipo.equals("withdraw")) {
                                System.out.println("Adversário desistiu!");
                                msg = "<" + aNome + "> <withdraw> <ack>;";
                                client.escreveMensagemParaSocket(msg);
                            } else {
                                coluna = Integer.parseInt(mensagem.getValor(msg));
                                if (colocaPeca(coluna, "o")) {
                                    msg = "<" + aNome + "> <result> <valid>;";
                                    client.escreveMensagemParaSocket(msg);
                                    printTabuleiro();
                                } else {
                                    msg = "<" + aNome + "> <result> <notvalid>;";
                                    client.escreveMensagemParaSocket(msg);
                                }
                            }
                        } else {
                            msg = client.lerMensagemDoSocket();//joga servidor
                            if (mensagem.getValor(msg).equals("ok")) {
                                System.out.println("Client disse ok");
                            }
                            printTabuleiro();
                            coluna = inputInt("Introduza a coluna que deseja colocar a peça(1 a 8)\nPara desistir introduza 0\nColuna: ");
                            if (coluna == 0) {
                                msg = "<" + aNome + "> <withdraw>;";
                                client.escreveMensagemParaSocket(msg);
                            } else {
                                colocaPeca(coluna, "x");
                                msg = "<" + aNome + "> <move> <" + coluna + ">;";
                                client.escreveMensagemParaSocket(msg);
                            }
                        }
                    } else if (mensagem.getValor(msg).equals("n")) {
                        msg = "<" + aNome + "> <bye>;";
                        client.escreveMensagemParaSocket(msg);
                        client.fecharLigacaoSocket();
                        server.fecharLigacaoSocketServidor();
                        return;
                    }
                } else if (tipo.equals("withdraw")) {

                    if (mensagem.getValor(msg).equals("ack")) {
                        msg = "<" + aNome + "> <new> <?>;";
                        client.escreveMensagemParaSocket(msg);
                    } else {
                        System.out.println("Adversário desistiu!");
                        msg = "<" + aNome + "> <withdraw> <ack>;";
                        client.escreveMensagemParaSocket(msg);
                    }

                } else if (tipo.equals("ready")) {

                    msg = "<" + aNome + "> <new> <?>;";
                    client.escreveMensagemParaSocket(msg);

                } else if (tipo.equals("status")) {

                    msg = "<" + aNome + "> <new> <?>;";
                    client.escreveMensagemParaSocket(msg);

                }

            }
        } catch (Exception e){
            return;
        }

    }

    public void multiplayerClient(String aIP, int aPort, String aNome) {
        resetJogo();
        client = new Cliente(aIP, aPort);

        try {
            while (true) {
                msg = client.lerMensagemDoSocket();
                tipo = mensagem.retornaTipoMensagem(msg);

                if (tipo.equals("hello")) {
                    msg = "<" + aNome + "> <hello>;";
                    client.escreveMensagemParaSocket(msg);
                    System.out.println("Conectado ao servidor\nA sua peça: o");

                } else if (tipo.equals("bye")) {
                    client.fecharLigacaoSocket();

                } else if (tipo.equals("start")) {
                    if (Integer.parseInt(mensagem.getValor(msg)) == 0) {

                        printTabuleiro();
                        coluna = inputInt("Introduza a coluna que deseja colocar a peça(1 a 8)\nPara desistir introduza 0\nColuna: ");
                        if (coluna == 0) {
                            msg = "<" + aNome + "> <withdraw>;";
                            client.escreveMensagemParaSocket(msg);
                        } else {
                            colocaPeca(coluna, "o");
                            msg = "<" + aNome + "> <move> <" + coluna + ">;";
                            client.escreveMensagemParaSocket(msg);
                            printTabuleiro();
                        }

                    } else if (Integer.parseInt(mensagem.getValor(msg)) == 1) {

                        msg = "<" + aNome + "> <start> <ok>;";
                        client.escreveMensagemParaSocket(msg);

                    }
                } else if (tipo.equals("move")) {

                    coluna = Integer.parseInt(mensagem.getValor(msg));
                    if (colocaPeca(coluna, "x")) {
                        msg = "<" + aNome + "> <result> <valid>;";
                        client.escreveMensagemParaSocket(msg);
                    } else {
                        msg = "<" + aNome + "> <result> <notvalid>;";
                        client.escreveMensagemParaSocket(msg);
                    }

                } else if (tipo.equals("result")) {

                    if (mensagem.getValor(msg).equals("valid")) {
                        msg = "<" + aNome + "> <result> <ack>;";
                        client.escreveMensagemParaSocket(msg);
                        printTabuleiro();
                    } else if (mensagem.getValor(msg).equals("notvalid")) {
                        coluna = inputInt("Introduza novamente a coluna que deseja colocar a peça(1 a 8)\nPara desistir introduza 0\nColuna: ");
                        if (coluna == 0) {
                            msg = "<" + aNome + "> <withdraw>;";
                            client.escreveMensagemParaSocket(msg);
                        } else {
                            colocaPeca(coluna, "o");
                            msg = "<" + aNome + "> <move> <" + coluna + ">;";
                            client.escreveMensagemParaSocket(msg);
                        }
                    } else if (mensagem.getValor(msg).equals("ack")) {
                        printTabuleiro();
                        coluna = inputInt("Introduza a coluna que deseja colocar a peça(1 a 8)\nPara desistir introduza 0\nColuna: ");
                        if (coluna == 0) {
                            msg = "<" + aNome + "> <withdraw>;";
                            client.escreveMensagemParaSocket(msg);
                        } else {
                            colocaPeca(coluna, "o");
                            msg = "<" + aNome + "> <move> <" + coluna + ">;";
                            client.escreveMensagemParaSocket(msg);
                        }
                    }

                } else if (tipo.equals("status")) {

                    if (mensagem.getValor(msg).equals("draw")) {
                        printTabuleiro();
                        System.out.println("Empatou!");
                        msg = "<" + aNome + "> <status> <ok>;";
                        client.escreveMensagemParaSocket(msg);
                    } else if (mensagem.getValor(msg).equals("win")) {
                        printTabuleiro();
                        System.out.println("Perdeu!");
                        msg = "<" + aNome + "> <status> <ok>;";
                        client.escreveMensagemParaSocket(msg);
                    } else if (mensagem.getValor(msg).equals("lose")) {
                        printTabuleiro();
                        System.out.println("Ganhou!");
                        msg = "<" + aNome + "> <status> <ok>;";
                        client.escreveMensagemParaSocket(msg);
                    }

                } else if (tipo.equals("new")) {

                    String yn = inputString("Deseja continuar (y ou n): ");
                    if (yn.equals("y")) {
                        resetJogo();
                        msg = "<" + aNome + "> <new> <y>;";
                        client.escreveMensagemParaSocket(msg);
                    } else {
                        msg = "<" + aNome + "> <new> <n>;";
                        client.escreveMensagemParaSocket(msg);
                    }

                } else if (tipo.equals("withdraw")) {

                    if (mensagem.getValor(msg).equals("ack")) {
                        msg = "<" + aNome + "> <ready>;";
                        client.escreveMensagemParaSocket(msg);
                    } else {
                        System.out.println("Adversário desistiu!");
                        msg = "<" + aNome + "> <withdraw> <ack>;";
                        client.escreveMensagemParaSocket(msg);
                    }

                } else {
                    System.out.println("Erro");
                }
            }
        } catch (Exception e){
            return;
        }
    }

    private boolean colocaPeca(int col, String simbolo) {
        if (col > 0 && col < 9 && tabuleiro[col][1].equals(" ")) {
            for (int i = 8; i > 0; i--) {
                if (tabuleiro[col][i].equals(" ")) {
                    tabuleiro[col][i] = simbolo;
                    return true;
                }
            }
        }
        return false;
    }

    public void printTabuleiro() {
        System.out.println("-----------------");
        for (int l = 1; l < 9; l++) {
            System.out.print("|");
            for (int c = 1; c < 9; c++) {
                System.out.print(tabuleiro[c][l] + "|");
            }
            System.out.println();
        }
        System.out.println("-----------------");
    }

    private int acabouJogo() {
        for (int j = 1; j < 9 - 3; j++) {
            for (int i = 1; i < 9; i++) {
                if (tabuleiro[i][j].equals("x") && tabuleiro[i][j + 1].equals("x") && tabuleiro[i][j + 2].equals("x") && tabuleiro[i][j + 3].equals("x")) {
                    return 1;
                }
                if (tabuleiro[i][j].equals("o") && tabuleiro[i][j + 1].equals("o") && tabuleiro[i][j + 2].equals("o") && tabuleiro[i][j + 3].equals("o")) {
                    return 2;
                }
            }
        }
        // verticalCheck
        for (int i = 1; i < 9 - 3; i++) {
            for (int j = 1; j < 9; j++) {
                if (tabuleiro[i][j].equals("x") && tabuleiro[i + 1][j].equals("x") && tabuleiro[i + 2][j].equals("x") && tabuleiro[i + 3][j].equals("x")) {
                    return 1;
                }
                if (tabuleiro[i][j].equals("o") && tabuleiro[i + 1][j].equals("o") && tabuleiro[i + 2][j].equals("o") && tabuleiro[i + 3][j].equals("o")) {
                    return 2;
                }
            }
        }
        // ascendingDiagonalCheck
        for (int i = 3; i < 9; i++) {
            for (int j = 1; j < 9 - 3; j++) {
                if (tabuleiro[i][j].equals("x") && tabuleiro[i - 1][j + 1].equals("x") && tabuleiro[i - 2][j + 2].equals("x") && tabuleiro[i - 3][j + 3].equals("x")) {
                    return 1;
                }
                if (tabuleiro[i][j].equals("o") && tabuleiro[i - 1][j + 1].equals("o") && tabuleiro[i - 2][j + 2].equals("o") && tabuleiro[i - 3][j + 3].equals("o")) {
                    return 2;
                }
            }
        }
        // descendingDiagonalCheck
        for (int i = 3; i < 9; i++) {
            for (int j = 3; j < 9; j++) {
                if (tabuleiro[i][j].equals("x") && tabuleiro[i - 1][j - 1].equals("x") && tabuleiro[i - 2][j - 2].equals("x") && tabuleiro[i - 3][j - 3].equals("x")) {
                    return 1;
                }
                if (tabuleiro[i][j].equals("o") && tabuleiro[i - 1][j - 1].equals("o") && tabuleiro[i - 2][j - 2].equals("o") && tabuleiro[i - 3][j - 3].equals("o")) {
                    return 2;
                }
            }
        }

        for (int c = 1; c != 9; c++) {
            if (tabuleiro[c][1].equals(" ")) {
                return 3;
            }
        }
        return 0;
    }

    public void simularJogo(String aSimulacao){
        String str[];
        resetJogo();
        str = aSimulacao.split(",");
        for(int i=1; i<str.length;i++){
            if(i%2==0){
                colocaPeca(Integer.parseInt(str[i]),"x");
            } else {
                colocaPeca(Integer.parseInt(str[i]),"o");
            }
            printTabuleiro();
            inputString("Pressione Enter para a próxima jogada");
        }
    }

    private static int inputInt(String n) {
        Scanner myObj = new Scanner(System.in);
        System.out.print(n);
        int read = myObj.nextInt();
        return read;
    }

    private static String inputString(String n) {
        Scanner myObj = new Scanner(System.in);
        System.out.print(n);
        String read = myObj.nextLine();
        return read;
    }
}
