package dataAccessObjects;

import domainObjectModel.Location;
import commandHandler.utils.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class LocationDAO extends AbstractDAO<Location, Integer>{

    private final String tableName = "locations";
    private final String SELECT_ALL = "SELECT * FROM "+tableName;
    private final String SELECT_BY_ID = "SELECT * FROM "+tableName+" WHERE id = ?";
    private final String UPDATE_BY_ID = "UPDATE "+tableName+" SET x = ?, y = ?, z = ? WHERE id = ?";
    private final String DELETE_BY_ID = "DELETE FROM " + tableName + " WHERE id = ?";
    private final String CREATE = "INSERT INTO " + tableName + "(x,y,z) VALUES (?, ?, ?) RETURNING id";
    private final String DELETE_ALL = "TRUNCATE TABLE " + tableName + " CASCADE";

    public LocationDAO(Connection connection) {
        super(connection);
    }

    @Override
    public List<Location> getAll(){
        List<Location> list = null;
        PreparedStatement statement = getPreparedStatement(SELECT_ALL);
        try{
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                Location location = new Location(
                        rs.getInt("x"),
                        rs.getFloat("y"),
                        rs.getDouble("z")
                );
                location.setId(rs.getInt("id"));
                list.add(location);
            }
            return list;
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Location get(Integer id) {
        PreparedStatement statement = getPreparedStatement(SELECT_BY_ID);
        try{
            statement.setInt(1,id);
            ResultSet rs = statement.executeQuery();
            if(rs.next()){
                Location location = new Location(
                        rs.getInt("x"),
                        rs.getFloat("y"),
                        rs.getDouble("z")
                );
                location.setId(id);
                return location;
            } else {
                Logger.info("Location with id " + id + " not found");
                return null;
            }
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void update(Integer id, Location entity) {
        PreparedStatement statement = getPreparedStatement(UPDATE_BY_ID);
        try{
            statement.setInt(1,entity.getX());
            statement.setFloat(2,entity.getY());
            statement.setDouble(3,entity.getZ());
            statement.setInt(4,id);
            statement.execute();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Integer id) {
        PreparedStatement statement = getPreparedStatement(DELETE_BY_ID);
        try{
            statement.setInt(1,id);
            statement.execute();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public Integer create(Location entity) {
        PreparedStatement statement = getPreparedStatement(CREATE);
        try{
            statement.setInt(1,entity.getX());
            statement.setFloat(2,entity.getY());
            statement.setDouble(3,entity.getZ());
            statement.execute();
            ResultSet lastUpdatedLocation = statement.getResultSet();
            if(lastUpdatedLocation.next()){
                int id = lastUpdatedLocation.getInt("id");
                return id;
            } else {
                Logger.error("Creation location in database error");
                return 0;
            }
        } catch (SQLException e){
            e.printStackTrace();
            return 0;
        }
    }
    @Override
    public void deleteAll(){
        PreparedStatement statement = getPreparedStatement(DELETE_ALL);
        try{
            statement.execute();
        } catch (SQLException e){
            Logger.error("Error truncating locations table");
        }
    }
}
