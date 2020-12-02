public abstract class Position {
    // TODO Question: Way to make these available to children but not instance vars, only through getter/setters
    // TODO Could use the getter/setters in the child constructors?
    protected int accountId, orderId, quantity;
    protected double profitLoss, initialValue, marketValue;
    protected String stockSymbol;

    // TODO Question: Best way to handle this?
    public abstract int getOwnedPositionId();

    public abstract int getSoldPositionId();

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
