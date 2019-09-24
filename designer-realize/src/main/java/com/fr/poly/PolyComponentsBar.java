package com.fr.poly;

import com.fr.base.BaseUtils;
import com.fr.base.chart.BaseChartCollection;
import com.fr.base.chart.BaseChartGetter;
import com.fr.base.vcs.DesignerMode;
import com.fr.chart.charttypes.ChartTypeManager;
import com.fr.design.ChartTypeInterfaceManager;
import com.fr.design.constants.UIConstants;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.itooltip.MultiLineToolTip;
import com.fr.design.i18n.Toolkit;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.mainframe.dnd.SerializableTransferable;
import com.fr.log.FineLoggerFactory;
import com.fr.report.poly.PolyECBlock;
import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.JToolTip;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.io.Serializable;

/**
 * 拖动聚合块的那个竖的动作条
 *
 * @editor zhou
 * @since 2012-3-23下午3:42:10
 */
public class PolyComponentsBar extends JToolBar {
    private SerIcon[] serIcons;
    private static final int MAX_BAR_NUM = 15;

    public PolyComponentsBar() {
        setOrientation(SwingConstants.VERTICAL);
        setBorder(BorderFactory.createEmptyBorder(4, 4, 0, 4));
        setFloatable(false);
        setBackground(UIConstants.TOOLBARUI_BACKGROUND);
        setLayout(FRGUIPaneFactory.create1ColumnGridLayout());
        String[] iDs = ChartTypeManager.getInstance().getAllChartIDs();
        int typeLen = iDs.length < MAX_BAR_NUM ? iDs.length : MAX_BAR_NUM;
        serIcons = new SerIcon[typeLen + 1];
        serIcons[0] = new SerIcon(
                PolyECBlock.class,
                Toolkit.i18nText("Fine-Design_Report_Poly_Report_Block"),
                "com/fr/design/images/poly/toolbar/Poly-Report_Block.png"
        );
        this.add(serIcons[0]);
        for (int i = 0; i < typeLen; i++) {
            String chartID = iDs[i];
            String iconPath = ChartTypeInterfaceManager.getInstance().getIconPath(chartID);
            BaseChartCollection chartCollection = BaseChartGetter.createChartCollection(chartID);
            serIcons[i + 1] = new SerIcon(chartCollection, ChartTypeInterfaceManager.getInstance().getName(chartID), iconPath);
            this.add(serIcons[i + 1]);
        }

    }

    /**
     * 设置是否可用状态
     */
    public void checkEnable() {
        for (SerIcon serIcon : serIcons) {
            serIcon.setEnabled(!DesignerMode.isAuthorityEditing());
        }
    }

    private class SerIcon extends UIButton implements DragGestureListener, DragSourceListener {
        private DragSource dragSource;
        private Serializable serializable;

        /**
         * @param serializable s
         * @param text         按钮名
         * @param iconPath     图标路径
         */
        public SerIcon(Serializable serializable, String text, String iconPath) {
            super(BaseUtils.readIcon(iconPath));
            this.serializable = serializable;
            this.setToolTipText(text);
            this.set4ToolbarButton();
            dragSource = new DragSource();
            dragSource.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_COPY_OR_MOVE, this);
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(getIcon().getIconWidth(), getIcon().getIconHeight());
        }

        @Override
        public JToolTip createToolTip() {
            MultiLineToolTip tip = new MultiLineToolTip();
            tip.setComponent(this);
            tip.setOpaque(false);
            return tip;
        }

        @Override
        public void dragGestureRecognized(DragGestureEvent dge) {
            Transferable t = new SerializableTransferable(serializable);
            dragSource.startDrag(dge, DragSource.DefaultCopyDrop, t, this);
            getModel().setArmed(false);
            getModel().setRollover(false);
            repaint();
        }

        @Override
        public void dragEnter(DragSourceDragEvent dsde) {

        }

        @Override
        public void dragOver(DragSourceDragEvent dsde) {

        }

        @Override
        public void dropActionChanged(DragSourceDragEvent dsde) {

        }

        @Override
        public void dragExit(DragSourceEvent dse) {

        }

        @Override
        public void dragDropEnd(DragSourceDropEvent dsde) {

        }
    }

    /**
     * 测试下
     *
     * @param args 参数
     */
    public static void main(String... args) {
        try {
            UIManager.setLookAndFeel(new WindowsLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
        JFrame f = new JFrame();
        JPanel p = (JPanel) f.getContentPane();
        p.setLayout(FRGUIPaneFactory.createBorderLayout());
        PolyComponentsBar pbp = new PolyComponentsBar();
        p.add(pbp, BorderLayout.CENTER);

        f.setSize(400, 300);
        f.setVisible(true);

    }
}
