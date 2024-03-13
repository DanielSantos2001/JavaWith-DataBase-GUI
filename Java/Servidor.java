import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Classe que representa o servidor do jogo remoto
 */

public class Servidor {
    private ServerSocket socketServidor;

    /**
     * Construtor do Servidor onde será iniciado o ServerSocket.
     * @param aPorto porto ao qual o cliente se vai conectar.
     */


    public Servidor(int aPorto) {
        try {
            socketServidor = new ServerSocket(aPorto);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método que obtem a ligação ao socket e que será aceite aquando da junção
     * do Cliente ao jogo remoto
     * @return Socket socket da ligação estabelecida
     * @return null caso a ligação não tenha sido estabelecida
     */

    public Socket obtemLigacaoSocket() {
        try {
            return socketServidor.accept();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Método que fecha a ligação do servidor ao socket
     * @return boolean boolean que é retornado para confirmar o fecho da ligação
     */

    public boolean fecharLigacaoSocketServidor(){
        if (socketServidor != null && !socketServidor.isClosed())
            try {
                socketServidor.close();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        return false;
    }
}
