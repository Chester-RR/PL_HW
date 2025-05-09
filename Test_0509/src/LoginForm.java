
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LoginForm extends JFrame {
    private JTextField emailField;
    private JPasswordField phoneField;
    private JButton loginButton;
    private JLabel messageLabel;
    private JTextField NameField;
    private Menu menu = new Menu();
    
    public LoginForm() {
        setTitle("登入介面");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(350, 200);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 2, 10, 10));
        emailField = new JTextField();
        phoneField = new JPasswordField();
        loginButton = new JButton("登入");
        messageLabel = new JLabel("", SwingConstants.CENTER);
        
        
        add(new JLabel("Email："));
        add(emailField);
        add(new JLabel("手機號碼："));
        add(phoneField);
        add(new JLabel()); // 空白行
        add(loginButton);
        add(new JLabel());
        add(messageLabel);

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String phone = new String(phoneField.getPassword());

                if (email.isEmpty() || phone.isEmpty()) {
                    messageLabel.setText("請填寫所有欄位");
                    return;
                }

                String server = "jdbc:mysql://140.119.19.73:3315/";
                String database = "MG07";
                String url = server + database + "?useSSL=false&serverTimezone=UTC";
                String dbUsername = "MG07";
                String dbPassword = "D4NrtF";

                try (Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword)) {
                    String sql = "SELECT * FROM New_taxi_users WHERE Email = ? AND PhoneNumber = ?";
                    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                        pstmt.setString(1, email);
                        pstmt.setString(2, phone);
                        ResultSet rs = pstmt.executeQuery();
                        if (rs.next()) {
                        	String name = rs.getString("Name"); // ← 取出 Name 欄位
                        	JOptionPane.showMessageDialog(null,"登入成功","Info",JOptionPane.INFORMATION_MESSAGE);
                            dispose();
                            menu.Back(name);
                        } else {
                            messageLabel.setText("帳號或手機號碼錯誤");
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
        SwingUtilities.invokeLater(() -> new LoginForm());
    }
}