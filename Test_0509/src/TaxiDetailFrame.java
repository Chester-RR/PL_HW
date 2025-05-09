import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;




class TaxiDetailFrame extends JFrame {
    private DefaultTableModel participantModel;
    private JTable participantTable;
    private JTextArea notesArea;
    private JButton confirmButton;
    private String initiator;

    public TaxiDetailFrame(String initiator) {
        this.initiator = initiator;
        setTitle("更多資訊 - " + initiator);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // 上半部：想搭車人的清單
        String[] cols = {"暱稱", "電話", "Email"};
        participantModel = new DefaultTableModel(cols, 0);
        participantTable = new JTable(participantModel);
        add(new JScrollPane(participantTable), BorderLayout.NORTH);

        // 下半部：備註與確認
        JPanel bottomPanel = new JPanel(new BorderLayout(5,5));
        bottomPanel.add(new JLabel("備註："), BorderLayout.NORTH);
        notesArea = new JTextArea(5, 20);
        bottomPanel.add(new JScrollPane(notesArea), BorderLayout.CENTER);

        confirmButton = new JButton("確認");
        bottomPanel.add(confirmButton, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.CENTER);

        // 確認後從 DB 或本地取得該 initiator 的檔案，並加入到上半部清單
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Person p = fetchUserData(initiator);
                participantModel.addRow(new Object[]{p.getNickname(), p.getPhone(), p.getEmail()});
            }
        });
    }

    // 範例：從資料庫或本地取回使用者資料
    private Person fetchUserData(String name) {
        // TODO: 在此實作實際的 DB 查詢，以下為範例
        return new Person(name, "0900000000", name.toLowerCase() + "@example.com");
    }
}

// 簡單的使用者資料結構
class Person {
    private String nickname;
    private String phone;
    private String email;

    public Person(String nickname, String phone, String email) {
        this.nickname = nickname;
        this.phone = phone;
        this.email = email;
    }

    public String getNickname() { return nickname; }
    public String getPhone()    { return phone; }
    public String getEmail()    { return email; }
}
