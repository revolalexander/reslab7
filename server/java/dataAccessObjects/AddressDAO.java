package dataAccessObjects;

import domainObjectModel.Address;
import commandHandler.utils.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class AddressDAO extends AbstractDAO<Address,Integer>{
    private LocationDAO locationDAO;
    private final String tableName = "addresses";
    private final String SELECT_ALL = "SELECT * FROM "+tableName;
    private final String SELECT_BY_ID = "SELECT * FROM "+tableName+" WHERE id = ?";
    private final String SELECT_LOCATION_ID_BY_ID = "SELECT location_id FROM "+tableName+" WHERE id = ?";
    private final String UPDATE_BY_ID = "UPDATE "+tableName+" SET street = ?, zip_code = ?, location_id = ? WHERE id = ?";
    private final String CREATE = "INSERT INTO " + tableName + "(street,zip_code,location_id) VALUES (?, ?, ?) RETURNING id";
    private final String DELETE_BY_ID = "DELETE FROM " + tableName + " WHERE id = ?";
    private final String DELETE_ALL = "TRUNCATE TABLE " + tableName;

    public AddressDAO(LocationDAO locationDAO, Connection connection) {
        super(connection);
        this.locationDAO = locationDAO;
    }

    public List<Address> getAll() {
        ResultSet rs = getAllResultSet(SELECT_ALL);
        List<Address> list = null;
        try{
            while(rs.next()){
                list.add(new Address(
                        rs.getInt("id"),
                        rs.getString("street"),
                        rs.getString("zip_code"),
                        locationDAO.get(rs.getInt("location_id"))
                ));
            }
            return list;
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Address get(Integer id) {
        PreparedStatement statement = getPreparedStatement(SELECT_BY_ID);
        try{
            statement.setInt(1,id);
            ResultSet rs = statement.executeQuery();
            rs.next();
            Address address = new Address(
                    rs.getInt("id"),
                    rs.getString("street"),
                    rs.getString("zip_code"),
                    locationDAO.get(rs.getInt("location_id"))
            );
            return address;
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void update(Integer id, Address entity) {
        PreparedStatement mainStatement = getPreparedStatement(UPDATE_BY_ID);
        PreparedStatement locationIdStatement = getPreparedStatement(SELECT_LOCATION_ID_BY_ID);
        try{
            locationIdStatement.setInt(1,id);
            ResultSet locationIdSet = locationIdStatement.executeQuery();
            int locationId;
            if(locationIdSet.next()){
                locationId = locationIdSet.getInt("location_id");
            } else {
                Logger.error("Error updating Address in databse");
                return;
            }

            mainStatement.setString(1,entity.getStreet());
            mainStatement.setString(2,entity.getZipCode());
            locationDAO.update(locationId,entity.getTown());
            mainStatement.setInt(3,locationId);
            mainStatement.setInt(4,id);
            mainStatement.execute();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Integer id) {
        Address address = get(id);
        PreparedStatement statement = getPreparedStatement(DELETE_BY_ID);
        try{
            statement.setInt(1,id);
            statement.execute();
        } catch (SQLException e){
            e.printStackTrace();
        }
        locationDAO.delete(address.getTown().getId());
    }

    @Override
    public Integer create(Address entity) {
        PreparedStatement statement = getPreparedStatement(CREATE);
        try{
            statement.setString(1,entity.getStreet());
            statement.setString(2,entity.getZipCode());
            int locationId = locationDAO.create(entity.getTown());
            statement.setInt(3,locationId);
            statement.execute();
            ResultSet lastUpdatedAddress = statement.getResultSet();
            if(lastUpdatedAddress.next()){
                int id = lastUpdatedAddress.getInt("id");
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
    public void deleteAll(){
        PreparedStatement statement = getPreparedStatement(DELETE_ALL);
        try{
            statement.execute();
        } catch (SQLException e){
            Logger.error("Error truncating address table");
        }
    }
}
