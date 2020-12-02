public class Stock {
    private String stockSymbol, name, description, sector;
    private double currentPrice;

    public Stock(String stockSymbol, String name, String description, String sector, double currentPrice){
        this.stockSymbol = stockSymbol;
        this.name = name;
        this.description = description;
        this.sector = sector;
        this.currentPrice = currentPrice;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getSector() {
        return sector;
    }

    @Override
    public boolean equals(Object o){
        if (!(o instanceof Stock)){
            return false;
        }
        o = (Stock)o;
        return this.stockSymbol.equals(((Stock) o).stockSymbol);
    }
}
