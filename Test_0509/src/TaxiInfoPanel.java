import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

/**
 * 面板：顯示計程車行程簡要資訊列表
 */
public class TaxiInfoPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;

    public TaxiInfoPanel() {
        setLayout(new BorderLayout());

        // 欄位定義與範例資料
        String[] columns = {"發起人", "預計出發時間", "預估價格", "目前人數"};
        Object[][] data = {
            {"A", "08:00", 200, 2},
            {"B", "09:30", 150, 3},
            {"C", "10:00", 180, 1},
            {"D", "11:15", 220, 4},
            {"E", "12:45", 160, 2}
        };

        model = new DefaultTableModel(data, columns);
        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // 選取一列後打開詳細視窗
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

        add(new JScrollPane(table), BorderLayout.CENTER);
    }
}
