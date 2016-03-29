package com.fr.design.present.dict;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import com.fr.design.data.DataCreatorUI;
import com.fr.design.gui.ilable.UILabel;

import javax.swing.*;

import com.fr.data.Dictionary;
import com.fr.data.impl.DynamicSQLDict;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.gui.frpane.UIComboBoxPane;
import com.fr.general.Inter;

/**
 * 
 * @author zhou
 * @since 2012-5-31下午12:20:41
 */
public class DictionaryPane extends UIComboBoxPane<Dictionary> implements DataCreatorUI {
	private TableDataDictPane tableDataDictPane;

	@Override
	protected void initLayout() {
		this.setLayout(new BorderLayout(0,4));
		JPanel northPane = new JPanel(new BorderLayout(4, 0));
		northPane.add(new UILabel(Inter.getLocText("Type_Set")+":", UILabel.RIGHT),BorderLayout.WEST);
		northPane.add(jcb,BorderLayout.CENTER);
		this.add(northPane,BorderLayout.NORTH);
		this.add(cardPane,BorderLayout.CENTER);
	}

	@Override
	protected String title4PopupWindow() {
		return Inter.getLocText("DS-Dictionary");
	}

    @Override
    public JComponent toSwingComponent() {
        return this;
    }

    @Override
	public void populateBean(Dictionary ob) {
        for (int i = 0; i < this.cards.size(); i++) {
            FurtherBasicBeanPane pane = cards.get(i);
            if (pane.accept(ob)) {
                pane.populateBean(ob);
                jcb.setSelectedIndex(i);
            } else {
                pane.reset();
            }
        }
		if(ob instanceof DynamicSQLDict){
			jcb.setSelectedIndex(1);
			tableDataDictPane.populateBean((DynamicSQLDict)ob);
		}
	}

	@Override
	protected List<FurtherBasicBeanPane<? extends Dictionary>> initPaneList() {
		List<FurtherBasicBeanPane<? extends Dictionary>> paneList = new ArrayList<FurtherBasicBeanPane<? extends Dictionary>>();
		paneList.add( new DatabaseDictPane());
		paneList.add(tableDataDictPane = new TableDataDictPane());
		paneList.add( new CustomDictPane());
		paneList.add( new FormulaDictPane());
		return paneList;
	}
}