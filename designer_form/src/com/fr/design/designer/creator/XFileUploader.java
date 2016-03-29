/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.designer.creator;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.beans.IntrospectionException;

import javax.swing.JComponent;

import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.itextfield.UITextField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.form.ui.FileEditor;
import com.fr.design.form.util.XCreatorConstants;
import com.fr.general.Inter;
import com.fr.stable.ArrayUtils;

/**
 * @author richer
 * @since 6.5.3
 */
public class XFileUploader extends XFieldEditor {

    public XFileUploader(FileEditor widget, Dimension initSize) {
        super(widget, initSize);
    }

    @Override
    public CRPropertyDescriptor[] supportedDescriptor() throws IntrospectionException {
        return (CRPropertyDescriptor[]) ArrayUtils.addAll(
            super.supportedDescriptor(),
            new CRPropertyDescriptor[]{
                new CRPropertyDescriptor("allowTypes", this.data.getClass())
                    .setI18NName(Inter.getLocText("File-Allow_Upload_Files"))
                    .putKeyValue(XCreatorConstants.PROPERTY_CATEGORY, "Advanced")
            });
    }

    @Override
    protected JComponent initEditor() {
        if (editor == null) {
            editor = FRGUIPaneFactory.createBorderLayout_S_Pane();
            UITextField textField = new UITextField(10);
            editor.add(textField, BorderLayout.CENTER);
            UIButton btn = new UIButton("...");
            btn.setPreferredSize(new Dimension(24, 24));
            editor.add(btn, BorderLayout.EAST);
        }
        return editor;
    }

    @Override
    protected String getIconName() {
        return "file_up.png";
    }
}