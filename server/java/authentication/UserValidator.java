package authentication;

import common.User;
import dataAccessObjects.UserDAO;

public class UserValidator {
    public UserDAO dao;
    public Encryptor encryptor;
    public UserValidator(UserDAO dao, Encryptor encryptor){
        this.dao = dao;
        this.encryptor = encryptor;
    }
    public boolean validate(User receivedUser){

        if (receivedUser == null){
            return false;
        }
        User existingUser = dao.get(receivedUser.getLogin());
        if (existingUser == null){
            return false;
        }
        String existingSalt = existingUser.getSalt();
        String hashOfReceivedPassword = encryptor.getHash(receivedUser.getPassword()+existingSalt);
        boolean validated = existingUser.getPassword().equals(hashOfReceivedPassword);
        return validated;
    }

}
