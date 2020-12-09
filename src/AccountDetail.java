import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class AccountDetail {
    private JTable profitPositionsTable;
    private JTable topProfitPositionsTable;
    private JLabel accountLabel;
    private JLabel netValueLabel;
    private JLabel marketValueLabel;
    private JLabel profitLossLabel;
    public JPanel mainPanel;
    private JButton backButton;
    private JLabel soldProfitLossLabel;

    private Account account;
    private double netValue, marketValue, profitLoss, soldProfitLoss;

    public AccountDetail(Account account){
        this.account = account;
        netValue = this.account.getNetValue();
        marketValue = this.account.getMarketValue();
        profitLoss = this.account.getProfitLoss();
        soldProfitLoss = this.account.getSoldProfitLoss();
        netValueLabel.setText("Account Net Value: $" + Home.twoDecimalPlaces.format(netValue));
        marketValueLabel.setText("Account Market Value: $" + Home.twoDecimalPlaces.format(marketValue));
        profitLossLabel.setText("Account Profit/Loss Value: $" + Home.twoDecimalPlaces.format(profitLoss));
        soldProfitLossLabel.setText("Account Sold Profit/Loss Value: $" + Home.twoDecimalPlaces.format(soldProfitLoss));
        buildTables();
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Home.frame.setContentPane(new Home(Facade.getUser(account.getUserId())).mainPanel);
                Home.frame.setVisible(true);
                Home.frame.pack();
            }
        });
    }

    public void buildTables(){
        // Top profit table
        DefaultTableModel topProfitPositionsTableModel = new DefaultTableModel();
        String columns[] = {"Stock", "Profit/Loss"};
        for (String c : columns){
            topProfitPositionsTableModel.addColumn(c);
        }
        topProfitPositionsTable.setModel(topProfitPositionsTableModel);
        topProfitPositionsTableModel.addRow(columns);
        ArrayList<OwnedPosition> positions = Facade.getTopProfitableOwnedPositions(account);
        for (OwnedPosition p : positions){
            topProfitPositionsTableModel.addRow(new Object[]{p.getStockSymbol(), "$" + Home.twoDecimalPlaces.format(p.getProfitLoss())});
        }
        // profit table
        DefaultTableModel positionsTableModel = new DefaultTableModel();
        String cols[] = {"Stock", "Profit/Loss"};
        for (String c : cols){
            positionsTableModel.addColumn(c);
        }
        profitPositionsTable.setModel(positionsTableModel);
        positionsTableModel.addRow(columns);
        positions = Facade.getCombinedOwnedStocks(account);
        for (OwnedPosition o : positions){
            positionsTableModel.addRow(new Object[]{o.getStockSymbol(), "$" + Home.twoDecimalPlaces.format(o.getProfitLoss())});
        }
    }
}
