import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class OrderProcessor {
    public static long OrderProcessorRefreshRate = 10000;

    private OrderProcessor() {
    }

    public static void process() throws InterruptedException {
        ArrayList<Order> orders = Facade.getPendingOrders();

        for (Order order : orders) {
            double orderPrice = order.getPrice();
            try {
                //TODO: figure out what to do below
                //Stock stock = YahooFinance.get(order.getStockSymbol());
                //double curPrice = stock.getQuote().getPrice().doubleValue();
                double curPrice = new Random().nextInt(100);
                if (order.getOrderBuyOrSell() == Enums.OrderBuyOrSell.BUY) {
                    if (true/*orderPrice <= curPrice*/)/*TODO different order types*/ {
                        try {
                            OwnedPosition ownedPosition = new OwnedPosition(Facade.getNextOwnedPositionId(), order.getAccountId(), order.getStockSymbol(),
                                    order.getOrderId(), order.getQuantity(), curPrice * order.getQuantity(), curPrice * order.getQuantity(),
                                    curPrice - orderPrice);

                            Facade.insertOwnedPosition(ownedPosition);
                            if (Facade.getOwnedPosition(order.getOrderId()) != null) {
                                order.setOrderStatus(Enums.OrderStatus.COMPLETED);
                                Facade.updateOrder(order);
                                Account account = Facade.getAccount(order.getAccountId());
                                account.incrementPositionsHeld();
                                account.setMarketValue(account.getMarketValue() + ownedPosition.getInitialValue());
                                account.setNetValue(account.getNetValue() + ownedPosition.getInitialValue());
                                account.setProfitLoss(account.getProfitLoss() + ownedPosition.getProfitLoss());
                                Facade.updateAccount(account);
                            }
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    }
                } else if (order.getOrderBuyOrSell() == Enums.OrderBuyOrSell.SELL) {
                    if (true/*curPrice >= orderPrice*/)/*TODO different order types*/ {
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
    }

    private static boolean marketBuyAvailable(){
        return true;
    }

    private static boolean marketSellAvailable(){
        return true;
    }

    private static boolean limitBuyAvailable(double limitPrice, double curPrice){
        return curPrice <= limitPrice;
    }

    private static boolean limitSellAvailable(double limitPrice, double curPrice){
        return curPrice >= limitPrice;
    }

    private static boolean stopLossBuyAvailable(double stopPrice, double curPrice){
        return curPrice >= stopPrice;
    }

    private static boolean stopLossSellAvailable(double stopPrice, double curPrice){
        return curPrice <= stopPrice;
    }

    //TODO buy-stop orders
}
