import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;





class TaxiDetailFrame extends JFrame {
    private DefaultTableModel participantModel;
    private JTable participantTable;

    private DefaultListModel<String> commentModel;
    private JList<String> commentList;
    private JTextField commentField;
    private JButton addCommentButton;
    private JButton confirmButton;

    public TaxiDetailFrame(String initiator) {
        setTitle("更多資訊 - 行程：" + initiator);
        setSize(450, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // 上半部：參與者表格
        String[] cols = {"暱稱", "電話", "Email"};
        participantModel = new DefaultTableModel(cols, 0);
        participantTable = new JTable(participantModel);
        add(new JScrollPane(participantTable), BorderLayout.NORTH);

        // 中間：留言板
        JPanel messagePanel = new JPanel(new BorderLayout(5,5));
        messagePanel.add(new JLabel("留言板："), BorderLayout.NORTH);

        commentModel = new DefaultListModel<>();
        commentList = new JList<>(commentModel);
        messagePanel.add(new JScrollPane(commentList), BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout(5,5));
        commentField = new JTextField();
        addCommentButton = new JButton("新增留言");
        inputPanel.add(commentField, BorderLayout.CENTER);
        inputPanel.add(addCommentButton, BorderLayout.EAST);
        messagePanel.add(inputPanel, BorderLayout.SOUTH);

        add(messagePanel, BorderLayout.CENTER);

        // 下方：確認按鈕（可用於加入參與者或其他操作）
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        confirmButton = new JButton("確認");
        bottomPanel.add(confirmButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // 事件：新增留言至列表（後續可連接資料庫）
        addCommentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String comment = commentField.getText().trim();
                if (!comment.isEmpty()) {
                    commentModel.addElement(comment);
                    commentField.setText("");
                }
            }
        });

        // 確認按鈕：範例操作，拉取使用者資料加入參與者表格
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Person p = fetchUserData(initiator);
                participantModel.addRow(new Object[]{p.getNickname(), p.getPhone(), p.getEmail()});
            }
        });
    }

    /**
     * 範例：從資料庫或本地讀取使用者資料，實際使用時實作 DB 存取
     */
    private Person fetchUserData(String name) {
        // TODO: 實作對應資料庫查詢，以下僅示範
        return new Person(name, "0900000000", name.toLowerCase() + "@example.com");
    }
}

