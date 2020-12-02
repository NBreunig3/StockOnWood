import java.util.Date;

public class Account {
    private int accountId, userId, positionsHeld;
    private double profitLoss, soldProfitLoss, marketValue, netValue;
    private Date createdDate;

    public Account(int accountId, int userId, int positionsHeld, double profitLoss, double soldProfitLoss, double marketValue, double netValue, Date createdDate){
        this.accountId = accountId;
        this.userId = userId;
        this.positionsHeld = positionsHeld;
        this.profitLoss = profitLoss;
        this.soldProfitLoss = soldProfitLoss;
        this.marketValue = marketValue;
        this.netValue = netValue;
        this.createdDate = createdDate;
    }

    public double getSoldProfitLoss() {
        return soldProfitLoss;
    }

    public void setSoldProfitLoss(double soldProfitLoss) {
        this.soldProfitLoss = soldProfitLoss;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public double getMarketValue() {
        return marketValue;
    }

    public double getNetValue() {
        return netValue;
    }

    public void decrementPositionsHeld(){
        positionsHeld -= 1;
    }

    public void incrementPositionsHeld(){
        positionsHeld += 1;
    }

    public void setMarketValue(double marketValue) {
        this.marketValue = marketValue;
    }

    public void setNetValue(double netValue) {
        this.netValue = netValue;
    }

    public void setProfitLoss(double profitLoss) {
        this.profitLoss = profitLoss;
    }

    public int getAccountId() {
        return accountId;
    }

    public int getUserId() {
        return userId;
    }

    public int getPositionsHeld() {
        return positionsHeld;
    }

    public double getProfitLoss() {
        return profitLoss;
    }

    public Date getCreatedDate() {
        return createdDate;
    }
}
