import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class Home {
    public static JFrame frame;
    public JPanel mainPanel;
    private JTextField searchBox;
    private JTable indexTable;
    private JTable ordersTable;
    private JScrollPane ordersTableScroll = new JScrollPane();
    private JPanel stocksOrdersPanel;
    private JPanel mainLabelPanel;
    private JPanel mainFunctionsPanel;
    private JTable positionsTable;
    private JPanel positionsPanel;
    private JTable stocksTable;
    private JScrollPane stocksTableScroll;
    private JPanel stockPanel;
    private JPanel accountValuesPanel;
    private JLabel accountNetValue;
    private JLabel accountMarketValue;
    private JLabel accountPriceLoss;

    private User user;
    private Account account;
    private ArrayList<Stock> stocks;
    private ArrayList<Order> orders;
    private ArrayList<OwnedPosition> positions;

    public Home(User user){
        // On Startup
        this.user = user;
        this.account = Facade.getAccount(user);
        this.stocks = Facade.getAllStocks();
        this.orders = Facade.getOrders(account);
        this.positions = Facade.getOwnedPositions(account);
        refreshUI();
    }

    public static void main(String[] args) {
        Database.getInstance().connect();
        frame = new JFrame("Stock On Wood");
        frame.setContentPane(new Login().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    // Use this method to refresh the UI when it opens from another form
    // Set correct values
    public void refreshUI(){
        accountNetValue.setText("$" + account.getNetValue());
        accountMarketValue.setText("$" + account.getMarketValue());
        accountPriceLoss.setText("$" + account.getProfitLoss());
        refreshOrdersTable();
        refreshStocksTable();
        refreshPositionsTable();
    }

    private void refreshPositionsTable(){
        //ordersTable.setEnabled(false);
        DefaultTableModel positionsTableModel = new DefaultTableModel();
        String columns[] = {"Stock", "Quantity", "Init Value", "Market Value", "P/L"};
        for (String c : columns){
            positionsTableModel.addColumn(c);
        }
        positionsTable.setModel(positionsTableModel);
        positionsTableModel.addRow(columns);
        for (OwnedPosition p : positions){
            positionsTableModel.addRow(new Object[]{p.getStockSymbol(), p.getQuantity(), p.getInitialValue(), p.getMarketValue(), p.getProfitLoss()});
        }
        //ordersTableScroll.setViewportView(ordersTable);
    }

    private void refreshOrdersTable(){
        //ordersTable.setEnabled(false);
        DefaultTableModel ordersTableModel = new DefaultTableModel();
        String[] columns = {"Order Id", "Stock", "Buy or Sell", "Type", "Quantity", "Price"};
        for (String c : columns){
            ordersTableModel.addColumn(c);
        }
        ordersTable.setModel(ordersTableModel);
        ordersTableModel.addRow(columns);
        for (Order o : orders){
            ordersTableModel.addRow(new Object[]{o.getOrderId(), o.getStockSymbol(), o.getOrderBuyOrSell(), o.getOrderType(), o.getQuantity(), o.getPrice()});
        }
        //ordersTableScroll.setViewportView(ordersTable);
    }

    private void refreshStocksTable(){
        //stocksTable.setEnabled(false);
        DefaultTableModel stocksTableModel = new DefaultTableModel();
        String[] columns = {"Symbol", "Name", "Price"};
        for (String c : columns){
            stocksTableModel.addColumn(c);
        }
        stocksTable.setModel(stocksTableModel);
        stocksTableModel.addRow(columns);
        for (Stock s : stocks){
            stocksTableModel.addRow(new Object[]{s.getStockSymbol(), s.getName(), s.getCurrentPrice()});
        }
        //TODO figure out
//        stocksTableScroll = new JScrollPane(stocksTable);
//        stocksTableScroll.setVisible(true);
//        stockPanel.add(stocksTableScroll);
    }
}
