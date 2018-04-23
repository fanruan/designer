package com.fr.design.gui.frpane;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.beans.FurtherBasicBeanPane;
import com.fr.design.gui.ibutton.UIButtonGroup;
import com.fr.design.layout.FRGUIPaneFactory;

/**
 * 
 * @author zhou
 * @since 2012-5-30下午1:12:00
 */
public abstract class UIRadioPane<T> extends BasicBeanPane<T> {
	private static final long serialVersionUID = 1L;

	protected UIButtonGroup<Object> cardNamesPane;
	protected JPanel cardPane;

	// 放弃下面带泛型的数组,用list,因为Java不支持泛型数组,导致初始化数组时不能用泛型、从而不能严格控制类型,那么用泛型就没有什么意义了
	// private FurtherBasicBeanPane<? extends T>[] cards; august
	private List<FurtherBasicBeanPane<? extends T>> cards;

	private String[] cardNames;
	
	protected abstract List<FurtherBasicBeanPane<? extends T>> initPaneList();
	
	public UIRadioPane() {
		cards = initPaneList();
		initComponents();
	}

	protected void initComponents() {
		cardNames = new String[cards.size()];
		cardPane = FRGUIPaneFactory.createCardLayout_S_Pane();

		for (int i = 0; i < this.cards.size(); i++) {
			cardNames[i] = getCardName(i);
			cardPane.add(this.cards.get(i), cardNames[i]);
		}

		cardNamesPane = getCardNamePane(cardNames);

		cardNamesPane.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				showCard();
			}
		});
		initLayout();
		cardNamesPane.setSelectedIndex(0);// 默认选中第一个
	}
	
	protected UIButtonGroup<Object> getCardNamePane(String[] cardNames) {
		return new UIButtonGroup<Object>(cardNames);
	}

	private void showCard() {
		CardLayout cl = (CardLayout)cardPane.getLayout();
		cl.show(cardPane, cardNames[cardNamesPane.getSelectedIndex()]);
	}

	/**
	 * august　如果需要的布局有变化，覆盖之
	 */
	protected void initLayout() {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		JPanel northPane = new JPanel(new BorderLayout(4, 6));
		northPane.add(cardNamesPane, BorderLayout.CENTER);

		this.add(northPane, BorderLayout.NORTH);
		this.add(cardPane, BorderLayout.CENTER);

	}
	
	protected String getCardName(int i) {
		return this.cards.get(i).title4PopupWindow();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void populateBean(T ob) {
		for (int i = 0; i < this.cards.size(); i++) {
			FurtherBasicBeanPane pane = cards.get(i);
			if (pane.accept(ob)) {
				pane.populateBean(ob);
				cardNamesPane.setSelectedIndex(i);
				showCard();
				return;
			}
		}
	}

	@Override
	public T updateBean() {
		return cards.get(cardNamesPane.getSelectedIndex()).updateBean();
	}

}