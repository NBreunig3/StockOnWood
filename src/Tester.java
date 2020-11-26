//import javax.swing.*;
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.Random;
//
//public class Tester {
//    public static void main(String[] args) {
//        Database.getInstance().connect();
//        JFrame jFrame = new JFrame();
////        // Add mock data to database
////        Random random = new Random();
////        int rand = random.nextInt(500);
////        try {
////            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("MOCK_DATA.csv")));
////            String line = bufferedReader.readLine();
////            while (line != null) {
////                String[] vals = line.split(",");
////                Facade.addStock(new Stock(vals[0].replaceAll("^", ""), vals[2].replaceAll("\"", ""), "", vals[1], random.nextInt(rand)));
////                line = bufferedReader.readLine();
////            }
////            bufferedReader.close();
////        }catch (IOException e){
////
////        }
//
////        User user = new User(Facade.getNextUserId(), "Nathan", "Ralph", "Breunig", "123-45-6789",
////                "123-456-7890", "123 Road Lane", "nbreunig", "password", "email@hotmail.com");
////        Facade.addUser(user);
////
////        Account account = new Account(Facade.getNextAccountId(), 0, 0, 0, 0, 0,null);
////        Facade.addAccount(account);
//
//        // buy order
//        Order order = new Order(Facade.getNextOrderId(), 0, "PSEC", Enums.OrderBuyOrSell.BUY, Enums.OrderType.MARKET,
//                1, null, Enums.OrderStatus.PENDING, null);
//        Facade.addOrder(order);
//
//        OrderProcessor.process();
//
////        // sell order
////        Order order = new Order(Facade.getNextOrderId(), 0, "PSEC", Enums.OrderBuyOrSell.SELL, Enums.OrderType.MARKET,
////                1, null, Enums.OrderStatus.PENDING, 0);
////        Facade.addOrder(order);
////
//        OrderProcessor.process();
//
//        Database.getInstance().disconnect();
//    }
//}
