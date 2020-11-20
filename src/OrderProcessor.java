import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class OrderProcessor {
    public static final long RefreshRate = 10000;

    private OrderProcessor() {
    }

    // TODO will have to make tweaks for different quantities
    public static void process() {
        refresh();
        ArrayList<Order> orders = Facade.getPendingOrders();

        for (Order order : orders) {
            try {
                //TODO: figure out what to do below
                //Stock stock = YahooFinance.get(order.getStockSymbol());
                //double curPrice = stock.getQuote().getPrice().doubleValue();
                double curPrice = 500; // TODO should pull from Stocks table
                if (order.getOrderBuyOrSell() == Enums.OrderBuyOrSell.BUY) {
                    switch (order.getOrderType()){
                        case MARKET -> {
                            processMarketBuy(order, curPrice);
                        }
                        case LIMIT -> {
                            processLimitBuy(order, curPrice);
                        }
                        case STOPLOSS -> {
                            processStopLossBuy(order, curPrice);
                        }
                    }
                } else if (order.getOrderBuyOrSell() == Enums.OrderBuyOrSell.SELL) {
                    curPrice = 500;
                    switch (order.getOrderType()){
                        case MARKET -> {
                            processMarketSell(order, curPrice);
                        }
                        case LIMIT -> {
                            processLimitSell(order, curPrice);
                        }
                        case STOPLOSS -> {
                            processStopLossSell(order, curPrice);
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        refresh();
    }

    private static void processSellOrder(Order order, double curPrice){
        try {
            OwnedPosition toSell = Facade.getOwnedPosition(order.getToSellOwnedPositionId());
            if (toSell != null){
                Facade.deleteOwnedPosition(toSell);
                Account account = Facade.getAccount(order.getAccountId());
                account.decrementPositionsHeld();
                account.setMarketValue(account.getMarketValue() - toSell.getMarketValue());
                account.setNetValue(account.getNetValue() - toSell.getMarketValue() + toSell.getProfitLoss());
                order.setOrderStatus(Enums.OrderStatus.COMPLETED);
                Facade.updateOrder(order);
                Facade.updateAccount(account);
            }
        }catch (Exception e){
            System.out.println(e);
        }
    }

    private static void processBuyOrder(Order order, double curPrice){
        try {
            OwnedPosition ownedPosition = new OwnedPosition(Facade.getNextOwnedPositionId(), order.getAccountId(), order.getStockSymbol(),
                    order.getOrderId(), order.getQuantity(), curPrice * order.getQuantity(), curPrice * order.getQuantity(),
                    curPrice - order.getPrice());

            Facade.insertOwnedPosition(ownedPosition);
            if (Facade.getOwnedPosition(order.getOrderId()) != null) {
                order.setOrderStatus(Enums.OrderStatus.COMPLETED);
                Facade.updateOrder(order);
                Account account = Facade.getAccount(order.getAccountId());
                account.incrementPositionsHeld();
                account.setMarketValue(account.getMarketValue() + ownedPosition.getInitialValue());
                account.setNetValue(account.getNetValue() + ownedPosition.getInitialValue());
                Facade.updateAccount(account);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void processMarketBuy(Order order, double curPrice){
        order.setPrice(curPrice); // Since market orders don't care on price
        processBuyOrder(order, curPrice);
    }

    private static void processMarketSell(Order order, double curPrice){
        order.setPrice(curPrice); // Since market orders don't care on price
        processSellOrder(order, curPrice);
    }

    private static void processLimitBuy(Order order, double curPrice){
        if (curPrice <= order.getPrice()){
            processBuyOrder(order, curPrice);
        }
    }

    private static void processLimitSell(Order order, double curPrice){
        if (curPrice >= order.getPrice()){
            processSellOrder(order, curPrice);
        }
    }

    private static void processStopLossBuy(Order order, double curPrice){
        if (curPrice >= order.getPrice()){
            processBuyOrder(order, curPrice);
        }
    }

    private static void processStopLossSell(Order order, double curPrice){
        if (curPrice <= order.getPrice()){
            processSellOrder(order, curPrice);
        }
    }

    public static void refresh() {
        ArrayList<OwnedPosition> positions = Facade.getAllOwnedPositions();
        HashMap<Integer, Account> accounts = new HashMap<>();

        for (OwnedPosition position : positions) {
            try {
                //Stock stock = YahooFinance.get(position.getStockSymbol());
                //double curPrice = stock.getQuote().getPrice().doubleValue();
                double curPrice = 500;
                // Update position
                position.setMarketValue(curPrice * position.getQuantity());
                position.setProfitLoss((position.getMarketValue() - position.getInitialValue()));
                Facade.updateOwnedPosition(position);
                // Update associated account
                // TODO fix date
                accounts.putIfAbsent(position.getAccountId(), new Account(position.getAccountId(), Facade.getAccount(position.getAccountId()).getUserId(),
                        0, 0, 0, 0, new Date(2020, 12, 3)));
                Account account = accounts.get(position.getAccountId());
                account.incrementPositionsHeld();
                account.setMarketValue(account.getMarketValue() + position.getMarketValue());
                account.setProfitLoss(account.getProfitLoss() + position.getProfitLoss());
                // TODO debug to find out why account PL isn't correctly updating among other things
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        for (Integer id : accounts.keySet()) {
            Account account = Facade.getAccount(id);
            accounts.get(id).setNetValue(account.getNetValue() + accounts.get(id).getProfitLoss());
            accounts.get(id).setProfitLoss(account.getProfitLoss() + accounts.get(id).getProfitLoss());
            accounts.get(id).setCreatedDate(account.getCreatedDate()); // copy over value
            Facade.updateAccount(accounts.get(id));
        }
    }
}
