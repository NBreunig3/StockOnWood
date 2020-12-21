import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class Home{
    public static JFrame frame;
    public JPanel mainPanel;
    private JTextField searchBox;
    private JTable searchTable;
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
    private JLabel ordersLabel;
    private JLabel positionsLabel;
    private JLabel lblWelcome;
    private JButton refreshButton;
    private JButton viewAccountButton;
    private JButton exitButton;

    public static DecimalFormat twoDecimalPlaces = new DecimalFormat("0.00");
    private Timer timer;
    private TimerTask uiRefresh = new UIRefresher();
//    private boolean timerStarted = false;
    private static boolean firstRun = true;

    public static User user;
    public static Account account;
    private static ArrayList<Stock> stocks;
    private static ArrayList<Order> orders;
    private static ArrayList<? extends Position> positions;
    private static HashMap<String, Double> openPrices = new HashMap<>();
    private int ordersTableType = 1; // 1 for Pending, 2 for Completed
    private int positionsTableType = 1; // 1 for Owned, 2 for Sold

    public Home(User u){
        // On Startup
        user = u;
        lblWelcome.setText("Welcome " + u.getFirstName() + "!");
        account = Facade.getAccount(user);
        stocks = Facade.getAllStocks();
        orders = Facade.getOrders(account);
        positions = Facade.getOwnedPositions(account);
//        if (timerStarted){
//            timer.cancel();
//            timer.purge();
//        }
        // TODO figure out how to only do this once
        timer = new Timer(true);
        timer.scheduleAtFixedRate(uiRefresh, 0, 10000);
//        timerStarted = true;
        if (firstRun) {
//            Thread uiRefresher = new Thread(new UIRefresher());
//            uiRefresher.start();
            Thread backgroundProcessor = new Thread(new BackgroundProcessor());
            backgroundProcessor.start();
            for (Stock s : stocks) {
                openPrices.put(s.getStockSymbol(), s.getCurrentPrice());
            }
        }
        stocksTable.addMouseListener(new StocksTableSelect());
        positionsTable.addMouseListener(new PositionsTableSelect());
        searchTable.addMouseListener(new SearchTableSelect());
        ordersLabel.addMouseListener(new OrderSwitch());
        positionsLabel.addMouseListener(new PositionsSwitch());
        searchBox.addKeyListener(new SearchBox());
        firstRun = false;
        refreshUI();
        viewAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setContentPane(new AccountDetail(account).mainPanel);
                frame.setVisible(true);
                frame.pack();
            }
        });
    }

    public static void main(String[] args) {
        Database.getInstance().connect();
        frame = new JFrame("Stock On Wood");
        frame.setContentPane(new Login().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void uiRefresh(){

    }

    private class UIRefresher extends TimerTask {

        @Override
        public void run() {
                System.out.println("UI Refresh Started");
                refreshUI();
                System.out.println("UI Refresh Ended");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    System.out.println("UIRefresher Interrupted: " + e.getMessage());
                }

        }
    }

    // Use this method to refresh the UI when it opens from another form
    // Set correct values
    public void refreshUI(){
        account = Facade.getAccount(user);
        stocks = Facade.getAllStocks();
        accountNetValue.setText("$" + twoDecimalPlaces.format(account.getNetValue()));
        accountMarketValue.setText("$" + twoDecimalPlaces.format(account.getMarketValue()));
        accountPriceLoss.setText("$" + twoDecimalPlaces.format(account.getProfitLoss()));
        refreshOrdersTable();
        refreshStocksTable();
        refreshPositionsTable();
    }

    private void refreshPositionsTable(){
        if (positionsTableType == 1) {
            positions = Facade.getOwnedPositions(account);
        } else {
            positions = Facade.getSoldPositions(account);
        }
        //ordersTable.setEnabled(false);
        int selectedIndex = positionsTable.getSelectedRow();
        DefaultTableModel positionsTableModel = new DefaultTableModel();
        String columns[] = {"ID", "Stock", "Quantity", "Init Value", "Market Value", "P/L"};
        for (String c : columns){
            positionsTableModel.addColumn(c);
        }
        positionsTable.setModel(positionsTableModel);
        positionsTableModel.addRow(columns);
        for (Position p : positions){
            if (p instanceof OwnedPosition){
                OwnedPosition o = (OwnedPosition)p;
                positionsTableModel.addRow(new Object[]{o.getOwnedPositionId(), o.getStockSymbol(), o.getQuantity(), "$" + twoDecimalPlaces.format(o.getInitialValue()), "$" + twoDecimalPlaces.format(o.getMarketValue()), "$" + twoDecimalPlaces.format(o.getProfitLoss())});
            }else if (p instanceof SoldPosition){
                SoldPosition o = (SoldPosition)p;
                positionsTableModel.addRow(new Object[]{o.getSoldPositionId(), o.getStockSymbol(), o.getQuantity(), "$" + twoDecimalPlaces.format(o.getInitialValue()), "$" + twoDecimalPlaces.format(o.getMarketValue()), "$" + twoDecimalPlaces.format(o.getProfitLoss())});
            }
        }
        if (selectedIndex != -1 && selectedIndex < positionsTable.getRowCount()) {
            positionsTable.setRowSelectionInterval(selectedIndex, selectedIndex);
        }
        //ordersTableScroll.setViewportView(ordersTable);
    }

    private void refreshOrdersTable(){
        if (ordersTableType == 1) {
            orders = Facade.getPendingOrders();
        }else {
            orders = Facade.getCompletedOrders();
        }
        //ordersTable.setEnabled(false);
        int selectedIndex = ordersTable.getSelectedRow();
        DefaultTableModel ordersTableModel = new DefaultTableModel();
        String[] columns = {"ID", "Stock", "Buy or Sell", "Type", "Quantity", "Price"};
        for (String c : columns){
            ordersTableModel.addColumn(c);
        }
        ordersTable.setModel(ordersTableModel);
        ordersTableModel.addRow(columns);
        for (Order o : orders){
            ordersTableModel.addRow(new Object[]{o.getOrderId(), o.getStockSymbol(), o.getOrderBuyOrSell(), o.getOrderType(), o.getQuantity(), "$" + twoDecimalPlaces.format(o.getPrice())});
        }
        if (selectedIndex != -1 && selectedIndex < ordersTable.getRowCount()) {
            ordersTable.setRowSelectionInterval(selectedIndex, selectedIndex);
        }
        //ordersTableScroll.setViewportView(ordersTable);
    }

    private void refreshStocksTable(){
        int selectedIndex = stocksTable.getSelectedRow();
        DefaultTableModel stocksTableModel = new DefaultTableModel();
        String[] columns = {"Symbol", "Name", "Open", "Current", "P/L"};
        for (String c : columns){
            stocksTableModel.addColumn(c);
        }
        stocksTable.setModel(stocksTableModel);
        stocksTableModel.addRow(columns);
        for (Stock s : stocks){
            String percentChange = "";
            if (openPrices.get(s.getStockSymbol()) > s.getCurrentPrice()){
                // decrease
                percentChange = "-" + String.valueOf(twoDecimalPlaces.format((Math.abs(openPrices.get(s.getStockSymbol())-s.getCurrentPrice()))/openPrices.get(s.getStockSymbol())*100) + "%");
            }else if (openPrices.get(s.getStockSymbol()) < s.getCurrentPrice()){
                // increase
                percentChange = "+" + String.valueOf(twoDecimalPlaces.format((Math.abs(openPrices.get(s.getStockSymbol())-s.getCurrentPrice()))/openPrices.get(s.getStockSymbol())*100) + "%");
            }else {
                percentChange = "0.00%";
            }
            stocksTableModel.addRow(new Object[]{s.getStockSymbol(), s.getName(), "$" + twoDecimalPlaces.format(openPrices.get(s.getStockSymbol())), "$" + twoDecimalPlaces.format(s.getCurrentPrice()), percentChange});
        }
        if (selectedIndex != -1 && selectedIndex < stocksTable.getRowCount()) {
            stocksTable.setRowSelectionInterval(selectedIndex, selectedIndex);
        }
    }

    private class SearchBox implements KeyListener {
        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER){
                DefaultTableModel searchTableModel = new DefaultTableModel();
                if (!searchBox.getText().trim().equals("")) {
                    ArrayList<Stock> results = Facade.getStocksLike(searchBox.getText().trim() + "%");

                    String[] columns = {"Symbol", "Name", "Open", "Current", "P/L"};
                    for (String c : columns) {
                        searchTableModel.addColumn(c);
                    }
                    searchTable.setModel(searchTableModel);
                    searchTableModel.addRow(columns);
                    for (Stock s : results) {
                        String percentChange = "";
                        if (openPrices.get(s.getStockSymbol()) > s.getCurrentPrice()) {
                            // decrease
                            percentChange = "-" + String.valueOf(twoDecimalPlaces.format((Math.abs(openPrices.get(s.getStockSymbol()) - s.getCurrentPrice())) / openPrices.get(s.getStockSymbol()) * 100) + "%");
                        } else if (openPrices.get(s.getStockSymbol()) < s.getCurrentPrice()) {
                            // increase
                            percentChange = "+" + String.valueOf(twoDecimalPlaces.format((Math.abs(openPrices.get(s.getStockSymbol()) - s.getCurrentPrice())) / openPrices.get(s.getStockSymbol()) * 100) + "%");
                        } else {
                            percentChange = "0.00%";
                        }
                        searchTableModel.addRow(new Object[]{s.getStockSymbol(), s.getName(), "$" + twoDecimalPlaces.format(openPrices.get(s.getStockSymbol())), "$" + twoDecimalPlaces.format(s.getCurrentPrice()), percentChange});
                    }
                }else {
                    searchTableModel.setRowCount(0);
                    searchTableModel.setColumnCount(0);
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    }

    private class PositionsSwitch implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            positionsTableType = positionsTableType == 1 ? 2 : 1;
            positionsLabel.setText(positionsTableType == 1 ? "My Positions" : "Sold Positions");
            refreshPositionsTable();
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

    private class OrderSwitch implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            ordersTableType = ordersTableType == 1 ? 2 : 1;
            ordersLabel.setText(ordersTableType == 1 ? "Pending Orders" : "Completed Orders");
            refreshOrdersTable();
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

    private class PositionsTableSelect implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (SwingUtilities.isRightMouseButton(e)) {
                StockInfo stockInfo = new StockInfo(Facade.getStock(positionsTable.getValueAt(positionsTable.rowAtPoint(e.getPoint()), 1).toString()));
                timer.cancel();
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

    private class SearchTableSelect implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (SwingUtilities.isRightMouseButton(e)) {
                StockInfo stockInfo = new StockInfo(Facade.getStock(searchTable.getValueAt(positionsTable.rowAtPoint(e.getPoint()), 0).toString()));
                Home.frame.setContentPane(stockInfo.mainPanel);
                timer.cancel();
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

    private class StocksTableSelect implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (SwingUtilities.isRightMouseButton(e)) {
                StockInfo stockInfo = new StockInfo(Facade.getStock(stocksTable.getValueAt(stocksTable.rowAtPoint(e.getPoint()), 0).toString()));
                Home.frame.setContentPane(stockInfo.mainPanel);
                timer.cancel();
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
}
