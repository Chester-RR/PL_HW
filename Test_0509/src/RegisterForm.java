import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class RegisterForm extends JFrame {
    private JTextField usernameField;
    private JTextField emailField;
    private JPasswordField phoneField;
    private JPasswordField passwordField;  // 新增密碼欄位
    private JButton registerButton;
    private JLabel messageLabel;

    public boolean isUserExists(String email, String phone) {
        String server = "jdbc:mysql://140.119.19.73:3315/";
        String database = "MG07";
        String url = server + database + "?useSSL=false&serverTimezone=UTC";
        String dbUsername = "MG07";
        String dbPassword = "D4NrtF";

        try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword)) {
            String sql = "SELECT * FROM user_info WHERE Email = ? OR PhoneNumber = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, email);
                pstmt.setString(2, phone);
                ResultSet rs = pstmt.executeQuery();
                return rs.next(); // 有資料代表 email 或 phone 已存在
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return true; 
        }
    }

    public RegisterForm() {
        setTitle("註冊介面");
        setSize(350, 300);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(6, 2, 10, 10));  // 調整為6行2列
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        usernameField = new JTextField();
        emailField = new JTextField();
        phoneField = new JPasswordField();
        passwordField = new JPasswordField();  // 初始化密碼欄位
        registerButton = new JButton("註冊");
        messageLabel = new JLabel("", SwingConstants.CENTER);

        // 加入元件
        add(new JLabel("使用者名稱："));
        add(usernameField);
        add(new JLabel("使用者Email："));
        add(emailField);
        add(new JLabel("手機號碼："));
        add(phoneField);
        add(new JLabel("使用者密碼："));  // 密碼標籤
        add(passwordField);             // 密碼欄位
        add(new JLabel());  // 空白佔位
        add(registerButton);
        add(new JLabel());  // 空白佔位
        add(messageLabel);

        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = usernameField.getText().trim();
                String email = emailField.getText().trim();
                String phone = new String(phoneField.getPassword()).trim();
                String password = new String(passwordField.getPassword()).trim();  // 取得密碼

                if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
                    messageLabel.setText("請填寫所有欄位");
                    return;
                }

                String server = "jdbc:mysql://140.119.19.73:3315/";
                String database = "MG07";
                String url = server + database + "?useSSL=false&serverTimezone=UTC";
                String dbUsername = "MG07";
                String dbPassword = "D4NrtF";

                if (isUserExists(email, phone)) {
                    messageLabel.setText("此 Email 或手機已註冊過！");
                    return;
                }

                try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword)) {
                    String insertSql = "INSERT INTO user_info (Name, Email, PhoneNumber, Password) VALUES (?, ?, ?, ?)";
                    try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
                        pstmt.setString(1, name);
                        pstmt.setString(2, email);
                        pstmt.setString(3, phone);
                        pstmt.setString(4, password);  // 設定密碼
                        int rows = pstmt.executeUpdate();
                        if (rows > 0) {
                            JOptionPane.showMessageDialog(null, "註冊成功", "Info", JOptionPane.INFORMATION_MESSAGE);
                            dispose();
                        } else {
                            messageLabel.setText("註冊失敗！");
                        }
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    messageLabel.setText("資料庫錯誤: " + ex.getMessage());
                    JOptionPane.showMessageDialog(null, "格式錯誤，請重新輸入", "Warnings", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RegisterForm().setVisible(true));
    }
}
