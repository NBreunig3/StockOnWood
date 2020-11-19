public class OwnedPosition {
    private int ownedPositionId, accountId, orderId, quantity;
    private double profitLoss, initialValue, marketValue;
    private String stockSymbol;

    public OwnedPosition(int ownedPositionId, int accountId, String stockSymbol, int orderId, int quantity, double initialValue, double marketValue, double profitLoss){
        this.ownedPositionId = ownedPositionId;
        this.accountId = accountId;
        this.stockSymbol = stockSymbol;
        this.orderId = orderId;
        this.quantity = quantity;
        this.initialValue = initialValue;
        this.marketValue = marketValue;
        this.profitLoss = profitLoss;
    }

    public int getOwnedPositionId(){
        return this.ownedPositionId;
    }

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
