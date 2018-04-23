package com.fr.design.mainframe.widget.accessibles;

import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JToggleButton;

import com.fr.design.mainframe.DesignerContext;
import com.fr.design.mainframe.widget.editors.ColorTextField;
import com.fr.design.mainframe.widget.editors.ITextComponent;
import com.fr.design.mainframe.widget.wrappers.ColorWrapper;
import com.fr.design.style.color.ColorCell;
import com.fr.design.style.color.ColorSelectDetailPane;
import com.fr.design.style.color.ColorSelectDialog;
import com.fr.design.style.color.ColorSelectable;

public class AccessibleColorEditor extends BaseAccessibleEditor implements ColorSelectable {

    private ColorPalette palette;
    private Color defaultColor;
    
    // 选中颜色
    private Color choosedColor;

    public AccessibleColorEditor() {
        super(new ColorWrapper(), new ColorWrapper(), true);
    }

    public void setDefaultColor(Color c) {
        this.defaultColor = c;
    }

    @Override
    protected ITextComponent createTextField() {
        return new ColorTextField();
    }

    @Override
    protected void showEditorPane() {
        if (palette == null) {
            palette = new ColorPalette();
            palette.addDefaultAction(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    setResult(defaultColor);
                    palette.setVisible(false);
                }
            });
            palette.addCustomAction(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    chooseCustomColor();
                    palette.setVisible(false);
                }
            });
            palette.addColorAction(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    Color c = ((ColorIcon) ((JToggleButton) e.getSource()).getIcon()).getColor();
                    setResult(c);
                    palette.setVisible(false);
                }
            });
        }
        palette.setChoosedColor((Color) getValue());
        palette.show(this, 0, getHeight());
    }

    @Override
    protected boolean isComboButton() {
        return true;
    }

    private void chooseCustomColor() throws HeadlessException {
    	ColorSelectDetailPane pane = new ColorSelectDetailPane(Color.WHITE);
		ColorSelectDialog.showDialog(DesignerContext.getDesignerFrame(), pane, Color.WHITE, this);
        Color choosedColor = this.getColor();
        setResult(choosedColor);
    }

    private void setResult(Color choosedColor) {
        if (choosedColor != null) {
            setValue(choosedColor);
            fireStateChanged();
        }
    }

	@Override
	public void setColor(Color color) {
		this.choosedColor = color;
		
	}

	@Override
	public Color getColor() {
		return choosedColor;
	}

	@Override
	/**
	 * 不处理
	 * 
	 * @param colorCell 颜色单元格
	 * 
	 */
	public void colorSetted(ColorCell colorCell) {
		
	}
}