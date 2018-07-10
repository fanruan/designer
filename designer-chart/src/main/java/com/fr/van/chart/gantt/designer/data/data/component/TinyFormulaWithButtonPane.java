package com.fr.van.chart.gantt.designer.data.data.component;

import com.fr.design.event.UIObserver;
import com.fr.design.event.UIObserverListener;
import com.fr.design.formula.TinyFormulaPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.mainframe.chart.gui.UIEditLabel;
import com.fr.general.IOUtils;

import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by hufan on 2017/1/11.
 */
public abstract class TinyFormulaWithButtonPane extends JPanel implements UIObserver{

    private UIEditLabel editLabel;
    private TinyFormulaPane tinyFormulaPane;

    protected UIObserverListener listener;

    public TinyFormulaWithButtonPane(String text) {
        this(text, "/com/fr/design/images/buttonicon/delete.png");
    }

    public TinyFormulaWithButtonPane(String text, String iconUrl) {
        editLabel = new UIEditLabel(text,SwingConstants.LEFT){
            protected void doAfterMousePress(){
                clearAllBackground();
            }

            @Override
            protected boolean appendOriginalLabel() {
                return false;
            }
        };
        editLabel.setPreferredSize(new Dimension(80,20));

        tinyFormulaPane = new TinyFormulaPane();

        UIButton button = new UIButton(IOUtils.readIcon(iconUrl));
        button.setPreferredSize(new Dimension(20, 20));
        button.addActionListener(initButtonListener(this));

        this.setLayout(new BorderLayout(4, 0));

        this.add(editLabel, BorderLayout.WEST);
        this.add(tinyFormulaPane, BorderLayout.CENTER);
        this.add(button, BorderLayout.EAST);
    }

    private ActionListener initButtonListener(final TinyFormulaWithButtonPane pane){
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buttonEvent(pane);
            }
        };
    }

    protected abstract void buttonEvent(TinyFormulaWithButtonPane pane);

    protected abstract void clearAllBackground();

    public void clearBackGround(){
        editLabel.resetNomalrBackground();
    }

    @Override
    public boolean shouldResponseChangeListener() {
        return true;
    }

    @Override
    public void registerChangeListener(UIObserverListener listener) {
        this.listener = listener;
        editLabel.registerChangeListener(listener);
    }

    public void populateFormula(Object o){
        if(o != null){
            tinyFormulaPane.populateBean(o.toString());
        }
    }

    public Object updateFormula(){
        return tinyFormulaPane.getUITextField().getText();
    }

    public String getHeaderName(){
        return editLabel.getText();
    }

    public void setHeaderName(String text){
        editLabel.setText(text);
    }
}
