package dataAccessObjects;

import common.User;
import commandHandler.utils.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDAO extends AbstractDAO<User,String>{
    private final String tableName = "users";
    private final String SELECT_ALL = "SELECT * FROM "+tableName;
    private final String SELECT_BY_LOGIN = "SELECT * FROM "+tableName+" WHERE login = ?;";
    private final String UPDATE_BY_ID = "UPDATE "+tableName+" SET login = ?, password = ?, salt = ? WHERE login = ?";
    private final String CREATE = "INSERT INTO " + tableName + "(login,password,salt) VALUES (?, ?, ?) RETURNING login";
    private final String DELETE_BY_ID = "DELETE FROM " + tableName + " WHERE login = ?";
    private final String DELETE_ALL = "TRUNCATE TABLE " + tableName;

    public UserDAO(Connection connection) {
        super(connection);
    }

    @Override
    public List<User> getAll() {
        List<User> list = null;
        PreparedStatement statement = getPreparedStatement(SELECT_ALL);
        try{
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                User user = new User(
                        rs.getString("login"),
                        rs.getString("password"),
                        rs.getString("salt")
                );
                list.add(user);
            }
            return list;
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public User get(String login) {
        PreparedStatement statement = getPreparedStatement(SELECT_BY_LOGIN);
        try{
            statement.setString(1,login);
            ResultSet rs = statement.executeQuery();
            if(rs.next()){
                User user = new User(
                        rs.getString("login"),
                        rs.getString("password"),
                        rs.getString("salt")
                );
                return user;
            } else {
                Logger.info("User with login " + login + " not found");
                return null;
            }
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void update(String login, User entity) {
        PreparedStatement statement = getPreparedStatement(UPDATE_BY_ID);
        try{
            statement.setString(1,entity.getLogin());
            statement.setString(2,entity.getPassword());
            statement.setString(3,entity.getSalt());
            statement.execute();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public String create(User entity) {
        PreparedStatement statement = getPreparedStatement(CREATE);
        try{
            statement.setString(1,entity.getLogin());
            statement.setString(2,entity.getPassword());
            statement.setString(3,entity.getSalt());
            statement.execute();
            ResultSet lastUpdatedEntity = statement.getResultSet();
            if(lastUpdatedEntity.next()){
                String login = lastUpdatedEntity.getString("login");
                return login;
            } else {
                Logger.error("Creation user in database error");
                return "";
            }
        } catch (SQLException e){
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public void delete(String login) {
        PreparedStatement statement = getPreparedStatement(DELETE_BY_ID);
        try{
            statement.setString(1,login);
            statement.execute();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void deleteAll() {
        PreparedStatement statement = getPreparedStatement(DELETE_ALL);
        try{
            statement.execute();
        } catch (SQLException e){
            Logger.error("Error truncating users table");
        }
    }
}
