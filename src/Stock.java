public class Stock {
    private String stockSymbol, name, description, sector;

    public Stock(String stockSymbol, String name, String description, String sector){
        this.stockSymbol = stockSymbol;
        this.name = name;
        this.description = description;
        this.sector = sector;
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
}
