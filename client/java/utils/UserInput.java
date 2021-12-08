package utils;


import utils.UserOutput;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Scanner;

public class UserInput {
    private Scanner scanner;
    private BufferedReader bufferedReader;
    private static final String REQUEST_COMMAND = "Enter your command:";
    public UserInput(Scanner scanner){
        this.scanner = scanner;
        this.bufferedReader = null;
    }
    public UserInput(BufferedReader bufferedReader){
        this.bufferedReader = bufferedReader;
        this.scanner = null;
    }
    public String getLine(){
        String input = "";
        if(isInteractive()){
            input = scanner.nextLine();
        } else {
            try{
                input = bufferedReader.readLine();
            } catch (IOException e){
                UserOutput.println(e.getMessage());
            }
        }
        return input;
    }
    public Integer getInt(){
        Integer res = null;
        try{
            res = Integer.parseInt(getLine().trim());
        }catch (NumberFormatException e){
            UserOutput.println("Incorrect input");
        }
        return res;
    }
    public boolean isInteractive(){
        return scanner!=null && bufferedReader==null;
    }

    public String[] askCommand(){
        if (isInteractive()) UserOutput.println(REQUEST_COMMAND);
        String line = getLine();
        if(line == null){
            return null;
        }
        String[] res = (line.trim()+" ").split(" ",2);
        if(res[1].isEmpty()) res[1] = " ";
        return res;
    }
    public String askPropertyString(String requestProperty){
        if (isInteractive()) UserOutput.println(requestProperty+":");
        return getLine();
    }
    public Integer askPropertyInt(String requestProperty){
        Integer res = null;
        if(isInteractive()){
            do{
                UserOutput.println(requestProperty);
                res = getInt();
            }while(res==null);
        } else {
            try{
                return Integer.parseInt(getLine());
            } catch (NumberFormatException e){
                return null;
            }
        }
        return res;
    }
    public boolean ask(String msg){
        String res = "";
        if(isInteractive()){
            do{
                UserOutput.println(msg);
                res = getLine();
            }while(!res.equals("y") && !res.equals("n"));
            return res.equals("y");
        } else {
            return getLine().equals("y");
        }
    }

}