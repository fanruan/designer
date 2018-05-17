package com.fr.design.mainframe.widget.accessibles;

import com.fr.base.iofileattr.WatermarkAttr;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.mainframe.widget.editors.ITextComponent;
import com.fr.design.mainframe.widget.renderer.WatermarkRenderer;
import com.fr.design.mainframe.widget.wrappers.WatermarkWrapper;
import com.fr.design.report.WatermarkPane;

import javax.swing.SwingUtilities;
import java.awt.Dimension;

/**
 * Created by plough on 2018/5/15.
 */

public class AccessibleBodyWatermarkEditor extends UneditableAccessibleEditor {
    private WatermarkPane watermarkPane;

    public AccessibleBodyWatermarkEditor() {
        super(new WatermarkWrapper());
    }

    @Override
    protected ITextComponent createTextField() {
        return new RendererField(new WatermarkRenderer());
    }

    @Override
    protected void showEditorPane() {
        if (watermarkPane == null) {
            watermarkPane = new WatermarkPane();
            watermarkPane.setPreferredSize(new Dimension(600, 400));
        }
        BasicDialog dlg = watermarkPane.showWindow(SwingUtilities.getWindowAncestor(this));
        dlg.addDialogActionListener(new DialogActionAdapter() {

            @Override
            public void doOk() {
                setValue(watermarkPane.update());
                fireStateChanged();
            }
        });
        watermarkPane.populate((WatermarkAttr) getValue());
        dlg.setVisible(true);
    }
}