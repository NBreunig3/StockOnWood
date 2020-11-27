import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Facade {
    private static final Database database = Database.getInstance();
    private Facade() {}

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

    public static User getUser(String username, String password){
        String query = "SELECT * FROM Users WHERE Username = '" + username + "' AND Password = '" + password + "'";
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
        String query = "SELECT * FROM Users";
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
        String query = "INSERT INTO Users(UserId, FirstName, MiddleName, LastName, SSN, PhoneNumber, Address, " +
                "Username, Password, Email) VALUES (V0, 'V2', 'V3', 'V4', 'V5', 'V6', 'V7', 'V8', 'V9', 'V10')";
        query = query.replace("V0", String.valueOf(user.getUserId()));
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

    public static int getNextUserId(){
        String query = "SELECT * FROM Users ORDER BY UserId DESC";
        ResultSet resultSet = database.executeQuery(query);
        try {
            return resultSet.getInt("UserId") + 1;
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return 0;
    }

    public static void updateUser(User user){
        String query = "UPDATE Users SET FirstName = 'V2', MiddleName = 'V3', LastName = 'V4', SSN = 'V5', " +
                "PhoneNumber = 'V6', Address = 'V7', Username = 'V8', Password = 'V9', Email = 'V10' WHERE UserId = V0";
        query = query.replace("V0", String.valueOf(user.getUserId()));
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

    public static void deleteUser(User user){
        String query = "DELETE FROM Users WHERE UserId = " + user.getUserId();
        database.executeUpdate(query);
    }
    // END REGION USER

    // REGION ACCOUNT
    public static Account getAccount(int accountId){
        String query = "SELECT * FROM Accounts WHERE AccountId = " + accountId;
        Account account = null;
        ResultSet resultSet = database.executeQuery(query);
        try {
            account = new Account(resultSet.getInt("AccountId"), resultSet.getInt("UserId"),
                    resultSet.getInt("PositionsHeld"), resultSet.getDouble("ProfitLoss"),
                    resultSet.getDouble("MarketValue"), resultSet.getDouble("NetValue") , new Date(2020, 11, 17));/*resultSet.getDate("CreatedDate")*///TODO);
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return account;
    }

    public static Account getAccount(User user){
        String query = "SELECT * FROM Accounts WHERE UserId = " + user.getUserId();
        Account account = null;
        ResultSet resultSet = database.executeQuery(query);
        try {
            account = new Account(resultSet.getInt("AccountId"), resultSet.getInt("UserId"),
                    resultSet.getInt("PositionsHeld"), resultSet.getDouble("ProfitLoss"),
                    resultSet.getDouble("MarketValue"), resultSet.getDouble("NetValue") , new Date(2020, 11, 17));/*resultSet.getDate("CreatedDate")*///TODO);
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return account;
    }

    public static ArrayList<Account> getAllAccounts(){
        ArrayList<Account> accounts = new ArrayList<>();
        String query = "SELECT * FROM Accounts";
        ResultSet resultSet = database.executeQuery(query);
        try {
            while (resultSet.next()) {
                accounts.add(getAccount(resultSet.getInt("AccountId")));
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return accounts;
    }

    public static void addAccount(Account account){
        // TODO check if user already exists
        String query = "INSERT INTO Accounts(AccountId, UserId, ProfitLoss, PositionsHeld, MarketValue, NetValue) " +
                "VALUES (V1, V2, V3, V4, V5, V6)";
        query = query.replace("V1", String.valueOf(account.getAccountId()));
        query = query.replace("V2", String.valueOf(account.getUserId()));
        query = query.replace("V3", String.valueOf(account.getProfitLoss()));
        query = query.replace("V4", String.valueOf(account.getPositionsHeld()));
        query = query.replace("V5", String.valueOf(account.getMarketValue()));
        query = query.replace("V6", String.valueOf(account.getNetValue()));
        database.executeUpdate(query);
    }

    public static int getNextAccountId(){
        String query = "SELECT * FROM Accounts ORDER BY AccountId DESC";
        ResultSet resultSet = database.executeQuery(query);
        try {
            return resultSet.getInt("AccountId") + 1;
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return 0;
    }

    public static void updateAccount(Account account){
        String query = "UPDATE Accounts SET UserId = V2, ProfitLoss = V3, PositionsHeld = V4, MarketValue = V5, " +
                "CreatedDate = V6, NetValue = V8 WHERE AccountId = V7";
        query = query.replace("V2", String.valueOf(account.getUserId()));
        query = query.replace("V3", String.valueOf(account.getProfitLoss()));
        query = query.replace("V4", String.valueOf(account.getPositionsHeld()));
        query = query.replace("V5", String.valueOf(account.getMarketValue()));
        query = query.replace("V6", String.valueOf(account.getCreatedDate()));
        query = query.replace("V7", String.valueOf(account.getAccountId()));
        query = query.replace("V8", String.valueOf(account.getNetValue()));
        database.executeUpdate(query);
    }

    public static void deleteAccount(Account account){
        String query = "DELETE FROM Accounts WHERE AccountId = " + account.getAccountId();
        database.executeUpdate(query);
    }
    // END REGION ACCOUNT

    // REGION ORDER
    public static Order getOrder(int orderId){
        String query = "SELECT * FROM Orders WHERE OrderId = " + orderId;
        Order order = null;
        ResultSet resultSet = database.executeQuery(query);
        try {
            order = new Order(resultSet.getInt("OrderId"), resultSet.getInt("AccountId") ,resultSet.getString("StockSymbol"),
                    Enums.OrderBuyOrSell.valueOf(resultSet.getString("OrderBuyOrSell")), Enums.OrderType.valueOf(resultSet.getString("OrderType")), resultSet.getInt("Quantity"),
                    resultSet.getDouble("Price"), Enums.OrderStatus.valueOf(resultSet.getString("Status")), resultSet.getInt("ToSellOwnedPositionId"));
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return order;
    }

    public static ArrayList<Order> getOrders(Account account){
        ArrayList<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM Orders WHERE AccountId = " + account.getAccountId();
        ResultSet resultSet = database.executeQuery(query);
        try {
            while (resultSet.next()) {
                orders.add(getOrder(resultSet.getInt("OrderId")));
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return orders;
    }

    public static ArrayList<Order> getAllOrders(){
        ArrayList<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM Orders";
        ResultSet resultSet = database.executeQuery(query);
        try {
            while (resultSet.next()) {
                orders.add(getOrder(resultSet.getInt("OrderId")));
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return orders;
    }

    public static ArrayList<Order> getPendingOrders(){
        ArrayList<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM Orders WHERE Status = 'PENDING'";
        ResultSet resultSet = database.executeQuery(query);
        try {
            while (resultSet.next()) {
                orders.add(getOrder(resultSet.getInt("OrderId")));
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return orders;
    }

    public static void addOrder(Order order){
        // TODO check if user already exists
        String query = "INSERT INTO Orders(OrderId, AccountId, StockSymbol, OrderBuyOrSell, OrderType, Quantity, Price, Status, ToSellOwnedPositionId)" +
                " VALUES (V1, 'V7', 'V2', 'V8', 'V3', 'V4', 'V5', 'V6', V9)";
        query = query.replace("V1", String.valueOf(order.getOrderId()));
        query = query.replace("V2", order.getStockSymbol());
        query = query.replace("V8", order.getOrderBuyOrSell().toString());
        query = query.replace("V3", order.getOrderType().toString());
        query = query.replace("V4", String.valueOf(order.getQuantity()));
        query = query.replace("V5", String.valueOf(order.getPrice()));
        query = query.replace("V6", order.getOrderStatus().toString());
        query = query.replace("V7", String.valueOf(order.getAccountId()));
        query = query.replace("V9", String.valueOf(order.getToSellOwnedPositionId()));
        database.executeUpdate(query);
    }

    public static void updateOrder(Order order){
        String query = "UPDATE Orders SET Status = 'V1', Price = V3 WHERE OrderId = V2";
        query = query.replace("V1", order.getOrderStatus().toString());
        query = query.replace("V2", String.valueOf(order.getOrderId()));
        query = query.replace("V3", String.valueOf(order.getPrice()));
        database.executeUpdate(query);
    }

    public static void deleteOrder(Order order){
        String query = "DELETE FROM Orders WHERE OrderId = " + order.getOrderId();
        database.executeUpdate(query);
    }

    public static int getNextOrderId(){
        String query = "SELECT * FROM Orders ORDER BY OrderId DESC";
        ResultSet resultSet = database.executeQuery(query);
        try {
            return resultSet.getInt("OrderId") + 1;
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return 0;
    }
    // END REGION ORDER

    // REGION STOCK
    public static Stock getStock(String stockSymbol){
        String query = "SELECT * FROM Stocks WHERE StockSymbol = '" + stockSymbol + "'";
        Stock stock = null;
        ResultSet resultSet = database.executeQuery(query);
        try {
            stock = new Stock(resultSet.getString("StockSymbol"), resultSet.getString("Name"),
                    resultSet.getString("Description"), resultSet.getString("Sector"), resultSet.getDouble("CurrentPrice"));
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return stock;
    }

    public static ArrayList<Stock> getAllStocks(){
        ArrayList<Stock> stocks = new ArrayList<>();
        String query = "SELECT * FROM Stocks";
        ResultSet resultSet = database.executeQuery(query);
        try {
            while (resultSet.next()) {
                stocks.add(getStock(resultSet.getString("StockSymbol")));
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return stocks;
    }

    public static void addStock(Stock stock){
        // TODO check if user already exists
        String query = "INSERT INTO Stocks(StockSymbol, Name, Description, Sector, CurrentPrice)" +
                " VALUES ('V1', 'V2', 'V3', 'V4', V5)";
        query = query.replace("V1", stock.getStockSymbol());
        query = query.replace("V2", stock.getName());
        query = query.replace("V3", stock.getDescription());
        query = query.replace("V4", stock.getSector());
        query = query.replace("V5", String.valueOf(stock.getCurrentPrice()));
        database.executeUpdate(query);
    }

    public static void updateStock(Stock stock){
        String query = "UPDATE Stocks SET Name = 'V2', Description = 'V3', Sector = 'V4', CurrentPrice = V6 WHERE StockSymbol = 'V5'";
        query = query.replace("V2", stock.getName());
        query = query.replace("V3", stock.getDescription());
        query = query.replace("V4", stock.getSector());
        query = query.replace("V5", stock.getStockSymbol());
        query = query.replace("V6", String.valueOf(stock.getCurrentPrice()));
        database.executeUpdate(query);
    }

    public static void deleteStock(Stock stock){
        String query = "DELETE FROM Stocks WHERE StockSymbol = " + stock.getStockSymbol();
        database.executeUpdate(query);
    }
    // END REGION STOCK

    // REGION OWNEDPOSITIONS
    public static void insertOwnedPosition(OwnedPosition ownedPosition){
        String query = "INSERT INTO OwnedPositions(OwnedPositionId, AccountId, StockSymbol, OrderId, Quantity, InitialValue, MarketValue, " +
                "ProfitLoss) VALUES (V0, V1, 'V2', V3, V4, V5, V6, V7)";
        query = query.replace("V0", String.valueOf(ownedPosition.getOwnedPositionId()));
        query = query.replace("V1", String.valueOf(ownedPosition.getAccountId()));
        query = query.replace("V2", ownedPosition.getStockSymbol());
        query = query.replace("V3", String.valueOf(ownedPosition.getOrderId()));
        query = query.replace("V4", String.valueOf(ownedPosition.getQuantity()));
        query = query.replace("V5", String.valueOf(ownedPosition.getInitialValue()));
        query = query.replace("V6", String.valueOf(ownedPosition.getMarketValue()));
        query = query.replace("V7", String.valueOf(ownedPosition.getProfitLoss()));
        database.executeUpdate(query);
    }

    public static void updateOwnedPosition(OwnedPosition ownedPosition){
        String query = "UPDATE OwnedPositions SET MarketValue = V1, ProfitLoss = V2 WHERE OwnedPositionId = V3";
        query = query.replace("V1", String.valueOf(ownedPosition.getMarketValue()));
        query = query.replace("V2", String.valueOf(ownedPosition.getProfitLoss()));
        query = query.replace("V3", String.valueOf(ownedPosition.getOwnedPositionId()));
        database.executeUpdate(query);
    }

    public static OwnedPosition getOwnedPosition(int ownedPositionId){
        String query = "SELECT * FROM OwnedPositions WHERE OwnedPositionId = " + ownedPositionId;
        ResultSet resultSet = database.executeQuery(query);
        try {
            return new OwnedPosition(resultSet.getInt("OwnedPositionId"), resultSet.getInt("AccountId"), resultSet.getString("StockSymbol"),
                    resultSet.getInt("OrderId"), resultSet.getInt("Quantity"), resultSet.getDouble("InitialValue"),
                    resultSet.getDouble("MarketValue"), resultSet.getDouble("ProfitLoss"));
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static OwnedPosition getOwnedPosition(Order order){
        String query = "SELECT * FROM OwnedPositions WHERE OrderId = " + order.getOrderId();
        ResultSet resultSet = database.executeQuery(query);
        try {
            return new OwnedPosition(resultSet.getInt("OwnedPositionId"), resultSet.getInt("AccountId"), resultSet.getString("StockSymbol"),
                    resultSet.getInt("OrderId"), resultSet.getInt("Quantity"), resultSet.getDouble("InitialValue"),
                    resultSet.getDouble("MarketValue"), resultSet.getDouble("ProfitLoss"));
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static ArrayList<OwnedPosition> getOwnedPositions(Account account, Stock stock){
        ArrayList<OwnedPosition> positions = new ArrayList<>();
        String query = "SELECT * FROM OwnedPositions WHERE AccountId = " + account.getAccountId() + " AND StockSymbol = '" + stock.getStockSymbol() + "'";
        ResultSet resultSet = database.executeQuery(query);
        try {
            while (resultSet.next()){
                positions.add(new OwnedPosition(resultSet.getInt("OwnedPositionId"), resultSet.getInt("AccountId"), resultSet.getString("StockSymbol"),
                        resultSet.getInt("OrderId"), resultSet.getInt("Quantity"),
                        resultSet.getDouble("InitialValue"), resultSet.getDouble("MarketValue"),
                        resultSet.getDouble("ProfitLoss")));
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return positions;
    }

    public static void deleteOwnedPosition(OwnedPosition ownedPosition){
        String query = "DELETE FROM OwnedPositions WHERE OwnedPositionId = " + ownedPosition.getOwnedPositionId();
        database.executeUpdate(query);
    }

    public static ArrayList<OwnedPosition> getOwnedPositions(Account account){
        ArrayList<OwnedPosition> positions = new ArrayList<>();
        String query = "SELECT * FROM OwnedPositions WHERE AccountId = " + account.getAccountId();
        ResultSet resultSet = database.executeQuery(query);
        try {
            while (resultSet.next()){
                positions.add(new OwnedPosition(resultSet.getInt("OwnedPositionId"), resultSet.getInt("AccountId"), resultSet.getString("StockSymbol"),
                        resultSet.getInt("OrderId"), resultSet.getInt("Quantity"),
                        resultSet.getDouble("InitialValue"), resultSet.getDouble("MarketValue"),
                        resultSet.getDouble("ProfitLoss")));
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return positions;
    }

    public static ArrayList<OwnedPosition> getAllOwnedPositions(){
        ArrayList<OwnedPosition> positions = new ArrayList<>();
        String query = "SELECT * FROM OwnedPositions";
        ResultSet resultSet = database.executeQuery(query);
        try {
            while (resultSet.next()){
                positions.add(new OwnedPosition(resultSet.getInt("OwnedPositionId"), resultSet.getInt("AccountId"), resultSet.getString("StockSymbol"),
                        resultSet.getInt("OrderId"), resultSet.getInt("Quantity"),
                        resultSet.getDouble("InitialValue"), resultSet.getDouble("MarketValue"),
                        resultSet.getDouble("ProfitLoss")));
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return positions;
    }

    public static int getNextOwnedPositionId(){
        String query = "SELECT * FROM OwnedPositions ORDER BY OwnedPositionId DESC";
        ResultSet resultSet = database.executeQuery(query);
        try {
            return resultSet.getInt("OwnedPositionId") + 1;
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return 0;
    }
    // END REGION OWNEDPOSITIONS
}
