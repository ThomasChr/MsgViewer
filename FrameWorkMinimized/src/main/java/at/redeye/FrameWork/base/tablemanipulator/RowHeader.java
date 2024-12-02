package at.redeye.FrameWork.base.tablemanipulator;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import static javax.swing.SwingConstants.RIGHT;

public class RowHeader
{
    static class RowHeaderRenderer implements ListCellRenderer<Integer> {

        private final JTableHeader header;

        private RowHeaderRenderer(JTableHeader header) {
            this.header = header;
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Integer> list, Integer value,
                                                      int index, boolean isSelected, boolean cellHasFocus) {
            String text = value == null ? " " : (value + " ");
            JLabel component = new JLabel(text);
            component.setOpaque(true);
            component.setBorder(UIManager.getBorder("TableHeader.cellBorder"));
            component.setHorizontalAlignment(RIGHT);

            component.setForeground(header.getForeground());
            component.setBackground(header.getBackground());
            component.setFont(header.getFont());

            return component;
        }

    }

    static class ListModel extends AbstractListModel<Integer> {
        private final JTable table;

        private ListModel(JTable table) {
            this.table = table;
        }

        @Override
        public int getSize() {
            int count = table.getRowCount();

            if (count == 0)
                return 1;

            return count;
        }

        @Override
        public Integer getElementAt(int index) {
            return index + 1;
        }
    }

    private JList<Integer> list;
    private JScrollPane scroll;
    private boolean visible_state = true;
    private boolean vertical_scroll_bar_visible;

    public RowHeader(JTable table, final Runnable visible_state_listener) {

        scroll = null;

        for (Container cont = table.getParent(); cont != null; cont = cont.getParent()) {
            if (cont instanceof JScrollPane) {
                scroll = (JScrollPane) cont;
                break;
            }
        }

        if (scroll != null) {
            list = new JList<>(new ListModel(table));

            list.setOpaque(false);

            ListCellRenderer<Integer> header = new RowHeaderRenderer(table.getTableHeader());

            list.setCellRenderer(header);

            scroll.setRowHeaderView(list);

            JScrollBar scroll_bar = scroll.getVerticalScrollBar();

            if( scroll_bar != null )
            {
                scroll_bar.addComponentListener(new ComponentAdapter() {
                    @Override
                    public void componentShown(ComponentEvent e) {
                        vertical_scroll_bar_visible = true;
                        visible_state_listener.run();
                    }

                    @Override
                    public void componentHidden(ComponentEvent e) {
                        vertical_scroll_bar_visible = false;
                        visible_state_listener.run();
                    }
                });
            }
        }
    }

    public void updateUI()
    {
        list.updateUI();
        scroll.updateUI();
    }

    public void setCellHeight( int height )
    {
        // obriger code funktioniert eh sehr gut, aber
        // performanter ist dieser hier.
        if( list != null )
            list.setFixedCellHeight(height);
    }

    void setVisible(boolean state) {
        if (state == visible_state) {
            return;
        }

        visible_state = state;

        scroll.setRowHeaderView(state ? list : null);

        updateUI();
    }

    boolean isScrollBarVisible()
    {
        return vertical_scroll_bar_visible;
    }
}
