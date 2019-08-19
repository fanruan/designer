package com.fr.design.designer.creator;

import com.fr.base.BaseFormula;
import com.fr.design.border.UIRoundedBorder;
import com.fr.form.ui.AbstractBorderStyleWidget;
import com.fr.form.ui.Label;
import com.fr.form.ui.LayoutBorderStyle;
import com.fr.form.ui.PaddingMargin;
import com.fr.form.ui.Widget;
import com.fr.form.ui.WidgetTitle;
import com.fr.form.ui.WidgetValue;
import com.fr.form.ui.container.WTitleLayout;
import com.fr.general.ComparatorUtils;
import com.fr.general.act.BorderPacker;
import com.fr.general.act.TitlePacker;
import com.fr.stable.Constants;
import com.fr.stable.StringUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: zx
 * Date: 14-9-22
 * Time: 上午10:40
 */

public class XBorderStyleWidgetCreator extends XWidgetCreator{
	protected static final Dimension BORDER_PREFERRED_SIZE = new Dimension(250, 150);

    public XBorderStyleWidgetCreator(Widget widget, Dimension initSize) {
        super(widget, initSize);
    }
    
    /**
	 * 返回容器对应的widget
	 * @return 同上
	 */
	@Override
    public AbstractBorderStyleWidget toData() {
        return (AbstractBorderStyleWidget) data;
    }
    
     protected void initStyle() {
    	BorderPacker style = toData().getBorderStyle();
     	initBorderStyle();
        if (ComparatorUtils.equals(style.getType(), LayoutBorderStyle.TITLE)) {
          	initTitleStyle(style);
        } else {
        	clearTitleWidget();
         }
     }

  // 边框默认值设为NONE,不然像scalelayout这种只用默认边框的会不显示边框
    protected void initBorderStyle() {
    	BorderPacker style = toData().getBorderStyle();
        if (style != null && style.getBorder() != Constants.LINE_NONE) {
            this.setBorder(new UIRoundedBorder(style.getBorder(), style.getColor(), style.getBorderRadius()));
        } else {
            this.setBorder(DEFALUTBORDER);
        }
    }
    
    private void clearTitleWidget() {
    	if (acceptType(XWFitLayout.class)) {
    		return;
    	}
    	XWTitleLayout parent = (XWTitleLayout) this.getParent();
    	if (parent.getComponentCount() > 1) {
    		parent.remove(parent.getTitleCreator());
    	}
    }
    
	 /**
     * 设置样式为标题样式时，对应组件加上标题
     * @param style 样式
     */
    protected void initTitleStyle(BorderPacker style){
    	if (style.getTitle() == null || style.getTitle().getTextObject() == null) {
    		return;
    	}
    	XWTitleLayout parent = (XWTitleLayout) this.getParent();
    	if (parent.getComponentCount() > 1) {
    		XLabel title = (XLabel) parent.getTitleCreator();
    		Label widget = title.toData();
    		updateTitleWidgetStyle(widget, style);
    		title.initXCreatorProperties();
    		return;
    	} 
    	// 初始化标题控件
    	XLabel title = new XLabel(new Label(), new Dimension());
    	Label label =  title.toData();
    	updateTitleWidgetStyle(label, style);
    	parent.add(title, WTitleLayout.TITLE);
    	// 初始化标题边框
    	title.initXCreatorProperties();
    	WTitleLayout layout = parent.toData();
    	layout.updateChildBounds(layout.getBodyBoundsWidget().getBounds());
    }
    
    /**
     * 更新标题控件所有的样式
     */
    private void updateTitleWidgetStyle(Label title, BorderPacker style) {
    	//标题的边框样式目前是取对应的控件的边框样式
    	title.setBorder(style.getBorder());
    	title.setColor(style.getColor());
//    	title.setCorner(style.isCorner());

    	TitlePacker wTitle = style.getTitle();
    	//设置成随机不重复的, 不然都用一个名字的话, 联动只能联动一个
    	title.setWidgetName(WidgetTitle.TITLE_NAME_INDEX + this.toData().getWidgetName());
    	title.setWidgetValue(getTitleValue(wTitle));
    	title.setFont(wTitle.getFrFont());
    	title.setTextalign(wTitle.getPosition());
    	title.setBackground(wTitle.getBackground());
    }
    
    private WidgetValue getTitleValue(TitlePacker wTitle){
    	String content = String.valueOf(wTitle.getTextObject());
    	Object value = content.startsWith("=") ? BaseFormula.createFormulaBuilder().build(content) : content;
    	return new WidgetValue(value);
    }

    @Override
    protected String getIconName() {
        return StringUtils.EMPTY;
    }
    @Override
    protected JComponent initEditor() {
        return this;
    }
    
    /**
     * 内边距
     * @return 同上
     */
    @Override
    public Insets getInsets() {
        PaddingMargin padding = toData().getMargin();
        if (padding == null) {
        	return new Insets(0, 0, 0, 0);
        }
		return new Insets(padding.getTop(), padding.getLeft(), padding.getBottom(), padding.getRight());
    }

	/**
	 * data属性改变触发其他操作
	 *
	 */
	public void firePropertyChange(){

	}
    
}