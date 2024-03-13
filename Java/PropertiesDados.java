public class PropertiesDados {
    private String bd;
    private String login;
    private String password;
    private String ip;
    private String porto;

    public PropertiesDados(String bd, String login, String password, String ip, String porto) {
        this.bd = bd;
        this.login = login;
        this.password = password;
        this.ip = ip;
        this.porto = porto;
    }

    public PropertiesDados(){
    }

    public String getBd() {
        return bd;
    }

    public void setBd(String bd) {
        this.bd = bd;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPorto() {
        return porto;
    }

    public void setPorto(String porto) {
        this.porto = porto;
    }
}
