

import commandHandler.utils.CollectionManager;
import commandHandler.utils.Logger;
import dataAccessObjects.*;
import domainObjectModel.Coordinates;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;


public class App {
    public static void main(String[] args){
        Properties properties = new Properties();
        try{
            properties.load(new FileInputStream("C:\\Users\\mainr\\Desktop\\reslab7_server\\src\\main\\resources\\db.properties"));
        } catch (IOException e){
            Logger.error("Can't find database connection properties file");
            return;
        }
        String userName = properties.getProperty("login");
        String userPassword = properties.getProperty("password");
        Connection dataBaseConnection = new DBConnection("localhost",5432, userName, userPassword, "studs").getConnection();

        if (dataBaseConnection==null){
            Logger.error("Can't connect the database");
            return;
        }
        new DBTables(dataBaseConnection).init();

        ExecutorService readingPool = ForkJoinPool.commonPool();
        ExecutorService writingPool = Executors.newCachedThreadPool();
        ExecutorService executingPool = ForkJoinPool.commonPool();
        LocationDAO locationDAO = new LocationDAO(dataBaseConnection);
        AddressDAO addressDAO = new AddressDAO(locationDAO,dataBaseConnection);
        VenueDAO venueDAO = new VenueDAO(addressDAO, dataBaseConnection);
        CoordinatesDAO coordinatesDAO = new CoordinatesDAO(dataBaseConnection);
        TicketDAO ticketDAO = new TicketDAO(dataBaseConnection, venueDAO, coordinatesDAO);
        CollectionManager collectionManager = new CollectionManager(ticketDAO, locationDAO, coordinatesDAO);

        new ServerListener().start();
        new Server(collectionManager).run(executingPool, readingPool, writingPool, dataBaseConnection, ticketDAO);
    }

}