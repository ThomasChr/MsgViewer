package at.redeye.FrameWork.base.translation;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.util.List;
import java.util.*;

public class ExtractStrings {
    static private class TabTitleWrapper extends JComponent {
        private final JTabbedPane parent;
        private final int index;

        private TabTitleWrapper(JTabbedPane parent, int index) {
            this.index = index;
            this.parent = parent;
        }

        private String getText() {
            return parent.getTitleAt(index);
        }

        private void setText(String text) {
            parent.setTitleAt(index, text);
        }
    }

    static private class ToolTipWrapper extends JComponent
    {
        private final JComponent parent;

        private ToolTipWrapper(JComponent parent) {
            this.parent = parent;
        }

        private void setText(String text) {
            parent.setToolTipText(text);
        }
    }

    static private class FrameTitleWrapper extends JComponent {
        private TitledBorder border;

        private FrameTitleWrapper(JComponent parent) {
            if (parent.getBorder() instanceof TitledBorder) {
                border = (TitledBorder) parent.getBorder();
            }
        }

        private void setText(String text) {
            border.setTitle(text);
        }
    }

    final Set<String> strings = new TreeSet<>();
    private final Map<String, List<JComponent>> components = new HashMap<>();

    public ExtractStrings(Container cont) {
        extractStrings(cont);
    }

    public Set<String> getStrings() {
        return strings;
    }

    public Map<String, List<JComponent>> getComponents() {
        return components;
    }

    public static void assign(JComponent comp, String value) {

        if (comp instanceof AbstractButton)
            ((AbstractButton) comp).setText(value);
        else if (comp instanceof JLabel)
            ((JLabel) comp).setText(value);
        else if (comp instanceof TabTitleWrapper)
            ((TabTitleWrapper) comp).setText(value);
        else if (comp instanceof ToolTipWrapper)
            ((ToolTipWrapper) comp).setText(value);
        else if (comp instanceof FrameTitleWrapper)
            ((FrameTitleWrapper) comp).setText(value);
        else if (comp instanceof JTextComponent)
            ((JTextComponent) comp).setText(value);
    }

    private void extractStrings(Container cont) {
        for (Component comp : cont.getComponents()) {
            if (comp instanceof JComponent jcomp) {

                String text = jcomp.getToolTipText();

                if( text != null && !text.isEmpty() )
                {
                    addComp( text, new ToolTipWrapper(jcomp));
                }

                Border border = jcomp.getBorder();

                if (border instanceof TitledBorder tborder) {
                    text = tborder.getTitle();

                    if( text != null && !text.isEmpty() )
                    {
                        addComp( text, new FrameTitleWrapper(jcomp));
                    }
                }
            }

            if( comp instanceof JLabel )
                addString((JLabel)comp);
            else if( comp instanceof JButton )
                addString((JButton)comp);
            else if( comp instanceof JMenu )
                addString((JMenu)comp);
            else if (comp instanceof JMenuItem)
                addString((JMenuItem) comp);
            else if (comp instanceof JToggleButton)
                addString((JToggleButton) comp);
            else if (comp instanceof JTextComponent)
                addString((JTextComponent) comp);
            else if (comp instanceof JTabbedPane) {
                addString((JTabbedPane) comp);

                try {
                    extractStrings((Container) comp);
                } catch (Exception ignored) {
                }
            } else {
                try {
                    extractStrings((Container) comp);
                } catch (Exception ignored) {
                }
            }
        }
    }

    private void addString( JLabel label )
    {
        addComp(label.getText(),label);
    }

    private void addString(JToggleButton button) {
        addComp(button.getText(), button);
    }

    private void addString( JButton button )
    {
        if( button.getText().isEmpty() )
            return;

        addComp(button.getText(),button);
    }

    private void addString(JMenu menu) {

        if( menu.getText().isEmpty() )
            return;

        addComp(menu.getText(),menu);

        extractStrings(menu.getPopupMenu());
    }

    private void addString(JMenuItem menu_item) {

        if (menu_item.getText().isEmpty())
            return;

        addComp(menu_item.getText(), menu_item);
    }

    private void addString(JTextComponent jTextComponent) {
        addComp(jTextComponent.getText(), jTextComponent);
    }

    private void addString(JTabbedPane jTabbedPane) {
        for (int i = 0; i < jTabbedPane.getTabCount(); i++) {
            TabTitleWrapper wrapper = new TabTitleWrapper(jTabbedPane, i);
            addComp(wrapper.getText(), wrapper);
        }
    }

    private void addComp(String text, JComponent comp) {
        strings.add(text);

        List<JComponent> vcomp = components.computeIfAbsent(text, k -> new ArrayList<>());
        vcomp.add(comp);
    }
}
