public class OwnedPosition {
    private int accountId, orderId, quantity;
    private double profitLoss, initialValue, marketValue;
    private String stockSymbol;

    public OwnedPosition(int accountId, String stockSymbol, int orderId, int quantity, double initialValue, double marketValue, double profitLoss){
        this.accountId = accountId;
        this.stockSymbol = stockSymbol;
        this.orderId = orderId;
        this.quantity = quantity;
        this.initialValue = initialValue;
        this.marketValue = marketValue;
        this.profitLoss = profitLoss;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getInitialValue() {
        return initialValue;
    }

    public double getMarketValue() {
        return marketValue;
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
