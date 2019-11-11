package com.fr.design.widget.ui.designer.layout;

import com.fr.design.constants.LayoutConstants;
import com.fr.design.designer.IntervalConstants;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.creator.cardlayout.XWCardLayout;
import com.fr.design.designer.creator.cardlayout.XWCardMainBorderLayout;
import com.fr.design.foldablepane.UIExpandablePane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.style.FRFontPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.widget.accessibles.AccessibleTabPaneBackgroundEditor;
import com.fr.design.mainframe.widget.accessibles.AccessibleTemplateStyleEditor;
import com.fr.design.mainframe.widget.accessibles.TemplateStylePane;
import com.fr.design.widget.ui.designer.AbstractDataModify;
import com.fr.form.ui.LayoutBorderStyle;
import com.fr.form.ui.container.WTabDisplayPosition;
import com.fr.form.ui.container.WTabTextDirection;
import com.fr.form.ui.container.cardlayout.WCardTagLayout;
import com.fr.general.Background;
import com.fr.general.ComparatorUtils;
import com.fr.general.FRFont;

import com.fr.general.cardtag.TemplateStyle;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Component;

/**
 * Created by kerry on 2017/11/16.
 */
public class WCardTagLayoutDefinePane extends AbstractDataModify<WCardTagLayout> {
    private AccessibleTabPaneBackgroundEditor backgroundEditor;
    private FRFontPane frFontPane;
    private UIButtonGroup displayPositionGroup;
    private UIButtonGroup textDirectionGroup;
    private AccessibleTemplateStyleEditor templateStyleEditor;

    public WCardTagLayoutDefinePane(XCreator xCreator) {
        super(xCreator);
        initComponent();
    }

    public void initComponent() {
        this.setLayout(FRGUIPaneFactory.createBorderLayout());

        backgroundEditor = new AccessibleTabPaneBackgroundEditor();
        templateStyleEditor = new AccessibleTemplateStyleEditor(new TemplateStylePane());
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        double[] rowSize = {p, p, p, p, p};
        double[] columnSize = {p, f};
        int[][] rowCount = {{1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}};

        UILabel fontLabel = new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Font"));
        fontLabel.setVerticalAlignment(SwingConstants.TOP);
        frFontPane = new FRFontPane() {
            protected JPanel createRightPane() {
                double p = TableLayout.PREFERRED;
                double f = TableLayout.FILL;
                double[] columnSize = {f};
                double[] rowSize = {p};
                int[][] rowCount = {{1, 1}};
                Component[][] components = new Component[][]{
                        new Component[]{fontSizeComboBox},
                };
                return TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, LayoutConstants.VGAP_MEDIUM, LayoutConstants.VGAP_MEDIUM);
            }

        };
        displayPositionGroup = new UIButtonGroup(WTabDisplayPosition.getStringArray()) {
            @Override
            public boolean shouldResponseNameListener() {
                return true;
            }
        };
        displayPositionGroup.setGlobalName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Tab_Style_Template"));
        textDirectionGroup = new UIButtonGroup(WTabTextDirection.getStringArray());
        Component[][] components = new Component[][]{
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Tab_Style_Template")), templateStyleEditor},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Tab_Display_Position")), displayPositionGroup},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Base_Background")), backgroundEditor},
                new Component[]{fontLabel, frFontPane},
                new Component[]{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_StyleAlignment_Text_Rotation")), textDirectionGroup}
        };
        JPanel panel = TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, IntervalConstants.INTERVAL_W1, IntervalConstants.INTERVAL_L1);
        JPanel jPanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        jPanel.add(panel, BorderLayout.CENTER);
        UIExpandablePane advanceExpandablePane = new UIExpandablePane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Advanced"), 280, 20, jPanel);
        this.add(advanceExpandablePane, BorderLayout.CENTER);
    }

    @Override
    public String title4PopupWindow() {
        return "tabFitLayout";
    }

    @Override
    public void populateBean(WCardTagLayout ob) {
        //标题背景和字体属性设置在WCardLayout上做兼容
        XLayoutContainer topLayout = creator.getTopLayout();
        LayoutBorderStyle layoutBorderStyle = (LayoutBorderStyle) ((XWCardMainBorderLayout) topLayout).getCardPart().toData().getBorderStyle();

        displayPositionGroup.setSelectedIndex(ob.getDisplayPosition().getType());
        textDirectionGroup.setSelectedIndex(ob.getTextDirection().getType());
        backgroundEditor.setValue(layoutBorderStyle.getTitle().getBackground());
        templateStyleEditor.setValue(ob.getTemplateStyle());
        FRFont frFont = layoutBorderStyle.getTitle().getFrFont();
        if (frFont != null) {
            ob.setTitleFont(frFont);
            frFontPane.populateBean(frFont);
        }
    }

    @Override
    public WCardTagLayout updateBean() {
        //标题背景和字体属性设置在WCardLayout上做兼容
        XLayoutContainer topLayout = creator.getTopLayout();
        XWCardLayout xCardLayout = ((XWCardMainBorderLayout) topLayout).getCardPart();
        LayoutBorderStyle layoutBorderStyle = (LayoutBorderStyle) xCardLayout.toData().getBorderStyle();
        FRFont frFont = layoutBorderStyle.getTitle().getFrFont() == null ? FRFont.getInstance() : layoutBorderStyle.getTitle().getFrFont();
        FRFont titleFont = frFontPane.update(frFont);
        layoutBorderStyle.getTitle().setFrFont(titleFont);
        WCardTagLayout layout = (WCardTagLayout) creator.toData();
        layout.setTitleFont(titleFont);
        boolean isHori = displayPositionGroup.getSelectedIndex() == WTabDisplayPosition.TOP_POSITION.getType() || displayPositionGroup.getSelectedIndex() == WTabDisplayPosition.BOTTOM_POSITION.getType();
        if (ComparatorUtils.equals(getGlobalName(), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Form_Tab_Style_Template"))) {
            layout.setDisplayPosition(WTabDisplayPosition.parse(displayPositionGroup.getSelectedIndex()));
            textDirectionGroup.setSelectedIndex(isHori ? WTabTextDirection.TEXT_HORI_DERECTION.getType() : WTabTextDirection.TEXT_VER_DIRECTION.getType());
            layout.setHgap(isHori ? WCardTagLayout.DESIGNER_DEFAULT_GAP : 0);
            layout.setVgap(isHori ? 0 : WCardTagLayout.DESIGNER_DEFAULT_GAP);
        }
        layout.setTextDirection(WTabTextDirection.parse(textDirectionGroup.getSelectedIndex()));
        TemplateStyle templateStyle = (TemplateStyle) templateStyleEditor.getValue();
        if (!ComparatorUtils.equals(layout.getTemplateStyle(), templateStyle)) {
            backgroundEditor.setValue(templateStyle.getDefaultBackground());
            layoutBorderStyle.getTitle().setBackground(templateStyle.getDefaultBackground());
            //重置内部tab的默认背景
            xCardLayout.resetTabBackground(templateStyle);
            layout.setTemplateStyle(templateStyle);
        } else {
            layoutBorderStyle.getTitle().setBackground((Background) backgroundEditor.getValue());
        }

        return layout;
    }
}
