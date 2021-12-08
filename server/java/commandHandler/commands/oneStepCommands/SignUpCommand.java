package commandHandler.commands.oneStepCommands;

import authentication.Encryptor;
import authentication.SaltGenerator;
import common.Instruction;
import common.User;
import commandHandler.commands.AbstractCommand;
import common.Response;
import dataAccessObjects.UserDAO;

public class SignUpCommand extends AbstractCommand {
    private UserDAO dao;
    private Encryptor encryptor;
    private SaltGenerator saltGenerator;
    public SignUpCommand(UserDAO dao, Encryptor encryptor){
        super("sign_up","saves data about user");
        this.dao = dao;
        this.encryptor = encryptor;
        this.saltGenerator = new SaltGenerator();
    }
    @Override
    public Response execute(String arg, User user) {
        if (user == null){
            return new Response("User isn't stated",true, Instruction.USER_NOT_VALIDATED);
        }
        String salt = saltGenerator.getSalt();
        user.setPassword(encryptor.getHash(user.getPassword()+salt  ));
        user.setSalt(salt);
        if(dao.get(user.getLogin()) == null){
            dao.create(user);
            return new Response("Signed up successfully", false);
        } else {
            return new Response("User with login " + user.getLogin() + " already exists", false, Instruction.USER_NOT_VALIDATED);
        }
    }
}
