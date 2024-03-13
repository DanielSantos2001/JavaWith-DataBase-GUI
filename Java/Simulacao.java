public class Simulacao {

    int id;
    String simulacao = "";

    public Simulacao(int id, String simulacao) {
        this.id = id;
        this.simulacao = simulacao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSimulacao() {
        return simulacao;
    }

    public void setSimulacao(String simulacao) {
        this.simulacao = simulacao;
    }

    @Override
    public String toString() {
        return "Jogo{" +
                "id=" + id +
                ", simulacao='" + simulacao + '\'' +
                '}';
    }
}
