public class Jogador {

    private String nome;
    private int nJogos;
    private int nVitorias;
    private int tempoTotal;

    public Jogador(String nome, int nJogos, int nVitorias, int tempoTotal) {
        this.nome = nome;
        this.nJogos = nJogos;
        this.nVitorias = nVitorias;
        this.tempoTotal = tempoTotal;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getnJogos() {
        return nJogos;
    }

    public void setnJogos(int nJogos) {
        this.nJogos = nJogos;
    }

    public int getnVitorias() {
        return nVitorias;
    }

    public void setnVitorias(int nVitorias) {
        this.nVitorias = nVitorias;
    }

    public int gettempoTotal() {
        return tempoTotal;
    }

    public void settempoTotal(int nTempoTotal) {
        this.tempoTotal = nTempoTotal;
    }

    @Override
    public String toString() {
        return "Jogador{" +
                "nome='" + nome + '\'' +
                ", nJogos=" + nJogos +
                ", nVitorias=" + nVitorias +
                ", nTempoTotal=" + tempoTotal + "min" +
                '}';
    }
}
