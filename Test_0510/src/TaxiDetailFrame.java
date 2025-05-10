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

        // 參與者表格
        String[] cols = {"暱稱", "電話", "Email"};
        participantModel = new DefaultTableModel(cols, 0);
        participantTable = new JTable(participantModel);
        JScrollPane topScroll = new JScrollPane(participantTable);
        topScroll.setPreferredSize(new Dimension(400, 120));
        add(topScroll, BorderLayout.NORTH);

        // 留言板
        JPanel messagePanel = new JPanel(new BorderLayout(5,5));
        messagePanel.add(new JLabel("留言板："), BorderLayout.NORTH);

        commentModel = new DefaultListModel<>();
        commentList = new JList<>(commentModel);
        JScrollPane commentScroll = new JScrollPane(commentList);
        commentScroll.setPreferredSize(new Dimension(400, 150));
        messagePanel.add(commentScroll, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout(5,5));
        commentField = new JTextField();
        addCommentButton = new JButton("新增留言");
        inputPanel.add(commentField, BorderLayout.CENTER);
        inputPanel.add(addCommentButton, BorderLayout.EAST);
        messagePanel.add(inputPanel, BorderLayout.SOUTH);

        add(messagePanel, BorderLayout.CENTER);

        // 確認按鈕
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        confirmButton = new JButton("確認");
        bottomPanel.add(confirmButton);
        add(bottomPanel, BorderLayout.SOUTH);

        addCommentButton.addActionListener(e -> {
            String comment = commentField.getText().trim();
            if (!comment.isEmpty()) {
                commentModel.addElement(comment);
                commentField.setText("");
            }
        });

        confirmButton.addActionListener(e -> {
            Person p = fetchUserData(initiator);
            participantModel.addRow(new Object[]{p.getNickname(), p.getPhone(), p.getEmail()});
        });
    }

    private Person fetchUserData(String name) {
        // TODO: 資料庫查詢
        return new Person(name, "0900000000", name.toLowerCase() + "@example.com");
    }
}

