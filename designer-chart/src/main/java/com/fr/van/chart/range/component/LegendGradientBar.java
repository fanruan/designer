package com.fr.van.chart.range.component;

import com.fr.chart.base.ChartBaseUtils;
import com.fr.design.DesignerEnvManager;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.mainframe.DesignerContext;
import com.fr.design.style.background.gradient.SelectColorPointBtn;
import com.fr.design.style.color.ColorCell;
import com.fr.design.style.color.ColorSelectConfigManager;
import com.fr.design.style.color.ColorSelectDetailPane;
import com.fr.design.style.color.ColorSelectDialog;
import com.fr.design.style.color.ColorSelectable;
import com.fr.plugin.chart.range.GradualIntervalConfig;
import com.fr.plugin.chart.range.glyph.GradualColorDist;
import com.fr.stable.AssistUtils;

import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LegendGradientBar extends JComponent implements ColorSelectable, UIObserver {

    private List<SelectColorPointBtn> selectColorPointBtnList;

    private int startPos;
    private int endPos;

    private SelectColorPointBtn selectColorSlotBtnStart;
    private SelectColorPointBtn selectColorSlotBtnEnd;

    private static final int MOUSE_OFFSET = 4;
    private static final int REC_HEIGHT = 30;
    private static final int MAX_VERTICAL = 45;

    //颜色选择器个数
    private int colorSelectionBtnNum;

    //主题色
    private Color subColor;

    private int max = 150;
    private int min = 4;

    //选中的颜色
    private Color color;

    //记录是否有滑块在滑动
    private boolean pointIsMoving = false;

    //颜色数组和位置数组
    private Color[] colors;
    private float[] dist;

    //偏移量
    private static final int OFFSETSTEP = 2;

    //拖动的滑块下标
    int index = -1;

    private UIObserverListener uiObserverListener;

    private List<ChangeListener> changeListenerList = new ArrayList<ChangeListener>();


    public LegendGradientBar(){
        startPos = 4;
        endPos = max;

        setSubColor(new Color(36,167,255));

        setColorSelectionBtnNum(2 + 1);

        initColorsAndDist();

        initMouseListener();

        iniListener();

        this.setPreferredSize(new Dimension(5 + max, MAX_VERTICAL));
    }

    private void initColorsAndDist() {
        colors = initColors(subColor, colorSelectionBtnNum);

        initColorSelectBtn(colors, colorSelectionBtnNum);

        dist = initDist(colorSelectionBtnNum);
    }

    private void iniListener() {
        if (shouldResponseChangeListener()) {
            this.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    if (uiObserverListener == null) {
                        return;
                    }
                    uiObserverListener.doChange();
                }
            });
        }
    }

    private float[] initDist(int colorSelectionBtnNum) {
        float[] dist = new float[colorSelectionBtnNum];
        dist[0] = 0f;
        for (int i = 0; i < colorSelectionBtnNum-2; i++) {
            dist[i+1] = (float) ((selectColorPointBtnList.get(i).getX() - 4) / (max - 4));
        }
        dist[colorSelectionBtnNum-1] = 1f;
        return dist;
    }

    private void initMouseListener() {
        addMouseEnteredListener();
        addMouseClickListener();
        addMouseReleasedListener();
        if (supportDrag()) {
            addMouseDragListener();
        }
        addMouseExitedListener();
    }

    protected boolean supportDrag() {
        return true;
    }

    private Color[] initColors(Color subColor, int colorSelectionBtnNum) {
        Color[] converseColors = getColorArray(subColor, colorSelectionBtnNum);
        Color[] colors = new Color[colorSelectionBtnNum];
        for (int i = 0; i < colorSelectionBtnNum; i++){
            colors[i] = converseColors[colorSelectionBtnNum-1 - i];
        }
        return colors;
    }

    private void initColorSelectBtn(Color[] colors, int colorSelectionBtnNum) {
        //获取按钮颜色数组
        Color startSlotColor = colors[0];
        Color endSlotColor = colors[colorSelectionBtnNum-1];

        Color[] pColors = new Color[colorSelectionBtnNum - 2];
        for (int i = 0; i < pColors.length; i++){
            pColors[i] = colors[i + 1];
        }

        selectColorSlotBtnStart = new SelectColorPointBtn(startPos, REC_HEIGHT, startSlotColor, new Color(138,138,138));
        selectColorSlotBtnEnd = new SelectColorPointBtn(endPos, REC_HEIGHT, endSlotColor, new Color(138,138,138));

        //初始化list
        selectColorPointBtnList = new ArrayList<SelectColorPointBtn>();

        //加入可滑动颜色选择器
        for (int i = 0; i < pColors.length; i++){
            SelectColorPointBtn pi = new SelectColorPointBtn(((startPos+endPos)/(pColors.length+1))*(i+1), REC_HEIGHT, pColors[i]);
            selectColorPointBtnList.add(pi);
        }
        this.repaint();
    }


    /**
     * 添加事件
     * @param changeListener 事件
     */
    public void addChangeListener(ChangeListener changeListener) {
        this.changeListenerList.add(changeListener);
    }

    private void addMouseEnteredListener() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                LegendGradientBar.this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
        });
    }

    private void addMouseExitedListener() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                LegendGradientBar.this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        });
    }

    private void addMouseDragListener() {
        this.addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (!pointIsMoving) {
                    index = -1;
                    for (int i = 0; i < selectColorPointBtnList.size(); i++) {
                        if (selectColorPointBtnList.get(i).contains(e.getX(), e.getY())) {
                            index = i;
                            pointIsMoving = true;
                            break;
                        }
                    }
                }
                boolean x = e.getX() <= max && e.getX() >= min;
                if (index != -1 && x && e.getY() < MAX_VERTICAL && e.getY() > 0) {
                    //如果该位置已经有滑块占领，则做跨越偏移
                    selectColorPointBtnList.get(index).setX(LegendGradientBar.this.setOffset(e.getX(), index, OFFSETSTEP));
                }

                LegendGradientBar.this.repaint();
            }
        });
    }

    //防止位置重复，设置偏移
    private int setOffset(int x, int index, int offset) {
        for (int i = 0; i < selectColorPointBtnList.size(); i++){
            if (i != index && AssistUtils.equals(x, selectColorPointBtnList.get(i).getX())) {
                if (x >= (min+max)/2) {
                    x -= offset;
                    x = setOffset(x, index, offset+OFFSETSTEP);
                }
                else{
                    x += offset;
                    x = setOffset(x, index, offset+OFFSETSTEP);
                }
            }
        }

        //边界情况
        if (x <= min){
            x = min + (index+1)*OFFSETSTEP;
        }
        else if (x >= max){
            x = max - (index+1)*OFFSETSTEP;
        }
        return x;
    }

    private void addMouseClickListener() {
        this.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getX() < max + MOUSE_OFFSET && e.getX() > 0) {
                    //是否选中滑块
                    int select = -1;
                    for (int i = 0; i < selectColorPointBtnList.size(); i++) {
                        if (selectColorPointBtnList.get(i).contains(e.getX(), e.getY())) {
                            select = i;
                            break;
                        }
                    }

                    if (select >= 0) {
                        ColorSelectDetailPane pane = new ColorSelectDetailPane(Color.WHITE);
                        ColorSelectDialog.showDialog(DesignerContext.getDesignerFrame(), pane, Color.WHITE, LegendGradientBar.this);
                        Color color = LegendGradientBar.this.getColor();
                        if (color != null) {
                            DesignerEnvManager.getEnvManager().getColorConfigManager().addToColorQueue(color);
                            selectColorPointBtnList.get(select).setColorInner(color);

                            LegendGradientBar.this.repaint();
                        }
                    }

                    //是否选中颜色编辑槽
                    else if (selectColorSlotBtnStart.contains(e.getX(), e.getY())) {
                        ColorSelectDetailPane pane = new ColorSelectDetailPane(Color.WHITE);
                        ColorSelectDialog.showDialog(DesignerContext.getDesignerFrame(), pane, Color.WHITE, LegendGradientBar.this);
                        Color color = LegendGradientBar.this.getColor();
                        if (color != null) {
                            DesignerEnvManager.getEnvManager().getColorConfigManager().addToColorQueue(color);
                            selectColorSlotBtnStart.setColorInner(color);
                            //stateChanged();
                            LegendGradientBar.this.repaint();
                        }
                    } else if (selectColorSlotBtnEnd.contains(e.getX(), e.getY())) {
                        ColorSelectDetailPane pane = new ColorSelectDetailPane(Color.WHITE);
                        ColorSelectDialog.showDialog(DesignerContext.getDesignerFrame(), pane, Color.WHITE, LegendGradientBar.this);
                        Color color = LegendGradientBar.this.getColor();
                        if (color != null) {
                            DesignerEnvManager.getEnvManager().getColorConfigManager().addToColorQueue(color);
                            selectColorSlotBtnEnd.setColorInner(color);
                            //stateChanged();
                            LegendGradientBar.this.repaint();
                        }
                    }

                    LegendGradientBar.this.repaint();
                }
            }
        });

    }

    private void addMouseReleasedListener(){
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                pointIsMoving = false;
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        Point2D start = new Point2D.Float(4, 0);
        Point2D end = new Point2D.Float(max, 0);

        //获取排序后的颜色数组和位置数组
        refreshColorsAndDist(selectColorPointBtnList);

        LinearGradientPaint paint = new LinearGradientPaint(start, end, dist, colors);
        g2.setPaint(paint);

        g2.fillRect(4, 0, max - 4, 30);
        g2.setColor(new Color(138,138,138));
        g2.drawRect(4, 0, max - 4, 30);

        //开始和结束位置颜色选择槽位
        selectColorSlotBtnStart.paint(g2);
        selectColorSlotBtnEnd.paint(g2);

        //颜色选择滑块
        for (int i = 0; i < selectColorPointBtnList.size(); i++) {
            selectColorPointBtnList.get(i).paint(g2);
        }
    }

    private void refreshColorsAndDist(List<SelectColorPointBtn> selectColorPointBtnList) {
        List<SelectColorPointBtn> select = new ArrayList<SelectColorPointBtn>(selectColorPointBtnList);
        Collections.sort(select);

        colors[0] = selectColorSlotBtnStart.getColorInner();
        for (int i = 0; i < colorSelectionBtnNum-2; i++) {
            colors[i+1] = select.get(i).getColorInner();
        }
        colors[colorSelectionBtnNum-1] = selectColorSlotBtnEnd.getColorInner();

        dist = new float[colorSelectionBtnNum];
        dist[0] = 0f;
        for (int i = 0; i < colorSelectionBtnNum-2; i++) {
            dist[i+1] = (float) ((select.get(i).getX() - 4) / (max - 4));
        }
        dist[colorSelectionBtnNum-1] = 1f;

        fireColorAndDistChangeListener();
    }

    public void refreshSubColor(Color subColor){

        //设置主题色
        setSubColor(subColor);

        //计算colors数组
        Color[] colors = initColors(subColor, colorSelectionBtnNum);

        initColorSelectBtn(colors, colorSelectionBtnNum);

    }

    public void refreshColorSelectionBtnNum(int num){
        //设置主题色
        colorSelectionBtnNum = num+1;

        //计算colors数组
        colors = initColors(subColor, colorSelectionBtnNum);

        initColorSelectBtn(colors, colorSelectionBtnNum);

        dist = initDist(colorSelectionBtnNum);

    }

    private Color[] getColorArray(Color color, int sum) {
        return ChartBaseUtils.createColorsWithHSB(color, sum);
    }

    public Color getSubColor() {
        return subColor;
    }

    public void setSubColor(Color subColor) {
        this.subColor = subColor;
    }

    public int getColorSelectionBtnNum() {
        return colorSelectionBtnNum;
    }

    public void setColorSelectionBtnNum(int colorSelectionBtnNum) {
        this.colorSelectionBtnNum = colorSelectionBtnNum;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public void colorSetted(ColorCell colorCell) {

    }

    public void populate(GradualIntervalConfig intervalConfig){
        if(intervalConfig == null) {
            return;
        }

        setSubColor(intervalConfig.getSubColor());

        setColorSelectionBtnNum((int)(intervalConfig.getDivStage() + 1));

        populateColorAndDist(intervalConfig.getGradualColorDistList());

        refreshColorSelectBtn(this.colors, this.dist);
    }

    private void populateColorAndDist(ArrayList<GradualColorDist> gradualColorDistList) {
        int num = gradualColorDistList.size();
        this.colors = new Color[num];
        this.dist = new float[num];
        for (int i = 0; i < num; i++){
            this.colors[i] = gradualColorDistList.get(i).getColor();
            this.dist[i] = gradualColorDistList.get(i).getPosition();
        }
    }

    public void update(GradualIntervalConfig intervalConfig){
        ArrayList<GradualColorDist> colorDistList = new ArrayList<GradualColorDist>();
        for (int i = 0; i < getColorSelectionBtnNum(); i++){
            colorDistList.add(new GradualColorDist(this.dist[i], this.colors[i]));
        }
        intervalConfig.setGradualColorDistList(colorDistList);
    }

    private void refreshColorSelectBtn(Color[] colors, float[] dist) {
        //获取按钮颜色数组
        Color startSlotColor = colors[0];
        Color endSlotColor = colors[colors.length-1];

        Color[] pColors = new Color[colors.length - 2];
        for (int i = 0; i < pColors.length; i++){
            pColors[i] = colors[i + 1];
        }

        float[] position = new float[dist.length - 2];
        for (int i = 0; i < position.length; i++){
            position[i] = dist[i+1]*(max-4) + 4;
        }

        selectColorSlotBtnStart = new SelectColorPointBtn(startPos, REC_HEIGHT, startSlotColor, new Color(138,138,138));
        selectColorSlotBtnEnd = new SelectColorPointBtn(endPos, REC_HEIGHT, endSlotColor, new Color(138,138,138));

        //刷新可移动按钮的位置
        selectColorPointBtnList = new ArrayList<SelectColorPointBtn>();

        //加入可滑动颜色选择器
        for (int i = 0; i < pColors.length; i++){
            SelectColorPointBtn pi = new SelectColorPointBtn(position[i], REC_HEIGHT, pColors[i]);
            selectColorPointBtnList.add(pi);
        }
        this.repaint();
    }

    @Override
    /**
     * 注册监听器
     * @param listener 监听器
     */
    public void registerChangeListener(UIObserverListener listener) {
        uiObserverListener = listener;
    }

    @Override
    public boolean shouldResponseChangeListener() {
        return true;
    }

    /**
     * 响应事件
     */
    public void fireColorAndDistChangeListener() {
        if (!changeListenerList.isEmpty()) {
            ChangeEvent evt = new ChangeEvent(this);
            for (int i = 0; i < changeListenerList.size(); i++) {
                this.changeListenerList.get(i).stateChanged(evt);
            }
        }
    }

}