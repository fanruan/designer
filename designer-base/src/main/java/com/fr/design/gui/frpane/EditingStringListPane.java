package com.fr.design.gui.frpane;

import com.fr.design.beans.BasicBeanPane;
import com.fr.design.dialog.FineJOptionPane;
import com.fr.design.gui.ibutton.UIButton;
import com.fr.design.layout.FRGUIPaneFactory;
import com.fr.design.utils.gui.GUICoreUtils;
import com.fr.design.utils.gui.JListUtils;
import com.fr.stable.StringUtils;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public abstract class EditingStringListPane extends BasicBeanPane<List<String>> {

	private static final long serialVersionUID = 1L;
	private DefaultListModel model;
	private JList jlist;
	private UIButton addButton;
	private UIButton editButton;
	private UIButton removeButton;
	private UIButton moveUpButton;
	private UIButton moveDownButton;

	public EditingStringListPane() {
		super();
		this.setLayout(FRGUIPaneFactory.createBorderLayout());
		model = new DefaultListModel();
		jlist = new JList(model);
		jlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jlist.setCellRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1L;

			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				this.setText(value.toString());
				return this;
			}

		});

		addButton = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Add"));
		editButton = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Modify"));
		removeButton = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Remove"));
		moveUpButton = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Utils_Move_Up"));
		moveDownButton = new UIButton(com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Utils_Move_Down"));

		JPanel eastPane = FRGUIPaneFactory.createNColumnGridInnerContainer_S_Pane(1);
		eastPane.add(editButton);
		eastPane.add(removeButton);
		eastPane.add(moveUpButton);
		eastPane.add(moveDownButton);
		this.add(GUICoreUtils.createBorderPane(eastPane, BorderLayout.NORTH), BorderLayout.EAST);

		JPanel centerPane = new JPanel(new BorderLayout(0, 5));
		this.add(centerPane, BorderLayout.CENTER);
		JPanel northcenterPane = FRGUIPaneFactory.createBorderLayout_S_Pane();
		northcenterPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
		northcenterPane.add(addButton, BorderLayout.EAST);
		centerPane.add(northcenterPane, BorderLayout.NORTH);
		centerPane.add(new JScrollPane(jlist), BorderLayout.CENTER);

		this.addListener();

		this.addButton.setEnabled(false);
		this.checkEnableState();
	}

	private void addListener() {
		addListener1();

		moveUpButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JListUtils.upListSelectedIndex(jlist);
			}
		});
		moveDownButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JListUtils.downListSelectedIndex(jlist);
			}
		});
		jlist.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				Object selected = jlist.getSelectedValue();
				selectedChanged((String)selected);
			}
		});
		jlist.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				Object selected = jlist.getSelectedValue();
				selectedChanged((String)selected);
			}
		});
	}

	private void addListener1() {
		addButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String newvalue = getAddOrEditString();
				if (!model.contains(newvalue)) {
					if (!StringUtils.isEmpty(newvalue)) {
						model.add(model.size(), newvalue);
						setAddEnabled(false);
					}
				} else {
					FineJOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(EditingStringListPane.this), newvalue + com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Already_Exists_Not_Add_Repeat")+"!");
				}
			}
		});
		editButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int selectededIndex = jlist.getSelectedIndex();
				if (selectededIndex > -1) {
					if (!StringUtils.isEmpty(getAddOrEditString())) {
						model.setElementAt(getAddOrEditString(), selectededIndex);
					}
				}
			}
		});
		removeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Object selected = jlist.getSelectedValue();
				if (selected != null) {
					int re = FineJOptionPane.showConfirmDialog(SwingUtilities.getWindowAncestor(EditingStringListPane.this), com.fr.design.i18n.Toolkit.i18nText("Fine-Design_Basic_Sure_To_Delete") + selected.toString() + "?");
					if (re == JOptionPane.OK_OPTION) {
						JListUtils.removeSelectedListItems(jlist);
					}
				}
				checkEnableState();
			}
		});
	}

	public void checkEnableState() {
		if (jlist.getSelectedIndex() < 0) {
			setEditEnabled(false);
		} else {
			setEditEnabled(true);
		}
	}

	private void setEditEnabled(boolean enabled) {
		this.removeButton.setEnabled(enabled);
		this.editButton.setEnabled(enabled);
		this.moveUpButton.setEnabled(enabled);
		this.moveDownButton.setEnabled(enabled);
	}

	public void setAddEnabled(boolean enabled) {
		this.addButton.setEnabled(enabled);
	}

	protected abstract void selectedChanged(String selected);

	protected abstract String getAddOrEditString();

	@Override
	protected String title4PopupWindow() {
		return "iloveyou";
	}

	@Override
	public void populateBean(List<String> lists) {
		for (String ob : lists) {
			model.addElement(ob);
		}
		jlist.setSelectedIndex(0);
	}

	@Override
	public List<String> updateBean() {
		List<String> lists = new ArrayList<String>();
		for (int i = 0, len = model.size(); i < len; i++) {
			lists.add((String)model.get(i));
		}
		return lists;
	}
}
