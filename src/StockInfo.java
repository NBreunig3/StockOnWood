import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class StockInfo {
    private JButton buyButton;
    private JButton sellButton;
    private JTable positionsTable;
    private JLabel stockSymbol;
    private JLabel companyName;
    private JLabel sector;
    private JLabel price;
    private JLabel ownedShares;
    public JPanel mainPanel;
    private JLabel priceloss;
    private JLabel ownedValue;
    private JButton backButton;

    private ArrayList<OwnedPosition> positions;
    private Stock stock;
    private int toSellId = -1;

    public StockInfo(Stock stock){
        this.stock = stock;
        positions = Facade.getOwnedPositions(Home.account, stock);
        stockSymbol.setText(stock.getStockSymbol());
        companyName.setText(stock.getName());
        sector.setText(stock.getSector());
        ownedShares.setText("Owned Shares: " + Home.twoDecimalPlaces.format(getTotalOwnedShares()));
        ownedValue.setText("Owned Value: " + Home.twoDecimalPlaces.format(getTotalOwnedValue()));
        price.setText("Current Price: " + String.valueOf(Home.twoDecimalPlaces.format(stock.getCurrentPrice())));
        priceloss.setText("Profit/Loss: " + String.valueOf(Home.twoDecimalPlaces.format(getTotalPL())));
        buildPositionsTable();
        sellButton.setEnabled(false);
        positionsTable.addMouseListener(new OwnedPositionsSelect());

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Home.frame.setContentPane(new Home(Home.user).mainPanel);
                Home.frame.pack();
                Home.frame.setVisible(true);
            }
        });
        buyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Home.frame.setContentPane(new OrderForm(stock, Enums.OrderBuyOrSell.BUY, null).mainPanel);
                Home.frame.pack();
                Home.frame.setVisible(true);
            }
        });
        sellButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OwnedPosition ownedPosition = Facade.getOwnedPosition(toSellId);
                Home.frame.setContentPane(new OrderForm(stock, Enums.OrderBuyOrSell.SELL, ownedPosition).mainPanel);
                Home.frame.pack();
                Home.frame.setVisible(true);
            }
        });
    }

    private void buildPositionsTable(){
        //ordersTable.setEnabled(false);
        DefaultTableModel positionsTableModel = new DefaultTableModel();
        String columns[] = {"ID", "Stock", "Quantity", "Init Value", "Market Value", "P/L"};
        for (String c : columns){
            positionsTableModel.addColumn(c);
        }
        positionsTable.setModel(positionsTableModel);
        positionsTableModel.addRow(columns);
        for (OwnedPosition p : positions){
            positionsTableModel.addRow(new Object[]{p.getOwnedPositionId(), p.getStockSymbol(), p.getQuantity(), Home.twoDecimalPlaces.format(p.getInitialValue()), Home.twoDecimalPlaces.format(p.getMarketValue()), Home.twoDecimalPlaces.format(p.getProfitLoss())});
        }
        //ordersTableScroll.setViewportView(ordersTable);
    }

    private int getTotalOwnedShares(){
        int shares = 0;
        for (OwnedPosition p : positions){
            shares += p.getQuantity();
        }
        return shares;
    }

    private double getTotalOwnedValue(){
        double value = 0.0;
        for (OwnedPosition p : positions){
            value += p.getMarketValue();
        }
        return value;
    }

    private double getTotalPL(){
        double value = 0.0;
        for (OwnedPosition p : positions){
            value += p.getProfitLoss();
        }
        return value;
    }

    private class OwnedPositionsSelect implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (positionsTable.rowAtPoint(e.getPoint()) != 0){
                sellButton.setEnabled(true);
                toSellId = Integer.parseInt(positionsTable.getValueAt(positionsTable.rowAtPoint(e.getPoint()), 0).toString());
            }else{
                sellButton.setEnabled(false);
                toSellId = -1;
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }
}
