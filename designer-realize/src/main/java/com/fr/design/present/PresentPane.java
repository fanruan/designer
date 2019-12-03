package com.fr.design.present;

import com.fr.base.present.DictPresent;
import com.fr.base.present.FormulaPresent;
import com.fr.base.present.Present;
import com.fr.design.ExtraDesignClassManager;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.fun.PresentKindProvider;
import com.fr.design.gui.frpane.UIComboBoxPane;
import com.fr.design.gui.icombobox.DictionaryComboBox;
import com.fr.design.gui.icombobox.UIComboBox;

import com.fr.report.cell.cellattr.BarcodePresent;
import com.fr.report.cell.cellattr.CurrencyLinePresent;

import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author zhou
 * @since 2012-5-31上午11:22:28
 */
public class PresentPane extends UIComboBoxPane<Present> {
	private DictPresentPane dictPresentPane;
	private FormulaPresentPane formulaPresentPane;
	private BarCodePane barCodePane;
	private CurrencyLinePane currencyLinePane;
	private List<String> keys;
	private List<String> displays;


	@Override
	protected String title4PopupWindow() {
		return com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Report_Present");
	}

	public void setSelectedByName(String radioName) {
		jcb.setSelectedItem(radioName);
	}

    /**
     * 增加tab改变监听事件
     * @param l   监听事件
     */
	public void addTabChangeListener(ItemListener l) {
		super.addTabChangeListener(l);
		dictPresentPane.addTabChangeListener(l);
	}

	@Override
	public void populateBean(Present ob) {
		if(ob == null) {
			dictPresentPane.reset();
			formulaPresentPane.reset();
			barCodePane.reset();
			currencyLinePane.reset();
		}
		super.populateBean(ob);
	}

	@Override
	protected List<FurtherBasicBeanPane<? extends Present>> initPaneList() {
		if (keys == null) {
			keys = new ArrayList<>();
		}
		if (displays == null) {
			displays = new ArrayList<>();
		}
		List<FurtherBasicBeanPane<? extends Present>> paneList = new ArrayList<>();
		FurtherBasicBeanPane<Present> none = new NonePresentPane();
		paneList.add(none);
		keys.add("NOPRESENT");
		displays.add(none.title4PopupWindow());

		dictPresentPane = new DictPresentPane();
		dictPresentPane.registerDSChangeListener();
		paneList.add(dictPresentPane);
		keys.add(DictPresent.class.getName());
		displays.add(dictPresentPane.title4PopupWindow());

		barCodePane = new BarCodePane();
		paneList.add(barCodePane);
		keys.add(BarcodePresent.class.getName());
		displays.add(barCodePane.title4PopupWindow());

		formulaPresentPane = new FormulaPresentPane();
		paneList.add(formulaPresentPane);
		keys.add(FormulaPresent.class.getName());
		displays.add(formulaPresentPane.title4PopupWindow());

		currencyLinePane = new CurrencyLinePane();
		paneList.add(currencyLinePane);
		keys.add(CurrencyLinePresent.class.getName());
		displays.add(currencyLinePane.title4PopupWindow());

		Set<PresentKindProvider> providers = ExtraDesignClassManager.getInstance().getArray(PresentKindProvider.MARK_STRING);
		for (PresentKindProvider provider : providers) {
			FurtherBasicBeanPane<? extends Present> extra = provider.appearanceForPresent();
			paneList.add(extra);
			keys.add(provider.kindOfPresent().getName());
			displays.add(extra.title4PopupWindow());
		}
		return paneList;
	}

	@Override
	protected UIComboBox createComboBox() {
		return new DictionaryComboBox<>(keys.toArray(new String[keys.size()]), displays.toArray(new String[displays.size()]));
	}

	@Override
	protected void addComboBoxItem(List<FurtherBasicBeanPane<? extends Present>> cards, int index) {
		// 初始化的时候已经加了，所以这里不用加了
	}
}
