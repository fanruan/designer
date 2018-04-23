package com.fr.design.style.background;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import com.fr.design.gui.ilable.UILabel;
import javax.swing.JPanel;

import com.fr.base.background.ColorBackground;
import com.fr.base.background.GradientBackground;
import com.fr.base.background.ImageBackground;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.dialog.BasicPane;
import com.fr.general.Background;
import com.fr.general.Inter;
import com.fr.design.style.background.gradient.GradientBackgroundSelectPane;
import com.fr.design.style.background.image.ImageSelectPane;
import com.fr.design.style.color.ColorUIComboBoxPane;

/**
 * @author kunsnat E-mail:kunsnat@gmail.com
 * @version 创建时间：2011-11-3 上午10:03:03
 * 类说明: 通过选择box切换界面的背景选择pane. bug原型图@5471
 */
public class BackgroundUIComboBoxPane extends BasicPane {
	private static final long serialVersionUID = -3751191424737067321L;
	
	protected static final String none = Inter.getLocText("DataFunction-None");
	protected static final String color = Inter.getLocText("Colors");
	protected static final String image = Inter.getLocText("Image");
	protected static final String gradient = Inter.getLocText("Gradient-Color");
	
	protected CardLayout cardLayout;
	protected JPanel layoutPane;
	protected ColorUIComboBoxPane colorPane;
	protected GradientBackgroundSelectPane gradientPane;
	
	private UIComboBox selectBox;
	private ImageSelectPane imagePane;
	
	public BackgroundUIComboBoxPane() {
		initPane();
	}
	
	protected void initPane() {
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		
		JPanel pane = FRGUIPaneFactory.createLeftFlowZeroGapBorderPane();
		this.add(pane);
		
		selectBox = new UIComboBox(getSelectType());
		selectBox.setPreferredSize(new Dimension(70, 20));
		
		JPanel labelPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
		pane.add(labelPane);
		labelPane.setPreferredSize(new Dimension(150, 40));
		
		JPanel northPane = FRGUIPaneFactory.createLeftFlowZeroGapBorderPane();
		labelPane.add(northPane, BorderLayout.NORTH);
		
		northPane.add(new UILabel(Inter.getLocText(new String[]{"Background_Settings"}) + ":"));
		northPane.add(selectBox);
		
		cardLayout = new CardLayout();
		layoutPane = new JPanel();
		pane.add(layoutPane);
		
		layoutPane.setLayout(cardLayout);
		
		initSelectPane();
		
		selectBox.addItemListener(itemListener);
	}
	
	protected String[] getSelectType() {
		return new String[]{none, color, image, gradient};
	}
	
	protected void initSelectPane() {
		layoutPane.add(new JPanel(), "none");
		layoutPane.add(colorPane = new ColorUIComboBoxPane(), "color");
		layoutPane.add(imagePane = new ImageSelectPane(), "image");
		layoutPane.add(gradientPane = new GradientBackgroundSelectPane(), "gradient");
		cardLayout.show(layoutPane, "none");
	}
	
	ItemListener itemListener = new ItemListener() {
		public void itemStateChanged(ItemEvent e) {
			UIComboBox source = (UIComboBox)e.getSource();
			Object select = source.getSelectedItem();
			if(none.equals(select)) {
				cardLayout.show(layoutPane, "none");
			} else if(color.equals(select)) {
				cardLayout.show(layoutPane, "color");
			} else if(image.equals(select)) {
				cardLayout.show(layoutPane, "image");
			} else if(gradient.equals(select)) {
				cardLayout.show(layoutPane, "gradient");
			}
		}
	};
	
	public void populate(Background background) {
		if(background instanceof ColorBackground && colorPane != null) {
			selectBox.setSelectedItem(color);
			colorPane.populate(background);
		} else if(background instanceof ImageBackground && imagePane != null) {
			selectBox.setSelectedItem(image);
			imagePane.populate(background);
		} else if(background instanceof GradientBackground && gradientPane != null) {
			selectBox.setSelectedItem(gradient);
			gradientPane.populate(background);
		}
	}
	
	public Background update() {
		Background background = null;
		Object select = selectBox.getSelectedItem();
		if(none.equals(select)) {
			
		} else if(color.equals(select) && colorPane != null) {
			background = colorPane.update();
		} else if(image.equals(select) && imagePane !=  null) {
			background = imagePane.update();
		} else if(gradient.equals(select) && gradientPane != null) {
			background = gradientPane.update();
		}
		return background;
	}
	
	public void populateAlpha(int alpha) {
		if(colorPane != null) {
			colorPane.populateAlpha(alpha);
		}
		if(imagePane != null) {
			imagePane.populateAlpha(alpha);
		}
		if(gradientPane != null) {
			gradientPane.populateAlpha(alpha);
		}
	}
	
	public float updateAlpha() {
		float alpha = 1;
		Object select = selectBox.getSelectedItem();
		if(none.equals(select)) {
			
		} else if(color.equals(select) && colorPane != null) {
			alpha = colorPane.updateAlpha();
		} else if(image.equals(select) && imagePane != null) {
			alpha = imagePane.updateAlpha();
		} else if(gradient.equals(select) && gradientPane != null) {
			alpha = gradientPane.updateAlpha();
		}
		
		return alpha;
	}
	
	@Override
	protected String title4PopupWindow() {
		return Inter.getLocText(new String[]{"Background", "Set"});
	}
}