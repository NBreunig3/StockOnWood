import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;

public class Refresher {
    public static long RefreshRate = 10000;

    private Refresher() {
    }

    public static void refresh() throws InterruptedException {
        ArrayList<OwnedPosition> positions = Facade.getAllOwnedPositions();
        HashMap<Integer, Account> accounts = new HashMap<>();

        for (OwnedPosition position : positions) {
            try {
                //Stock stock = YahooFinance.get(position.getStockSymbol());
                //double curPrice = stock.getQuote().getPrice().doubleValue();
                double curPrice = 83.43;
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
                account.setNetValue(account.getNetValue() + position.getProfitLoss() + position.getMarketValue());
                account.setProfitLoss(account.getProfitLoss() + position.getProfitLoss());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        for (Integer id : accounts.keySet()) {
            Facade.updateAccount(accounts.get(id));
        }
    }
}
