import java.sql.*;
import java.util.ArrayList;

public class Database {
    private static final Database INSTANCE = new Database();
    private String url = "jdbc:sqlite:/Users/nathan/Documents/Programming/School/College/Databases/Semester Project/StockOnWood/Database/StockOnWood.db";
    private Connection connection;
    private Database(){}

    public ResultSet executeQuery(String q){
        ResultSet resultSet = null;
        try {
            PreparedStatement statement = connection.prepareStatement(q);
            resultSet = statement.executeQuery();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return resultSet;
    }

    public void executeUpdate(String q){
        try {
            PreparedStatement statement = connection.prepareStatement(q);
            statement.executeUpdate();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void connect(){
        try {
            connection = DriverManager.getConnection(url);
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void disconnect(){
        try {
            connection.close();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public static Database getInstance(){
        return INSTANCE;
    }
}
