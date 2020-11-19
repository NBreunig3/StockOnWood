public class Tester {
    public static void main(String[] args) throws InterruptedException {
        Database.getInstance().connect();

        User user = new User(Facade.getNextUserId(), "Nathan", "Ralph", "Breunig", "123-45-6789",
                "123-456-7890", "123 Road Lane", "nbreunig", "password", "email@hotmail.com");
        Facade.addUser(user);

        Account account = new Account(Facade.getNextAccountId(), user.getUserId(), 0, 0, 0, 0,null);
        Facade.addAccount(account);

        // buy order
        Order order = new Order(Facade.getNextOrderId(), 0, "TSLA", Enums.OrderBuyOrSell.BUY, Enums.OrderType.MARKET,
                1, 400, Enums.OrderStatus.PENDING);
        Facade.addOrder(order);

//        order = new Order(Facade.getNextOrderId(), 0, "PSEC", Enums.OrderType.BUY,
//                4, 10, Enums.OrderStatus.PENDING);

        // sell order
//        Order order = new Order(Facade.getNextOrderId(), 0, "TSLA", Enums.OrderType.SELL,
//                1, 400, Enums.OrderStatus.PENDING);

        OrderProcessor.process();

        Refresher.refresh();

        Database.getInstance().disconnect();
    }
}
