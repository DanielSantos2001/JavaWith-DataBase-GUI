import java.time.LocalDateTime;
import java.util.Scanner;

public class Main {

    private static String nome;
    private static int opcao;
    private static String simulacao;
    private static RunTime runtime = new RunTime();
    private static Jogo jogo = new Jogo();
    private static Jogador jogador;
    public static GereJogador gereJogador = new GereJogador();
    private static GereJogo gereJogo = new GereJogo();
    private static LocalDateTime tempoInicio = LocalDateTime.now();
    private static Log log = new Log();

    //log e mais vitorias

    public static void main(String[] args) {
        log.limparLog();
        System.out.println("Bem vindo ao jogo 4 em linha!");
        nome = inputString("Introduza o nome do jogador (para entrar como anónimo apenas pressione ENTER): ");
        if(!gereJogador.jogadorExiste(nome) && !nome.equals("")){
            jogador = new Jogador(nome,0,0,0);
            gereJogador.addJogador(jogador);
        }
        if (nome.equals("")) {
            System.out.println("Bem-vindo Anónimo!");
        } else {
            System.out.println("Bem-vindo " + nome + "!");
            log.criarLog(nome,"Entrou na aplicação");
            if(gereJogador.maiorNVitorias().getNome().equals(nome)){
                System.out.println("Parabéns é o utilizador com maior número de vitórias");
            }
        }
        menuJogo();
    }

    public static void menuJogo() {
        do{
            System.out.println("Modos de jogo:\n1.Singleplayer  2.Local Multiplayer(host)  3.Local Multiplayer(client)" +
                    "\n4.Remote Multiplayer(host)  5.Remote Multiplayer(client)\n" +
                    "Listagens:\n6.Listar jogadores  7.Listar jogos  8.Listar Log\n0.Sair");
            opcao = inputInt("Introduza a opção que deseja: ");
            switch (opcao){
                case 0:
                    if(nome.equals("")){
                        System.out.println("Adeus Anónimo!");
                        runtime.calcularTempoExecucao(tempoInicio);
                    } else {
                        System.out.println("Adeus " + nome + "!");
                        runtime.calcularTempoExecucao(tempoInicio);
                        log.criarLog(nome,"Saiu da aplicação");
                        log.fecharLog();
                    }
                    break;
                case 1:
                    jogo.singleplayer(nome);
                    log.criarLog(nome,"Entrou num jogo Singleplayer");
                    break;
                case 2:
                    jogo.multiplayerHost(inputInt("Indique o porto: "),nome);
                    log.criarLog(nome,"Entrou num jogo multiplayer como Server");
                    break;
                case 3:
                    jogo.multiplayerClient("localhost",inputInt("Indique o porto: "),nome);
                    log.criarLog(nome,"Entrou num jogo multiplayer como Client");
                    break;
                case 4:
                    jogo.multiplayerHost(inputInt("Indique o porto: "),nome);
                    log.criarLog(nome,"Entrou num jogo multiplayer como Server");
                    break;
                case 5:
                    jogo.multiplayerClient(inputString("Introduza o ip que deseja conectar: "),inputInt("Indique o porto: "),nome);
                    log.criarLog(nome,"Entrou num jogo multiplayer como Client");
                    break;
                case 6:
                    gereJogador.atualizaList();
                    gereJogador.listarJogador();
                    log.criarLog(nome,"Listou os jogadores");
                    break;
                case 7:
                    gereJogo.atualizaList();
                    gereJogo.listarJogo();
                    simulacao = gereJogo.procurarJogo(inputInt("Indique o id de jogo que deseja: "));
                    jogo.simularJogo(simulacao);
                    log.criarLog(nome,"Simulou um jogo");
                    break;
                case 8:
                    log.mostrarLog();
                    break;
            }
        }while(opcao!=0);
    }

    private static String inputString(String n) {
        Scanner myObj = new Scanner(System.in);
        System.out.print(n);
        String read = myObj.nextLine();
        return read;
    }

    private static int inputInt(String n) {
        Scanner myObj = new Scanner(System.in);
        System.out.print(n);
        int read = myObj.nextInt();
        return read;
    }

}
