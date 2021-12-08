package dataAccessObjects;

import domainObjectModel.Coordinates;
import domainObjectModel.Location;
import commandHandler.utils.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CoordinatesDAO extends AbstractDAO<Coordinates, Integer>{

    private final String tableName = "coordinates";
    private final String SELECT_ALL = "SELECT * FROM "+tableName;
    private final String SELECT_BY_ID = "SELECT * FROM "+tableName+" WHERE id = ?";
    private final String UPDATE_BY_ID = "UPDATE "+tableName+" SET x = ?, y = ? WHERE id = ?";
    private final String DELETE_BY_ID = "DELETE FROM " + tableName + " WHERE id = ?";
    private final String CREATE = "INSERT INTO " + tableName + "(x,y) VALUES (?, ?) RETURNING id";
    private final String DELETE_ALL = "TRUNCATE TABLE " + tableName + " CASCADE";

    public CoordinatesDAO(Connection connection) {
        super(connection);
    }

    @Override
    public List<Coordinates> getAll(){
        List<Coordinates> list = null;
        PreparedStatement statement = getPreparedStatement(SELECT_ALL);
        try{
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                Coordinates coordinates = new Coordinates(
                        rs.getDouble("x"),
                        rs.getLong("y")
                );
                coordinates.setId(rs.getInt("id"));
                list.add(coordinates);
            }
            return list;
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Coordinates get(Integer id) {
        PreparedStatement statement = getPreparedStatement(SELECT_BY_ID);
        try{
            statement.setInt(1,id);
            ResultSet rs = statement.executeQuery();
            if(rs.next()){
                Coordinates coordinates = new Coordinates(
                        rs.getDouble("x"),
                        rs.getLong("y")
                );
                coordinates.setId(id);
                return coordinates;
            } else {
                Logger.info("Coordinates with id " + id + " not found");
                return null;
            }
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void update(Integer id, Coordinates entity) {
        PreparedStatement statement = getPreparedStatement(UPDATE_BY_ID);
        try{
            statement.setDouble(1,entity.getX());
            statement.setLong(2,entity.getY());
            statement.setInt(3,id);
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
    public Integer create(Coordinates entity) {
        PreparedStatement statement = getPreparedStatement(CREATE);
        try{
            statement.setDouble(1,entity.getX());
            statement.setLong(2,entity.getY());
            statement.execute();
            ResultSet lastUpdatedCoordinates = statement.getResultSet();
            if(lastUpdatedCoordinates.next()){
                int id = lastUpdatedCoordinates.getInt("id");
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
            Logger.error("Error truncating coordinates table");
        }
    }
}
