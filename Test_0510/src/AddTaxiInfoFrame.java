import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;







class AddTaxiInfoFrame extends JFrame {
    private JTextField initiatorField;
    private JTextField timeField;
    private JTextField moneyField;
    private JTextArea textArea;
    private JButton submitButton;
    private TaxiInfoPanel parent;

    // 資料庫連線參數（使用 MG07 資料庫）
    private static final String URL = "jdbc:mysql://140.119.19.73:3315/MG07?useSSL=false&serverTimezone=UTC";
    private static final String DB_USER = "MG07";
    private static final String DB_PASS = "D4NrtF";

    public AddTaxiInfoFrame(TaxiInfoPanel parent) {
        this.parent = parent;
        setTitle("新增計程車行程");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 2, 10, 10));

        initiatorField = new JTextField();
        // 帶 placeholder 的時間欄位
        timeField = new JTextField("2025-01-01 12:00:00");
        timeField.setForeground(Color.BLUE);
        timeField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (timeField.getText().equals("2025-01-01 12:00:00")) {
                    timeField.setText("");
                    timeField.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (timeField.getText().isEmpty()) {
                    timeField.setText("HH:mm");
                    timeField.setForeground(Color.GRAY);
                }
            }
        });
        moneyField = new JTextField();
        textArea = new JTextArea();
        submitButton = new JButton("提交");

        add(new JLabel("發起人 (暱稱)："));       add(initiatorField);
        add(new JLabel("預計出發時間："));       add(timeField);
        add(new JLabel("預估總價格："));         add(moneyField);
        add(new JLabel("備註："));              add(new JScrollPane(textArea));
        add(new JLabel());                       add(submitButton);

        submitButton.addActionListener(e -> submit());
    }

    private void submit() {
        String name = initiatorField.getText().trim();
        String time = timeField.getText().trim();
        String money = moneyField.getText().trim();
        String text = textArea.getText().trim();

        if (name.isEmpty() || time.isEmpty() || money.isEmpty() || time.equals("HH:mm")) {
            JOptionPane.showMessageDialog(this, "請填寫所有必填欄位", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String sql = "INSERT INTO taxi_info (NickName, NumberOfPeople, Money, departureTime, Text) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, DB_USER, DB_PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // 設定參數：1=NickName, 2=NumberOfPeople, 3=Money, 4=departureTime, 5=Text
            ps.setString(1, name);
            ps.setInt(2, 1); // 預設人數為1
            ps.setInt(3, Integer.parseInt(money));
            ps.setString(4, time);
            ps.setString(5, text);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "新增成功", "Info", JOptionPane.INFORMATION_MESSAGE);
            parent.loadData();
            dispose();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "新增失敗: " + ex.getMessage(), "錯誤", JOptionPane.ERROR_MESSAGE);
        }
    }
}

