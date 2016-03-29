package com.fr.design.mainframe.widget.editors;

import java.awt.event.ActionListener;

/**
 * 具有显示值和实际值的显示器
 * 
 * @since 6.5.2
 */
public interface ITextComponent {

    public void setText(String text);

    public String getText();

    public void setEditable(boolean editable);

    public void addActionListener(ActionListener l);

    public void selectAll();

    public void setValue(Object v);
}