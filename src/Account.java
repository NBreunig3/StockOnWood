import java.util.Date;

public class Account {
    private int accountId, userId, positionsHeld;
    private double profitLoss, value;
    private Date createdDate;

    public Account(int accountId, int userId, int positionsHeld, double profitLoss, double value, Date createdDate){
        this.accountId = accountId;
        this.userId = userId;
        this.positionsHeld = positionsHeld;
        this.profitLoss = profitLoss;
        this.value = value;
        this.createdDate = createdDate;
    }

    public void incrementPositionsHeld(){
        positionsHeld += 1;
    }

    public void setProfitLoss(double profitLoss) {
        this.profitLoss = profitLoss;
    }

    public void setValue(double value) {
        this.value = value;
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

    public double getValue() {
        return value;
    }

    public Date getCreatedDate() {
        return createdDate;
    }
}
