public class SoldPosition extends Position{
    private int soldPositionId;

    public SoldPosition(int soldPositionId, int accountId, String stockSymbol, int orderId, int quantity, double initialValue, double marketValue, double profitLoss){
        this.soldPositionId = soldPositionId;
        this.accountId = accountId;
        this.stockSymbol = stockSymbol;
        this.orderId = orderId;
        this.quantity = quantity;
        this.initialValue = initialValue;
        this.marketValue = marketValue;
        this.profitLoss = profitLoss;
    }

    @Override
    public int getSoldPositionId() {
        return soldPositionId;
    }

    @Override
    public int getOwnedPositionId() {
        return -1;
    }
}
