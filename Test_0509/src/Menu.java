import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Menu {
    private static JButton[] buttons = new JButton[3];
    private static JTable table;
    private static RegisterForm registerForm = new RegisterForm();
    private static LoginForm loginForm = new LoginForm();
    private static DefaultTableModel[] models = new DefaultTableModel[3];
    private static JPanel upperPanel = new JPanel(new GridLayout(3, 1));
    private static JPanel underPanel = new JPanel(new GridLayout(2, 1));
    private static JPanel perInfo = new JPanel(new BorderLayout());
    private static JLabel labInfo = new JLabel("現在時間 (Current Time) : ");
    private static JLabel label = new JLabel("尚無最新消息");
    private static JLabel timeLabel = new JLabel();
    private static JPanel buttonPanel = new JPanel();
    private static String[] group = {"台北車站", "捷運動物園站", "捷運市政府站"};
    private static JButton buttonLogin = new JButton("登入");
    private static JButton buttonRegister = new JButton("註冊");

    private static void LatestNews() {
        String text = "";
        for (int i = 0; i < models.length; i++) {
            DefaultTableModel model = models[i];
            if (model.getRowCount() > 0) {
                int time = (int) model.getValueAt(0, 1);
                if (time >= 10) {
                    text += group[i] + " ";
                }
            }
        }
        if (text.isEmpty()) {
            text = "2025雙北世界壯年運動會 2025.5.17-30   2025 Taipei & New Taipei City World Masters Games May 17–30";
            label.setForeground(Color.BLACK);
        } else {
            text = "--------------------------------將抵達公車的方向為 : " + text;
            label.setForeground(Color.RED);
        }
        label.setFont(new Font("微軟正黑體", Font.BOLD, 20));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        String finalText = text;
        Timer timer = new Timer(500, e -> {
            int index = (int) (System.currentTimeMillis() / 500 % finalText.length());
            String movingText = finalText.substring(index) + finalText.substring(0, index);
            label.setText(movingText);
        });
        timer.start();
    }

    private static void setupPerInfo() {
        buttonPanel.add(buttonRegister);
        buttonPanel.add(buttonLogin);
        perInfo.add(buttonPanel, BorderLayout.EAST);

        buttonLogin.addActionListener(e -> loginForm.setVisible(true));
        buttonRegister.addActionListener(e -> registerForm.setVisible(true));
    }

    public static void Back(String name) {
        JPanel infoPanel = new JPanel();
        JLabel welcomeLabel = new JLabel("哈囉，" + name + "  歡迎使用約車服務");
        welcomeLabel.setFont(new Font("微軟正黑體", Font.ITALIC, 15));
        infoPanel.add(welcomeLabel);
        buttonPanel.setVisible(false);
        perInfo.add(infoPanel, BorderLayout.EAST);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("政大公車系統");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 1000);
            frame.setLayout(new BorderLayout());

            // 時鐘
            LocalTime now = LocalTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            timeLabel.setText(now.format(formatter));
            Timer clockTimer = new Timer(1000, e -> timeLabel.setText(LocalTime.now().format(formatter)));
            clockTimer.start();

            // 最新消息區
            perInfo.add(labInfo, BorderLayout.WEST);
            perInfo.add(timeLabel, BorderLayout.CENTER);
            setupPerInfo();
            upperPanel.add(perInfo);
            upperPanel.add(label);

            // 公車選單按鈕
            JPanel btnPanel = new JPanel(new GridLayout(1, 3));
            String[] btnNames = {"台北車站", "捷運動物園站", "捷運市政府站"};
            for (int i = 0; i < btnNames.length; i++) {
                int idx = i;
                buttons[i] = new JButton(btnNames[i]);
                buttons[i].setFocusPainted(false);
                buttons[i].setBorderPainted(false);
                buttons[i].setContentAreaFilled(false);
                buttons[i].setOpaque(true);
                buttons[i].setFont(new Font("SansSerif", Font.BOLD, 16));
                buttons[i].addActionListener(e -> switchTable(idx));
                btnPanel.add(buttons[i]);
            }
            upperPanel.add(btnPanel);
            frame.add(upperPanel, BorderLayout.NORTH);

            // 資料模型與表格
            String[] cols = {"公車類別", "時間", "路線連結"};
            models[0] = new DefaultTableModel(new Object[][]{{"113", 60, 10}, {"566", 20, 10}}, cols);
            models[1] = new DefaultTableModel(new Object[][]{{"622", 5, 10}, {"142", 15, 10}}, cols);
            models[2] = new DefaultTableModel(new Object[][]{{"321", 5, 10}, {"654", 10, 10}}, cols);

            table = new JTable(models[0]);
            switchTable(0);

            // 下方面板：公車表格 + 計程車資訊面板
            underPanel.add(new JScrollPane(table));
            underPanel.add(new TaxiInfoPanel());  // 新增 TaxiInfoPanel
            frame.add(underPanel, BorderLayout.CENTER);

            LatestNews();
            frame.setVisible(true);
        });
    }

    private static void switchTable(int index) {
        table.setModel(models[index]);
        for (int i = 0; i < buttons.length; i++) {
            if (i == index) {
                buttons[i].setForeground(Color.BLUE);
                buttons[i].setBackground(new Color(173, 216, 230));
            } else {
                buttons[i].setForeground(Color.BLACK);
                buttons[i].setBackground(Color.WHITE);
            }
        }
    }
}
