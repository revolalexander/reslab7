package authentication;

public class SaltGenerator {
    private final String symbols = "abcdefghijklmnopqrstuvwxyz1234567890!@#$%^&*()_+\"№;%:?_={}[]";
    private final int saltLength = 10;
    public String getSalt(){
        String salt = "";
        for (int i = 0; i<saltLength; i++){
            salt+=symbols.charAt((int) (Math.random()*symbols.length()));
        }
        return salt;
    }
}
