package com.fr.design.style.background.gradient;

import com.fr.design.DesignerEnvManager;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.gui.itextfield.UINumberField;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.style.color.ColorCell;
import com.fr.design.style.color.ColorSelectDetailPane;
import com.fr.design.style.color.ColorSelectDialog;
import com.fr.design.style.color.ColorSelectable;
import com.fr.stable.AssistUtils;

import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

/**
 * TODO:面板缩放的功能没有考虑（就是尾值过大，导致超过界面显示的情况），原来的那个实现完全是个BUG。要缩放的情况也比较少，就干脆以后弄吧
 */
public class GradientBar extends JComponent implements UIObserver, ColorSelectable {

    /**
     *
     */
    private static final long serialVersionUID = -8503629815871053585L;

    private List<SelectColorPointBtn> list = new ArrayList<SelectColorPointBtn>();
    private SelectColorPointBtn p1;
    private SelectColorPointBtn p2;

    private int index;// 选中了p1还是p2

    private final int min;// 最小值
    private final int max;// 最大值

    private UINumberField startLabel;
    private UINumberField endLabel;

    private ChangeListener changeListener = null;
    private List<UIObserverListener> uiObserverListener;

    private static final int MOUSE_OFFSET = 4;

    private static final int MAX_VERTICAL = 45;

    // 选中的颜色
    private Color color;

    public GradientBar(int minvalue, int maxvalue) {
        min = minvalue;
        max = maxvalue;

        startLabel = new UINumberField(11);
        startLabel.setValue(min);
        startLabel.getDocument().addDocumentListener(docListener);

        endLabel = new UINumberField(11);
        endLabel.setValue(max);
        endLabel.getDocument().addDocumentListener(docListener);

        this.setPreferredSize(new Dimension(max + 5, 50));

        p1 = new SelectColorPointBtn(startLabel.getValue(), 30, Color.WHITE);
        p2 = new SelectColorPointBtn(endLabel.getValue(), 30, Color.BLACK);
        list.add(p1);
        list.add(p2);

        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        addMouseClickListener();
        addMouseDragListener();
        iniListener();
    }

    public void updateColor(Color begin, Color end) {
        p1.setColorInner(begin);
        p2.setColorInner(end);
    }

    protected void addMouseClickListener() {
        this.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getX() < max + MOUSE_OFFSET && e.getX() > 0) {
                    int select = -1;
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).contains(e.getX(), e.getY())) {
                            select = i;
                            break;
                        }
                    }

                    if (select >= 0) {
                        ColorSelectDetailPane pane = new ColorSelectDetailPane(Color.WHITE);
                        ColorSelectDialog.showDialog(DesignerContext.getDesignerFrame(), pane, Color.WHITE, GradientBar.this);
                        Color color = GradientBar.this.getColor();
                        if (color != null) {
                            DesignerEnvManager.getEnvManager().getColorConfigManager().addToColorQueue(color);
                            list.get(select).setColorInner(color);
                            stateChanged();
                            GradientBar.this.repaint();
                        }
                    }
                    GradientBar.this.repaint();
                }
            }
        });
    }

    protected void addMouseDragListener() {
        this.addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {

                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).contains(e.getX(), e.getY())) {
                        index = i;
                        break;
                    }
                }

                boolean x = e.getX() <= max && e.getX() >= min;
                if (x && e.getY() < MAX_VERTICAL) {
                    list.get(index).setX(e.getX());
                }

                GradientBar.this.repaint();
                startLabel.setText(Double.toString(p1.getX()));
                endLabel.setText(Double.toString(p2.getX()));
            }
        });
    }

    private void iniListener() {
        uiObserverListener = new ArrayList<>();
        if (shouldResponseChangeListener()) {
            this.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    for (UIObserverListener observerListener : uiObserverListener) {
                        observerListener.doChange();
                    }
                }
            });
        }
    }

    DocumentListener docListener = new DocumentListener() {
        public void changedUpdate(DocumentEvent e) {
            stateChanged();
        }

        public void insertUpdate(DocumentEvent e) {
            stateChanged();
        }

        public void removeUpdate(DocumentEvent e) {
            stateChanged();
        }
    };

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        Point2D start = new Point2D.Float(4, 0);
        Point2D end = new Point2D.Float(max, 0);
        Collections.sort(list);
        Color[] c = new Color[list.size()];
        for (int i = 0; i < list.size(); i++) {
            c[i] = list.get(i).getColorInner();
        }
        float[] dist = new float[list.size()];
        for (int i = 0; i < list.size(); i++) {
            dist[i] = (float) ((list.get(i).getX() - 4) / (max - 4));
        }
        LinearGradientPaint paint = new LinearGradientPaint(start, end, dist, c);

        g2.setPaint(paint);
        g2.fillRect(4, 0, max - 4, 30);
        g2.setColor(new Color(138, 138, 138));
        g2.drawRect(4, 0, max - 4, 30);
        for (int i = 0; i < list.size(); i++) {
            list.get(i).paint(g2);
        }
    }

    /**
     * 状态改变
     */
    public void stateChanged() {
        if (changeListener != null) {
            changeListener.stateChanged(null);
        }
    }

    /**
     * 增加监听
     *
     * @param changeListener 监听
     */
    public void addChangeListener(ChangeListener changeListener) {
        this.changeListener = changeListener;
    }

    /**
     * 如果左右两个按钮还在初始位置，就为true
     *
     * @return 同上
     */
    public boolean isOriginalPlace() {
        return AssistUtils.equals(startLabel.getValue(), min) && AssistUtils.equals(endLabel.getValue(), max);
    }

    /**
     * @return
     */
    public double getStartValue() {
        return startLabel.getValue();
    }

    /**
     * @return
     */
    public double getEndValue() {
        return endLabel.getValue();
    }

    /**
     * @param startValue
     */
    public void setStartValue(double startValue) {
        startLabel.setValue(startValue);
        p1.setX(startValue);
    }

    /**
     * @param endValue
     */
    public void setEndValue(double endValue) {
        endLabel.setValue(endValue);
        p2.setX(endValue);
    }

    /**
     * @return
     */
    public SelectColorPointBtn getSelectColorPointBtnP1() {
        return p1;
    }

    /**
     * @return
     */
    public SelectColorPointBtn getSelectColorPointBtnP2() {
        return p2;
    }

    @Override
    /**
     * 注册监听
     * @param UIObserverListener 监听
     *
     */
    public void registerChangeListener(UIObserverListener listener) {
        uiObserverListener.add(listener);
    }

    @Override
    /**
     * 是否响应监听
     * @return 同上
     */
    public boolean shouldResponseChangeListener() {
        return true;
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public Color getColor() {
        return this.color;
    }

    /**
     * 选中颜色
     *
     * @param ColorCell 颜色单元格
     */
    @Override
    public void colorSetted(ColorCell colorCell) {

    }
}