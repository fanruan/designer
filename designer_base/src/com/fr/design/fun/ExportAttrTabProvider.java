package com.fr.design.fun;

import com.fr.design.report.AbstractExportPane;
import com.fr.stable.fun.Level;

import javax.swing.*;

/**
 * Created by vito on 16/5/5.
 */
public interface ExportAttrTabProvider extends Level{
    String XML_TAG = "ExportAttrTabProvider";

    int CURRENT_LEVEL = 1;

    JComponent toSwingComponent();

    AbstractExportPane toExportPane();

    String title();

    String tag();
}
