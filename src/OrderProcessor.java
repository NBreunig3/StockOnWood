import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import java.io.IOException;
import java.util.ArrayList;

public class OrderProcessor {
    public static long OrderProcessorRefreshRate = 10000;
    private OrderProcessor() {}

    public static void process() throws InterruptedException {
        while (true) {
            ArrayList<Order> orders = Facade.getPendingOrders();

            for (Order order : orders) {
                double orderPrice = order.getPrice();
                try {
                    //TODO: figure out what to do below
                    //Stock stock = YahooFinance.get(order.getStockSymbol());
                    //double curPrice = stock.getQuote().getPrice().doubleValue();
                    double curPrice = 8;
                    if (order.getOrderType() == Enums.OrderType.BUY) {
                        if (curPrice <= orderPrice) {
                            try {
                                OwnedPosition ownedPosition = new OwnedPosition(order.getAccountId(), order.getStockSymbol(),
                                        order.getOrderId(), order.getQuantity(), curPrice * order.getQuantity(), curPrice * order.getQuantity(),
                                        0);

                                Facade.insertOwnedPosition(ownedPosition);
                                if (Facade.getOwnedPosition(order.getOrderId()) != null) {
                                    order.setOrderStatus(Enums.OrderStatus.COMPLETED);
                                    Facade.updateOrder(order);
                                    Account account = Facade.getAccount(order.getAccountId());
                                    account.incrementPositionsHeld();
                                    account.setValue(account.getValue() + ownedPosition.getMarketValue());
                                    Facade.updateAccount(account);
                                }
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            }
                        }
                    } else if (order.getOrderType() == Enums.OrderType.SELL) {
                        if (curPrice >= orderPrice) {
                            try {
                                // TODO see TODO.txt
                                order.setOrderStatus(Enums.OrderStatus.COMPLETED);
                                Facade.deleteOwnedPosition(Facade.getOwnedPosition(order));
                                Facade.updateOrder(order);
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            Thread.sleep(OrderProcessorRefreshRate);
        }
    }
}
