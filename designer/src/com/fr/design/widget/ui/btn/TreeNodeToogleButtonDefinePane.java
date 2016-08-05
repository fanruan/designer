package com.fr.design.widget.ui.btn;

import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.form.ui.Button;
import com.fr.general.Inter;
import com.fr.report.web.button.form.TreeNodeToggleButton;
import com.fr.design.widget.btn.ButtonDetailPane;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6
 * Date   : 11-11-15
 * Time   : 下午7:45
 */
public class TreeNodeToogleButtonDefinePane<T extends TreeNodeToggleButton> extends ButtonDetailPane<Button> {

    public TreeNodeToogleButtonDefinePane() {
        initComponents();
    }

    protected void initComponents() {
        setLayout(FRGUIPaneFactory.createBorderLayout());
        double p = TableLayout.PREFERRED;
        double rowSize[] = {p};
        double columnSize[] = {p, 184};
        Component[][] n_components = {
                {new UILabel(Inter.getLocText(new String[]{"Form-Button", "Type"}) + ":"), createButtonTypeComboBox()}
        };
        JPanel northPane = TableLayoutHelper.createTableLayoutPane(n_components, rowSize, columnSize);
        JPanel advancedPane = FRGUIPaneFactory.createTitledBorderPane(Inter.getLocText("FR-Designer_Advanced"));
        advancedPane.add(northPane);
        add(advancedPane, BorderLayout.CENTER);
    }

    @Override
    public TreeNodeToggleButton update() {
        return createButton();
    }

    @Override
    public TreeNodeToggleButton createButton() {
        return new TreeNodeToggleButton();
    }

    @Override
    public Class classType() {
        return TreeNodeToggleButton.class;
    }
}