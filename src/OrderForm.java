import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OrderForm {
    private JTextField stockText;
    private JTextField quantity;
    private JTextField price;
    private JComboBox orderType;
    private JButton createOrderButton;
    public JPanel mainPanel;
    private JComboBox buyorsellCombo;

    private Stock stock;
    private Enums.OrderBuyOrSell buyOrSell;
    private OwnedPosition ownedPosition;

    public OrderForm(Stock stock, Enums.OrderBuyOrSell buyOrSell, OwnedPosition ownedPosition){
        this.stock = stock;
        this.buyOrSell = buyOrSell;
        this.ownedPosition = ownedPosition;
        quantity.setText("1");
        stockText.setText(stock.getStockSymbol());
        price.setText(String.valueOf(Home.twoDecimalPlaces.format(stock.getCurrentPrice())));
        orderType.addItem(Enums.OrderType.LIMIT);
        orderType.addItem(Enums.OrderType.MARKET);
        orderType.addItem(Enums.OrderType.STOPLOSS);
        if (buyOrSell == Enums.OrderBuyOrSell.SELL){
            buyorsellCombo.addItem(Enums.OrderBuyOrSell.SELL);
        }else {
            buyorsellCombo.addItem(Enums.OrderBuyOrSell.BUY);
        }
        createOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Double p = null;
                if (orderType.getSelectedItem() == Enums.OrderType.MARKET){
                    price.setText("NULL");
                }else{
                    p = Double.valueOf(price.getText().trim());
                }
                Order order = new Order(Facade.getNextOrderId(), Home.account.getAccountId(), stockText.getText().trim(),
                        buyOrSell, Enums.OrderType.valueOf(orderType.getSelectedItem().toString()), Integer.parseInt(quantity.getText().trim()),
                        p, Enums.OrderStatus.PENDING, buyOrSell == Enums.OrderBuyOrSell.SELL ? ownedPosition.getOwnedPositionId() : null);
                Facade.addOrder(order);
                Home.frame.setContentPane(new Home(Home.user).mainPanel);
                Home.frame.pack();
                Home.frame.setVisible(true);
            }
        });
    }

}
