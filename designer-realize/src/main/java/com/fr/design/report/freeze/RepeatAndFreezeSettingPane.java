package com.fr.design.report.freeze;

import com.fr.base.FRContext;
import com.fr.design.dialog.BasicPane;
import com.fr.design.extra.WebViewDlgHelper;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.ActionLabel;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;

import com.fr.page.ReportPageAttrProvider;
import com.fr.stable.ColumnRow;
import com.fr.stable.FT;
import com.fr.stable.bridge.StableFactory;
import com.fr.workspace.WorkContext;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Sets Report Page Attributes
 */
public class RepeatAndFreezeSettingPane extends BasicPane {

    //边框高度
    private static final int LABEL_HEIGHT = 45;

    // 重复标题行
    private RepeatRowPane repeatTitleRowPane;
    // 重复标题列
    private RepeatColPane repeatTitleColPane;
    // 重复结尾行
    private RepeatRowPane repeatFinisRowPane;
    // 重复结尾列
    private RepeatColPane repeatFinisColPane;
    // 分页冻结行
    private FreezePagePane freezePageRowPane;
    // 分页冻结列
    private FreezePagePane freezePageColPane;
    // 填报冻结行
    private FreezeWriteRowPane freezeWriteRowPane;
    // 填报冻结列
    private FreezeWriteColPane freezeWriteColPane;
    // 重复标题
    private UICheckBox useRepeatTitleRCheckBox;
    private UICheckBox useRepeatTitleCCheckBox;
    // 重复结尾
    private UICheckBox useRepeatFinisRCheckBox;
    private UICheckBox useRepeatFinisCCheckBox;

    // 分页冻结
    private UICheckBox usePageFrozenCCheckBox;
    private UICheckBox usePageFrozenRCheckBox;
    // 填报冻结
    private UICheckBox useWriteFrozenCCheckBox;
    private UICheckBox useWriteFrozenRCheckBox;

    /**
     * 重复标题行
     */
    private JPanel initRowStartPane() {
        JPanel soverlapRowStartPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        useRepeatTitleRCheckBox = new UICheckBox();
        soverlapRowStartPane.add(useRepeatTitleRCheckBox);
        soverlapRowStartPane.add(new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Title_Start_Row_Form")));
        repeatTitleRowPane = new RepeatRowPane();
        soverlapRowStartPane.add(repeatTitleRowPane);
        soverlapRowStartPane.add(new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Row")));

        return soverlapRowStartPane;
    }

    /**
     * 重复标题列
     */
    private JPanel initColStartPane() {
        // 重复打印标题的起始列
        JPanel soverlapColStartPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        useRepeatTitleCCheckBox = new UICheckBox();
        soverlapColStartPane.add(useRepeatTitleCCheckBox);
        soverlapColStartPane.add(new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Title_Start_Column_Form")));
        // 重复打印标题的结束列
        repeatTitleColPane = new RepeatColPane();
        soverlapColStartPane.add(repeatTitleColPane);
        soverlapColStartPane.add(new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Column")));

        return soverlapColStartPane;
    }

    /**
     * 重复结尾行
     */
    private JPanel initFootRowStarPane() {
        // 重复打印结尾的起始行
        JPanel foverlapRowStartPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        useRepeatFinisRCheckBox = new UICheckBox();
        foverlapRowStartPane.add(useRepeatFinisRCheckBox);
        foverlapRowStartPane.add(new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Finis_Start_Row_Form")));
        repeatFinisRowPane = new RepeatRowPane();
        foverlapRowStartPane.add(repeatFinisRowPane);
        foverlapRowStartPane.add(new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Row")));

        return foverlapRowStartPane;
    }

    /**
     * 重复结尾列
     */
    private JPanel initFootColStartPane() {
        // 重复打印结尾的起始列
        JPanel foverlapColStartPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        useRepeatFinisCCheckBox = new UICheckBox();
        foverlapColStartPane.add(useRepeatFinisCCheckBox);
        foverlapColStartPane.add(new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Finis_Start_Column_Form")));
        repeatFinisColPane = new RepeatColPane();
        foverlapColStartPane.add(repeatFinisColPane);
        foverlapColStartPane.add(new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Column")));

        return foverlapColStartPane;
    }

    /**
     * 获取分页冻结的标题(表单中不需要写分页二字)
     *
     * @return 分页冻结的标题
     * @date 2014-11-14-下午1:32:08
     */
    protected String getPageFrozenTitle() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Engine_Page-Frozen");
    }

    /**
     * 分页冻结Pane
     */
    private JPanel initPageFrozenPane() {
        JPanel pagePanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
        // 分页冻结
        UILabel pageLabel = new UILabel(getPageFrozenTitle());
        JPanel pageLabelPanel = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        pageLabelPanel.add(pageLabel);
        pagePanel.add(pageLabelPanel, BorderLayout.NORTH);
        JPanel pagecon = FRGUIPaneFactory.createNColumnGridInnerContainer_S_Pane(1);
        pagecon.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        pagePanel.add(pagecon, BorderLayout.CENTER);
        UILabel warningx = new UILabel("(" + com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Engine_Please_Set_Repeat_First") + ")");
        warningx.setForeground(Color.red);
        pageLabelPanel.add(warningx);

        JPanel pageRowGridPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        pageRowGridPane.add(this.usePageFrozenRCheckBox = new UICheckBox());
        pagecon.add(pageRowGridPane);
        // 显示行冻结信息的panel
        freezePageRowPane = new FreezePagePane(true);
        pageRowGridPane.add(freezePageRowPane);
        addPageFrozenCol(pagecon);

        return pagePanel;
    }

    /**
     * 分页冻结列
     */
    private void addPageFrozenCol(JPanel pagecon) {
        // 显示列冻结信息的panel
        freezePageColPane = new FreezePagePane(false);
        JPanel pageColGridPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        pageColGridPane.add(this.usePageFrozenCCheckBox = new UICheckBox());
        pagecon.add(pageColGridPane);
        pageColGridPane.add(freezePageColPane);
    }

    /**
     * 填报冻结Pane
     */
    private JPanel initWriteFrozenPane() {
        JPanel writePanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
        // 填报冻结
        UILabel writeLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Face_Write_Frozen") + ":");
        JPanel writeLabelPanel = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        writeLabelPanel.add(writeLabel);
        writePanel.add(writeLabelPanel, BorderLayout.NORTH);
        JPanel writecon = FRGUIPaneFactory.createNColumnGridInnerContainer_S_Pane(1);
        writecon.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        writePanel.add(writecon, BorderLayout.CENTER);

        // 行冻结
        JPanel writeRowPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        writeRowPane.add(this.useWriteFrozenRCheckBox = new UICheckBox());
        // 列冻结
        JPanel writeColPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        writeColPane.add(this.useWriteFrozenCCheckBox = new UICheckBox());
        writecon.add(writeRowPane);
        freezeWriteRowPane = new FreezeWriteRowPane();
        writeRowPane.add(freezeWriteRowPane);
        writecon.add(writeColPane);
        freezeWriteColPane = new FreezeWriteColPane();
        writeColPane.add(freezeWriteColPane);

        return writePanel;
    }

    public RepeatAndFreezeSettingPane() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        JPanel outrepeatPanel = FRGUIPaneFactory.createTitledBorderPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Repeat"));
        JPanel cenrepeatPanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
        outrepeatPanel.add(cenrepeatPanel);
        JPanel outfreezePanel = FRGUIPaneFactory.createTitledBorderPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Engine_Frozen"));
        this.add(outrepeatPanel, BorderLayout.NORTH);
        this.add(outfreezePanel, BorderLayout.CENTER);
        JPanel repeatPanel = FRGUIPaneFactory.createNColumnGridInnerContainer_S_Pane(1);
        repeatPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        JPanel freezePanel = FRGUIPaneFactory.createBorderLayout_S_Pane();

        //自适应插件
/*        if (shouldShowTip()) {
            JPanel infoPane = FRGUIPaneFactory.createTitledBorderPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Attention"));
            BoxCenterAligmentPane actionLabel = getURLActionLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Form_Fit_Tip"));
            infoPane.add(actionLabel, BorderLayout.SOUTH);
            this.add(infoPane, BorderLayout.SOUTH);
        }*/
        outfreezePanel.add(freezePanel);
        // 重复打印部分
        // 重复打印标题的起始行
        JPanel labelPanel = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
        labelPanel.add(new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Engine_Select_the_repeated_row_and_column") + ":"));
        UILabel warning = new UILabel("(" + com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Engine_FreezeWarning") + ")");
        warning.setForeground(Color.red);
        labelPanel.add(warning);
        cenrepeatPanel.add(labelPanel, BorderLayout.NORTH);
        cenrepeatPanel.add(repeatPanel, BorderLayout.CENTER);

        repeatPanel.add(initRowStartPane());
        addColStart(repeatPanel);
        repeatPanel.add(initFootRowStarPane());
        addFootColStart(repeatPanel);

        freezePanel.add(initPageFrozenPane(), BorderLayout.NORTH);
        addWriteFrozen(freezePanel);

        initPageRwoListener();
        initPageColListener();
        initWriteListener();
    }

    private boolean shouldShowTip() {
    
        return WorkContext.getCurrent().isLocal() && FRContext.isChineseEnv();
    }

    protected void initWriteListener() {
        // 填报重复冻结行
        useWriteFrozenCCheckBox.addChangeListener(useWriteFrozenCListener);
        // 填报重复冻结列
        useWriteFrozenRCheckBox.addChangeListener(useWriteFrozenRListener);
    }

    private void initPageRwoListener() {
        repeatTitleRowPane.addListener(freezePageRowListener);
        // 分页重复冻结行
        usePageFrozenRCheckBox.addChangeListener(usePageFrozenRListener);
        //重复标题行
        useRepeatTitleRCheckBox.addChangeListener(useRepeatTitleRListener);
        //重复结尾行
        useRepeatFinisRCheckBox.addChangeListener(useRepeatFinisRListener);
    }

    protected void initPageColListener() {
        repeatTitleColPane.addListener(freezePageColListener);
        // 分页重复冻结列
        usePageFrozenCCheckBox.addChangeListener(usePageFrozenCListener);
        //重复标题列
        useRepeatTitleCCheckBox.addChangeListener(useRepeatTitleCListener);
        //重复结尾列
        useRepeatFinisCCheckBox.addChangeListener(useRepeatFinisCListener);
    }

    protected void addWriteFrozen(JPanel freezePanel) {
        freezePanel.add(initWriteFrozenPane(), BorderLayout.CENTER);
    }

    protected void addFootColStart(JPanel repeatPanel) {
        repeatPanel.add(initFootColStartPane());
    }

    protected void addColStart(JPanel repeatPanel) {
        repeatPanel.add(initColStartPane());
    }

    ChangeListener useRepeatFinisCListener = new ChangeListener() {

        @Override
        public void stateChanged(ChangeEvent e) {
            repeatFinisColPane.setEnabled(useRepeatFinisCCheckBox.isSelected());

        }
    };

    ChangeListener useRepeatFinisRListener = new ChangeListener() {

        @Override
        public void stateChanged(ChangeEvent e) {
            repeatFinisRowPane.setEnabled(useRepeatFinisRCheckBox.isSelected());

        }
    };

    ChangeListener useRepeatTitleCListener = new ChangeListener() {

        @Override
        public void stateChanged(ChangeEvent e) {
            boolean flag = useRepeatTitleCCheckBox.isSelected();
            repeatTitleColPane.setEnabled(flag);
            if (!flag) {
                usePageFrozenCCheckBox.setSelected(false);
                usePageFrozenCCheckBox.setEnabled(false);
            } else {
                usePageFrozenCCheckBox.setEnabled(true);
            }
        }
    };

    ChangeListener useRepeatTitleRListener = new ChangeListener() {

        @Override
        public void stateChanged(ChangeEvent e) {
            boolean flag = useRepeatTitleRCheckBox.isSelected();
            repeatTitleRowPane.setEnabled(flag);
            if (!flag) {
                usePageFrozenRCheckBox.setSelected(false);
                usePageFrozenRCheckBox.setEnabled(false);
            } else {
                usePageFrozenRCheckBox.setEnabled(true);
            }
        }
    };

    ChangeListener useWriteFrozenRListener = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            boolean flag = useWriteFrozenRCheckBox.isSelected();
            freezeWriteRowPane.setEnabled(flag);

        }
    };

    ChangeListener useWriteFrozenCListener = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            boolean flag = useWriteFrozenCCheckBox.isSelected();
            freezeWriteColPane.setEnabled(flag);

        }
    };

    ChangeListener usePageFrozenCListener = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            freezePageColPane.setEnabled(usePageFrozenCCheckBox.isSelected());
        }
    };

    ChangeListener usePageFrozenRListener = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            freezePageRowPane.setEnabled(usePageFrozenRCheckBox.isSelected());
        }
    };

    // 分页重复冻结行数据联动
    ChangeListener freezePageRowListener = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            FT ft = repeatTitleRowPane.updateBean();
            int to = ft.getTo();
            freezePageRowPane.populateBean(new FT(to > -1 ? 0 : -1, to));
        }
    };

    // 分页重复冻结列数据联动
    ChangeListener freezePageColListener = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            FT ft = repeatTitleColPane.updateBean();
            int to = ft.getTo();
            freezePageColPane.populateBean(new FT(to > -1 ? 0 : -1, to));
        }
    };

    /**
     * 窗口标题
     *
     * @date 2014-11-14-下午2:30:58
     */
    protected String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Repeat-Freeze");
    }

    public void populate(ReportPageAttrProvider attribute) {

        if (attribute == null) {
            attribute = (ReportPageAttrProvider) StableFactory.createXmlObject(ReportPageAttrProvider.XML_TAG);
        }
        FT defaultFT = new FT(0, 0);
        populatColPane(attribute, defaultFT);
        populateRowPane(attribute, defaultFT);
    }

    protected void populateRowPane(ReportPageAttrProvider attribute, FT defaultFT) {
        FT ft = new FT(new Integer(attribute.getRepeatHeaderRowFrom()), new Integer(attribute.getRepeatHeaderRowTo()));
        if (isDefalut(ft)) {
            this.repeatTitleRowPane.populateBean(defaultFT);
            this.repeatTitleRowPane.setEnabled(false);
            usePageFrozenRCheckBox.setEnabled(false);
        } else {
            this.repeatTitleRowPane.populateBean(ft);
            useRepeatTitleRCheckBox.setSelected(true);
        }

        ft = new FT(new Integer(attribute.getRepeatFooterRowFrom()), new Integer(attribute.getRepeatFooterRowTo()));
        if (isDefalut(ft)) {
            this.repeatFinisRowPane.populateBean(defaultFT);
            this.repeatFinisRowPane.setEnabled(false);
            useRepeatFinisRCheckBox.setSelected(false);
        } else {
            this.repeatFinisRowPane.populateBean(ft);
            useRepeatFinisRCheckBox.setSelected(true);
        }

        this.usePageFrozenRCheckBox.setSelected(attribute.isUsePageFrozenRow());
        this.freezePageRowPane.setEnabled(attribute.isUsePageFrozenRow());
    }

    protected void populatColPane(ReportPageAttrProvider attribute, FT defaultFT) {
        FT ft = new FT(new Integer(attribute.getRepeatHeaderColumnFrom()), new Integer(attribute.getRepeatHeaderColumnTo()));
        if (isDefalut(ft)) {
            this.repeatTitleColPane.populateBean(defaultFT);
            this.repeatTitleColPane.setEnabled(false);
            usePageFrozenCCheckBox.setEnabled(false);
        } else {
            this.repeatTitleColPane.populateBean(ft);
            useRepeatTitleCCheckBox.setSelected(true);
        }

        ft = new FT(new Integer(attribute.getRepeatFooterColumnFrom()), new Integer(attribute.getRepeatFooterColumnTo()));
        if (isDefalut(ft)) {
            this.repeatFinisColPane.populateBean(defaultFT);
            this.repeatFinisColPane.setEnabled(false);
            useRepeatFinisCCheckBox.setSelected(false);
        } else {
            this.repeatFinisColPane.populateBean(ft);
            useRepeatFinisCCheckBox.setSelected(true);
        }

        this.usePageFrozenCCheckBox.setSelected(attribute.isUsePageFrozenColumn());
        this.freezePageColPane.setEnabled(attribute.isUsePageFrozenColumn());
    }

    private boolean isDefalut(FT ob) {
        return ob.getFrom() == -1 && ob.getTo() == -1;
    }

    /**
     * 初始化填报冻结pane
     *
     * @param writeFrozenColumnRow 填报冻结格子
     * @date 2014-11-14-下午2:30:15
     */
    public void populateWriteFrozenColumnRow(ColumnRow writeFrozenColumnRow) {
        if (writeFrozenColumnRow != null) {
            int col = writeFrozenColumnRow.getColumn();
            int row = writeFrozenColumnRow.getRow();
            if (col > 0) {
                freezeWriteColPane.populateBean(new FT(1, col - 1));
            }
            if (row > 0) {
                freezeWriteRowPane.populateBean(new FT(1, row - 1));
            }

            useWriteFrozenCCheckBox.setSelected(col > 0);
            useWriteFrozenRCheckBox.setSelected(row > 0);
            freezeWriteColPane.setEnabled(col > 0);
            freezeWriteRowPane.setEnabled(row > 0);
        } else {
            useWriteFrozenCCheckBox.setSelected(false);
            useWriteFrozenRCheckBox.setSelected(false);
            freezeWriteRowPane.setEnabled(false);
            freezeWriteColPane.setEnabled(false);
        }
    }

    public ReportPageAttrProvider update() {
        ReportPageAttrProvider attribute = (ReportPageAttrProvider) StableFactory.createXmlObject(ReportPageAttrProvider.XML_TAG);

        updateRowPane(attribute);
        updateColPane(attribute);

        return attribute;
    }

    protected void updateRowPane(ReportPageAttrProvider attribute) {
        // 重复标题行
        int titleFrom = valid(useRepeatTitleRCheckBox, this.repeatTitleRowPane.updateBean().getFrom());
        int titleTo = valid(useRepeatTitleRCheckBox, this.repeatTitleRowPane.updateBean().getTo());
        attribute.setRepeatHeaderRowFrom(titleFrom);
        attribute.setRepeatHeaderRowTo(titleTo);

        int finishFrom = valid(useRepeatFinisRCheckBox, this.repeatFinisRowPane.updateBean().getFrom());
        int finishTo = valid(useRepeatFinisRCheckBox, this.repeatFinisRowPane.updateBean().getTo());
        attribute.setRepeatFooterRowFrom(finishFrom);
        attribute.setRepeatFooterRowTo(finishTo);

        attribute.setUsePageFrozenRow(this.usePageFrozenRCheckBox.isSelected());
    }

    private int valid(UICheckBox checkBox, int num) {
        return checkBox.isSelected() ? num : -1;
    }

    protected void updateColPane(ReportPageAttrProvider attribute) {
        int titleFrom = valid(useRepeatTitleCCheckBox, this.repeatTitleColPane.updateBean().getFrom());
        int titleTo = valid(useRepeatTitleCCheckBox, this.repeatTitleColPane.updateBean().getTo());
        attribute.setRepeatHeaderColumnFrom(titleFrom);
        attribute.setRepeatHeaderColumnTo(titleTo);

        int finishFrom = valid(useRepeatFinisCCheckBox, this.repeatFinisColPane.updateBean().getFrom());
        int finishTo = valid(useRepeatFinisCCheckBox, this.repeatFinisColPane.updateBean().getTo());
        attribute.setRepeatFooterColumnFrom(finishFrom);
        attribute.setRepeatFooterColumnTo(finishTo);

        attribute.setUsePageFrozenColumn(this.usePageFrozenCCheckBox.isSelected());
    }

    /**
     * 更新WriteFrozenColumnRow
     *
     * @return 行列
     * @date 2014-11-14-下午2:29:45
     */
    public ColumnRow updateWriteFrozenColumnRow() {
        if (useWriteFrozenCCheckBox.isSelected() || useWriteFrozenRCheckBox.isSelected()) {
            int col = useWriteFrozenCCheckBox.isSelected() ? freezeWriteColPane.updateBean().getTo() + 1 : 0;
            int row = useWriteFrozenRCheckBox.isSelected() ? freezeWriteRowPane.updateBean().getTo() + 1 : 0;
            return ColumnRow.valueOf(col, row);
        }
        return null;
    }

    private BoxCenterAligmentPane getURLActionLabel(final String text) {
        ActionLabel actionLabel = new ActionLabel(text);

        actionLabel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    //Desktop.getDesktop().browse(new URI(url));
                    WebViewDlgHelper.createPluginDialog();
                    RepeatAndFreezeSettingPane.this.getTopLevelAncestor().setVisible(false);
                } catch (Exception exp) {

                }
            }
        });

        return new BoxCenterAligmentPane(actionLabel);
    }

    class BoxCenterAligmentPane extends JPanel {

        private UILabel textLabel;

        public BoxCenterAligmentPane(String text) {
            this(new UILabel(text));
        }

        public BoxCenterAligmentPane(UILabel label) {
            this.setLayout(FRGUIPaneFactory.createBorderLayout());

            JPanel centerPane = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
            this.add(centerPane, BorderLayout.CENTER);
            UILabel label1 = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Form_Frozen_Tip"));
            label1.setForeground(new Color(255, 0, 0));
            UILabel label2 = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Form_Forzen_Speed"));
            label2.setForeground(new Color(255, 0, 0));
            this.textLabel = label;
            centerPane.add(label1);
            centerPane.add(textLabel);
            centerPane.add(label2);
        }

        public void setFont(Font font) {
            super.setFont(font);

            if (textLabel != null) {
                textLabel.setFont(font);
            }
        }
    }

}
