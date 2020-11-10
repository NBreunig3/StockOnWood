import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Facade {
    private static final Database database = Database.getInstance();

    // REGION USER
    public static User getUser(int userId){
        String query = "SELECT * FROM Users WHERE UserId = " + userId;
        User user = null;
        ResultSet resultSet = database.executeQuery(query);
        try {
            user = new User(resultSet.getInt("UserId"), resultSet.getString("FirstName"), resultSet.getString("MiddleName"),
                    resultSet.getString("LastName"), resultSet.getString("SSN"), resultSet.getString("PhoneNumber"),
                    resultSet.getString("Address"), resultSet.getString("Username"), resultSet.getString("Password"),
                    resultSet.getString("Email"));
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return user;
    }

    public static ArrayList<User> getAllUsers(){
        ArrayList<User> users = new ArrayList<>();
        String query = "SELECT * FROM Users WHERE UserId";
        ResultSet resultSet = database.executeQuery(query);
        try {
            while (resultSet.next()) {
                users.add(getUser(resultSet.getInt("UserId")));
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return users;
    }

    public static void addUser(User user){
        // TODO check if user already exists
        String query = "INSERT INTO Users(UserId, FirstName, MiddleName, LastName, SSN, PhoneNumber, Address" +
                "Username, Password, Email) VALUES (V1, 'V2', 'V3', 'V4', 'V5', 'V6', 'V7', 'V8', 'V9', 'V10')";
        query = query.replace("V1", String.valueOf(user.getUserId()));
        query = query.replace("V2", user.getFirstName());
        query = query.replace("V3", user.getMiddleName());
        query = query.replace("V4", user.getLastName());
        query = query.replace("V5", user.getSsn());
        query = query.replace("V6", user.getPhoneNumber());
        query = query.replace("V7", user.getAddress());
        query = query.replace("V8", user.getUsername());
        query = query.replace("V9", user.getPassword());
        query = query.replace("V10", user.getEmail());
        database.executeUpdate(query);
    }

    public static void updateUser(User user){
        String query = "UPDATE Users SET UserId = V1, FirstName = 'V2', MiddleName = 'V3', LastName = 'V4', SSN = 'V5', " +
                "PhoneNumber = 'V6', Address = 'V7', Username = 'V8', Password = 'V9', Email = 'V10' WHERE UserId = V11";
        query = query.replace("V1", String.valueOf(user.getUserId()));
        query = query.replace("V2", user.getFirstName());
        query = query.replace("V3", user.getMiddleName());
        query = query.replace("V4", user.getLastName());
        query = query.replace("V5", user.getSsn());
        query = query.replace("V6", user.getPhoneNumber());
        query = query.replace("V7", user.getAddress());
        query = query.replace("V8", user.getUsername());
        query = query.replace("V9", user.getPassword());
        query = query.replace("V10", user.getEmail());
        query = query.replace("V11", String.valueOf(user.getUserId()));
        database.executeUpdate(query);
    }

    public static void deleteUser(User user){
        String query = "DELETE FROM Users WHERE UserId = " + user.getUserId();
        database.executeUpdate(query);
    }
    // END REGION USER
}
