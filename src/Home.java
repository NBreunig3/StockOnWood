import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Home{
    public static JFrame frame;
    public JPanel mainPanel;
    private JTextField searchBox;
    private JTable indexTable;
    private JTable ordersTable;
    private JPanel stocksOrdersPanel;
    private JPanel mainLabelPanel;
    private JPanel mainFunctionsPanel;
    private JTable positionsTable;
    private JPanel positionsPanel;
    private JTable stocksTable;
    private JPanel stockPanel;
    private JPanel accountValuesPanel;
    private JLabel accountNetValue;
    private JLabel accountMarketValue;
    private JLabel accountPriceLoss;
    private JLabel lblStockOnWood;

    public static DecimalFormat twoDecimalPlaces = new DecimalFormat("#.##");
    private Timer timer = new Timer(true);
    private TimerTask uiRefresh = new UIRefresher();
    private static boolean firstRun = true;

    public static User user;
    public static Account account;
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
        stocksTable.addMouseListener(new StocksTableSelect());
        refreshUI();
        if (firstRun) {
            timer.scheduleAtFixedRate(uiRefresh, 5000, 10000);
        }
        firstRun = false;
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
        this.stocks = Facade.getAllStocks();
        this.orders = Facade.getAllOrders();
        this.positions = Facade.getOwnedPositions(account);
        accountNetValue.setText("$" + twoDecimalPlaces.format(account.getNetValue()));
        accountMarketValue.setText("$" + twoDecimalPlaces.format(account.getMarketValue()));
        accountPriceLoss.setText("$" + twoDecimalPlaces.format(account.getProfitLoss()));
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
            positionsTableModel.addRow(new Object[]{p.getStockSymbol(), p.getQuantity(), twoDecimalPlaces.format(p.getInitialValue()), twoDecimalPlaces.format(p.getMarketValue()), twoDecimalPlaces.format(p.getProfitLoss())});
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
            ordersTableModel.addRow(new Object[]{o.getOrderId(), o.getStockSymbol(), o.getOrderBuyOrSell(), o.getOrderType(), o.getQuantity(), twoDecimalPlaces.format(o.getPrice())});
        }
        //ordersTableScroll.setViewportView(ordersTable);
    }

    private void refreshStocksTable(){
        DefaultTableModel stocksTableModel = new DefaultTableModel();
        String[] columns = {"Symbol", "Name", "Price"};
        for (String c : columns){
            stocksTableModel.addColumn(c);
        }
        stocksTable.setModel(stocksTableModel);
        stocksTableModel.addRow(columns);
        for (Stock s : stocks){
            stocksTableModel.addRow(new Object[]{s.getStockSymbol(), s.getName(), twoDecimalPlaces.format(s.getCurrentPrice())});
        }
    }

    private class StocksTableSelect implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (SwingUtilities.isRightMouseButton(e)) {
                System.out.println();
                StockInfo stockInfo = new StockInfo(Facade.getStock(stocksTable.getValueAt(stocksTable.rowAtPoint(e.getPoint()), 0).toString()));
                Home.frame.setContentPane(stockInfo.mainPanel);
                Home.frame.pack();
                Home.frame.setVisible(true);
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

   private class UIRefresher extends TimerTask {
       @Override
       public void run() {
           refreshUI();
           System.out.println("UIRefreshed");
       }
   }
}
