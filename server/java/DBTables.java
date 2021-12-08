import commandHandler.utils.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBTables {
    private Connection dbconn;
    private String locationsTableName = "locations";
    private String addressesTableName = "addresses";
    private String venuesTableName = "venues";
    private String coordinatesTableName = "coordinates";
    private String ticketsTableName = "tickets";
    private String usersTableName = "users";
    public DBTables(Connection dbconn){
        this.dbconn = dbconn;
    }

    public void init(){
        try{
            createLocations();
            createCoordinates();
            createAddresses();
            createVenues();
            createTickets();
            createUsers();
        }catch (SQLException e){
            Logger.error("Error initializing tables");
            e.printStackTrace();
        }
    }
    public void createLocations() throws SQLException {
        create(
                "create table if not exists " + locationsTableName +
                        "(" +
                        "id serial primary key," +
                        "x int," +
                        "y real," +
                        "z double precision" +
                        ");"
        );
    }
    public void createAddresses() throws SQLException{
        create(
                "create table if not exists " + addressesTableName +
                        "(" +
                        "id serial primary key," +
                        "street varchar," +
                        "zip_code varchar," +
                        "location_id int," +
                        "foreign key (location_id) references "+ locationsTableName + "(id)" +
                        ");"
        );
    }

    public void createCoordinates() throws SQLException{
        create(
                "create table if not exists " + coordinatesTableName +
                        "(" +
                        "id serial primary key," +
                        "x double precision," +
                        "y double precision" +
                        ")"
        );
    }

    public void createVenues() throws SQLException{
        create(
                "create table if not exists " + venuesTableName +
                        "(" +
                        "id serial primary key," +
                        "name varchar," +
                        "capacity double precision," +
                        "type varchar," +
                        "address_id int," +
                        "foreign key (address_id) references " + addressesTableName + "(id)" +
                        ");"
        );
    }

    public void createTickets() throws SQLException{
        create(
                "create table if not exists " + ticketsTableName + "" +
                        "(" +
                        "id serial primary key," +
                        "name varchar," +
                        "coordinates_id int," +
                        "creation_date date," +
                        "price double precision," +
                        "comment varchar," +
                        "refundable boolean," +
                        "type varchar," +
                        "venue_id int," +
                        "creator varchar," +
                        "foreign key (coordinates_id) references " + coordinatesTableName + "(id)," +
                        "foreign key (venue_id) references " + venuesTableName + "(id)" +
                        ");"
        );
    }

    public void createUsers() throws SQLException {
        create(
                "create table if not exists " + usersTableName + "(" +
                        "login varchar primary key," +
                        "password varchar," +
                        "salt varchar"+
                        ")"
        );
    }

    public void create(String sql) throws SQLException{
        PreparedStatement createStatement = dbconn.prepareStatement(sql);
        createStatement.execute();
    }
}
