import java.util.List;
import java.util.Vector;

public class TratamentoMensagens {
    /**Representa a classe que trata das Mensagens que se encontram na Socket.
     */

    /**Construtor do TratamentoMensagens
     * @param String que contem a mensagem que se encontra na Socket
     */

    private String mensagemSocket = "";

    /**Metodo responsavel por carregar a mensagem da Socket, trata la e coloca la num Vector
     * @return Vector lista com as mensagens da socket para depois ser mais fácil aceder a cada campo da mensagem
     */


    private List<String> carregaMensagemSocket(){
        List<String> listaMensagens = new Vector<>();

        String [] valoresMensagem = mensagemSocket.split(">");

        for(String novaMensagem : valoresMensagem) {
            listaMensagens.add(novaMensagem.trim().substring(1));
        }

        return listaMensagens;
    }


    /**Metodo responsavel retornar os vários tipos de mensagem consoante a mensagem que se encontra
     * na Socket e que vai ser retornada no método anterior
     * @return String conexao a base de dados
     * @return null caso não seja nenhum dos tipos de mensagem
     */


    public String retornaTipoMensagem(String aMensagem){
        if (aMensagem != null && aMensagem.length() > 0) {
            mensagemSocket = aMensagem;
            List<String> listaMensagens = carregaMensagemSocket();
            if (listaMensagens.size() >= 2) {
                String mensagemRecebida = listaMensagens.get(1);

                switch(mensagemRecebida) {
                    case "hello":
                        return "hello";
                    case "bye":
                        return "bye";
                    case "start":
                        return "start";
                    case "move":
                        return "move";
                    case "result":
                        return "result";
                    case "status":
                        return "status";
                    case "new":
                        return "new";
                    case "withdraw":
                        return "withdraw";
                    case "ready":
                        return "ready";
                }
            }
        }
        return null;
    }

    public String getValor(String aMensagem){
        if (aMensagem != null && aMensagem.length() > 0) {
            mensagemSocket = aMensagem;
            List<String> listaMensagens = carregaMensagemSocket();
                String mensagemRecebida = listaMensagens.get(2);
                return mensagemRecebida;
        }
        return null;
    }

    /**Metodo responsavel por carregar o nickname do jogador que se encontra na mensagem
     * da Socket e que é carregada através do método "carregaMensagemSocket"
     * @return String nickname do Jogador
     * @return null caso não exista nenhum nickname naquela posição da mensagem
     */


    public String carregaNicknameMensagem(){
        List<String> listaMensagens = carregaMensagemSocket();

        if (listaMensagens != null && listaMensagens.size() > 0) {
            return listaMensagens.get(0);
        }
        return null;
    }

}
