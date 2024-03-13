import java.io.*;
import java.util.Properties;

public class gereProperties {
    /**Class responsavel para gerir o properties
     */

    private Properties prop;
    private FileInputStream fileInputStream;
    private InputStreamReader isr;
    private BufferedReader br;
    private BufferedWriter bw;
    private FileOutputStream fos;

    /** construtor do gere properties que inicia uma conexao
     */

    public gereProperties() {
        prop = new Properties();
    }

    /**
     * Metodo para abrir o ficheiro e ler o conteudo para um properties.
     * @param caminho caminho do ficheiro
     * @return boolean a confirmar a operação
     */

    public boolean AbrirLeituraProperties(String caminho) {
        if (caminho != null)
            try {
                fileInputStream = new FileInputStream(caminho);
                isr = new InputStreamReader(fileInputStream);
                prop.load(isr);
                return true;

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        return false;
    }

    /**Metodo para alterar o ficheiro properties
     * @param caminho caminho do ficheiro
     * @param properties ficheiro properties
     * @return boolean a confirmar a acao.
     */

    public boolean alterarPropertiesFicheiro(String caminho, PropertiesDados properties) {
        if (caminho != null && prop != null){
            prop.setProperty("ip",properties.getIp());
            prop.setProperty("porto",properties.getPorto());
            prop.setProperty("baseDados",properties.getBd());
            prop.setProperty("utilizador",properties.getLogin());
            prop.setProperty("password",properties.getPassword());
            try{
                fos = new  FileOutputStream(caminho,false);
                prop.store(fos,null);
                return true;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**Metodo para ir buscar os dados do properties.
     * @return PropertiesDados, objeto da classe que contem as informações necessários para ligar a uma
     * base de dados.
     */

    public PropertiesDados buscarPropertiesDados(){

        if (!prop.isEmpty()){
            String ip = prop.getProperty("ip");
            String porto = prop.getProperty("porto");
            String bd = prop.getProperty("baseDados");
            String utilizador = prop.getProperty("utilizador");
            String password = prop.getProperty("password");

            PropertiesDados pd = new PropertiesDados(bd,utilizador,password,ip,porto);
            return pd;
        }
        return null;
    }

    /**Metodo para fechar leitura de um ficheiro de texto
     * @return boolean a confirmar a acao
     */

    public boolean closeRead() {
        try {
            if (isr != null)
                isr.close();

            if (fileInputStream != null)
                fileInputStream.close();
            return true;
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return false;
    }

    /**Metodo para fechar a leitur de um ficheiro de texto
     * @return boolean a confirmar.
     */

    public boolean closeWrite() {
        try {
            if  (fos != null)
                fos.close();
            return true;
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return false;
    }
}
