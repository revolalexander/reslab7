package authentication;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD2Encryptor implements Encryptor{
    private MessageDigest md;
    private final String hashingAlgorithm = "MD2";
    public MD2Encryptor(){
        try {
            md = MessageDigest.getInstance(hashingAlgorithm);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
    @Override
    public String getHash(String s) {
        return new String(md.digest(s.getBytes()));
    }
}
