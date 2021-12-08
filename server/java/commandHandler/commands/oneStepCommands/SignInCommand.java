package commandHandler.commands.oneStepCommands;

import authentication.Encryptor;
import authentication.SaltGenerator;
import common.Instruction;
import common.User;
import commandHandler.commands.AbstractCommand;
import common.Response;
import dataAccessObjects.UserDAO;

public class SignInCommand extends AbstractCommand {
    private UserDAO dao;
    private Encryptor encryptor;
    public SignInCommand(UserDAO dao, Encryptor encryptor){
        super("sign_in","signs user in");
        this.dao = dao;
        this.encryptor = encryptor;
    }
    @Override
    public Response execute(String arg, User user) {
        if(user == null){
            return new Response("User isn't stated", true, Instruction.USER_NOT_VALIDATED);
        }
        User existingUser = dao.get(user.getLogin());
        if(existingUser == null){
            return new Response("User " + user.getLogin() + " doesn't exist", false);
        } else {
            if (existingUser.getPassword().equals(encryptor.getHash(user.getPassword()+existingUser.getSalt()))){
                return new Response("Logged in", false);
            } else {
                return new Response("Password isn't correct", true, Instruction.USER_NOT_VALIDATED);
            }
        }

    }
}
