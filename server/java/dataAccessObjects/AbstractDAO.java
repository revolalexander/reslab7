package dataAccessObjects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public abstract class AbstractDAO<E,K>  {
    private Connection connection;

    public AbstractDAO(Connection connection){
        this.connection = connection;
    }

    public abstract List<E> getAll();
    public abstract E get(K id);
    public abstract void update(K id, E entity);
    public abstract K create(E entity);
    public abstract void delete(K id);
    public abstract void deleteAll();
    protected ResultSet getAllResultSet(String sql){
        PreparedStatement statement = getPreparedStatement(sql);
        try{
            ResultSet rs = statement.executeQuery();
            return rs;
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }
    public PreparedStatement getPreparedStatement(String sql){
        PreparedStatement statement = null;
        try{
            statement = connection.prepareStatement(sql);
        } catch (SQLException e){
            e.printStackTrace();
        }
        return statement;
    }

}
