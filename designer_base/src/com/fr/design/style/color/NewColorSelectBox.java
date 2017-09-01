package com.fr.design.style.color;

import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.fr.base.background.ColorBackground;
import com.fr.design.event.GlobalNameListener;
import com.fr.design.event.GlobalNameObserver;
import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.style.AbstractSelectBox;

/**
 * Color select pane.
 */
public class NewColorSelectBox extends AbstractSelectBox<Color> implements UIObserver, GlobalNameObserver {
    private static final long serialVersionUID = 2782150678943960557L;

    private Color color;
    private NewColorSelectPane colorPane = new NewColorSelectPane();
    private UIObserverListener uiObserverListener;
    private String newColorSelectBoxName = "";
    private GlobalNameListener globalNameListener = null;

    public NewColorSelectBox(int preferredWidth) {
    	initBox(preferredWidth);
        iniListener();
    }

    private void iniListener(){
        if(shouldResponseChangeListener()){
            this.addSelectChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    if(uiObserverListener == null){
                        return;
                    }
                    if (globalNameListener != null && shouldResponseNameListener()){
                        globalNameListener.setGlobalName(newColorSelectBoxName);
                    }
                    uiObserverListener.doChange();
                }
            });
        }
    }

    /**
     * 初始化下拉面板
     * @param preferredWidth 面板大小
     * @return 面板
     */
    public JPanel initWindowPane(double preferredWidth) {
    	// 下拉的时候重新生成面板，以刷新最近使用颜色
    	colorPane = new NewColorSelectPane();
    	colorPane.addChangeListener(new ChangeListener() {
    		public void stateChanged(ChangeEvent e) {
    			hidePopupMenu();
    			color = ((NewColorSelectPane)e.getSource()).getColor();
    			fireDisplayComponent(ColorBackground.getInstance(color));
    		}
    	});
    	return colorPane;
    }

    /**
     *
     * @return
     */
    public Color getSelectObject() {
        return this.color;
    }

    /**
     *
     * @param color
     */
    public void setSelectObject(Color color) {
    	this.color = color;
    	colorPane.setColor(color);
    	fireDisplayComponent(ColorBackground.getInstance(color));
    }

    @Override
    /**
     * 祖册监听
     * @param listener 监听
     */
    public void registerChangeListener(UIObserverListener listener) {
        uiObserverListener = listener;
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
    public void registerNameListener(GlobalNameListener listener) {
        globalNameListener = listener;
    }

    @Override
    public boolean shouldResponseNameListener() {
        return true;
    }

    @Override
    public void setGlobalName(String name) {
        newColorSelectBoxName = name;
    }
}