package com.fr.design.mainframe.widget.accessibles;

import com.fr.base.iofile.attr.WatermarkAttr;
import com.fr.design.dialog.BasicDialog;
import com.fr.design.dialog.DialogActionAdapter;
import com.fr.design.mainframe.widget.editors.ITextComponent;
import com.fr.design.mainframe.widget.renderer.WatermarkRenderer;
import com.fr.design.mainframe.widget.wrappers.WatermarkWrapper;
import com.fr.design.report.WatermarkPane;
import com.fr.design.report.WatermarkSettingPane;
import com.fr.intelli.record.Focus;
import com.fr.intelli.record.Original;
import com.fr.record.analyzer.EnableMetrics;

import javax.swing.*;
import java.awt.*;

/**
 * Created by plough on 2018/5/15.
 */

@EnableMetrics
public class AccessibleBodyWatermarkEditor extends UneditableAccessibleEditor {
    private WatermarkSettingPane watermarkPane;

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
            watermarkPane = new WatermarkSettingPane();
            watermarkPane.setPreferredSize(new Dimension(600, 400));
        }
        BasicDialog dlg = watermarkPane.showWindow(SwingUtilities.getWindowAncestor(this));
        dlg.addDialogActionListener(new DialogActionAdapter() {

            @Override
            public void doOk() {
                setValue(watermarkPane.update());
                fireStateChanged();
                recordFunction();
            }
        });
        watermarkPane.populate((WatermarkAttr) getValue());
        dlg.setVisible(true);
    }

    @Focus(id = "com.fr.watermark", text = "Fine-Design_Form_WaterMark", source = Original.EMBED)
    private void recordFunction() {
        // do nothing
    }
}