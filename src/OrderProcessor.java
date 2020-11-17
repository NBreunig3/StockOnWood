import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import java.io.IOException;
import java.util.ArrayList;

public class OrderProcessor {
    private OrderProcessor() {}

    public static void process() throws InterruptedException {
        ArrayList<Order> orders = Facade.getPendingOrders();

        for (Order order : orders){
            double orderPrice = order.getPrice();
            try {
                Stock stock = YahooFinance.get(order.getStockSymbol());
                double curPrice = stock.getQuote().getPrice().doubleValue();
                if (order.getOrderType() == Enums.OrderType.BUY){
                    if (curPrice <= orderPrice){
                        try {
                            OwnedPosition ownedPosition = new OwnedPosition(order.getAccountId(), order.getStockSymbol(),
                                    order.getOrderId(), order.getQuantity(), curPrice*order.getQuantity(), curPrice*order.getQuantity(),
                                    0);
                            Facade.insertOwnedPosition(ownedPosition);
                            order.setOrderStatus(Enums.OrderStatus.COMPLETED);
                        }catch (Exception e){
                            System.out.println(e.getMessage());
                        }finally {
                            order.setOrderStatus(Enums.OrderStatus.PENDING);
                        }
                    }
                }else if (order.getOrderType() == Enums.OrderType.SELL){
                    if (curPrice >= orderPrice){
                        try {
                            Facade.deleteOwnedPosition(Facade.getOwnedPosition(order));
                            order.setOrderStatus(Enums.OrderStatus.COMPLETED);
                        }catch (Exception e){
                            System.out.println(e.getMessage());
                        }finally {
                            order.setOrderStatus(Enums.OrderStatus.PENDING);
                        }
                    }
                }
            }catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        Thread.sleep(Settings.OrderProcessorRefreshRate);
    }
}
