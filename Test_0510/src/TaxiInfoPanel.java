import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

/**
 * 面板：顯示計程車行程簡要資訊列表
 */
public class TaxiInfoPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    // 資料庫連線參數
    private static final String URL = "jdbc:mysql://140.119.19.73:3315/MG07?useSSL=false&serverTimezone=UTC";
    private static final String DB_USER = "MG07";
    private static final String DB_PASS = "D4NrtF";

    public TaxiInfoPanel() {
        setLayout(new BorderLayout(5,5));

        // 上方按鈕：新增行程
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addButton = new JButton("新增行程");
        topPanel.add(addButton);
        add(topPanel, BorderLayout.NORTH);

        // 表格：發起人、預計出發時間、預估價格、目前人數
        String[] columns = {"發起人", "預計出發時間", "預估價格", "目前人數"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // 點選一列，開啟詳細資訊視窗
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) {
                    String initiator = model.getValueAt(table.getSelectedRow(), 0).toString();
                    TaxiDetailFrame detailFrame = new TaxiDetailFrame(initiator);
                    detailFrame.setVisible(true);
                }
            }
        });

        // 新增行程按鈕
        addButton.addActionListener(e -> new AddTaxiInfoFrame(this).setVisible(true));

        // 初次載入資料
        loadData();
    }

    /**
     * 從資料庫撈取行程並更新表格
     */
    public void loadData() {
        model.setRowCount(0);
        String sql = "SELECT NickName, departureTime, Money, NumberOfPeople FROM taxi_info";
        try (Connection conn = DriverManager.getConnection(URL, DB_USER, DB_PASS);
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Object[] row = {
                    rs.getString("NickName"),
                    rs.getString("departureTime"),
                    rs.getInt("Money"),
                    rs.getInt("NumberOfPeople")
                };
                model.addRow(row);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "載入行程失敗: " + ex.getMessage(), "錯誤", JOptionPane.ERROR_MESSAGE);
        }
    }
}
