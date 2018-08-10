package com.fr.design.mainframe;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.fr.design.constants.UIConstants;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;

import com.fr.main.impl.WorkBook;

/**
 * Created by IntelliJ IDEA.
 * Author : Richer
 * Version: 6.5.6
 * Date: 13-12-16
 * Time: 上午10:09
 */
public class AuthoritySheetEditedPane extends AuthorityPropertyPane {

    private static final int TITLE_HEIGHT = 19;
    private AuthorityEditPane authorityEditPane = null;


    public AuthoritySheetEditedPane(WorkBook editingWorkBook, int selectedIndex) {
        super(HistoryTemplateListPane.getInstance().getCurrentEditingTemplate());
        this.setLayout(new BorderLayout());
        this.setBorder(null);
//        UILabel authorityTitle = new UILabel(com.fr.design.i18n.Toolkit.i18nTextArray(new String[]{"DashBoard-Potence", "Edit"})) {
        UILabel authorityTitle = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Edit_DashBoard_Potence")) {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(super.getPreferredSize().width, TITLE_HEIGHT);
            }
        };
        authorityTitle.setHorizontalAlignment(SwingConstants.CENTER);
        authorityTitle.setVerticalAlignment(SwingConstants.CENTER);
        JPanel northPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
        northPane.add(authorityTitle, BorderLayout.CENTER);
        northPane.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UIConstants.LINE_COLOR));
//        this.add(northPane, BorderLayout.NORTH);
        authorityEditPane = new SheetAuthorityEditPane(editingWorkBook, selectedIndex);
        this.add(authorityEditPane, BorderLayout.CENTER);

    }

    public void populate() {
        authorityEditPane.populateDetials();
    }
}