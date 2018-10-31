package com.fr.design.reportfit.menupane;

import com.fr.design.reportfit.FitType;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.gui.ibutton.UIRadioButton;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.log.FineLoggerFactory;
import com.fr.main.ReportFitAttr;
import com.fr.main.ReportFitConfig;
import com.fr.report.fun.ReportFitAttrProvider;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Administrator on 2016/5/5/0005.
 */
public class BrowserFitAttrPane extends BasicBeanPane<ReportFitAttrProvider> {

    protected FontRadioGroup fontRadioGroup;
    protected FitRadioGroup fitRadionGroup;
    protected UICheckBox globalCheck;
    protected FitPreviewPane fitPreviewPane;
    protected ReportFitAttrProvider localFitAttr;
    protected UIRadioButton defaultRadio;
    protected UIRadioButton horizonRadio;
    protected UIRadioButton doubleRadio;
    protected UIRadioButton notFitRadio;
    protected UIRadioButton fontFitRadio;
    protected UIRadioButton fontNotFitRadio;
    private UIButton editGlobalOps;
    private JPanel borderPane;
    private JPanel globalOpsPane;
    private JPanel fitOpsPane;

    public BrowserFitAttrPane() {
        initComponents(ReportFitConfig.getInstance().getFrmFitAttr());
    }

    protected void initComponents(ReportFitAttr globalFitAttr) {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());
        globalOpsPane = initGlobalOpsPane(globalFitAttr);
        this.add(globalOpsPane, BorderLayout.NORTH);
        fitOpsPane = initFitOpsPane();

    }

    protected void initBorderPane(String title) {
        borderPane = FRGUIPaneFactory.createTitledBorderPaneCenter(title);
        borderPane.add(fitOpsPane, BorderLayout.CENTER);
        fitPreviewPane = new FitPreviewPane();
        borderPane.add(fitPreviewPane, BorderLayout.SOUTH);
        this.add(borderPane, BorderLayout.CENTER);
    }

    private JPanel initFitOpsPane() {
        double p = TableLayout.PREFERRED;
        double[] rowSize = {p, p};
        double[] columnSize = {p, p, p, p, p};

        ActionListener actionListener = getPreviewActionListener();

        fontRadioGroup = new FontRadioGroup();
        fontFitRadio = new UIRadioButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Designer_Fit"));
        fontFitRadio.setSelected(true);
        fontNotFitRadio = new UIRadioButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Designer_Fit-No"));
        addRadioToGroup(fontRadioGroup, fontFitRadio, fontNotFitRadio);
        fontRadioGroup.addActionListener(actionListener);

        fitRadionGroup = new FitRadioGroup();
        defaultRadio = new UIRadioButton(FitType.DEFAULT.description());
        horizonRadio = new UIRadioButton(FitType.HORIZONTAL_FIT.description());
        doubleRadio = new UIRadioButton(FitType.DOUBLE_FIT.description());
        notFitRadio = new UIRadioButton(FitType.NOT_FIT.description());
        addRadioToGroup(fitRadionGroup, defaultRadio, horizonRadio, doubleRadio, notFitRadio);
        fitRadionGroup.addActionListener(actionListener);


        JPanel fitOpsPane = TableLayoutHelper.createTableLayoutPane(initFitComponents(), rowSize, columnSize);
        fitOpsPane.setBorder(BorderFactory.createEmptyBorder(10, 13, 10, 10));
        return fitOpsPane;
    }

    protected Component[][] initFitComponents() {
        Component[][] components = new Component[][]{
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Designer_Fit-Font")), fontFitRadio, null, fontNotFitRadio},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Designer_Fit-Element")), defaultRadio, horizonRadio, doubleRadio, notFitRadio}
        };
        return components;
    }

    private void addRadioToGroup(ButtonGroup buttonGroup, UIRadioButton... radios) {
        for (UIRadioButton radio : radios) {
            buttonGroup.add(radio);
        }
    }

    private JPanel initGlobalOpsPane(final ReportFitAttr globalFitAttr) {
        final JPanel globalOpsPane = FRGUIPaneFactory.createRightFlowInnerContainer_S_Pane();
        globalCheck = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Designer_Fit-UseGlobal"));
        globalOpsPane.add(globalCheck);
        globalCheck.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean isLocalConfig = !globalCheck.isSelected();
                //勾选全局时，采用全局保存的自适应属性更新界面
                if (!isLocalConfig) {
                    ReportFitAttrProvider attr = globalFitAttr;
                    fontRadioGroup.selectFontFit(((ReportFitAttr) attr).isFitFont());
                    fitRadionGroup.selectIndexButton(attr.fitStateInPC());
                    fitPreviewPane.refreshPreview(getCurrentFitOptions(), fitRadionGroup.isEnabled());
                    remove(BrowserFitAttrPane.this.borderPane);
                    initBorderPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Designer_Fit-Global"));
                } else {
                    ReportFitAttrProvider attr = localFitAttr;
                    fontRadioGroup.selectFontFit(((ReportFitAttr) attr).isFitFont());
                    fitRadionGroup.selectIndexButton(attr.fitStateInPC());
                    fitPreviewPane.refreshPreview(getCurrentFitOptions(), fitRadionGroup.isEnabled());
                    remove(BrowserFitAttrPane.this.borderPane);
                    initBorderPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Designer_Fit-Local"));
                }
                fontRadioGroup.setEnabled(isLocalConfig);
                fitRadionGroup.setEnabled(isLocalConfig);
                editGlobalOps.setVisible(!isLocalConfig);
                String fitOptions = getCurrentFitOptions();
                fitPreviewPane.refreshPreview(fitOptions, fitRadionGroup.isEnabled());
            }
        });

        editGlobalOps = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Designer_Fit-EditGlobal"));
        editGlobalOps.setVisible(false);
        editGlobalOps.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                fontRadioGroup.setEnabled(true);
                fitRadionGroup.setEnabled(true);
                String fitOptions = getCurrentFitOptions();

                fitPreviewPane.refreshPreview(fitOptions, fitRadionGroup.isEnabled());
            }

            public void mouseEntered(MouseEvent e) {
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            public void mouseExited(MouseEvent e) {
                setCursor(Cursor.getDefaultCursor());
            }
        });
        globalOpsPane.add(editGlobalOps);
        return globalOpsPane;
    }

    @Override
    protected String title4PopupWindow() {
        return com.fr.design.i18n.Toolkit.i18nText("Fine-Designer_Fit-AttrSet");
    }

    @Override
    public void populateBean(ReportFitAttrProvider attr) {
        if (attr == null) {
            //如果为空, 就用全局的
            attr = ReportFitConfig.getInstance().getFrmFitAttr();
            populateGlobalComponents();
        } else {
            initBorderPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Designer_Fit-Local"));
        }
        this.localFitAttr = attr;
        fontRadioGroup.selectFontFit(((ReportFitAttr) attr).isFitFont());
        fitRadionGroup.selectIndexButton(attr.fitStateInPC());
        fitPreviewPane.refreshPreview(getCurrentFitOptions(), fitRadionGroup.isEnabled());
    }

    protected void populateGlobalComponents() {
        globalCheck.setSelected(true);
        fontRadioGroup.setEnabled(false);
        fitRadionGroup.setEnabled(false);
        editGlobalOps.setVisible(true);
        initBorderPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Designer_Fit-Global"));
    }

    //有八种组合, 不过有意义的就是6种, 以此为key去缓存里找对应的预览图片
    public String getCurrentFitOptions() {
        return fitRadionGroup.getSelectRadioIndex() + "" + fontRadioGroup.getSelectRadioIndex();
    }

    private ActionListener getPreviewActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fitOptions = getCurrentFitOptions();
                fitPreviewPane.refreshPreview(fitOptions, fontRadioGroup.isEnabled());
            }
        };
    }

    @Override
    public ReportFitAttrProvider updateBean() {
        ReportFitAttr attr = new ReportFitAttr();
        attr.setFitFont(fontRadioGroup.isFontFit());
        attr.setFitStateInPC(fitRadionGroup.getSelectRadioIndex());

        // 直接用全局的
        if (globalCheck.isSelected()) {
            updateGlobalConfig(attr);
            return null;
        }
        this.localFitAttr = attr;
        return attr;
    }

    private void updateGlobalConfig(ReportFitAttr attr) {
        try {
            ReportFitConfig manager = ReportFitConfig.getInstance();
            manager.setFrmFitAttr(attr);
        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage());
        }
    }
}