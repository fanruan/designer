package com.fr.design.mainframe.chart.gui;

import com.fr.base.ScreenResolution;
import com.fr.chart.base.GlyphUtils;
import com.fr.chart.base.TextAttr;
import com.fr.design.constants.UIConstants;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRFont;
import com.fr.stable.StringUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by IntelliJ IDEA.
 * Author : daisy
 * Version: 7.1.1
 */
public class UIEditLabel extends JPanel implements UIObserver{

    private static final int OFF_LEFT = 10;

    private UITextField currentEditingEditor = null;
    private UILabel showLabel;
    private boolean isEditingStopped = true;
    private String originalLabel = StringUtils.EMPTY;
    private DocumentListener documentListener = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            fireChange();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            fireChange();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            fireChange();
        }
    };

    private void fireChange(){
        setText(currentEditingEditor.getText());
        observerListener.doChange();
    }


    private AWTEventListener awt = new AWTEventListener() {
        public void eventDispatched(AWTEvent event) {
            if(!UIEditLabel.this.isShowing()){
                return;
            }
    		doSomeInAll(event);
    	}
    };

    private void doSomeInAll(AWTEvent event) {
        Rectangle bounds = new Rectangle(getLocationOnScreen().x, getLocationOnScreen().y, getWidth(), getHeight());
        if (event instanceof MouseEvent) {
    		MouseEvent mv = (MouseEvent) event;
    		if (mv.getClickCount() > 0) {
    			Point point = new Point((int) (mv.getLocationOnScreen().getX()) - 2 * OFF_LEFT, (int) mv.getLocationOnScreen().getY());
    			// 判断鼠标点击是否在边界内
    			if (!bounds.contains(point)) {
                    if (!isEditingStopped) {
                        stopEditing();
                    }
    			}
    		}
    	}
    }

    private UIObserverListener observerListener;

    public UIEditLabel(String label,int horizontalAlignment){
        initComponents();
        showLabel.setHorizontalAlignment(horizontalAlignment);
        this.originalLabel = label;
        setText(label);
    }

    public UIEditLabel(int horizontalAlignment){
        initComponents();
        showLabel.setHorizontalAlignment(horizontalAlignment);
    }

    private void initComponents(){
        showLabel = new UILabel();
        currentEditingEditor = new UITextField();
        this.setLayout(new BorderLayout());
        this.add(showLabel, BorderLayout.CENTER);
        Toolkit.getDefaultToolkit().addAWTEventListener(awt, AWTEvent.MOUSE_EVENT_MASK);
        showLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                doAfterMousePress();
                isEditingStopped = false;
                UIEditLabel.this.removeAll();
                currentEditingEditor.getDocument().removeDocumentListener(documentListener);
                currentEditingEditor.setText(getText());
                currentEditingEditor.getDocument().addDocumentListener(documentListener);
                UIEditLabel.this.add(currentEditingEditor,BorderLayout.CENTER);
                UIEditLabel.this.setBackground(UIConstants.FLESH_BLUE);
                UIEditLabel.this.revalidate();
            }

            /**
             * {@inheritDoc}
             */
            public void mouseEntered(MouseEvent e) {
                showLabel.setToolTipText(getTooltip());
                UIEditLabel.this.setBackground(UIConstants.FLESH_BLUE);
            }

            /**
             * {@inheritDoc}
             */
            public void mouseExited(MouseEvent e) {
                showLabel.setToolTipText(getTooltip());
                UIEditLabel.this.setBackground(UIConstants.NORMAL_BACKGROUND);
            }

        });
        currentEditingEditor.addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()==KeyEvent.VK_ENTER){
                    stopEditing();
                }
            }
        });
        currentEditingEditor.getDocument().addDocumentListener(documentListener);
    }

    protected void doAfterMousePress(){

    }

    /**
     * 设置成正常的背景
     */
    public void resetNomalrBackground(){
        UIEditLabel.this.setBackground(UIConstants.NORMAL_BACKGROUND);
    }

    private String getTooltip(){
        String text = showLabel.getText();
        double width = GlyphUtils.calculateTextDimensionWithNoRotation(text, new TextAttr(FRFont.getInstance()), ScreenResolution.getScreenResolution()).getWidth();
        if(width<=showLabel.getWidth()){
            return null;
        }
        return text;
    }

    private void stopEditing(){
        isEditingStopped = true;
        this.removeAll();
        String text =this.showLabel.getText();
        text = StringUtils.cutStringEndWith(text,":");
        text = !ComparatorUtils.equals(text, this.originalLabel) ?
                StringUtils.perfectEnd(text, "(" + this.originalLabel + ")"): this.originalLabel;
        showLabel.setText(StringUtils.perfectEnd(text,":"));
        this.add(showLabel, BorderLayout.CENTER);
        this.revalidate();
        this.repaint();
    }

    public String getText(){
        String text =this.showLabel.getText();
        text = StringUtils.cutStringEndWith(text,":");
        return StringUtils.cutStringEndWith(text,"("+this.originalLabel+")");
    }

    public void setText(String text){
        if(text == null || StringUtils.isEmpty(text)){
            this.showLabel.setText(this.originalLabel);
            return;
        }
        if(!ComparatorUtils.equals(text,originalLabel)){
            text = StringUtils.cutStringEndWith(text, ":");
            text = StringUtils.perfectEnd(text,"("+originalLabel+")");
        }
        text = StringUtils.perfectEnd(text,":");
        this.showLabel.setText(text);
    }

    /**
	 * 给组件登记一个观察者监听事件
	 *
	 * @param listener 观察者监听事件
	 */
    public void registerChangeListener(UIObserverListener listener) {
        this.observerListener = listener;
    }

    /**
	 * 组件是否需要响应添加的观察者事件
	 *
	 * @return 如果需要响应观察者事件则返回true，否则返回false
	 */
    public boolean shouldResponseChangeListener() {
        return true;
    }
}