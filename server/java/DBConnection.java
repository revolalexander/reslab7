import commandHandler.utils.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {
    private String host;
    private int port;
    private String url;
    private String dbname;
    private String userName;
    private String userPassword;
    private Connection connection;

    public DBConnection(String host, int port, String userName, String userPassword, String dbname){
        this.host = host;
        this.port = port;
        this.dbname = dbname;
        this.userName = userName;
        this.userPassword = userPassword;
        this.url = "jdbc:postgresql://"+this.host+":"+this.port+"/" + this.dbname;
        try{
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e){
            Logger.error("Cannot connect the database");
            e.printStackTrace();
        }
    }

    public Connection getConnection(){
        if(connection != null){
            return connection;
        }
        try{
            connection = DriverManager.getConnection(this.url, userName, userPassword);
        } catch (SQLException e){
            Logger.error("Connection error");
            e.printStackTrace();
            return null;
        }
        return connection;
    }

}
