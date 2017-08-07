package com.fr.design.widget.ui.designer.component;

import com.fr.design.dialog.BasicPane;
import com.fr.design.gui.ibutton.UIHeadGroup;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.widget.accessibles.AccessibleBackgroundEditor;
import com.fr.general.Background;
import com.fr.general.Inter;

import javax.swing.*;
import java.awt.*;

/**
 * Created by ibm on 2017/8/6.
 */
public class BackgroundCompPane extends BasicPane {
    private UIHeadGroup backgroundHead;
    private AccessibleBackgroundEditor initalBackgroundEditor;
    private AccessibleBackgroundEditor overBackgroundEditor;
    private AccessibleBackgroundEditor clickBackgroundEditor;


    public BackgroundCompPane() {
        initComponent();
    }

    public void initComponent() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        initalBackgroundEditor = new AccessibleBackgroundEditor();
        overBackgroundEditor = new AccessibleBackgroundEditor();
        clickBackgroundEditor = new AccessibleBackgroundEditor();
        String [] titles = new String[]{Inter.getLocText("FR-Designer_DEFAULT"), Inter.getLocText("FR-Designer_Custom")};

        double f = TableLayout.FILL;
        final double p = TableLayout.PREFERRED;
        double[] rowSize = {p, p, p};
        double[] columnSize = {p, f};
        int[][] rowCount = {{1, 1},{1, 1},{1, 1}};
        Component[][] components = new Component[][]{
                new Component[]{new UILabel(Inter.getLocText("FR-Designer_Background-Initial")), initalBackgroundEditor},
                new Component[]{new UILabel(Inter.getLocText("FR-Designer_Background-Over")), overBackgroundEditor},
                new Component[]{new UILabel(Inter.getLocText("FR-Designer_Background-Click")), clickBackgroundEditor},
        };
        final JPanel panel = TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, 7, 7);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        backgroundHead = new UIHeadGroup(titles){
            @Override
            public void tabChanged(int index) {
                //todo
                if (index == 1) {
                    panel.setVisible(true);
                }else{
                    panel.setVisible(false);
                }
            }
        };
        this.add(backgroundHead, BorderLayout.NORTH);
        this.add(panel, BorderLayout.CENTER);

    }

    public MouseActionBackground update() {
        MouseActionBackground mouseActionBackground = new MouseActionBackground((Background) initalBackgroundEditor.getValue(), (Background) overBackgroundEditor.getValue(), (Background) clickBackgroundEditor.getValue());
        return mouseActionBackground;
    }

    protected String title4PopupWindow() {
        return "";
    }

    public void populate(MouseActionBackground background) {
        initalBackgroundEditor.setValue(background.getInitialBackground());
        overBackgroundEditor.setValue(background.getOverBackground());
        clickBackgroundEditor.setValue(background.getClickBackground());
    }
}
