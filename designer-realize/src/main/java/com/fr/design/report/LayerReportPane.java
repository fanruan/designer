package com.fr.design.report;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.dialog.FineJOptionPane;
import com.fr.design.editor.editor.IntegerEditor;
import com.fr.design.gui.icheckbox.UICheckBox;
import com.fr.design.gui.ilable.FRExplainLabel;
import com.fr.design.gui.ilable.UILabel;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.layout.TableLayout;
import com.fr.design.layout.TableLayoutHelper;
import com.fr.design.mainframe.DesignerContext;

import com.fr.report.core.ReportUtils;
import com.fr.report.stable.LayerReportAttr;
import com.fr.report.worksheet.WorkSheet;
import com.fr.stable.ProductConstants;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 
 * @editor zhou
 * @since 2012-3-23下午3:00:28
 */
public class LayerReportPane extends BasicBeanPane<LayerReportAttr> {
    private static final int LABEL_HEIGHT = 55;
	private UICheckBox isLayerReportBox;
	private UICheckBox isPageQueryBox;
	private IntegerEditor countPerPageEditor;
	private CardLayout card;
	private JPanel cardPane;
	private WorkSheet worksheet;

	public LayerReportPane(WorkSheet worksheet) {
		this.worksheet = worksheet;
		this.initComponents();
	}

	public LayerReportPane() {
		this(null);
	}

	protected void initComponents() {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		JPanel outlayerReportPane = FRGUIPaneFactory.createTitledBorderPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Report_Engine_Attribute"));
		JPanel layerReportPane = FRGUIPaneFactory.createY_AXISBoxInnerContainer_M_Pane();
		this.add(outlayerReportPane);
		outlayerReportPane.add(layerReportPane);
		JPanel isLayerReportBoxPanle = FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane();
		isLayerReportBox = new UICheckBox(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Execute_Report_by_Layer_Engine"));
		isLayerReportBox.setSelected(false);
		isLayerReportBoxPanle.add(isLayerReportBox);
		layerReportPane.add(isLayerReportBoxPanle);

		card = new CardLayout();
		cardPane = FRGUIPaneFactory.createCardLayout_S_Pane();
		cardPane.setLayout(card);
		cardPane.add(FRGUIPaneFactory.createNormalFlowInnerContainer_S_Pane(), "none");
		JPanel pageQueryPane = createPageQueryPane();
        cardPane.add(pageQueryPane, "page");
        
		layerReportPane.add(cardPane);

		isLayerReportBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (isLayerReportBox.isSelected()) {
					card.show(cardPane, "page");
				} else {
					card.show(cardPane, "none");
				}
			}
		});

		JPanel infoPane = FRGUIPaneFactory.createTitledBorderPane(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Attention"));
		FRExplainLabel label = new FRExplainLabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Layer_Report_Warnning_Info"));
        label.setPreferredSize(new Dimension(label.getPreferredSize().width, LABEL_HEIGHT));
		infoPane.add(label);
		layerReportPane.add(infoPane);
	}

    private JPanel createPageQueryPane() {

        double p = TableLayout.PREFERRED;
        double rowSize[] = {p, p};
        double columnSize[] = {p, p};
        isPageQueryBox = new UICheckBox();
        isPageQueryBox.setSelected(false);
        isPageQueryBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (isPageQueryBox.isSelected()) {
                    countPerPageEditor.setEnabled(true);
                } else {
                    countPerPageEditor.setEnabled(false);
                }
            }
        });
        countPerPageEditor = new IntegerEditor(new Integer(30));
        countPerPageEditor.setPreferredSize(new Dimension(120,20));
        countPerPageEditor.setEnabled(false);
        Component[][] components = {{new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_LayerPageReport_PageEngine") + ":"), isPageQueryBox},
                {new UILabel(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_LayerPageReport_Count_PerPage") + ":"), countPerPageEditor}};
        JPanel pageQueryPane = TableLayoutHelper.createTableLayoutPane(components, rowSize, columnSize);
        pageQueryPane.setBorder(BorderFactory.createEmptyBorder(5, 15, 0, 0));
        return pageQueryPane;
    }

	@Override
	protected String title4PopupWindow() {
		return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Report_Engine_Attribute");
	}

	@Override
	public void populateBean(LayerReportAttr ob) {
		isLayerReportBox.setSelected(ob != null);
		if (ob != null) {
			card.show(cardPane, "page");
			countPerPageEditor.setValue(new Integer(ob.getCountPerPage()));
			if (ob.isPageQuery()) {
				isPageQueryBox.setSelected(true);
				countPerPageEditor.setEnabled(true);
			} else {
				isPageQueryBox.setSelected(false);
				countPerPageEditor.setEnabled(false);
			}
		} else {
			card.show(cardPane, "none");
		}
	}

	@Override
	public LayerReportAttr updateBean() {
		LayerReportAttr attr = null;
		if (isLayerReportBox.isSelected()) {
			attr = new LayerReportAttr();
			attr.setPageQuery(isPageQueryBox.isSelected());
			attr.setCountPerPage(Math.min(500, ((Integer)countPerPageEditor.getValue()).intValue()));
		}

		return attr;
	}

	@Override
	public void checkValid() throws Exception {
		if (isLayerReportBox.isSelected()) {
			if (!ReportUtils.isLayerReportUsable(worksheet)) {
				int value = FineJOptionPane.showConfirmDialog(DesignerContext.getDesignerFrame(), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_After_Changed_Some_Attributes_Are_Different") + "?",
						com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Confirm"), JOptionPane.YES_NO_OPTION);
				if (value != JOptionPane.YES_OPTION) {
					isLayerReportBox.setSelected(false);
				}
			}
		}
	}
}
