// TODO not sure if needed
public class OwnedPosition {
    private int accountId, orderId;
    private double profitLoss;
    private String stockSymbol;

    public OwnedPosition(int accountId, String stockSymbol, int orderId, double profitLoss){
        this.accountId = accountId;
        this.stockSymbol = stockSymbol;
        this.orderId = orderId;
        this.profitLoss = profitLoss;
    }

    public int getAccountId() {
        return accountId;
    }

    public int getOrderId() {
        return orderId;
    }

    public double getProfitLoss() {
        return profitLoss;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }
}
