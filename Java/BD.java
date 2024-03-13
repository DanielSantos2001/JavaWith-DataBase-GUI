import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class BD {

    /**Classe para gerir a conexão com a bd.
     */

    private static String URL;
    private static String USER;
    private static String PASS;

    private static BD conectorBd = null;
    private Connection conectarBd;

    /**Criar uma instancia da classe.
     * @return Connection á bd.
     */

    public static BD getInstance(){
        if (conectorBd == null) conectorBd = new BD();
        return conectorBd;
    }

    /**inicia conexão com a bd.
     */

    public void getConnection() {
        gereProperties grp = new gereProperties();
        PropertiesDados pdr = new PropertiesDados();

        grp.AbrirLeituraProperties("properties.txt");
        pdr = grp.buscarPropertiesDados();
        URL = "jdbc:mysql://localhost:"+pdr.getPorto()+"/"+pdr.getBd();
        PASS =pdr.getPassword();
        USER = pdr.getLogin();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.conectarBd = DriverManager.getConnection(URL, USER, PASS);
            //this.conectarBd = DriverManager.getConnection("jdbc:mysql://localhost:3306/pa1", "root",null);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Error connecting to the database");
        }
    }

    /** método para obter uma conexao
     * @return Connection com a bd.
     */

    public Connection getConexao(){

        if (this.conectarBd == null)
            getConnection();
        return this.conectarBd;

    }

    /**termina a conexão com a bd
     * @return boolean a informar o sucesso da operacao
     */

    public boolean closeConnection() {
        if (this.conectarBd != null)
            try {
                this.conectarBd.close();
                return true;
            } catch (SQLException e) {
            }
        return false;
    }
}
