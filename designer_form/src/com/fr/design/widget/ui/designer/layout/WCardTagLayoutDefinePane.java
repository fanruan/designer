package com.fr.design.widget.ui.designer.layout;

import com.fr.base.TemplateStyle;
import com.fr.design.designer.IntervalConstants;
import com.fr.design.designer.creator.XCreator;
import com.fr.design.designer.creator.XLayoutContainer;
import com.fr.design.designer.creator.cardlayout.XWCardMainBorderLayout;
import com.fr.design.foldablepane.UIExpandablePane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.gui.style.FRFontPane;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.widget.accessibles.AccessibleImgBackgroundEditor;
import com.fr.design.mainframe.widget.accessibles.AccessibleTemplateStyleEditor;
import com.fr.design.widget.ui.designer.AbstractDataModify;
import com.fr.form.ui.LayoutBorderStyle;
import com.fr.form.ui.container.WTabDisplayPosition;
import com.fr.form.ui.container.WTabTextDirection;
import com.fr.form.ui.container.cardlayout.WCardTagLayout;
import com.fr.general.Background;
import com.fr.general.FRFont;
import com.fr.general.Inter;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Component;

/**
 * Created by kerry on 2017/11/16.
 */
public class WCardTagLayoutDefinePane extends AbstractDataModify<WCardTagLayout> {
    private AccessibleImgBackgroundEditor backgroundEditor;
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

        backgroundEditor = new AccessibleImgBackgroundEditor();
        templateStyleEditor = new AccessibleTemplateStyleEditor();
        double f = TableLayout.FILL;
        double p = TableLayout.PREFERRED;
        double[] rowSize = {p, p, p, p, p};
        double[] columnSize = {p, f};
        int[][] rowCount = {{1, 1}, {1, 1}, {1, 1}, {1, 1}, {1, 1}};

        UILabel fontLabel = new UILabel(Inter.getLocText("FR-Designer_Font"));
        fontLabel.setVerticalAlignment(SwingConstants.TOP);
        frFontPane = new FRFontPane();
        displayPositionGroup =  new UIButtonGroup(WTabDisplayPosition.getStringArray());
        textDirectionGroup = new UIButtonGroup(WTabTextDirection.getStringArray());
        Component[][] components = new Component[][]{
                new Component[]{new UILabel(Inter.getLocText("FR-Designer_Tab_Style_Template")), templateStyleEditor},
                new Component[]{new UILabel(Inter.getLocText("FR-Designer_Tab_Display_Position")), displayPositionGroup},
                new Component[]{new UILabel(Inter.getLocText("FR-Designer_Background")), backgroundEditor},
                new Component[]{fontLabel, frFontPane},
                new Component[]{new UILabel(Inter.getLocText("FR-Designer_StyleAlignment_Text_Rotation")), textDirectionGroup}
        };
        JPanel panel = TableLayoutHelper.createGapTableLayoutPane(components, rowSize, columnSize, rowCount, IntervalConstants.INTERVAL_W1, IntervalConstants.INTERVAL_L1);
        JPanel jPanel = FRGUIPaneFactory.createBorderLayout_S_Pane();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        jPanel.add(panel, BorderLayout.CENTER);
        UIExpandablePane advanceExpandablePane = new UIExpandablePane(Inter.getLocText("FR-Designer_Advanced"), 280, 20, jPanel);
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
        LayoutBorderStyle layoutBorderStyle = ((XWCardMainBorderLayout)topLayout).getCardPart().toData().getBorderStyle();

        displayPositionGroup.setSelectedIndex(ob.getDisplayPosition().getType());
        textDirectionGroup.setSelectedIndex(ob.getTextDirection().getType());
        backgroundEditor.setValue(layoutBorderStyle.getTitle().getBackground());
        templateStyleEditor.setValue(ob.getTemplateStyle());
        FRFont frFont = layoutBorderStyle.getTitle().getFrFont();
        if (frFont != null) {
            frFontPane.populateBean(frFont);
        }
    }

    @Override
    public WCardTagLayout updateBean() {
        //标题背景和字体属性设置在WCardLayout上做兼容
        XLayoutContainer topLayout = creator.getTopLayout();
        LayoutBorderStyle layoutBorderStyle = ((XWCardMainBorderLayout)topLayout).getCardPart().toData().getBorderStyle();
        FRFont frFont = layoutBorderStyle.getTitle().getFrFont() == null ? FRFont.getInstance() : layoutBorderStyle.getTitle().getFrFont();
        layoutBorderStyle.getTitle().setBackground((Background) backgroundEditor.getValue());
        layoutBorderStyle.getTitle().setFrFont(frFontPane.update(frFont));
        WCardTagLayout layout = (WCardTagLayout) creator.toData();
        layout.setDisplayPosition(WTabDisplayPosition.parse(displayPositionGroup.getSelectedIndex()));
        layout.setTextDirection(WTabTextDirection.parse(textDirectionGroup.getSelectedIndex()));
        layout.setTemplateStyle((TemplateStyle) templateStyleEditor.getValue());

        return layout;
    }
}
