package dataAccessObjects;

import commandHandler.utils.Logger;
import domainObjectModel.Venue;
import domainObjectModel.VenueType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class VenueDAO extends AbstractDAO<Venue,Integer>{
    private AddressDAO addressDAO;
    private final String tableName = "venues";
    private final String SELECT_ALL = "SELECT * FROM "+tableName;
    private final String SELECT_BY_ID = "SELECT * FROM "+tableName+" WHERE id = ?";
    private final String UPDATE_BY_ID = "UPDATE "+tableName+" SET name = ?, capacity = ?, type = ?, address_id = ? WHERE id = ?";
    private final String DELETE_BY_ID = "DELETE FROM " + tableName + " WHERE id = ?";
    private final String CREATE = "INSERT INTO " + tableName + "(name,capacity,type,address_id) VALUES (?, ?, ?, ?) RETURNING id";
    private final String SELECT_ADDRESS_ID_BY_ID = "SELECT address_id FROM "+tableName+" WHERE id = ?";
    private final String DELETE_ALL = "TRUNCATE TABLE " + tableName;


    public VenueDAO(AddressDAO addressDAO, Connection connection) {
        super(connection);
        this.addressDAO = addressDAO;
    }
    @Override
    public List<Venue> getAll() {
        ResultSet rs = getAllResultSet(SELECT_ALL);
        List<Venue> list = null;
        try{
            while(rs.next()){
                Venue venue = new Venue(
                        rs.getString("name"),
                        rs.getLong("capacity"),
                        VenueType.valueOf(rs.getString("type")),
                        addressDAO.get(rs.getInt("address_id"))
                );
                venue.setId(rs.getInt("id"));
                list.add(venue);
            }
            return list;
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Venue get(Integer id) {
        PreparedStatement statement = getPreparedStatement(SELECT_BY_ID);
        try{
            statement.setInt(1,id);
            ResultSet rs = statement.executeQuery();
            if(rs.next()){
                Venue venue = new Venue(
                        rs.getString("name"),
                        rs.getLong("capacity"),
                        VenueType.valueOf(rs.getString("type")),
                        addressDAO.get(rs.getInt("address_id"))
                );
                venue.setId(id);
                return venue;
            } else {
                Logger.info("Venue with id " + id + " not found");
                return null;
            }
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void update(Integer id, Venue entity) {
        PreparedStatement mainStatement = getPreparedStatement(UPDATE_BY_ID);
        PreparedStatement addressIdStatement = getPreparedStatement(SELECT_ADDRESS_ID_BY_ID);
        try{
            addressIdStatement.setInt(1,id);
            ResultSet addressIdSet = addressIdStatement.executeQuery();
            int addressId;
            if(addressIdSet.next()){
                addressId = addressIdSet.getInt("address_id");
            } else {
                Logger.error("Error updating Venue in database");
                return;
            }

            mainStatement.setString(1,entity.getName());
            mainStatement.setLong(2,entity.getCapacity());
            mainStatement.setString(3,entity.getType().toString());
            addressDAO.update(addressId,entity.getAddress());
            mainStatement.setInt(4,addressId);
            mainStatement.setInt(5,id);
            mainStatement.execute();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public Integer create(Venue entity) {
        PreparedStatement statement = getPreparedStatement(CREATE);
        try{
            statement.setString(1,entity.getName());
            statement.setLong(2,entity.getCapacity());
            statement.setString(3,entity.getType().toString());
            int addressId = addressDAO.create(entity.getAddress());
            statement.setInt(4,addressId);
            statement.execute();
            ResultSet lastUpdatedVenue = statement.getResultSet();
            if(lastUpdatedVenue.next()){
                int id = lastUpdatedVenue.getInt("id");
                return id;
            } else {
                Logger.error("Creation address in database error");
                return 0;
            }
        } catch (SQLException e){
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public void delete(Integer id) {
        Venue venue = get(id);
        PreparedStatement statement = getPreparedStatement(DELETE_BY_ID);
        try{
            statement.setInt(1,id);
            statement.execute();
        } catch (SQLException e){
            e.printStackTrace();
        }
        addressDAO.delete(venue.getAddress().getId());
    }
    @Override
    public void deleteAll(){
        PreparedStatement statement = getPreparedStatement(DELETE_ALL);
        try{
            statement.execute();
        } catch (SQLException e){
            Logger.error("Error truncatin venue table");
        }
    }
}
