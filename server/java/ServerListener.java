import commandHandler.utils.CollectionManager;
import commandHandler.utils.CommandManager;
import commandHandler.utils.Logger;

import java.io.InputStream;
import java.util.Scanner;

public class ServerListener extends Thread{
    @Override
    public void run() {
        while(true){
            Scanner scanner = new Scanner(System.in);
            String command = scanner.nextLine();
            if (command.equals("save")){
                Logger.info("Saved");
            }
            if(command.equals("exit")){
                System.exit(1);
            }
        }
    }
}
