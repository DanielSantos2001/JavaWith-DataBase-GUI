import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

/**
 * Classe que representa o cliente do jogo remoto
 */

public class Cliente {
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;
    private Socket socket;

    /**
     * Construtor do Cliente, inicia a socket, bufferedReader e PrintWriter.
     * @param aHost host da maquina ao qual se vai ligar
     * @param aPorto porta da maquina ao qual se vai ligar
     */

    public Cliente(String aHost,int aPorto) {
        try {
            socket = new Socket(aHost, aPorto);
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            printWriter = new PrintWriter(socket.getOutputStream(), true);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Construtor do Cliente, este construtor será usado pelo servidor, que será simultaneamente servidor e cliente.
     * @param aSocket Socket da ligação.
     */

    public Cliente(Socket aSocket) {
        socket = aSocket;

        try {
            printWriter = new PrintWriter(socket.getOutputStream(), true);
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método que escreve mensagens para os sockets
     * @param aMensagem mensagem a escrever na socket
     */

    public void escreveMensagemParaSocket(String aMensagem) {
        if (socket != null && socket.isConnected() && aMensagem != null && printWriter != null) {
            printWriter.println(aMensagem);
        }
    }

    /**
     * Método que lê a mensagem que foi escrita para o socket
     * @return String string com conteúdo da mensagem
     */

    public String lerMensagemDoSocket() {
        if (socket != null && socket.isConnected() && bufferedReader != null) {
            try {
                StringBuilder stringBuilder = new StringBuilder();
                String inputLine = "";

                while ((inputLine = bufferedReader.readLine()) != null) {
                    stringBuilder.append(inputLine);
                    if (inputLine.contains(";")) {
                        break;
                    }
                }

                return stringBuilder.toString();
            }catch (SocketException se){
                //se.printStackTrace();
            } catch (IOException e) {
                //e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Método que verifica o estado da ligação
     * @return boolean boolean que informa sobre o estado da ligação
     */

    public boolean estadoDaLigacao(){
        if (socket == null) {
            return false;
        }
        return socket.isConnected() && !socket.isClosed();
    }


    /**
     * Método que fecha a ligação do Cliente ao socket
     * @return boolean boolean que é retornado para confirmar o fecho da ligação
     */

    public boolean fecharLigacaoSocket(){
        if (socket != null && !socket.isClosed()){
            try {
                socket.close();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
        return false;
    }

}
