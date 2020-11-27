import javax.sql.rowset.Predicate;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

    public StockInfo(Stock stock){
        positions = Facade.getOwnedPositions(Home.account, stock);
        stockSymbol.setText(stock.getStockSymbol());
        companyName.setText(stock.getName());
        sector.setText(stock.getSector());
        ownedShares.setText("Owned Shares: " + Home.twoDecimalPlaces.format(getTotalOwnedShares()));
        ownedValue.setText("Owned Value: " + Home.twoDecimalPlaces.format(getTotalOwnedValue()));
        price.setText("Current Price: " + String.valueOf(Home.twoDecimalPlaces.format(stock.getCurrentPrice())));
        priceloss.setText("Profit/Loss: " + String.valueOf(Home.twoDecimalPlaces.format(getTotalPL())));
        buildPositionsTable();

        if (positions.size() == 0){
            sellButton.setEnabled(false);
        }

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Home.frame.setContentPane(new Home(Home.user).mainPanel);
                Home.frame.pack();
                Home.frame.setVisible(true);
            }
        });
    }

    private void buildPositionsTable(){
        //ordersTable.setEnabled(false);
        DefaultTableModel positionsTableModel = new DefaultTableModel();
        String columns[] = {"Stock", "Quantity", "Init Value", "Market Value", "P/L"};
        for (String c : columns){
            positionsTableModel.addColumn(c);
        }
        positionsTable.setModel(positionsTableModel);
        positionsTableModel.addRow(columns);
        for (OwnedPosition p : positions){
            positionsTableModel.addRow(new Object[]{p.getStockSymbol(), p.getQuantity(), Home.twoDecimalPlaces.format(p.getInitialValue()), Home.twoDecimalPlaces.format(p.getMarketValue()), Home.twoDecimalPlaces.format(p.getProfitLoss())});
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
}
