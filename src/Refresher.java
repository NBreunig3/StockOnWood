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

    public static void refresh() {
        ArrayList<OwnedPosition> positions = Facade.getAllOwnedPositions();
        HashMap<Integer, Account> accounts = new HashMap<>();

        for (OwnedPosition position : positions) {
            try {
                //Stock stock = YahooFinance.get(position.getStockSymbol());
                //double curPrice = stock.getQuote().getPrice().doubleValue();
                double curPrice = 600;
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
                // TODO debug to find out why account PL isn't correctly updating among other things
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        for (Integer id : accounts.keySet()) {
            Account account = Facade.getAccount(id);
            accounts.get(id).setNetValue(accounts.get(id).getNetValue() + account.getNetValue());
            accounts.get(id).setProfitLoss(accounts.get(id).getProfitLoss() + account.getProfitLoss());
            Facade.updateAccount(accounts.get(id));
        }
    }
}
