import utils.*;

import java.util.Scanner;

public class Main {
    public static void main(String... args){
        Client client = new Client("127.0.0.1",14413);
        client.run();
    }
}