package inputHandler;

import common.Command;
import common.Request;
import common.User;
import utils.UserInput;

import java.util.Scanner;

public class UserDataAsker {
    private UserInput userInput;
    public UserDataAsker(UserInput userInput){
        this.userInput = userInput;
    }

    public User askUserData(){
        User user = new User(
                userInput.askPropertyString("Enter your login: "),
                userInput.askPropertyString("Enter your password: ")
        );
        return user;
    }

    public Request setUser(Request request){
        if(userInput.ask("Do you want to sign up (y) or sign in (n)? [y/n]")){
            request.setCommand(new Command("sign_up",""));
        } else {
            request.setCommand(new Command("sign_in", ""));
        }
        request.setUser(askUserData());
        return request;
    }
}
