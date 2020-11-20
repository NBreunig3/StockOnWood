public class Order {
    private Integer orderId, accountId, quantity, toSellOwnedPositionId;
    private Enums.OrderStatus orderStatus;
    private Enums.OrderType orderType;
    private Enums.OrderBuyOrSell orderBuyOrSell;
    private String stockSymbol;
    private Double price;

    public Order(Integer orderId, Integer accountId, String stockSymbol, Enums.OrderBuyOrSell buyOrSell, Enums.OrderType orderType, Integer quantity, Double price, Enums.OrderStatus orderStatus, Integer toSellOwnedPositionId){
        this.orderId = orderId;
        this.accountId = accountId;
        this.stockSymbol = stockSymbol;
        this.orderBuyOrSell = buyOrSell;
        this.orderType = orderType;
        this.quantity = quantity;
        this.price = price;
        this.orderStatus = orderStatus;
        this.toSellOwnedPositionId = toSellOwnedPositionId;
    }

    public Order(Order order){
        this.orderId = order.getOrderId();
        this.accountId = order.getAccountId();
        this.quantity = order.getQuantity();
        this.toSellOwnedPositionId = order.getToSellOwnedPositionId();
        this.orderStatus = order.getOrderStatus();
        this.orderType = order.getOrderType();
        this.orderBuyOrSell = order.getOrderBuyOrSell();
        this.stockSymbol = order.getStockSymbol();
        this.price = order.getPrice();
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Integer getToSellOwnedPositionId() {
        return toSellOwnedPositionId;
    }

    public Enums.OrderBuyOrSell getOrderBuyOrSell() {
        return orderBuyOrSell;
    }

    public void setOrderStatus(Enums.OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getAccountId(){
        return accountId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public Integer getQuantity() {
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

    public Double getPrice() {
        return price;
    }
}
