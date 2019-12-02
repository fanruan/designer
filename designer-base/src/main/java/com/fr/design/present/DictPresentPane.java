package com.fr.design.present;

import java.awt.BorderLayout;
import java.awt.event.ItemListener;

import com.fr.base.present.DictPresent;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.data.tabledata.Prepare4DataSourceChange;
import com.fr.design.present.dict.DictionaryPane;


/**
 * 
 * @author zhou
 * @since 2012-5-31上午10:54:20
 */
public class DictPresentPane extends FurtherBasicBeanPane<DictPresent> implements Prepare4DataSourceChange {
	private DictionaryPane dictionaryPane;

	public DictPresentPane() {
		dictionaryPane = new DictionaryPane();
		this.setLayout(new BorderLayout());
		this.add(dictionaryPane, BorderLayout.CENTER);
	}

	@Override
	public String title4PopupWindow() {
		return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_DS_Dictionary");
	}

	@Override
	public void populateBean(DictPresent ob) {
		dictionaryPane.populateBean(ob.getDictionary());
	}

	@Override
	public DictPresent updateBean() {
		return new DictPresent(dictionaryPane.updateBean());
	}

	@Override
	public boolean accept(Object ob) {
		return ob instanceof DictPresent;
	}
	
	public void addTabChangeListener(ItemListener l){
		dictionaryPane.addTabChangeListener(l);
	}

	public void reset() {
		dictionaryPane.reset();
	}

	@Override
	public void registerDSChangeListener() {
		dictionaryPane.registerDSChangeListener();
	}
}