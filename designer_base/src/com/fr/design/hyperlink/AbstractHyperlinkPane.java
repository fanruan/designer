package com.fr.design.hyperlink;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;

import com.fr.design.gui.ilable.UILabel;

import javax.swing.JList;
import javax.swing.JPanel;

import com.fr.base.Utils;
import com.fr.design.beans.BasicBeanPane;
import com.fr.design.gui.icombobox.UIComboBox;
import com.fr.design.gui.itextfield.UINumberField;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.general.Inter;
import com.fr.js.Hyperlink;

public abstract class AbstractHyperlinkPane<T extends Hyperlink> extends BasicBeanPane<T> {
	public static final int NEW_WINDOW = 0;
	public static final int DIALOG = 1;
	public static final int SELF = 2;
	public static final int DEFAULT_H_VALUE = 400;
	public static final int DEFAULT_V_VALUE = 600;

	private JPanel headerPane;
	private UIComboBox targetFrameComboBox;

	private UINumberField heightTextFiled;
	private UINumberField widthTextFiled;


	public AbstractHyperlinkPane() {
		this.initComponents();
	}

	protected void initComponents() {
		this.setLayout(FRGUIPaneFactory.createM_BorderLayout());
		JPanel centerPane = FRGUIPaneFactory.createBorderLayout_L_Pane();
		headerPane = this.setHeaderPanel();
		this.add(headerPane, BorderLayout.NORTH);
		this.add(centerPane, BorderLayout.CENTER);
		targetFrameComboBox = new UIComboBox(new String[]{Inter.getLocText("Hyperlink-New_Window"), Inter.getLocText("FR-Hyperlink_Dialog"), Inter.getLocText("Hyperlink-Self_Window")});
		targetFrameComboBox.setRenderer(new DefaultListCellRenderer() {
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				return this;
			}
		});
		JPanel targetFramePanel = new JPanel();
		targetFramePanel.add(new UILabel(Inter.getLocText("Hyperlink-Link_Opened_in")));
		targetFramePanel.add(targetFrameComboBox);
		targetFrameComboBox.setEditable(true);
		targetFrameComboBox.setPreferredSize(new Dimension(100, 20));

		final JPanel newWindowConfPane = new JPanel();
		newWindowConfPane.add(new UILabel(Inter.getLocText("FR-Designer_Height") + ": "));
		heightTextFiled = new UINumberField();
        heightTextFiled.setText(String.valueOf(DEFAULT_H_VALUE));
		heightTextFiled.setPreferredSize(new Dimension(60, 20));
		newWindowConfPane.add(heightTextFiled);
		newWindowConfPane.add(new UILabel("  " + Inter.getLocText("FR-Designer_Width") + ": "));
		widthTextFiled = new UINumberField();
        widthTextFiled.setText(String.valueOf(DEFAULT_V_VALUE));
		widthTextFiled.setPreferredSize(new Dimension(60, 20));
		newWindowConfPane.add(widthTextFiled);

		JPanel centerPanel = new JPanel(new BorderLayout());
		centerPanel.add(targetFramePanel, BorderLayout.WEST);
		centerPanel.add(newWindowConfPane, BorderLayout.EAST);
        newWindowConfPane.setVisible(false);

		centerPane.add(centerPanel);
		targetFrameComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				newWindowConfPane.setVisible(DIALOG == targetFrameComboBox.getSelectedIndex());
			}
		});

		this.add(this.setFootPanel(), BorderLayout.SOUTH);
	}

	protected abstract JPanel setHeaderPanel();

	protected abstract JPanel setFootPanel();

	protected abstract void populateSubHyperlinkBean(T link);

	@Override
	public void populateBean(T link) {
		String name = link.getTargetFrame();
		if ("_self".equals(name)) {
			targetFrameComboBox.setSelectedIndex(SELF);
		} else if ("_dialog".equals(name)) {
			targetFrameComboBox.setSelectedIndex(DIALOG);
		} else if ("_blank".equals(name)) {
			targetFrameComboBox.setSelectedIndex(NEW_WINDOW);
		} else {
			DefaultComboBoxModel model = (DefaultComboBoxModel) targetFrameComboBox.getModel();
			model.addElement(name);
			targetFrameComboBox.setSelectedItem(name);
		}
		heightTextFiled.setText(String.valueOf(link.getHeight() == 0 ? DEFAULT_H_VALUE : link.getHeight()));
		widthTextFiled.setText(String.valueOf(link.getWidth() == 0 ? DEFAULT_V_VALUE : link.getWidth()));
		populateSubHyperlinkBean(link);
	}

	protected abstract T updateSubHyperlinkBean();

	protected abstract void updateSubHyperlinkBean(T t);

	@Override
	public T updateBean() {
		T link = updateSubHyperlinkBean();

		updateBean(link);

		return link;
	}

	public void updateBean(T link) {
		updateSubHyperlinkBean(link);
		link.setTargetFrame(HyperlinkTargetFrame.getName(targetFrameComboBox.getSelectedIndex()));
		link.setHeight(Utils.objectToNumber(heightTextFiled.getText(), false).intValue());
		link.setWidth(Utils.objectToNumber(widthTextFiled.getText(), false).intValue());
	}

}