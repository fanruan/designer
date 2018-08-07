package com.fr.design.expand;

import java.awt.BorderLayout;
import java.awt.CardLayout;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.fr.base.BaseUtils;
import com.fr.data.DataConstants;
import com.fr.design.formula.TinyFormulaPane;
import com.fr.design.gui.ibutton.UIButtonGroup;

import com.fr.report.cell.cellattr.CellExpandAttr;
import com.fr.stable.StringUtils;

public class SortExpandAttrPane extends JPanel {
	private UIButtonGroup sort_type_pane;
	private TinyFormulaPane tinyFormulaPane;
	private CardLayout cardLayout;
	private JPanel centerPane;


	public SortExpandAttrPane() {
		this.setLayout(new BorderLayout(0, 4));
		Icon[][] iconArray = {
				{BaseUtils.readIcon("/com/fr/design/images/expand/none16x16.png"), BaseUtils.readIcon("/com/fr/design/images/expand/none16x16_selected@1x.png")},
				{BaseUtils.readIcon("/com/fr/design/images/expand/asc.png"), BaseUtils.readIcon("/com/fr/design/images/expand/asc_selected.png")},
				{BaseUtils.readIcon("/com/fr/design/images/expand/des.png"), BaseUtils.readIcon("/com/fr/design/images/expand/des_selected.png")}
		};
		String[] nameArray = { com.fr.design.i18n.Toolkit.i18nText("Sort-Original"), com.fr.design.i18n.Toolkit.i18nText("Sort-Ascending"), com.fr.design.i18n.Toolkit.i18nText("Sort-Descending") };
        sort_type_pane = new UIButtonGroup(iconArray);
        sort_type_pane.setAllToolTips(nameArray);
		sort_type_pane.setGlobalName(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_ExpandD_Sort_After_Expand"));
		this.add(sort_type_pane, BorderLayout.NORTH);

		cardLayout = new CardLayout();
		centerPane = new JPanel(cardLayout);

		tinyFormulaPane = new TinyFormulaPane();

		centerPane.add(new JPanel(), "none");
		centerPane.add(tinyFormulaPane, "content");

		this.add(centerPane, BorderLayout.CENTER);

		sort_type_pane.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				cardLayout.show(centerPane, sort_type_pane.getSelectedIndex() == 0 ? "none" : "content");
			}
		});
	}



	public void populate(CellExpandAttr cellExpandAttr) {
		if (cellExpandAttr == null) {
			cellExpandAttr = new CellExpandAttr();
		}

		int sort = cellExpandAttr.getOrder();
		this.sort_type_pane.setSelectedIndex(sort);

		String sortFormula = cellExpandAttr.getSortFormula();
		tinyFormulaPane.populateBean(sortFormula);
		cardLayout.show(centerPane, sort_type_pane.getSelectedIndex() == 0 ? "none" : "content");
	}

	public void update(CellExpandAttr cellExpandAttr) {
		if (cellExpandAttr == null) {
			cellExpandAttr = new CellExpandAttr();
		}

		String sortFormula = null;

		cellExpandAttr.setOrder(this.sort_type_pane.getSelectedIndex());

		if (cellExpandAttr.getOrder() != DataConstants.NONE) {
			String sText = tinyFormulaPane.updateBean();
			if (StringUtils.isNotEmpty(sText)) {
				cellExpandAttr.setSortFormula(sText);
			} else {
				cellExpandAttr.setSortFormula(sortFormula);
			}
		} else {
			cellExpandAttr.setSortFormula(sortFormula);
		}
	}

}