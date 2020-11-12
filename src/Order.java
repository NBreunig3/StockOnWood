public class Order {
    private int orderId, quantity;
    private Enums.OrderStatus orderStatus;
    private Enums.OrderType orderType;
    private String stockSymbol;
    private double price;

    public Order(int orderId, String stockSymbol, Enums.OrderType orderType, int quantity, double price, Enums.OrderStatus orderStatus){
        this.orderId = orderId;
        this.stockSymbol = stockSymbol;
        this.orderType = orderType;
        this.quantity = quantity;
        this.price = price;
        this.orderStatus = orderStatus;
    }

    public void setOrderStatus(Enums.OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getQuantity() {
        return quantity;
    }

    public Enums.OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public Enums.OrderType getOrderType() {
        return orderType;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public double getPrice() {
        return price;
    }
}
