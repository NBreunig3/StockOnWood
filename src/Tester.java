public class Tester {
    public static void main(String[] args) {
        Database.getInstance().connect();

//        User user = new User(Facade.getNextUserId(), "Nathan", "Ralph", "Breunig", "123-45-6789",
//                "123-456-7890", "123 Road Lane", "nbreunig", "password", "email@hotmail.com");
//        Facade.addUser(user);
//
//        Account account = new Account(Facade.getNextAccountId(), 0, 0, 0, 0, 0,null);
//        Facade.addAccount(account);

        // buy order
        Order order = new Order(Facade.getNextOrderId(), 0, "PSEC", Enums.OrderBuyOrSell.BUY, Enums.OrderType.MARKET,
                1, null, Enums.OrderStatus.PENDING, null);
        Facade.addOrder(order);

        OrderProcessor.process();

//        // sell order
//        Order order = new Order(Facade.getNextOrderId(), 0, "PSEC", Enums.OrderBuyOrSell.SELL, Enums.OrderType.MARKET,
//                1, null, Enums.OrderStatus.PENDING, 0);
//        Facade.addOrder(order);
//
        OrderProcessor.process();

        Database.getInstance().disconnect();
    }
}
