package cbot.accept.fttimer.model;

public class User {

    private  String login;
    private  String password;

    /**
     * Default no-args constructor
     */
    public User(){}

    public User(String uLogin, String uPassword){
        this.login = uLogin;
        this.password = uPassword;
    }

    public String getLogin(){
        return login;
    }
    public String getPassword(){
        return password;
    }

    public void setLogin(String uLog){
        this.login = uLog;
    }
    public void setPassword(String uPass){
        this.password = uPass;
    }

    @Override
    public String toString() {
        return "Login=" + getLogin() +";Password=" + getPassword();
    }
}
