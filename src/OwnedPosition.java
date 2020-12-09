public class OwnedPosition extends Position{
    private int ownedPositionId;

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
}
