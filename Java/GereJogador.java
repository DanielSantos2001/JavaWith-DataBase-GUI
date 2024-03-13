import java.sql.*;
import java.util.ArrayList;

public class GereJogador {

    ArrayList<Jogador> listJogador = new ArrayList<Jogador>();
    Jogador jogador;
    private ResultSet rs = null;
    private PreparedStatement ps = null;
    private Statement st = null;
    private Connection con;
    private String nome;
    private int nJogos;
    private int nVitorias;
    private int tempoTotal;

    public GereJogador() {
        con = BD.getInstance().getConexao();
        try {
            st = con.createStatement();
            rs = st.executeQuery("SELECT * FROM jogador");

            if (rs != null) {
                while (rs.next()) {
                    nome = rs.getString("nome");
                    nJogos = rs.getInt("njogos");
                    nVitorias = rs.getInt("nvitorias");
                    tempoTotal = rs.getInt("tempototal");

                    //Assuming you have a user object
                    jogador = new Jogador(nome, nJogos, nVitorias, tempoTotal);

                    listJogador.add(jogador);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void atualizaList() {
        listJogador.clear();
        con = BD.getInstance().getConexao();
        try {
            st = con.createStatement();
            rs = st.executeQuery("SELECT * FROM jogador");

            if (rs != null) {
                while (rs.next()) {
                    nome = rs.getString("nome");
                    nJogos = rs.getInt("njogos");
                    nVitorias = rs.getInt("nvitorias");
                    tempoTotal = rs.getInt("tempototal");

                    //Assuming you have a user object
                    jogador = new Jogador(nome, nJogos, nVitorias, tempoTotal);

                    listJogador.add(jogador);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void addJogador(Jogador aJogador) {

        try {
            ps = con.prepareStatement("INSERT INTO jogador (nome, njogos, nvitorias, tempototal) values (?,?,?,?)");
            ps.clearParameters();
            ps.setString(1, aJogador.getNome());
            ps.setInt(2, aJogador.getnJogos());
            ps.setInt(3, aJogador.getnVitorias());
            ps.setInt(4, aJogador.gettempoTotal());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        atualizaList();
    }

    public boolean jogadorExiste(String aNome) {
        for (int i = 0; i < listJogador.size(); i++) {
            if (listJogador.get(i).getNome().equals(aNome)) {
                return true;
            }
        }
        return false;
    }

    public void listarJogador() {
        for (int i = 0; i < listJogador.size(); i++) {
            System.out.println(listJogador.get(i));
        }
    }

    public void atualizarJogador(String aNome,int aNJogos,int aNVitorias,int aTempoTotal) {
        for (int i = 0; i < listJogador.size(); i++) {
            if (listJogador.get(i).getNome().equals(aNome)) {
                jogador=listJogador.get(i);
            }
        }
        try {
            ps = con.prepareStatement("UPDATE JOGADOR SET njogos=?, nvitorias=?, TempoTotal =? WHERE nome = ?");
            ps.clearParameters();
            ps.setInt(1, jogador.getnJogos()+aNJogos);
            ps.setInt(2, jogador.getnVitorias()+aNVitorias);
            ps.setInt(3, jogador.gettempoTotal()+aTempoTotal);
            ps.setString(4, aNome);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Jogador maiorNVitorias(){
        int nVitorias=0;
        for (int i = 0; i < listJogador.size(); i++) {
            if (listJogador.get(i).getnVitorias()>nVitorias) {
                nVitorias=listJogador.get(i).getnVitorias();
                jogador=listJogador.get(i);
            }
        }
        return jogador;
    }
}
