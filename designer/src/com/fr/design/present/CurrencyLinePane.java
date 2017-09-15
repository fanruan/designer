package com.fr.design.present;

import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.border.UIRoundedBorder;
import com.fr.design.constants.LayoutConstants;
import com.fr.design.constants.UIConstants;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.ispinner.UIBasicSpinner;
import com.fr.design.gui.ispinner.UISpinner;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.general.Inter;
import com.fr.report.cell.cellattr.CurrencyLineAttr;
import com.fr.report.cell.cellattr.CurrencyLinePresent;
import com.fr.report.cell.painter.barcode.BarcodeException;
import com.fr.report.core.CurrencyLineImpl;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

/**
 * @author zhou
 * @since 2012-6-4下午7:34:52
 */
public class CurrencyLinePane extends FurtherBasicBeanPane<CurrencyLinePresent> {
    private static final int VS_NUM = 4;
    private static final int VG_NUM = 6;
    private static final Dimension NORMAL_DIMENSION = new Dimension(155, 20);
    private UISpinner intPartSpinner;
    private UISpinner deciPartSpinner;
    private UITextField textField;
    private CurrencyLinePreviewPane CurrencyLinePreviewPane;
    private int intPart = 9;
    private int deciPart = 3;

    private static final int POSITION = 8;
    ChangeListener listener2 = new ChangeListener() {

        @Override
        public void stateChanged(ChangeEvent e) {
            CurrencyLinePreviewPane.setObject(textField.getText(), update());
        }

    };

    DocumentListener listener = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            CurrencyLinePreviewPane.setObject(textField.getText(), update());
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            CurrencyLinePreviewPane.setObject(textField.getText(), update());
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            CurrencyLinePreviewPane.setObject(textField.getText(), update());
        }
    };

    public CurrencyLinePane() {
        this.initComponents();
    }

    protected void initComponents() {
        // 整数位选择
        intPartSpinner = new UISpinner(1, 20, 1, 9);
        intPartSpinner.setPreferredSize(NORMAL_DIMENSION);

        // 小数位选择
        deciPartSpinner = new UISpinner(1, 10, 1, 2);
        deciPartSpinner.setPreferredSize(NORMAL_DIMENSION);
        // 预览区域
        textField = new UITextField(10);

        CurrencyLinePreviewPane = new CurrencyLinePreviewPane();
        CurrencyLinePreviewPane.setPreferredSize(new Dimension(0, 145));
        JPanel borderPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        TitledBorder titledBorder = new TitledBorder(new UIRoundedBorder(UIConstants.LINE_COLOR, 1, 5), Inter.getLocText("StyleFormat-Sample"), 4, 2, this.getFont(), UIConstants.LINE_COLOR);
        borderPane.setBorder(titledBorder);
        borderPane.add(CurrencyLinePreviewPane, BorderLayout.CENTER);

        textField.requestFocus();

        double vs = VS_NUM;
        double vg = VG_NUM;
        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {p, f};
        double[] rowSize = {p, p, p, p};
        int[][] rowCount = {{1, 1}, {1, 1}, {1, 1}, {1, 1}};

        Component[][] components = new Component[][]{
                new Component[]{new UILabel(Inter.getLocText("Data"), UILabel.LEFT), textField},
                new Component[]{borderPane, null},
                new Component[]{new UILabel(Inter.getLocText("IntPart"), UILabel.LEFT), groupPane(intPartSpinner)},
                new Component[]{new UILabel(Inter.getLocText("DeciPart"), UILabel.LEFT), groupPane(deciPartSpinner)}

        };

        JPanel linePane = TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, LayoutConstants.VGAP_HUGER, LayoutConstants.VGAP_LARGE);
        this.setLayout(new BorderLayout());
        this.add(linePane, BorderLayout.CENTER);


        textField.getDocument().addDocumentListener(listener);
        intPartSpinner.addChangeListener(listener2);
        deciPartSpinner.addChangeListener(listener2);
        textField.setText("123456.78");
    }

    @Override
    /**
     * 窗口名
     *    @return 同上
     */
    public String title4PopupWindow() {
        return Inter.getLocText("Currency_Line");
    }

    /**
     *
     */
    public CurrencyLineAttr update() {
        CurrencyLineAttr currencylineAttr = new CurrencyLineAttr();
        currencylineAttr.setintPart((int) this.intPartSpinner.getValue());
        currencylineAttr.setdeciPart((int) this.deciPartSpinner.getValue());
        return currencylineAttr;
    }

    /**
     *
     */
    public void setintPart(int intpart) {
        this.intPart = intpart;
    }

    /**
     *
     */
    public void setdeciPart(int decipart) {
        this.deciPart = decipart;
    }

    private class CurrencyLinePreviewPane extends JPanel {
        private String text;
        CurrencyLineAttr currencyLineAttr;

        public CurrencyLinePreviewPane() {
//			setBackground(Color.WHITE);
        }

        public void setObject(String text, CurrencyLineAttr currencyLineAttr) {
            this.text = text;
            this.currencyLineAttr = currencyLineAttr;
            GUICoreUtils.repaint(this);
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (text == null) {
                return;
            }
            Dimension size = this.getSize();
            try {
                CurrencyLineImpl currencyLineImpl = new CurrencyLineImpl(text, currencyLineAttr);
                currencyLineImpl.draw((Graphics2D) g, (int) (size.getWidth()), (int) (size.getHeight()));
            } catch (BarcodeException e) {
                Color oldColor = g.getColor();
                g.setColor(Color.red);
                g.drawString(e.getMessage(), (int) (size.getWidth() / POSITION), (int) (size.getHeight() / POSITION));
                g.setColor(oldColor);
            }
        }

    }

    protected static JPanel groupPane(JComponent comp) {
        JPanel jp = new JPanel();
        jp.setBorder(null);
        jp.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        jp.add(comp);
        return jp;
    }

    @Override
    /**
     * 是否为该类型
     * @param ob 对象
     * @return 同上
     *
     */
    public boolean accept(Object ob) {
        return ob instanceof CurrencyLinePresent;
    }

    /**
     * 重置
     */
    public void reset() {
        this.intPartSpinner.setValue(9);
        this.deciPartSpinner.setValue(3);
    }

    @Override
    public void populateBean(CurrencyLinePresent ob) {
        CurrencyLineAttr currencyLine = ob.getCurrencyLineAttr();
        if (currencyLine == null) {
            currencyLine = new CurrencyLineAttr();
        }
        this.intPartSpinner.setValue(new Integer(currencyLine.getintPart()));
        this.deciPartSpinner.setValue(new Integer(currencyLine.getdeciPart()));
    }

    @Override
    public CurrencyLinePresent updateBean() {
        CurrencyLineAttr currencylineAttr = new CurrencyLineAttr();
        currencylineAttr.setintPart((int) this.intPartSpinner.getValue());
        currencylineAttr.setdeciPart((int) this.deciPartSpinner.getValue());
        return new CurrencyLinePresent(currencylineAttr);
    }

}