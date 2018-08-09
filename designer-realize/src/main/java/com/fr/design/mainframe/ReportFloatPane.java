package com.fr.design.mainframe;

import com.fr.design.actions.UpdateAction;
import com.fr.design.actions.core.ActionFactory;
import com.fr.design.constants.UIConstants;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.itoolbar.UIToolbar;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.menu.KeySetUtils;
import com.fr.design.menu.MenuDef;


import javax.swing.*;
import java.awt.*;

/**
 * 悬浮元素
 * Created by MoMeak on 2017/7/27.
 */
public class ReportFloatPane extends JPanel {

    private static ReportFloatPane THIS;
    private MenuDef insertFloatMenu;

    private ReportFloatPane() {
        initComponent();
    }

    public synchronized static final ReportFloatPane getInstance() {
        if (THIS == null) {
            THIS = new ReportFloatPane();
        }
        return THIS;
    }

    public void refreshInsertFloatMenu(ElementCasePaneDelegate elementCasePaneDelegate) {
        insertFloatMenu.clearShortCuts();
        UpdateAction[] actions = ActionFactory.createFloatInsertAction(ElementCasePane.class, elementCasePaneDelegate);
        for (int i = 0; i < actions.length; i++) {
            insertFloatMenu.addShortCut(actions[i]);
        }
    }

    private void initComponent() {
        this.setLayout(new BorderLayout());

        UIToolbar topToolBar = new UIToolbar();
        topToolBar.setLayout(new BorderLayout());
        initInsertToolBar();
        topToolBar.setPreferredSize(new Dimension(155,20));
        topToolBar.add(createButtonUI(), BorderLayout.CENTER);
        topToolBar.setBorder(BorderFactory.createEmptyBorder(-1, -1, -1, -1));

        JPanel toolBarPane = new JPanel(new BorderLayout());
        toolBarPane.add(topToolBar, BorderLayout.CENTER);
        toolBarPane.setBorder(BorderFactory.createLineBorder(UIConstants.POP_DIALOG_BORDER));
        toolBarPane.setPreferredSize(new Dimension(155,20));
        UILabel emptyLabel = new UILabel();
        emptyLabel.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 0));

        double p = TableLayout.PREFERRED;
        double f = TableLayout.FILL;
        double[] columnSize = {p, p, p, f};
        double[] rowSize = {p};
        Component[][] components = new Component[][]{
                new Component[]{new UILabel(), new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Add_FloatElement")), emptyLabel, toolBarPane},
        };
        JPanel leftTopPane = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
        leftTopPane.setBorder(BorderFactory.createEmptyBorder(12, 5, 0, 15));
        this.add(leftTopPane, BorderLayout.NORTH);
    }

    private void initInsertToolBar() {
        insertFloatMenu = new MenuDef(true);
        insertFloatMenu.setName(KeySetUtils.INSERT_FLOAT.getMenuKeySetName());
        insertFloatMenu.setTooltip(com.fr.design.i18n.Toolkit.i18nText("FR-Designer_T_Insert_Float"));
        insertFloatMenu.setIconPath("com/fr/design/images/control/addPopup.png");
        JTemplate editingTemplate = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
        JComponent currentElementCasePane = editingTemplate.getCurrentElementCasePane();
        if (currentElementCasePane != null) {
            insertFloatMenu.clearShortCuts();
            UpdateAction[] actions = ActionFactory.createFloatInsertAction(ElementCasePane.class, currentElementCasePane);
            for (int i = 0; i < actions.length; i++) {
                insertFloatMenu.addShortCut(actions[i]);
            }
        }
    }

    private UIButton createButtonUI() {
        UIButton createdButton = insertFloatMenu.createUIButton();
        // 此按钮单独抽出，不应使用工具栏外观
        if (!createdButton.isOpaque()) {
            createdButton.setOpaque(true);
            createdButton.setNormalPainted(true);
            createdButton.setBorderPaintedOnlyWhenPressed(false);
        }
        return createdButton;
    }


    public static void main(String[] args) {
//        JFrame jf = new JFrame("test");
//        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        JPanel content = (JPanel) jf.getContentPane();
//        content.setLayout(new BorderLayout());
//        content.add(ReportFloatPane.getInstance(), BorderLayout.CENTER);
//        GUICoreUtils.centerWindow(jf);
//        jf.setSize(250, 400);
//        jf.setVisible(true);
    }


}
