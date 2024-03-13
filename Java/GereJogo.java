import java.sql.*;
import java.util.ArrayList;

public class GereJogo {

    int id;
    String sim;

    ArrayList<Simulacao> listJogo = new ArrayList<Simulacao>();
    Jogo jogo;
    Simulacao simulacao;
    private ResultSet rs = null;
    private PreparedStatement ps = null;
    private Statement st = null;
    private Connection con;

    public GereJogo(){
        atualizaList();
    }

    public void atualizaList(){
        listJogo.clear();
        con = BD.getInstance().getConexao();
        try {
            st = con.createStatement();
            rs = st.executeQuery("SELECT * FROM simulacao");

            if(rs!=null) {
                while (rs.next()) {
                    id = rs.getInt("idjogo");
                    sim = rs.getString("posicao");

                    //Assuming you have a user object
                    simulacao = new Simulacao(id,sim);

                    listJogo.add(simulacao);
                }
            }

        } catch(SQLException e){
            e.printStackTrace();
        }
        finally {
            try {
                rs.close();
                st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void addJogo(String aSimulacao) {
        try {
            ps = con.prepareStatement("INSERT INTO simulacao (posicao) values (?)");
            ps.clearParameters();
            ps.setString(1,aSimulacao);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        atualizaList();
    }

    public void listarJogo(){
        for(int i=0;i<listJogo.size();i++){
            System.out.println(listJogo.get(i));
        }
    }

    public String procurarJogo(int aID){
        for(int i=0;i<listJogo.size();i++){
            if(listJogo.get(i).getId()==aID){
                return listJogo.get(i).getSimulacao();
            }
        }
        return null;
    }

}
