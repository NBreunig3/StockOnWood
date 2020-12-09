public abstract class Position {
    protected int accountId, orderId, quantity;
    protected double profitLoss, initialValue, marketValue;
    protected String stockSymbol;

    public void setProfitLoss(double profitLoss) {
        this.profitLoss = profitLoss;
    }

    public void setMarketValue(double marketValue) {
        this.marketValue = marketValue;
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
