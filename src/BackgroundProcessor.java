import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class BackgroundProcessor implements Runnable{
    public static final long ProcessRate = 10000;

    @Override
    public void run() {
        while (true) {
//            System.out.println("Refresh Started");
            refresh();
            processOrders();
            refresh();
//            System.out.println("Refresh Ended");
            try {
                Thread.sleep(ProcessRate);
            } catch (InterruptedException e) {
                System.out.println("BackgroundProcessor Interrupted: " + e.getMessage());
            }
        }
    }

    private void processOrders() {
        ArrayList<Order> orders = Facade.getPendingOrders();

        for (Order order : orders) {
            try {
                double curPrice = Facade.getStock(order.getStockSymbol()).getCurrentPrice();
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
                System.out.println("Error processing orders: " + e.getMessage());
            }
        }
    }

    private static void processSellOrder(Order order){
        try {
            OwnedPosition toSell = Facade.getOwnedPosition(order.getToSellOwnedPositionId());
            if (toSell != null){
                if (toSell.getQuantity() == order.getQuantity()) {
                    Facade.deleteOwnedPosition(toSell);
                    Facade.insertSoldPosition(new SoldPosition(Facade.getNextSoldPositionId(), toSell.getAccountId(), toSell.getStockSymbol(),
                            toSell.getOrderId(), toSell.getQuantity(), toSell.getInitialValue(), toSell.getMarketValue(), toSell.getProfitLoss()));
                    Account account = Facade.getAccount(order.getAccountId());
                    account.decrementPositionsHeld();
                    account.setMarketValue(account.getMarketValue() - toSell.getMarketValue());
                    account.setNetValue(account.getNetValue() - toSell.getMarketValue() + toSell.getProfitLoss());
                    account.setSoldProfitLoss(toSell.getProfitLoss());
                    order.setOrderStatus(Enums.OrderStatus.COMPLETED);
                    Facade.updateOrder(order);
                    Facade.updateAccount(account);
                }else if (toSell.getQuantity() > order.getQuantity()){
                    Account account = Facade.getAccount(order.getAccountId());
                    account.setMarketValue(account.getMarketValue() - (toSell.getMarketValue()/toSell.getQuantity())*order.getQuantity());
                    account.setNetValue(account.getNetValue() - ((toSell.getMarketValue()/toSell.getQuantity())*order.getQuantity()) + (toSell.getProfitLoss()/toSell.getQuantity())*order.getQuantity());
                    account.setSoldProfitLoss((toSell.getMarketValue()/toSell.getQuantity())*order.getQuantity());
                    Facade.insertSoldPosition(new SoldPosition(Facade.getNextSoldPositionId(), toSell.getAccountId(), toSell.getStockSymbol(),
                            toSell.getOrderId(), toSell.getQuantity(), toSell.getInitialValue()/order.getQuantity(), toSell.getMarketValue()/order.getQuantity(), toSell.getProfitLoss()/order.getQuantity()));
                    order.setOrderStatus(Enums.OrderStatus.COMPLETED);
                    Facade.updateOrder(order);
                    Facade.updateAccount(account);
                }else{
                    throw new Error("Can't sell more shares than you own. ");
                }
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
            if (Facade.getOwnedPosition(order) != null) {
                order.setOrderStatus(Enums.OrderStatus.COMPLETED);
                Facade.updateOrder(order);
                Account account = Facade.getAccount(order.getAccountId());
                account.incrementPositionsHeld();
                account.setMarketValue(account.getMarketValue() + ownedPosition.getInitialValue());
                account.setNetValue(account.getNetValue() + ownedPosition.getInitialValue());
                Facade.updateAccount(account);
            }
        } catch (Exception e) {
            System.out.println("Error processing buy order" + e.getMessage());
        }
    }

    private static void processMarketBuy(Order order, double curPrice){
        Order o = new Order(order);
        o.setPrice(curPrice); // Since market orders don't care on price
        processBuyOrder(o, curPrice);
    }

    private static void processMarketSell(Order order, double curPrice){
        Order o = new Order(order);
        o.setPrice(curPrice); // Since market orders don't care on price
        processSellOrder(o);
    }

    private static void processLimitBuy(Order order, double curPrice){
        if (curPrice <= order.getPrice()){
            processBuyOrder(order, curPrice);
        }
    }

    private static void processLimitSell(Order order, double curPrice){
        if (curPrice >= order.getPrice()){
            processSellOrder(order);
        }
    }

    private static void processStopLossBuy(Order order, double curPrice){
        if (curPrice >= order.getPrice()){
            processBuyOrder(order, curPrice);
        }
    }

    private static void processStopLossSell(Order order, double curPrice){
        if (curPrice <= order.getPrice()){
            processSellOrder(order);
        }
    }

    private void refresh() {
        ArrayList<Stock> stocks = Facade.getAllStocks();
        ArrayList<OwnedPosition> positions = Facade.getAllOwnedPositions();
        HashMap<Integer, Account> accounts = new HashMap<>();
        Random random = new Random();

        for (Stock s : stocks){
            try {
                double prevPrice = s.getCurrentPrice();
                double newPrice;
                if (random.nextInt(2) == 0) {
                    newPrice = prevPrice + (prevPrice * 0.01);
                }else {
                    newPrice = prevPrice - (prevPrice * 0.01);
                }
                s.setCurrentPrice(newPrice);
                Facade.updateStock(s);
            } catch (Exception e){
                System.out.println("Error refreshing stocks: " + e.getMessage());
            }
        }

        for (OwnedPosition position : positions) {
            try {
                double curPrice = Facade.getStock(position.getStockSymbol()).getCurrentPrice();
                // Update position
                position.setMarketValue(curPrice * position.getQuantity());
                position.setProfitLoss((position.getMarketValue() - position.getInitialValue()));
                Facade.updateOwnedPosition(position);
                // Update associated account
                accounts.putIfAbsent(position.getAccountId(), new Account(position.getAccountId(), Facade.getAccount(position.getAccountId()).getUserId(),
                        0, 0, 0, 0,0));
                Account account = accounts.get(position.getAccountId());
                account.incrementPositionsHeld();
                account.setMarketValue(account.getMarketValue() + position.getMarketValue());
                account.setProfitLoss(account.getProfitLoss() + position.getProfitLoss());
            } catch (Exception e) {
                System.out.println("Error refreshing owned positions: " + e.getMessage());
            }
        }
        for (Integer id : accounts.keySet()) {
            Account account = Facade.getAccount(id);
            accounts.get(id).setSoldProfitLoss(account.getSoldProfitLoss()); // copy over value
            accounts.get(id).setProfitLoss(accounts.get(id).getProfitLoss() + account.getSoldProfitLoss());
            accounts.get(id).setNetValue(accounts.get(id).getMarketValue() + account.getSoldProfitLoss());
            Facade.updateAccount(accounts.get(id));
        }
    }
}
