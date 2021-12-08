package commandHandler.utils;
import org.apache.logging.log4j.LogManager;
/**
 * Makes logs
 * **/
public class Logger {
    private static org.apache.logging.log4j.Logger logger = LogManager.getLogger();
    public static void info(String msg){
        logger.info("[+]" + msg);
    }
    public static void error(String msg){
        logger.error("[!]" + msg);
    }
}
