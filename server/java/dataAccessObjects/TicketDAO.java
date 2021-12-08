package dataAccessObjects;

import commandHandler.utils.Logger;
import domainObjectModel.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TicketDAO extends AbstractDAO<Ticket,Integer>{
    private VenueDAO venueDAO;
    private CoordinatesDAO coordinatesDAO;
    private final String tableName = "tickets";
    private final String SELECT_ALL = "SELECT * FROM "+tableName;
    private final String SELECT_BY_ID = "SELECT * FROM "+tableName+" WHERE id = ?";
    private final String UPDATE_BY_ID = "UPDATE "+tableName+" SET name = ?, coordinates_id = ?, creation_date = ?, price = ?, comment = ?, refundable = ?, type = ?, venue_id = ?, creator = ? WHERE id = ?";
    private final String DELETE_BY_ID = "DELETE FROM " + tableName + " WHERE id = ?";
    private final String CREATE = "INSERT INTO " + tableName + "(id,name,coordinates_id,creation_date,price,comment,refundable,type,venue_id,creator) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";
    private final String SELECT_VENUE_ID_BY_ID = "SELECT venue_id FROM "+tableName+" WHERE id = ?";
    private final String SELECT_COORDINATES_ID_BY_ID = "SELECT coordinates_id FROM "+tableName+" WHERE id = ?";
    private final String DELETE_ALL = "TRUNCATE TABLE " + tableName;


    public TicketDAO(Connection connection, VenueDAO venueDAO, CoordinatesDAO coordinatesDAO) {
        super(connection);
        this.venueDAO = venueDAO;
        this.coordinatesDAO = coordinatesDAO;
    }
    @Override
    public List<Ticket> getAll() {
        ResultSet rs = getAllResultSet(SELECT_ALL);
        List<Ticket> list = new ArrayList<>();
        try{
            while(rs.next()){
                Ticket ticket = new Ticket(
                        rs.getString("name"),
                        rs.getDate("creation_date"),
                        coordinatesDAO.get(rs.getInt("coordinates_id")),
                        rs.getDouble("price"),
                        rs.getString("comment"),
                        rs.getBoolean("refundable"),
                        TicketType.valueOf(rs.getString("type")),
                        venueDAO.get(rs.getInt("venue_id"))
                );
                ticket.setCreator(rs.getString("creator"));
                ticket.setId(rs.getInt("id"));
                list.add(ticket);
            }
            return list;
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Ticket get(Integer id) {
        PreparedStatement statement = getPreparedStatement(SELECT_BY_ID);
        try{
            statement.setInt(1,id);
            ResultSet rs = statement.executeQuery();
            if(rs.next()){
                Ticket ticket = new Ticket(
                        rs.getString("name"),
                        rs.getDate("creation_date"),
                        coordinatesDAO.get(rs.getInt("coordinates_id")),
                        rs.getDouble("price"),
                        rs.getString("comment"),
                        rs.getBoolean("refundable"),
                        TicketType.valueOf(rs.getString("type")),
                        venueDAO.get(rs.getInt("venue_id"))
                );
                ticket.setId(id);
                ticket.setCreator(rs.getString("creator"));
                return ticket;
            } else {
                Logger.info("Ticket with id " + id + " not found");
                return null;
            }
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void update(Integer id, Ticket entity) {
        PreparedStatement mainStatement = getPreparedStatement(UPDATE_BY_ID);
        PreparedStatement venueIdStatement = getPreparedStatement(SELECT_VENUE_ID_BY_ID);
        PreparedStatement coordinatesIdStatement = getPreparedStatement(SELECT_COORDINATES_ID_BY_ID);
        try{
            coordinatesIdStatement.setInt(1,id);
            ResultSet coordsIdSet = coordinatesIdStatement.executeQuery();
            int coordsId;
            if(coordsIdSet.next()){
                coordsId = coordsIdSet.getInt("coordinates_id");
            } else {
                Logger.error("Error updating Coordinates in database");
                return;
            }
            venueIdStatement.setInt(1,id);
            ResultSet venueIdSet = venueIdStatement.executeQuery();
            int venueId;
            if(venueIdSet.next()){
                venueId = venueIdSet.getInt("venue_id");
            } else {
                Logger.error("Error updating Venue in database");
                return;
            }

            mainStatement.setString(1,entity.getName());
            coordinatesDAO.update(coordsId, entity.getCoordinates());
            mainStatement.setInt(2,coordsId);
            mainStatement.setDate(3, new java.sql.Date(entity.getCreationDate().getTime()));
            mainStatement.setDouble(4, entity.getPrice());
            mainStatement.setString(5, entity.getComment());
            mainStatement.setBoolean(6, entity.isRefundable());
            mainStatement.setString(7,entity.getTicketType().toString());
            venueDAO.update(venueId,entity.getVenue());
            mainStatement.setInt(8,venueId);
            mainStatement.setString(9, entity.getCreator());
            mainStatement.setInt(10,id);
            mainStatement.execute();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public Integer create(Ticket entity) {
        PreparedStatement statement = getPreparedStatement(CREATE);
        try{
            statement.setInt(1,entity.getId());
            statement.setString(2,entity.getName());
            int coordsId = coordinatesDAO.create(entity.getCoordinates());
            statement.setInt(3, coordsId);
            statement.setDate(4, new java.sql.Date(entity.getCreationDate().getTime()));
            statement.setDouble(5,entity.getPrice());
            statement.setString(6,entity.getComment());
            statement.setBoolean(7,entity.isRefundable());
            statement.setString(8,entity.getTicketType().toString());
            int venueId = venueDAO.create(entity.getVenue());
            statement.setInt(9,venueId);
            statement.setString(10, entity.getCreator());
            statement.execute();
            ResultSet lastUpdatedTicket = statement.getResultSet();
            if(lastUpdatedTicket.next()){
                int id = lastUpdatedTicket.getInt("id");
                return id;
            } else {
                Logger.error("Creation Ticket in database error");
                return 0;
            }
        } catch (SQLException e){
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public void delete(Integer id) {

        Ticket ticket = get(id);
        PreparedStatement statement = getPreparedStatement(DELETE_BY_ID);
        try{
            statement.setInt(1,id);
            statement.execute();
        } catch (SQLException e){
            e.printStackTrace();
        }
        venueDAO.delete(ticket.getVenue().getId());
        coordinatesDAO.delete(ticket.getCoordinates().getId());
    }
    @Override
    public void deleteAll(){
        PreparedStatement statement = getPreparedStatement(DELETE_ALL);
        try{
            statement.execute();
        } catch (SQLException e){
            Logger.error("Error truncating ticket table");
        }
    }
}
