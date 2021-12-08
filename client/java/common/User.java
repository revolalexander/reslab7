package common;

import java.io.Serializable;

public class User implements Serializable {
    private String login;
    private String password;
    private String salt;

    public User(String login, String password){
        this.login = login;
        this.password = password;
    }
    public User(String login, String password, String salt) {
        this.login = login;
        this.password = password;
        this.salt = salt;
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

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}

