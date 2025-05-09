<<<<<<< HEAD
=======
>>>>>>> 6151e6f5479fd1c1941c1ad5257c98590a4fa206
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LoginForm extends JFrame {
    private JTextField emailField;
    private JPasswordField phoneField;  // 保留原手機欄
    private JPasswordField passwordField; // 新增密碼欄
    private JButton loginButton;
    private JLabel messageLabel;
    private JTextField nameField;
    private Menu menu = new Menu();

    public LoginForm() {
        setTitle("登入介面");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(350, 220);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(6, 2, 10, 10)); // 調整為6行2列

        emailField = new JTextField();
        phoneField = new JPasswordField();
        passwordField = new JPasswordField();  // 初始化密碼欄
        loginButton = new JButton("登入");
        messageLabel = new JLabel("", SwingConstants.CENTER);

        add(new JLabel("Email："));
        add(emailField);
        add(new JLabel("手機號碼："));
        add(phoneField);
        add(new JLabel("使用者密碼：")); // 密碼標籤
        add(passwordField);               // 密碼欄位
        add(new JLabel()); // 空白行
        add(loginButton);
        add(new JLabel());
        add(messageLabel);

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText().trim();
                String phone = new String(phoneField.getPassword()).trim();
                String password = new String(passwordField.getPassword()).trim(); // 取得密碼

                if (email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
                    messageLabel.setText("請填寫所有欄位");
                    return;
                }

                String server = "jdbc:mysql://140.119.19.73:3315/";
                String database = "MG07";
                String url = server + database + "?useSSL=false&serverTimezone=UTC";
                String dbUsername = "MG07";
                String dbPassword = "D4NrtF";

                try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword)) {
                    String sql = "SELECT * FROM New_taxi_users WHERE Email = ? AND PhoneNumber = ? AND Password = ?";
                    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                        pstmt.setString(1, email);
                        pstmt.setString(2, phone);
                        pstmt.setString(3, password); // 設定密碼參數
                        ResultSet rs = pstmt.executeQuery();
                        if (rs.next()) {
                            String name = rs.getString("Name");
                            JOptionPane.showMessageDialog(null, "登入成功", "Info", JOptionPane.INFORMATION_MESSAGE);
                            dispose();
                            menu.Back(name);
                        } else {
                            messageLabel.setText("帳號、手機或密碼錯誤");
                        }
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    messageLabel.setText("資料庫錯誤: " + ex.getMessage());
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginForm().setVisible(true));
    }
}
